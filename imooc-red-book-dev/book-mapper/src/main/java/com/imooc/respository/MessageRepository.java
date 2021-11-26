package com.imooc.respository;

import com.imooc.mo.MessageMO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 包建丰
 * @date 2021/11/26 08 :59
 * @description
 **/
@Repository
public interface MessageRepository extends MongoRepository<MessageMO, String> {

    // 通过实现Repository接口，自定义条件查询
    List<MessageMO> findAllByToUserIdEqualsOrderByCreateTimeDesc(String toUserId, Pageable pageable);

}


