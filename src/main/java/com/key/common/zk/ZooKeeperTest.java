package com.key.common.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

public class ZooKeeperTest {
    public static final int SESSION_TIMEOUT = 30000;

    private ZooKeeper zk;
    private Watcher wh = new Watcher() {
        public void process(WatchedEvent event) {
            System.out.println(event.toString());
            // 如果连接诶成功，则输出其状态
            if (event.getState() == Event.KeeperState.SyncConnected) {
                System.out.println("客户端与zookeeper的连接会话创建成功" + event.getState());
            }
        }
    };

    /**
     * 创建zookeeper客户端对象
     *
     * @throws IOException
     */
    public void createZKInstance() throws IOException {
        zk = new ZooKeeper("192.168.204.130:2181", SESSION_TIMEOUT, wh);
        System.out.println("状态:" + zk.getState());
    }

    /**
     * 使用客户端来对zookeeper操作
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void zkOperations() throws KeeperException, InterruptedException {
        //创建一个节点
        System.out.println("创建javanode节点");
        zk.create("/javanode2", "sss".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        String data = new String(zk.getData("/javanode2", false, null));
        System.out.println(data);
    }

    public void list(String groupName) throws KeeperException, InterruptedException {
        try {
            List<String> children = zk.getChildren(groupName, false);
            if (children.isEmpty()) {
                System.out.println("No members in group " + groupName);
                System.exit(1);
            }
            System.out.println("--------------");
            for (String child : children) {
                System.out.println(child);
            }
            System.out.println("--------------");
        } catch (KeeperException.NoNodeException e) {
            System.out.println("Group :" + groupName + " does not exist");
            System.exit(1);
        }
    }

    public void zkClose() throws InterruptedException {
        zk.close();
        System.out.println("客户端关闭");
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeperTest zkt = new ZooKeeperTest();
        zkt.createZKInstance();
        zkt.zkOperations();
        zkt.list("/");
        zkt.zkClose();
    }
}