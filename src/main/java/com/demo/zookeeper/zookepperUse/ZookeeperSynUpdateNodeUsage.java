package java.com.demo.zookeeper.zookepperUse;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2019-01-29 15:05
 * @Description: 同步的方式更新节点数据
 * To change this template use File | Settings | File and Templates.
 */

public class ZookeeperSynUpdateNodeUsage implements Watcher{
    /**
     * 用于标识连接的信号量
     */
    private static CountDownLatch connectFlag = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    @Override
    public void process(WatchedEvent watchedEvent) {
        //监测当前时间的状态
        System.out.println("Hello zookeeper--->>>" +watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            if(Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()){
                connectFlag.countDown();
            }
        }
    }

    public static void main(String[] args){
        try {
            String path = "/zk-book-4";
            zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperSynUpdateNodeUsage());
            connectFlag.await();

            zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success create znode: " + path);
            zk.getData(path, true, null);
            //version为-1表示 客户端要基于最新的数据进行更新操作
            Stat stat = zk.setData(path, "456".getBytes(), -1);
            System.out.println("czxID: " + stat.getCzxid() + ", mzxID: " + stat.getMzxid() + ", version: " + stat.getVersion());
            //设置一次值后，zk的版本号就会发生变化，这里stat.getVersion() = 1
            Stat stat2 = zk.setData(path, "456-654".getBytes(), stat.getVersion());
            //setDat操作完成后，该zk的version值就变成了2
            System.out.println("czxID: " + stat2.getCzxid() + ", mzxID: " + stat2.getMzxid() + ", version: " + stat2.getVersion());
            try {
                //这里再根据1的版本去更新zk的数据，就会出错了。即zk的版本是一个单值，不会同时出现多个版本的情况
                zk.setData(path, "456".getBytes(), stat.getVersion());
            } catch (KeeperException e) {
                System.out.println("Error: " + e.code() + "," + e.getMessage());
            }
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("为了养成习惯而已");
        }
    }
}
