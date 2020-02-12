package com.ruiyang.du.param;

import java.io.Serializable;

public class OrderPayRespDTO implements Serializable {

    private String code;

    private String message;

    private String payTool;

    private String payType;

    private String token;

    private String merchantNo;

    private String resultType;

    private String resultData;

    private String extParamMap;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public String getExtParamMap() {
        return extParamMap;
    }

    public void setExtParamMap(String extParamMap) {
        this.extParamMap = extParamMap;
    }
}
