package com.lkm.menu.service;

import com.lkm.menu.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Linkm on 2022/5/15.
 */
public interface UserJPA extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User>, Serializable {


    List<User> findByUserCode(String userCode);

    List<User> findAllByOrderById();

    @Query(value = "select user_name from menu.user where id=:userId  ", nativeQuery = true)
    String findUserNameByUserId(String userId);

    List<User> findByUserNameAndMobile(String userName, String mobile);
}
