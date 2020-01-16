package com.myyh.security.dao;

import com.myyh.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByAccount(String account);

    User findByAccountAndStatusNot(String account, Integer status);

    User findByName(String username);
}
