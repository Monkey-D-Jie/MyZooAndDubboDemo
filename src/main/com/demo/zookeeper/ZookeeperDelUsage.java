package com.demo.zookeeper;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2019-01-29 15:22
 * @Description: 同步删除zookepper的子节点。
 * zookeeper的积淀删除规则为：
 * 当前的节点如果有叶子节点，则不能删除，
 * 得把它的叶子节点删除完后，才能删除该节点
 * To change this template use File | Settings | File and Templates.
 */

public class ZookeeperDelUsage implements Watcher{

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
//            System.out.println("--------------同步方法删除节点--------------");
            System.out.println("--------------异步方法删除节点--------------");
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,new ZookeeperDelUsage());
            System.out.println(zooKeeper.getState());
            connectedSemaphore.await();
            String path = "/zk-book";
//            zooKeeper.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//            System.out.println("success to create node1,path is "+path);
            String path2 = path + "/c1";
//           zooKeeper.create(path2,"".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
//            System.out.println("success to create node2,path is "+path2);
//            System.out.println("------在有节点的情况下尝试删除主节点------");
//            try {
//                zooKeeper.delete(path,-1);
//            } catch (Exception e) {
//                System.out.println("出现了异常，信息为:"+e.getMessage());
//            }
//
//            System.out.println("删除子路径下的节点");
//            zooKeeper.delete(path2,-1);
//            System.out.println("重新尝试删除子节点");
//            zooKeeper.delete(path,-1);
//            System.out.println("所有节点删除成功");
            zooKeeper.delete(path2,-1,new IVoidCallback(),null);
            zooKeeper.delete(path,-1,new IVoidCallback(),null);
            zooKeeper.delete(path,-1,new IVoidCallback(),null);
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("为测试的严谨性而生");
        }
    }

    /**
     * 异步创建节点时用
     */
     static class IStringCallback implements AsyncCallback.StringCallback{
        @Override
        public void processResult(int i, String s, Object o, String s1) {
            System.out.println("Create path result：["+i+","+ s+","+o+",real path info :"+s1);
        }
    }

    /**
     * 异步删除子节点时用
     */
    static class IVoidCallback implements AsyncCallback.VoidCallback {
        @Override
        public void processResult(int rc, String path, Object ctx) {
            System.out.println(rc + ", " + path + ", " + ctx);
        }
    }
}
