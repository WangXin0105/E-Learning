package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private XcUserRepository xcUserRepository;

    @Autowired
    private XcCompanyUserRepository xcCompanyUserRepository;

    public XcUser findXcUserByUsername(String username) {
        return xcUserRepository.findByUsername(username);
    }

    public XcUserExt getUserExt(String username) {
        XcUser xcUser = findXcUserByUsername(username);
        if (xcUser == null) {
            return null;
        }
        String userId = xcUser.getId();
        XcCompanyUser xcCompanyUser = xcCompanyUserRepository.findByUserId(userId);
        String companyId = null;
        if(xcCompanyUser!=null){
            companyId = xcCompanyUser.getCompanyId();
        }
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser,xcUserExt);
        xcUserExt.setCompanyId(companyId);
        return xcUserExt;
    }

}
