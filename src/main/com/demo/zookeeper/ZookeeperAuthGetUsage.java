package com.demo.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2019-01-29 15:05
 * @Description: 使用无权限信息的zookeepper会话访问含
 * 权限信息的数据节点
 * To change this template use File | Settings | File and Templates.
 */

public class ZookeeperAuthGetUsage {

    final static String PATH = "/zk-book-auth_test";


    public static void main(String[] args) {
        try {
            ZooKeeper zookeeper1 = new ZooKeeper("127.0.0.1:2181", 5000, null);
            zookeeper1.addAuthInfo("digest", "foo:true".getBytes());
            zookeeper1.create(PATH, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
            System.out.println("success create znode: " + PATH);
            ZooKeeper zookeeper2 = new ZooKeeper("127.0.0.1:2181", 5000, null);
            //没有设置权限，直接访问，会报错KeeperException$NoAuthException
            zookeeper2.getData(PATH, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("为了养成习惯而已");
        }
    }
}
