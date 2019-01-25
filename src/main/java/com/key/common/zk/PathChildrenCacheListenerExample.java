package com.key.common.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathChildrenCacheListenerExample implements PathChildrenCacheListener {

    private static final Logger LOG = LoggerFactory.getLogger(PathChildrenCacheListenerExample.class);
    private PathChildrenCache pathChildrenCache;

    public PathChildrenCacheListenerExample(PathChildrenCache cache) {
        this.pathChildrenCache = cache;
    }

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
            // 逻辑代码
            LOG.info("init client Child data end!!!");
            LOG.info("size=" + pathChildrenCache.getCurrentData().size());
        } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
            LOG.info("Add Child Node:" + event.getData().getPath());
            LOG.info("Update Child Node Data:" + new String(event.getData().getData(), "UTF-8"));
        } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
            LOG.info("Delete Child Node:" + event.getData().getPath());
        } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
            LOG.info("Update Child Node Data" + event.getData().getPath());
            LOG.info("Update Child Node Data" + new String(event.getData().getData(), "UTF-8"));
        }
    }
}
