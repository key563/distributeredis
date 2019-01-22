package com.key.common.zk;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LeaderLatchExample {

    public static void main(String[] args) {

        List<CuratorFramework> clients = Lists.newArrayList();
        List<LeaderLatch> examples = Lists.newArrayList();

//        InstanceSpec spec = new InstanceSpec(null, 2181, 0, -1, false, -1, -1, -1, null,"192.168.204.130");
//        TestingServer server = new TestingServer(spec,true);

        try {
            for (int i = 0; i < ZkConstants.CLIENT_QTY; ++i) {
                CuratorFramework client = CuratorFrameworkFactory.newClient(ZkConstants.CONNECT_SERVER, new ExponentialBackoffRetry(1000, 3));
                clients.add(client);
                LeaderLatch example = new LeaderLatch(client, ZkConstants.PATH_LEADER, "Client #" + i);
                examples.add(example);
                client.start();
                example.start();
            }

            Thread.sleep(20000);

            LeaderLatch currentLeader = null;
            for (int i = 0; i < ZkConstants.CLIENT_QTY; ++i) {
                LeaderLatch example = examples.get(i);
                if (example.hasLeadership())
                    currentLeader = example;
            }
            System.out.println("current leader is " + currentLeader.getId());
            System.out.println("release the leader " + currentLeader.getId());
            currentLeader.close();
            // 阻塞方法，尝试成为leader，但不一定成功
            examples.get(0).await(2, TimeUnit.SECONDS);

            System.out.println("Client #0 maybe is elected as the leader or not although it want to be");
            System.out.println("the new leader is " + examples.get(0).getLeader().getId());

            System.out.println("Press enter/return to quit\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Shutting down...");
            for (LeaderLatch exampleClient : examples) {
                CloseableUtils.closeQuietly(exampleClient);
            }
            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }
//            CloseableUtils.closeQuietly(server);
        }
    }
}
