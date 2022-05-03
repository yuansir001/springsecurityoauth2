package org.example.config;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKUtils {

    private static ZooKeeper zk;

    private static String address = "39.107.230.218:2181/testConf";

    private static DefaultWatch watch = new DefaultWatch();

    private static CountDownLatch init = new CountDownLatch(1);

    public static ZooKeeper getZk() {
        try {
            zk = new ZooKeeper(address, 1000, watch);
            watch.setCc(init);
            init.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }
}
