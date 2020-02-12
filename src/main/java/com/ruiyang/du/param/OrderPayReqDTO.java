package com.ruiyang.du.param;

import java.io.Serializable;

public class OrderPayReqDTO implements Serializable {

    private String token;

    private String payTool;

    private String payType;

    private String userNo;

    private String userType;

    private String appId;

    private String openId;

    private String version;

    private String extParamMap;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPayTool() {
        return payTool;
    }

    public void setPayTool(String payTool) {
        this.payTool = payTool;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExtParamMap() {
        return extParamMap;
    }

    public void setExtParamMap(String extParamMap) {
        this.extParamMap = extParamMap;
    }

}
