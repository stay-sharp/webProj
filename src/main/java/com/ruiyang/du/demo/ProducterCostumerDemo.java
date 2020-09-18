package com.ruiyang.du.demo;


import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
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
    public static final String priKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDKLgI+64mmJdNg1TwlCPBnNH3b3qfw2TdHVc2uDd4LTyQI8nRr0heFhhdj0OZi6agqekIyzAH/XmO9PdLrTi4YXJXOfiO/dYwKA6gSktRe6FKY4C2WzX1yA4fGfqJMV7RYVoL6In50Hur6rGnavNSQZqbiDJOgy5yokJ14Mey1iMqqqWvADtKN9SqxtbyIxYD/jj/6qLWwmu88wSwSaGdO3wNFgzajsHgRJe9G9IhD0zr5d72HvJGoedq7VaPn3jhIszcPQE6oqbXAddZRGKBehA4WSCjLEl87XH33zZPrxrQlBTHVVGzfxjbB4QvYz0hlEoWh1ntxeDHTfgyhdPQpAgMBAAECggEATmxMSLW6Xe08McpkmwT9ozq0Oy4BvKW1EIGS15nfcEmRc7sAN7Z1k0BxIDGuu91gcqGbvfJuL+0gCQ7LGqTnsmFvZnp9SU3CNTw33ISBxhKdv1jtthodN7Vw3CjQsYYvmThtc7Mfk9FOWk+4e7VVSnHW98XjGbMBIE2AF1heNgeZ40ubdgzuz9+4g4pphjWncPpwcaMfsDZm3JtFyvUp0+LME0CmUqrxvONZAkpFR/PyejGHnIh3ptHzhe/VjNcuIC4PphkCNBakCBCrtohTy0YeeWfDAUTAO4tPXF/JUhlxjPuqR6rpQY/0uQdMAtTpiWHVJar7eGdK81QnuuOFRQKBgQDrklUPM0pkvGG/wREa0bgUI+ki+1/wv7O8X94/8onomJqPpkD8z4hv/Lev/wD5gDcgmgLC36u/XDuhFfVNOmw4eUWenU6pzonroEjhi91AKcRRfzDfOfWg3wPm1J9WQOn5A033tNRydCpVcX/Ot4qDbKcAwLiPNPXXMTn4LUQE/wKBgQDbtmE0KS/kSfjscWJOqwv1XbxckipkxncqIbdiSdU+DzaLd+Vuaco7TLQJRFp7S7WJW4Tz6KBX2UiA7O7ezXY9PwlgXxXiZDDtneXNAqk7DNxmTTZHrF2C7qdU98klppCFiFx9bysGY6lFWofWmg3Pu5IiPqO3iLRPTvZgQOE+1wKBgQC9SCgmfYzyIlfcjtIinY5uSGiEnjz5od9WpiVbdpOPHEdc0zZ2rH6xlPs3ZAuxbm9dN8KuOLC0ovSau50Nv7rDKdZh234gfP9fH7xP1mUhsC25Why30MdnyqpE6GVbFe+qERitx1PI30RAwWDzhZC7hystNK1XDDPZBAnTOvPjmwKBgDFuujX7IkxRnFDOPdkHQNyGp2+Ib0NXJ85x4YmapQCeeZ4tbpBF+vsWidcf6t+crA5oaeRarWC2gUqIhEHapkSnXxuwqQLTmfKMOPzEIYEoppnZu2Gq1Ss1OK60RSxUamWwxWZvUZXRbG8vLCrLZFodkIZl433SowbI9EO5tTPnAoGAJRsy1z95Q1GPkKrFtKivkxZy1k7zJXjM0VWDc7lT9fBnoeGUyt+vuq+lC5i2aiWKJK7pe8MM9QFDGlWPnly+J8jbyMfm99k5oJtCWDfF0or1pAQ4mw0kjL9TvDVXdojgYA+rxSMQ09hwsYukQ4bblrwfBUmRjLN5WibcRzIW5ZA=";

    private static String json="{\"response\":[\"hBE7DzFh4C24eyCMqFRf6NaQwp_gZVST6sMN3iEohYgSWrC8t0k4CmEzkdxhmx16o6yzR15CFu0h6fIOgB0GXyIIiQ0AiU1BuyR-MhZb-zS2j0WQHPxeqv0DfVIGRouTgkvESjOgzKuq6K753FMqI74i3nBViOxdDykrko_7j5BPyXL9if8aEXdFeFWPj4RhdQrKoAbrv_FMsLoVEzZVLUOYXcjB3IgJaUYJ-mD0WZIXXIj-cCTeXbn9O0of5K8sTwdpjhVK-LWYsRFmS-I1TRdT2yKL9sGmrvjfYWUNWlOBXihz--JmHrXTMbitgOwSilfCORo4x3tRPZ5mL1vHWg$bamqMRxcSE9M9StFLECMteF73YeuesejC0bpO2nsJ0Xu40mc7jN7qrrGLuH6B8rhKPdxi1XGhcsuJSlOzO5CRbQvL6XALriESa6rAYrPoFhJ6o5CGxHGX8PCWOqucwU0tOlLk43a_D0lc3XNDzkn59Jr_hRwBzR67-BDwxsJ6nz9KdX5q3P7pwy1keBrazmSbmIM4p2pokHbL1E8LAgc1OXq4zLwHomeOs9wW2RCt7VhMlVpoqrZEhtLwbYIOiyFsGbY8ZA0iPgL_7CYGz7DGQnapMin6lz9yx0uMWQy32xe_IXFQjzhdIYzMIUyCw7Q7mrtKIofHbkfEaQyZoD6y65MSHAYDZwIMTA65vpolnNVM9u2gthdHoIwlrjyCRj_WhLxtw9CFhacH2XFZGlrRZSERAvyGKEdpC-_DlBLMKFA6kkA20eoVLUVNa2PwckwcFgyFrmeFplug0m8vzEKsJyyjNU290WXcm1QyR5Eq2r4oE7w4iGj3YL7QyGeeq_1vHfF9NkHfK-MG9Qcg5wqHfyf9U93642e5HGOIYVMb8KvO2UwE_LcAhDvWUV9Gyl7Kj-IkioG7br4PYF7w32J_tmKLNUbgrXq6YvqwN8F1v5F688cINWPRMBa2VClFDR25_C5zc_myVF-AFIPcHu1YJ5mahRnaA7AWT8oqGFcaJ3lluuSMYRcbQvUWZsSCoWkcdzyVeOWkGadNQAybK4oHmdunyLDJV_iNw7xu557HHwyVBMqX6AA2AvCd984IZP-sYjk4xpFoyt2CBGP3OiNSSEIzuc_g8CqWdCPkbd-IYdRgysj2wIYJvMSpPyzLbjLtp6pzFIsn23m3qSwPAFcsIXO8cNPpHrY8ccy9t8MxNQ$AES$SHA256\"],\"customerIdentification\":[\"app_10040041322\"]}";

    private static String content="hBE7DzFh4C24eyCMqFRf6NaQwp_gZVST6sMN3iEohYgSWrC8t0k4CmEzkdxhmx16o6yzR15CFu0h6fIOgB0GXyIIiQ0AiU1BuyR-MhZb-zS2j0WQHPxeqv0DfVIGRouTgkvESjOgzKuq6K753FMqI74i3nBViOxdDykrko_7j5BPyXL9if8aEXdFeFWPj4RhdQrKoAbrv_FMsLoVEzZVLUOYXcjB3IgJaUYJ-mD0WZIXXIj-cCTeXbn9O0of5K8sTwdpjhVK-LWYsRFmS-I1TRdT2yKL9sGmrvjfYWUNWlOBXihz--JmHrXTMbitgOwSilfCORo4x3tRPZ5mL1vHWg$bamqMRxcSE9M9StFLECMteF73YeuesejC0bpO2nsJ0Xu40mc7jN7qrrGLuH6B8rhKPdxi1XGhcsuJSlOzO5CRbQvL6XALriESa6rAYrPoFhJ6o5CGxHGX8PCWOqucwU0tOlLk43a_D0lc3XNDzkn59Jr_hRwBzR67-BDwxsJ6nz9KdX5q3P7pwy1keBrazmSbmIM4p2pokHbL1E8LAgc1OXq4zLwHomeOs9wW2RCt7VhMlVpoqrZEhtLwbYIOiyFsGbY8ZA0iPgL_7CYGz7DGQnapMin6lz9yx0uMWQy32xe_IXFQjzhdIYzMIUyCw7Q7mrtKIofHbkfEaQyZoD6y65MSHAYDZwIMTA65vpolnNVM9u2gthdHoIwlrjyCRj_WhLxtw9CFhacH2XFZGlrRZSERAvyGKEdpC-_DlBLMKFA6kkA20eoVLUVNa2PwckwcFgyFrmeFplug0m8vzEKsJyyjNU290WXcm1QyR5Eq2r4oE7w4iGj3YL7QyGeeq_1vHfF9NkHfK-MG9Qcg5wqHfyf9U93642e5HGOIYVMb8KvO2UwE_LcAhDvWUV9Gyl7Kj-IkioG7br4PYF7w32J_tmKLNUbgrXq6YvqwN8F1v5F688cINWPRMBa2VClFDR25_C5zc_myVF-AFIPcHu1YJ5mahRnaA7AWT8oqGFcaJ3lluuSMYRcbQvUWZsSCoWkcdzyVeOWkGadNQAybK4oHmdunyLDJV_iNw7xu557HHwyVBMqX6AA2AvCd984IZP-sYjk4xpFoyt2CBGP3OiNSSEIzuc_g8CqWdCPkbd-IYdRgysj2wIYJvMSpPyzLbjLtp6pzFIsn23m3qSwPAFcsIXO8cNPpHrY8ccy9t8MxNQ$AES$SHA256";




    private static PublicKey getPubKey() {
        PublicKey publicKey = null;
        try {
            //易宝公钥请勿修改
            String publickey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyi4CPuuJpiXTYNU8JQjwZzR9296n8Nk3R1XNrg3eC08kCPJ0a9IXhYYXY9DmYumoKnpCMswB/15jvT3S604uGFyVzn4jv3WMCgOoEpLUXuhSmOAtls19cgOHxn6iTFe0WFaC+iJ+dB7q+qxp2rzUkGam4gyToMucqJCdeDHstYjKqqlrwA7SjfUqsbW8iMWA/44/+qi1sJrvPMEsEmhnTt8DRYM2o7B4ESXvRvSIQ9M6+Xe9h7yRqHnau1Wj5944SLM3D0BOqKm1wHXWURigXoQOFkgoyxJfO1x9982T68a0JQUx1VRs38Y2weEL2M9IZRKFodZ7cXgx034MoXT0KQIDAQAB";
            java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
                    new BASE64Decoder().decodeBuffer(publickey));
            java.security.KeyFactory keyFactory;
            keyFactory = java.security.KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(bobPubKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    private static PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        //商户私钥
        PKCS8EncodedKeySpec priPKCS8;
        try {
            priPKCS8 = new PKCS8EncodedKeySpec(
                    new BASE64Decoder().decodeBuffer(priKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            privateKey = keyf.generatePrivate(priPKCS8);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }
}
