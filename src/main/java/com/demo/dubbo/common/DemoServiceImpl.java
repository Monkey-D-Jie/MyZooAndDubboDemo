package com.demo.dubbo.common;

import com.demo.dubbo.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2019-02-19 13:55
 * @Description: 这里是描述
 * To change this template use File | Settings | File and Templates.
 */
public class DemoServiceImpl implements DemoService{
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }

    @Override
    public List getUsers() {
        List list = new ArrayList();
        User u1 = new User();
        u1.setName("jack");
        u1.setAge(20);
        u1.setSex("男");

        User u2 = new User();
        u2.setName("tom");
        u2.setAge(21);
        u2.setSex("女");

        User u3 = new User();
        u3.setName("rose");
        u3.setAge(19);
        u3.setSex("女");

        list.add(u1);
        list.add(u2);
        list.add(u3);
        return list;
    }
}
