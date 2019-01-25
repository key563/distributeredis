package com.key.common.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CuratorClient {

    private static Executor executor = Executors.newCachedThreadPool();
    // 基于Curator的zkClient对象
    private CuratorFramework client = null;


    /**
     * 初始化client，启动client客户端
     */
    public CuratorClient(String path) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(ZkConstants.SLEEP_TIMEOUT, ZkConstants.MAX_RETRIES);
        client = CuratorFrameworkFactory.builder()
                .connectString(ZkConstants.CONNECT_SERVER)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(10000)
                .namespace(path)
                .build();
        client.start();
    }

    /**
     * 创建Zookeeper节点信息
     *
     * @param path
     * @param data
     * @throws Exception
     */
    public void createNode(String path, byte[] data) throws Exception {
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(path, data);
    }

    /**
     * 删除Zookeeper节点
     *
     * @param path
     * @param version
     * @throws Exception
     */
    public void deleteNode(String path, int version) throws Exception {
        client.delete().guaranteed().deletingChildrenIfNeeded()
                .withVersion(version)
                .inBackground().forPath(path);

    }

    /**
     * 获取Zookeeper节点信息
     *
     * @param path
     * @throws Exception
     */
    public String readNode(String path) throws Exception {
        Stat stat = new Stat();
        byte[] data = client.getData().storingStatIn(stat).forPath(path);
        return new String(data, ZkConstants.CHARSET);
    }

    /**
     * 根据版本，更新节点数据信息
     *
     * @param path
     * @param data
     * @param version
     * @throws Exception
     */
    public void updateNodeWithVersion(String path, byte[] data, int version) throws Exception {
        client.setData().withVersion(version).forPath(path, data);
    }

    /**
     * 更新数据信息
     *
     * @param path
     * @param data
     * @throws Exception
     */
    public void updateNode(String path, byte[] data) throws Exception {
        client.setData().forPath(path, data);
    }

    /**
     * 更新节点数据，并返回更新结果
     *
     * @param path
     * @param data
     * @return
     * @throws Exception
     */
    public Boolean updateNodeSuccess(String path, byte[] data)
            throws Exception {
        Stat stat = client.setData().forPath(path, data);
        if (stat != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 为Zookeeper的节点添加监听事件
     *
     * @param path
     * @throws Exception
     */
    public void addNodeDataWatcher(String path) throws Exception {
        final NodeCache nodeCache = new NodeCache(client, path);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(new NodeCacheListenerExample(nodeCache));
    }

    /**
     * 用线程来实现Zookeeper节点的监听
     *
     * @throws Exception
     */
    public void initWatcherNodeDataInfo(String path) throws Exception {
        final NodeCache nodeCache = new NodeCache(client, path);
        nodeCache.getListenable().addListener(new NodeCacheListenerExample(nodeCache), executor);
        nodeCache.start();
    }


    /**
     * 为Zookeeper的子节点添加监听事件
     *
     * @param path
     * @throws Exception
     */
    public void addChildWatcher(String path) throws Exception {
        final PathChildrenCache cache = new PathChildrenCache(this.client, path, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListenerExample(cache));
    }

    /**
     * 判断Zookeeper目录是否存在
     *
     * @param path
     * @return
     * @throws Exception
     */
    public boolean isExistWithPath(String path) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        if (stat != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 关闭client
     */
    public void closeClient() {
        if (client != null)
            this.client.close();
    }

}
