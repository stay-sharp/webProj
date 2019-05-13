package com.ruiyang.du.demo;

import java.util.concurrent.*;

public class OperateDemo {


    static class MyThread1 extends Thread {
        @Override
        public void run() {
            System.out.println("MyThread1 extends Thread, running");
        }
    }

    static class MyThread2 implements Runnable {
        @Override
        public void run() {
            System.out.println("MyThread2 implements Runnable, running");
        }
    }

    static class MyThread3 implements Callable {
        @Override
        public Object call() throws Exception {
            System.out.println("MyThread3 implements Callable, running");
            return "call finish";
        }
    }
    public static void main(String[] args) throws Exception {
        MyThread1 myThread1 = new MyThread1();
        myThread1.start();



        MyThread2 myThread2 = new MyThread2();
        new Thread(myThread2).start();

        MyThread3 myThread3 = new MyThread3();
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(128);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,2,10, TimeUnit.SECONDS, queue);
        Future submit = executor.submit(myThread3);
        System.out.println(submit.get());



        System.out.println("执行完毕");
    }
}
