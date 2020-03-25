package com.ruiyang.du.bo;

public class SuperMan {

    private static volatile SuperMan singleton;
    private static volatile boolean inited=false;

    public static SuperMan getInstance() {
        if (singleton == null) {
            synchronized (SuperMan.class) {
                if (singleton == null) {
                    singleton = new SuperMan();
                }
            }
        }
        return singleton;
    }

    private SuperMan() {
        if (inited) {
            throw new RuntimeException("SuperMan 类设计为单例");
        }
        inited = true;
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
