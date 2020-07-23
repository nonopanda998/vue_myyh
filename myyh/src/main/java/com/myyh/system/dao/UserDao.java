package com.myyh.system.dao;

import com.myyh.system.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserDao extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {


    User findByUsername(String name);
}
