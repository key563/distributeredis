package com.key.common.zk;

import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

public class NodeCacheListenerExample implements NodeCacheListener {

    private NodeCache nodeCache;

    public NodeCacheListenerExample(NodeCache nodeCache){
        this.nodeCache = nodeCache;
    }

    @Override
    public void nodeChanged() throws Exception {
        String data = new String(nodeCache.getCurrentData().getData(), ZkConstants.CHARSET);
        // 在监听事件中做逻辑处理
    }
}
