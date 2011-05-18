package org.ssh.pm.utils;

import java.util.concurrent.CountDownLatch;

//10个线程同时运行
public class ThreadUtil {
    public static void main(String[] args) throws InterruptedException {
        int threadNumber = 10;
        final CountDownLatch countDownLatch = new CountDownLatch(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            final int threadID = i;
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep((long) (Math.random() * 10000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(String.format("threadID:[%s] finished!!", threadID));
                    countDownLatch.countDown();
                }
            }.start();
        }


        countDownLatch.await();
        System.out.println("main thread finished!!");
    }

}
