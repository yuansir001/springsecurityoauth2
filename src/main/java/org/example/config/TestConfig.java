package org.example.config;

import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConfig {

    ZooKeeper zk;

    @Before
    public void conn (){
        zk  = ZKUtils.getZk();
    }

    @After
    public void close(){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Test(){

        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setZk(zk);
        MyConf myConf = new MyConf();
        watchCallBack.setConf(myConf);

        watchCallBack.aWait();
        // 结点不存在
        // 结点存在

        while (true){
            if (myConf.getConf().equals("")){
                System.out.println("----conf dui le----");
                watchCallBack.aWait();
            }else {
                System.out.println(myConf.getConf());
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
