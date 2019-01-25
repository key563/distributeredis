package com.key.common.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * Curator框架创建Client连接Demo
 */
public class CreateClientExample {
    private static final String PATH = "/example/basic";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = null;
        try {
            client = createSimple(ZkConstants.CONNECT_SERVER);
            client.start();
            client.create().creatingParentsIfNeeded().forPath(PATH, "test".getBytes());
            System.out.println("the data is :");
            System.out.println(new String(client.getData().forPath(PATH)));
            CloseableUtils.closeQuietly(client);

            // 注意 timeout时间不宜设置过小，会导致连接失败
            client = createWithOptions(ZkConstants.CONNECT_SERVER, new ExponentialBackoffRetry(1000, 3), 10000, 1000);
            client.start();
            System.out.println("the data is :");
            System.out.println(new String(client.getData().forPath(PATH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }

    }

    /**
     * 快速创建Client实例方式
     *
     * @param connectionString
     * @return
     */
    public static CuratorFramework createSimple(String connectionString) {
        // 参数解释：retry每次的时间是依据前一个参数和上一次retry而定的，第一次retry等待1秒，第二次retry等待2秒，第三次retry等待4秒...
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        // 创建Client实例最简单的方式：接收连接参数和重试机制
        return CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
    }

    /**
     * 创建Client实例，并配置连接参数
     *
     * @param connectionString
     * @param retryPolicy
     * @param connectionTimeoutMs
     * @param sessionTimeoutMs
     * @return
     */
    public static CuratorFramework createWithOptions(String connectionString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs) {
        // 通过CuratorFrameworkFactory.builder()提供细粒度参数设置
        return CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }
}
