package com.myyh.mybatis;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserTest {

//    @Resource
//    UserMapper userMapper;
//
//
//    //查询
//    @Test
//    public void select(){
//        List<User> users = userMapper.selectList(null);
//        users.forEach(user -> log.info(user.toString()));
//    }
//
//    //新增
//    @Test
//    public void add(){
//        User user = new User();
//        user.setCreateTime(new Date());
//        user.setOid(1);
//        user.setUserName("wangss");
//        user.setPassword("123");
//        int insert = userMapper.insert(user);
//        log.info(Integer.toString(insert));
//    }
//
//
//    //删除
//    @Test
//    public void delete(){
//        int i = userMapper.deleteById(28);
//        log.info(Integer.toString(i));
//    }
//
//
//    //修改
//    @Test
//    public void update(){
//        User user =new User();
//        user.setId(29);
//        user.setNickName("222");
//        int update = userMapper.updateById(user);
//        log.info(Integer.toString(update));
//    }

    //分页

}
