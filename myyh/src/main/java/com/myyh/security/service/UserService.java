package com.myyh.security.service;

import com.myyh.bean.User;
import com.myyh.security.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByName(String username) {
    return  userRepository.findByName(username);
    }
}
