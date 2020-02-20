package com.ruiyang.du.bo;

public class SuperMan {

    private static volatile SuperMan singleton;

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
        throw new RuntimeException("SuperMan 类设计为单例");
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
