package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RestTemplate restTemplate;

    public AuthToken login(String username, String password, String clientId, String clientSecret) {

        //请求spring security申请令牌
        AuthToken authToken = this.applyToken(username, password, clientId, clientSecret);
        if(authToken == null){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        //用户身份令牌
        String access_token = authToken.getAccess_token();
        //存储到redis中的内容
        String jsonString = JSON.toJSONString(authToken);
        //将令牌存储到redis
        boolean result = this.saveToken(access_token, jsonString, tokenValiditySeconds);
        if (!result) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
        }
        return authToken;

    }

    private boolean saveToken(String access_token,String content,long ttl){
        String key = "user_token:" + access_token;
        stringRedisTemplate.boundValueOps(key).set(content,ttl, TimeUnit.SECONDS);
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire>0;
    }

    private AuthToken applyToken(String username, String password, String clientId, String clientSecret){
        //从eureka中获取认证服务的地址（因为spring security在认证服务中）
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        //此地址就是http://ip:port
        URI uri = serviceInstance.getUri();
        //令牌申请的地址 http://localhost:40400/auth/oauth/token
        String authUrl = uri+ "/auth/oauth/token";

        //定义header
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic(clientId, clientSecret);
        header.add("Authorization",httpBasic);

        //定义body
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);

        //设置restTemplate远程调用时候，对400和401不让报错，正确返回数据
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });

        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
        Map bodyMap = exchange.getBody();
        if(bodyMap == null ||
                bodyMap.get("access_token") == null ||
                bodyMap.get("refresh_token") == null ||
                bodyMap.get("jti") == null){
            return null;
        }
        AuthToken authToken = new AuthToken();
        authToken.setAccess_token((String) bodyMap.get("jti"));//用户身份令牌
        authToken.setRefresh_token((String) bodyMap.get("refresh_token"));//刷新令牌
        authToken.setJwt_token((String) bodyMap.get("access_token"));//jwt令牌
        return authToken;
    }

    private String getHttpBasic(String clientId,String clientSecret){
        String string = clientId+":"+clientSecret;
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic "+new String(encode);
    }
}
