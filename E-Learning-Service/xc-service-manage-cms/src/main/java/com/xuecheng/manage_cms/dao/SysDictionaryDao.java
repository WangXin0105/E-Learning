package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysDictionaryDao extends MongoRepository<SysDictionary,String> {
    SysDictionary findBydType(String dType);
}

