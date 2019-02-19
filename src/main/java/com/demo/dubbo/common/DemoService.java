package com.demo.dubbo.common;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2019-02-19 13:53
 * @Description: 这里是描述
 * To change this template use File | Settings | File and Templates.
 */

public interface DemoService {
    String sayHello(String name);
    List getUsers();
}
