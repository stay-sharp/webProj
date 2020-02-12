package com.ruiyang.du.param;

import java.io.Serializable;

public class MiniProgramPayParam implements Serializable {

    //订单号
    private String orderId;
    //订单金额
    private String orderAmount;
    //商品扩展信息
    private String goodsName;
    //商家APPID
    private String openId;

    public MiniProgramPayParam() {
    }

    public MiniProgramPayParam(String orderId, String orderAmount, String goodsName, String openId) {
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.goodsName = goodsName;
        this.openId = openId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
