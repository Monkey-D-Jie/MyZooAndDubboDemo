package java.com.demo.zookeeper.zookepperUse;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2019-01-29 15:05
 * @Description: 使用无权限信息的zookeepper会话删除含
 * 权限信息的数据节点
 * To change this template use File | Settings | File and Templates.
 */

public class ZookeeperAuthDelUsage {

    final static String PATH = "/zk-book-auth_test";
    final static String PATH2 = "/zk-book-auth_test/child";


    public static void main(String[] args) {
        try {
            ZooKeeper zookeeper1 = new ZooKeeper("127.0.0.1:2181", 5000, null);
            zookeeper1.addAuthInfo("digest", "foo:true".getBytes());
            zookeeper1.create(PATH, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);

            zookeeper1.create(PATH2, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

            try {
                ZooKeeper zookeeper2 = new ZooKeeper("127.0.0.1:2181", 5000, null);
                //未设置权限，删除失败
                zookeeper2.delete(PATH2, -1);
            } catch (Exception e) {
                System.out.println("fail to delete: " + e.getMessage());
            }

            ZooKeeper zookeeper3 = new ZooKeeper("127.0.0.1:2181", 5000, null);
            zookeeper3.addAuthInfo("digest", "foo:true".getBytes());
            zookeeper3.delete(PATH2, -1);
            System.out.println("success delete znode: " + PATH2);

            ZooKeeper zookeeper4 = new ZooKeeper("127.00.1:2181", 5000, null);
            //设置了权限后再删除，删除成功
            zookeeper4.delete(PATH, -1);
            System.out.println("success delete znode: " + PATH);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("为了养成习惯而已");
        }
    }
}
