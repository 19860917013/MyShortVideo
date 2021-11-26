package com.imooc.respository;

import com.imooc.mo.MessageMO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 包建丰
 * @date 2021/11/26 08 :59
 * @description
 **/
@Repository
public interface MessageRepository extends MongoRepository<MessageMO, String> {
}


