package com.ruiyang.du.demo;

import java.util.concurrent.*;

public class ForkJoinDemo {


    public static void main(String[] args) {
        //主线程开始执行
        System.out.println("start... time = " + System.currentTimeMillis());
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //ForkJoinTask<Long> forkJoinTask1 = forkJoinPool.submit(new MyTask(5L));
        ForkJoinTask forkJoinTask1 = forkJoinPool.submit(new MyVoidTask(5L));
        //主线程执行完毕
        System.out.println("finish... value = " + forkJoinTask1.join());
    }


    /**
     * 递归任务
     */
    static class MyTask extends RecursiveTask<Long> {

        private Long value;

        public MyTask() {
        }

        public MyTask(Long value) {
            this.value = value;
        }

        /**
         * 业务处理：传入数字value，进行递减，直到0返回
         * @return
         */
        @Override
        protected Long compute() {
            long time = System.currentTimeMillis();
            System.out.println("value = " + value + "... time = " + time + "... tid = "+ Thread.currentThread().getName());
            if(value<=0) {
                return value;
            }
            value--;
            //不到0，开启递归
            MyTask myTask = new MyTask(value);
            myTask.fork();
            return myTask.join();
        }
    }

    static class MyVoidTask extends RecursiveAction{

        private Long value;

        public MyVoidTask() {
        }

        public MyVoidTask(Long value) {
            this.value = value;
        }

        @Override
        protected void compute() {
            long time = System.currentTimeMillis();
            System.out.println("value = " + value + "... time = " + time + "... tid = "+ Thread.currentThread().getName());
            if(value<=0) {
                return ;
            }
            value--;
            //不到0，开启递归
            MyVoidTask myTask = new MyVoidTask(value);
            myTask.fork();
            myTask.join();
        }
    }
}


