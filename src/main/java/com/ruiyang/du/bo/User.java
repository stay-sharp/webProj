package com.ruiyang.du.bo;

public class User {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static void main(String[] args) {
        StringBuilder logStr = new StringBuilder("调用中国银联交易接口，请求地址:[");
        logStr.append("www.qq.com").append("],").append(System.lineSeparator());
        logStr.append("请求报文:[").append("k1=v1&k2=v2").append("],").append(System.lineSeparator());
        logStr.append("通道编码:[").append("YLSJ6601").append("],").append(System.lineSeparator());
        logStr.append("请求耗时:[").append((999 - 800)).append("]ms,").append(System.lineSeparator());
        logStr.append("响应报文:[").append("RESULT=SUCCESS").append("]");
        System.out.println(logStr.toString());
    }
}
