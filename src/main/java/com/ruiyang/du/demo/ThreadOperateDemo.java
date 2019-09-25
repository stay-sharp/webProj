package com.ruiyang.du.demo;

import com.alibaba.fastjson.JSONObject;
import com.ruiyang.du.bo.HttpClientResult;
import com.ruiyang.du.utils.HttpClientUtils;

import java.util.concurrent.*;

public class ThreadOperateDemo {


    static class MyThread1 extends Thread {
        @Override
        public void run() {
            System.out.println("MyThread1 extends Thread, running");
        }
    }

    static class MyThread2 implements Runnable {
        @Override
        public void run() {
            testGet("http://localhost:8081/bankfront-callback/interest/ylbank");
        }
    }

    static class MyThread3 implements Callable {
        @Override
        public Object call() throws Exception {
            System.out.println("MyThread3 implements Callable, running");
            return "call finish";
        }
    }

    private static void testGet(String url){
        try {
            System.out.println(Thread.currentThread().getName() + "子线程开始执行");
            HttpClientResult httpClientResult = HttpClientUtils.doGet(url, 5000, 5000, null, null);
            System.out.println(Thread.currentThread().getName() + "子线程执行完毕，返回信息：" + httpClientResult.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(128);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 30, TimeUnit.SECONDS, queue);
        for (int i = 0; i < 20; i++) {
            MyThread2 myThread = new MyThread2();
            executor.submit(myThread);
        }
    }
}
