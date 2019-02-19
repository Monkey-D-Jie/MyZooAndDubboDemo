package java.com.demo.zookeeper.zookepperUse;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2019-01-29 15:22
 * @Description: 异步创建zookepper的子节点
 * To change this template use File | Settings | File and Templates.
 */

public class ZookeeperASynUsage implements Watcher{

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
            System.out.println("--------------异步方法创建节点--------------");
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,new ZookeeperASynUsage());
            System.out.println(zooKeeper.getState());
            connectedSemaphore.await();
            //创建临时节点(ephemeral,临时的)
            zooKeeper.create("/zk-Asyn-node1","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,new IStringCallback(),"node1 content");
            //创建临时顺序节点
           zooKeeper.create("/zk-Asyn-node2","".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL,new IStringCallback(),"node2 content");
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("为测试的严谨性而生");
        }
    }

     static class IStringCallback implements AsyncCallback.StringCallback{
        @Override
        public void processResult(int i, String s, Object o, String s1) {
            System.out.println("Create path result：["+i+","+ s+","+o+",real path info :"+s1);
        }
    }
}
