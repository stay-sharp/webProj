package com.ruiyang.du.demo;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducterCostumerDemo {

    static class Dept extends ReentrantLock{
        private volatile Integer goods;
        private Condition PCondition = this.newCondition();
        private Condition CCondition = this.newCondition();

        public Dept(Integer goods) {
            this.goods = goods;
        }
    }


    static class Producter implements Runnable {
        private Dept dept;

        public Producter(Dept dept) {
            this.dept = dept;
        }

        @Override
        public void run() {
            product(dept);
        }

        /**
         * 同步锁实现
         *
         * @param dept
         */
        public void product(Dept dept) {
            try {
                while (true) {
                    synchronized (dept) {
                        while (dept.goods.intValue() >= 5) {
                            dept.wait();
                        }
                        dept.goods++;
                        System.out.println(Thread.currentThread().getName() + "生产完毕，当前库存数：" + dept.goods.intValue());
                        dept.notifyAll();
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * JUC锁实现
         */
        public void productWithLock(Dept dept) {
            while (true) {
                try {
                    dept.lock();
                    while (dept.goods.longValue() >= 5) {
                        dept.PCondition.await();
                    }
                    dept.goods++;
                    System.out.println(Thread.currentThread().getName() + "生产完毕，当前库存数：" + dept.goods.intValue());
                    dept.CCondition.signalAll();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dept.unlock();
                }
            }
        }
    }

    static class Costumer implements Runnable {
        private Dept dept;

        public Costumer(Dept dept) {
            this.dept = dept;
        }

        @Override
        public void run() {
            costume(dept);
        }

        /**
         * 同步锁实现
         */
        public void costume(Dept dept) {
            try {
                while (true) {
                    synchronized (dept) {
                        while (dept.goods.intValue() <= 0) {
                            dept.wait();
                        }
                        dept.goods--;
                        System.out.println(Thread.currentThread().getName() + "消费完毕，当前库存数：" + dept.goods.intValue());
                        dept.notifyAll();
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * JUC锁实现
         */
        public void costumeWithLock(Dept dept) {
            while (true) {
                try {
                    dept.lock();
                    while (dept.goods.intValue() <= 0) {
                        dept.CCondition.await();
                    }
                    dept.goods--;
                    System.out.println(Thread.currentThread().getName() + "消费完毕，当前库存数：" + dept.goods.intValue());
                    dept.PCondition.signalAll();
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dept.unlock();
                }
            }
        }
    }

//    public static void main(String[] args) {
//        Integer goods = new Integer(0);
//        Dept dept = new Dept(goods);
//        Producter producter1 = new Producter(dept);
//        Producter producter2 = new Producter(dept);
//        Costumer costumer1 = new Costumer(dept);
//        Costumer costumer2 = new Costumer(dept);
//        new Thread(producter1).start();
//        new Thread(producter2).start();
//        new Thread(costumer1).start();
//        new Thread(costumer2).start();
//    }

    public static void main(String[] args) {
        Date startDate = dateParamFormat(30);
        Date startEnd = dateParamFormat(5);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateStr = simpleDateFormat.format(startDate);
        String startDateEnd = simpleDateFormat.format(startEnd);
        System.out.println(startDateStr+"====="+startDateEnd);
    }

    private static Date dateParamFormat(Integer minutesBeforeNow){
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE,-minutesBeforeNow);
        Date date = calendar.getTime();
        return date;
    }
}
