package java.com.demo.zookeeper.zookepperUse;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2019-01-29 15:22
 * @Description: 同步创建zookepper的子节点
 * To change this template use File | Settings | File and Templates.
 */

public class ZookeeperSynUsage implements Watcher{

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent watchedEvent) {
            if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
                System.out.println("当前被监控事件的状态为:"+watchedEvent.getState());
                connectedSemaphore.countDown();
            }
    }

    public static void main(String[] args) {
        try {
            System.out.println("--------------同步方法创建节点--------------");
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,new ZookeeperSynUsage());
            System.out.println(zooKeeper.getState());
            connectedSemaphore.await();
            //创建临时节点(ephemeral,临时的)
            String path1 = zooKeeper.create("/zk-node1","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("Success create znode:"+path1);
            //创建临时顺序节点
            String path2 = zooKeeper.create("/zk-node2","".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("Success create znode："+path2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("为测试的严谨性而生");
        }
    }
}
