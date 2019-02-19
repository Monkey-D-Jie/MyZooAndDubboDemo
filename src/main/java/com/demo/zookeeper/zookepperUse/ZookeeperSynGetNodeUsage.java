package java.com.demo.zookeeper.zookepperUse;

import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2019-01-29 15:22
 * @Description: 同步获取zookepper的子节点
 * To change this template use File | Settings | File and Templates.
 */

public class ZookeeperSynGetNodeUsage implements Watcher{

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static  ZooKeeper zooKeeper = null;
    @Override
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                connectedSemaphore.countDown();
            } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                try {
                    System.out.println("ReGet Child:" + zooKeeper.getChildren(watchedEvent.getPath(), true));
                } catch (Exception e) {
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("--------------同步方法获取节点--------------");
            zooKeeper =  new ZooKeeper("127.0.0.1:2181",5000,new ZookeeperSynGetNodeUsage());
            System.out.println(zooKeeper.getState());
            connectedSemaphore.await();
            String path = "/zk-book-1";
            zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("success create znode: " + path);
            zooKeeper.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success create znode: " + path + "/c1");
            List<String> childrenList = zooKeeper.getChildren(path, true);
            System.out.println(childrenList);

            zooKeeper.create(path + "/c2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success create znode: " + path + "/c2");
            Thread.sleep(1000);
            zooKeeper.create(path + "/c3", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success create znode: " + path + "/c3");
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("为测试的严谨性而生");
        }
    }
}
