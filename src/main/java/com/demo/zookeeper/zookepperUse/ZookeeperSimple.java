package java.com.demo.zookeeper.zookepperUse;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2019-01-29 15:05
 * @Description: 这里是描述
 * To change this template use File | Settings | File and Templates.
 */

public class ZookeeperSimple implements Watcher{
    /**
     * 用于标识连接的信号量
     */
    private static CountDownLatch connectFlag = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent watchedEvent) {
        //监测当前时间的状态
        System.out.println("Hello zookeeper--->>>" +watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            //如果是同步的，则说明状态是正常的，可以执行其他事项
            connectFlag.countDown();
        }
    }

    public static void main(String[] args){
        try {
            ZooKeeper zooKeeper = new ZooKeeper("localhost:2181",5000,new ZookeeperSimple());
            System.out.println("当前zookeeper的状态为:"+zooKeeper.getState());
            //临时阻塞，模拟业务中的进程活动
            connectFlag.await();
            System.out.println("zookeeper has been equired");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("为了养成习惯而已");
        }
    }
}
