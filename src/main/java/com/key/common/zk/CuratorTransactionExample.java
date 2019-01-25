package com.key.common.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorMultiTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;

import java.util.Collection;

/**
 * CuratorFramework事务操作
 */
public class CuratorTransactionExample {
    public static void main(String[] args) {

    }

    public static Collection<CuratorTransactionResult> transaction(CuratorFramework client) throws Exception {
        // this example shows how to use ZooKeeper's new transactions
        CuratorMultiTransaction transaction = client.transaction();
        Collection<CuratorTransactionResult> results = client.inTransaction().create().forPath("/examples/a/path", "some data".getBytes())
                .and().setData().forPath("/examples/another/path", "other data".getBytes())
                .and().delete().forPath("/examples/another/path")
                .and().commit(); // IMPORTANT!
        // called
        for (CuratorTransactionResult result : results) {
            System.out.println(result.getForPath() + " - " + result.getType());
        }
        return results;
    }

    /*
     * These next four methods show how to use Curator's transaction APIs in a
     * more traditional - one-at-a-time - manner
     */
    public static CuratorTransaction startTransaction(CuratorFramework client) {
        // start the transaction builder
        return client.inTransaction();
    }

    public static CuratorTransactionFinal addCreateToTransaction(CuratorTransaction transaction) throws Exception {
        // add a create operation
        return transaction.create().forPath("/examples/path", "some data".getBytes()).and();
    }

    public static CuratorTransactionFinal addDeleteToTransaction(CuratorTransaction transaction) throws Exception {
        // add a delete operation
        return transaction.delete().forPath("/examples/another/path").and();
    }

    public static void commitTransaction(CuratorTransactionFinal transaction) throws Exception {
        // commit the transaction
        transaction.commit();
    }
}
