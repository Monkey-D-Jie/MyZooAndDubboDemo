package com.demo.zookeeper;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2019-01-29 15:05
 * @Description: 同步的方式监听节点的情况
 * To change this template use File | Settings | File and Templates.
 */

public class ZookeeperNodeExistListenUsage implements Watcher{
    /**
     * 用于标识连接的信号量
     */
    private static CountDownLatch connectFlag = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                    connectFlag.countDown();
                } else if (Event.EventType.NodeCreated == watchedEvent.getType()) {
                    System.out.println("success create znode: " + watchedEvent.getPath());
                    zk.exists(watchedEvent.getPath(), true);
                } else if (Event.EventType.NodeDeleted == watchedEvent.getType()) {
                    System.out.println("success delete znode: " + watchedEvent.getPath());
                    zk.exists(watchedEvent.getPath(), true);
                } else if (Event.EventType.NodeDataChanged == watchedEvent.getType()) {
                    System.out.println("data changed of znode: " + watchedEvent.getPath());
                    zk.exists(watchedEvent.getPath(), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("异常该处理还是要处理的，成就=量级x做事的速度x成功率-来自吴军老师的话");
        } finally {
        }
    }

    public static void main(String[] args){
        try {
            String path = "/zk-book";
            zk = new ZooKeeper("127.0.0.1:2181", 5000, //
                    new ZookeeperNodeExistListenUsage());
            connectFlag.await();

            zk.exists(path, true);

            zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zk.setData(path, "123".getBytes(), -1);

            zk.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("success create znode: " + path + "/c1");

            zk.delete(path + "/c1", -1);
            zk.delete(path, -1);

            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("为了养成习惯而已");
        }
    }
}
