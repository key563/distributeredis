package com.key.common.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DistributedBarrierExample {
    private static final int QTY = 5;

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZkConstants.CONNECT_SERVER, new ExponentialBackoffRetry(1000, 3));
        client.start();

        ExecutorService service = Executors.newFixedThreadPool(QTY);
        DistributedBarrier controlBarrier = new DistributedBarrier(client, ZkConstants.PATH_BARRIER);
        // 设置栅栏，线程会阻塞
        controlBarrier.setBarrier();

        for (int i = 0; i < QTY; ++i) {
            final DistributedBarrier barrier = new DistributedBarrier(client, ZkConstants.PATH_BARRIER);
            final int index = i;
            Callable<Void> task = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    Thread.sleep((long) (3 * Math.random()));
                    System.out.println("Client #" + index + " waits on Barrier");
                    barrier.waitOnBarrier();
                    System.out.println("Client #" + index + " begins");
                    return null;
                }
            };
            service.submit(task);
        }

        Thread.sleep(10000);
        System.out.println("all Barrier instances should wait the condition");

        // 移出栅栏
        controlBarrier.removeBarrier();

        service.shutdown();
        // 监听所有线程，直到所有线程结束后往下运行
        service.awaitTermination(10, TimeUnit.SECONDS);
    }

}
