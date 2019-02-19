package java.com.demo.zookeeper.zookepperUse;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2019-01-29 15:05
 * @Description: 异步的方式更新节点数据
 * To change this template use File | Settings | File and Templates.
 */

public class ZookeeperASynUpdateNodeUsage implements Watcher{
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
            String path = "/zk-book-5";
            zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperASynUpdateNodeUsage());
            connectFlag.await();

            zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success create znode: " + path);
            zk.setData(path, "456".getBytes(), -1, new IStatCallback(), null);
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("为了养成习惯而已");
        }
    }

    static class IStatCallback implements AsyncCallback.StatCallback {
        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            //rc 即ResultCode 为0，表是成功地更新了节点的数据
            System.out.println("rc: " + rc + ", path: " + path + ", stat: " + stat);
        }
    }
}
