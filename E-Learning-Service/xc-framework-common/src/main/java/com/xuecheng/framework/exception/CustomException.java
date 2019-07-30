package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

public class CustomException extends RuntimeException {

    //错误代码
    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }
    public ResultCode getResultCode(){
        return resultCode;
    }


}
