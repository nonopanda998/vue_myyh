package com.myyh;

import java.util.HashMap;

public class HashMapDemo {

    public static void main(String[] args) {

        HashMap map  =new HashMap();
        //增加
        map.put("key1","1");
        //删除
        map.remove("key1");
        //修改
        map.put("key1","2");
        //查询
        map.get("key1");

    }
}
