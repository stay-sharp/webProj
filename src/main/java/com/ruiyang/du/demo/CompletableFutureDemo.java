package com.ruiyang.du.demo;

import com.alibaba.fastjson.JSONObject;

import java.time.Instant;
import java.util.concurrent.*;

public class CompletableFutureDemo {

    private static CopyOnWriteArrayList concurrentList = new CopyOnWriteArrayList();

    private static ExecutorService executor = Executors.newCachedThreadPool();

    private static long timestamp = 0;

    public static void main(String[] args) {
        //demo1 并发、异步执行supplier型任务，并按all/any of汇总
//        timestamp = System.currentTimeMillis();
//        System.out.println("begin...time = " + timestamp);
//        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(CompletableFutureDemo::supplier1);
//        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(CompletableFutureDemo::supplier2);
//        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(CompletableFutureDemo::supplier3);
//        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(future1, future2, future3);
//        /*CompletableFuture<Object> voidCompletableFuture = CompletableFuture.anyOf(future1, future2, future3);*/
//        try {
//            voidCompletableFuture.get();
//            System.out.println("future1 = "+future1.get());
//            System.out.println("future2 = "+future2.get());
//            System.out.println("future3 = "+future3.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        System.out.println("allof...time = " + (System.currentTimeMillis() - timestamp));


        //demo2 并发、异步执行consume型任务，并按all of汇总
//        timestamp = System.currentTimeMillis();
//        System.out.println("begin...time = " + timestamp+", concurrentList = "+ JSONObject.toJSONString(concurrentList));
//        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> consumer1(1, concurrentList));
//        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> consumer2(2, concurrentList));
//        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> consumer3(3, concurrentList));
//        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(future1, future2, future3);
//        try {
//            voidCompletableFuture.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        System.out.println("allof...time = " + (System.currentTimeMillis() - timestamp)+", concurrentList = "+ JSONObject.toJSONString(concurrentList));


        //demo3 串行supplier1+func1，并发执行supplier1+func1、supplier3，并按all of 汇总
        timestamp = System.currentTimeMillis();
        System.out.println("begin...time = " + timestamp);
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(CompletableFutureDemo::supplier1).thenApplyAsync(integer -> func1(integer));
        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(CompletableFutureDemo::supplier3);
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(future1, future3);
        try {
            voidCompletableFuture.get();
            System.out.println("future1 = "+future1.get());
            System.out.println("future2 = "+future3.get());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("allof...time = " + (System.currentTimeMillis() - timestamp));


    }


    private static int supplier1() {
        try {
            Thread.sleep(100);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("supplier1... time = " + (System.currentTimeMillis() - timestamp));
        return 1;
    }

    private static int supplier2() {
        try {
            Thread.sleep(200);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("supplier2... time = " + (System.currentTimeMillis() - timestamp));
        return 2;
    }

    private static int supplier3() {
        try {
            Thread.sleep(300);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("supplier3... time = " + (System.currentTimeMillis() - timestamp));
        return 3;
    }

    private static void consumer1(int var, CopyOnWriteArrayList concurrentList) {
        try {
            Thread.sleep(100);
        }catch (Exception e){
            e.printStackTrace();
        }
        concurrentList.add(var);
        System.out.println("consumer1... var = " + var + " time = " + (System.currentTimeMillis() - timestamp));
    }

    private static void consumer2(int var, CopyOnWriteArrayList concurrentList) {
        try {
            Thread.sleep(200);
        }catch (Exception e){
            e.printStackTrace();
        }
        concurrentList.add(var);
        System.out.println("consumer2... var = " + var + " time = " + (System.currentTimeMillis() - timestamp));
    }

    private static void consumer3(int var, CopyOnWriteArrayList concurrentList) {
        try {
            Thread.sleep(300);
        }catch (Exception e){
            e.printStackTrace();
        }
        concurrentList.add(var);
        System.out.println("consumer3... var = " + var + " time = " + (System.currentTimeMillis() - timestamp));
    }

    private static int func1(int var){
        try {
            Thread.sleep(100);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("func1... var = " + var + " time = " + (System.currentTimeMillis() - timestamp));
        return var;
    }
}
