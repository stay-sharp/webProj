package com.ruiyang.du.demo;


public class ProducterCostumerDemo {

    static class Dept {
        private Integer goods;
        public Dept(Integer goods) {
            this.goods = goods;
        }

        public synchronized void product() {
            try {
                /*同步锁实现start*/
                while (true) {
                    while (goods.intValue() >= 5) {
                        this.wait();
                    }
                    goods++;
                    System.out.println(Thread.currentThread().getName() + "生产完毕，当前库存数：" + goods.intValue());
                    if (goods.intValue() > 0) {
                        this.notifyAll();
                    }
                    Thread.sleep(100);

                }
                /*同步锁实现end*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void costume() {
            try {
                /*同步锁实现start*/
                while (true) {
                    while (goods.intValue() <= 0) {
                        this.wait();
                    }
                    goods--;
                    System.out.println(Thread.currentThread().getName() + "消费完毕，当前库存数：" + goods.intValue());
                    if (goods.intValue() < 5) {
                        this.notifyAll();
                    }
                    Thread.sleep(100);

                }
                /*同步锁实现end*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    static class Producter implements Runnable {
        private Dept dept;

        public Producter(Dept dept) {
            this.dept = dept;
        }

        @Override
        public void run() {
            dept.product();
        }
    }

    static class Costumer implements Runnable {
        private Dept dept;

        public Costumer(Dept dept) {
            this.dept = dept;
        }

        @Override
        public void run() {
            dept.costume();
        }
    }

    public static void main(String[] args) {
        Integer goods = new Integer(0);
        Dept dept = new Dept(goods);
        Producter producter1 = new Producter(dept);
        Producter producter2 = new Producter(dept);
        Costumer costumer1 = new Costumer(dept);
        Costumer costumer2 = new Costumer(dept);
        new Thread(producter1).start();
        new Thread(producter2).start();
        new Thread(costumer1).start();
        new Thread(costumer2).start();
    }
}
