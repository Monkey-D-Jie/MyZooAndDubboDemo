package com.demo.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2019-01-29 15:22
 * @Description: 异步获取zookepper的子节点
 * To change this template use File | Settings | File and Templates.
 */

public class ZookeeperASynGetNodeUsage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;


    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("我是process中的方法");
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                connectedSemaphore.countDown();
            } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                try {
                    System.out.println("ReGet Child:" + zk.getChildren(watchedEvent.getPath(), true));
                } catch (Exception e) {
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            String path = "/zk-book";
            zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperASynGetNodeUsage());
            connectedSemaphore.await();
            zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("success create znode: " + path);
            zk.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success create znode: " + path + "/c1");

            zk.getChildren(path, true, new IChildren2Callback(), null);

            zk.create(path + "/c2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success create znode: " + path + "/c2");

            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("为测试的严谨性而生");
        }
    }

   static class IChildren2Callback implements AsyncCallback.Children2Callback {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
            System.out.println("Get Children znode result: [response code: " + rc + ", param path: " + path + ", ctx: "
                    + ctx + ", children list: " + children + ", stat: " + stat);
        }
    }
}
