package org.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main(String[] args ) throws Exception {
        System.out.println( "Hello World!" );

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final ZooKeeper zk = new ZooKeeper("39.107.230.218:2181", 3000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                Event.KeeperState state = event.getState();
                String path = event.getPath();
                Event.EventType type = event.getType();
                System.out.println("new zk watch:" + event.toString());
                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        System.out.println("connected");
                        countDownLatch.countDown();
                        break;
                    case AuthFailed:
                        break;
                    case ConnectedReadOnly:
                        break;
                    case SaslAuthenticated:
                        break;
                    case Expired:
                        break;
                }
                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        break;
                    case NodeDeleted:
                        break;
                    case NodeDataChanged:
                        break;
                    case NodeChildrenChanged:
                        break;
                }
            }
        });

        countDownLatch.await();
        ZooKeeper.States state = zk.getState();
        switch (state) {
            case CONNECTING:
                System.out.println("ing..........");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("ed...........");
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }

        String path = zk.create("/ooxx", "oldData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        final Stat stat = new Stat();
        byte[] data = zk.getData("/ooxx", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("getNewData watch:" + event.toString());
                try {
                    zk.getData("/ooxx", this, stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, stat);

        System.out.println(new String(data));

        Stat stat1 = zk.setData("/ooxx", "newData".getBytes(), 0);
        Stat stat2 = zk.setData("/ooxx", "newData1".getBytes(), stat1.getVersion());

        System.out.println("----------async start-------------");
        zk.getData("/ooxx", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                System.out.println("----------async call back-------------");
                System.out.println(ctx.toString());
                    System.out.println(new String(data));
            }
        }, "abc");

        System.out.println("----------async over-------------");


        Thread.sleep(22222222);
    }
}
