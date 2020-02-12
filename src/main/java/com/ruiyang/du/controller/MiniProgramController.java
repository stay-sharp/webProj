package com.ruiyang.du.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruiyang.du.param.CreateOrderRespDTO;
import com.ruiyang.du.param.MiniProgramPayParam;
import com.ruiyang.du.param.OrderPayRespDTO;
import com.ruiyang.du.utils.HttpsHelper;
import com.ruiyang.du.utils.MapToObject;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.client.YopRsaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.ruiyang.du.utils.EncoderHmacSha256.encoderHmacSha256;

@RestController
@RequestMapping(value = "/api/miniprogram")
public class MiniProgramController {

    private static final String APP_ID = "wxc2b60cc7983dce85";
    private static final String APP_SECRET = "7488d0bd780da4b43d5f647bb478010c";
    private static final String parentMerchantNo = "10015386831";
    private static final String merchantNo = "10027785415";
    private static final String privateKey = "";
    private static final String CREATE_URL = "/rest/v1.0/sys/trade/order";
    private static final String PAY_URL = "/rest/v1.0/nccashierapi/api/pay";

    @RequestMapping(value = "/openid")
    public String miniProgramPreRoute(String jsCode) {
        try {
            String openId = oauthForOpenidForMiniProgram(APP_ID, APP_SECRET, jsCode);
            return openId;
        } catch (Exception e) {
            return null;
        }
    }

    private String oauthForOpenidForMiniProgram(String appId, String appSecret, String jsCode) {
        try {
            // 获取微信小程序获取openID的API接口
            String openIdUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
            openIdUrl = openIdUrl.replace("APPID", appId).replace("SECRET", appSecret).replace("JSCODE", jsCode);
            String jsonString = HttpsHelper.doGet("", openIdUrl, "utf-8");
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            String openId = jsonObject.getString("openid");
            return openId;
        } catch (Throwable e) {
            e.fillInStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/pay")
    public String miniProgramPay(String openId, String orderAmount, String goodsName, String orderId) {
        try {
            MiniProgramPayParam miniProgramPayParam = new MiniProgramPayParam(openId, orderAmount, goodsName, orderId);
            String resultData = orderPay(miniProgramPayParam);
            return resultData;
        } catch (Exception e) {
            e.fillInStackTrace();
            return null;
        }
    }

    private String orderPay(MiniProgramPayParam miniProgramPayParam) {
        CreateOrderRespDTO createOrderRespDTO = null;
        try {
            YopRequest request = new YopRequest("OPR:" + parentMerchantNo, privateKey);
            this.buildCreateOrder(request, miniProgramPayParam);
            YopResponse response = YopRsaClient.post(CREATE_URL, request);
            Map<Object, Object> createOrderResult = (Map<Object, Object>) response.getResult();
            createOrderRespDTO = (CreateOrderRespDTO) MapToObject.mapToObject(createOrderResult, CreateOrderRespDTO.class);
        } catch (Exception e) {
            e.fillInStackTrace();
            return null;
        }
        try {
            YopRequest request = new YopRequest("OPR:" + parentMerchantNo, privateKey);
            this.buildOrderPay(request, miniProgramPayParam, createOrderRespDTO.getToken());
            YopResponse response = YopRsaClient.post(PAY_URL, request);
            Map<Object, Object> orderPayResult = (Map<Object, Object>) response.getResult();
            OrderPayRespDTO orderPayRespDTO = (OrderPayRespDTO) MapToObject.mapToObject(orderPayResult, OrderPayRespDTO.class);
            return orderPayRespDTO.getResultData();
        } catch (Exception e) {
            e.fillInStackTrace();
            return null;
        }
    }

    private void buildOrderPay(YopRequest request, MiniProgramPayParam miniProgramPayParam, String token) {

        request.addParam("token", token);
        request.addParam("payTool", "MINI_PROGRAM");
        request.addParam("payType", "WECHAT");
        request.addParam("appId", APP_ID);
        request.addParam("openId", miniProgramPayParam.getOpenId());
        request.addParam("version", "1.0");
    }

    private void buildCreateOrder(YopRequest request, MiniProgramPayParam miniProgramPayParam) {
        request.addParam("parentMerchantNo", parentMerchantNo);
        request.addParam("merchantNo", merchantNo);
        request.addParam("orderId", miniProgramPayParam.getOrderId());
        request.addParam("orderAmount", miniProgramPayParam.getOrderAmount());
        request.addParam("notifyUrl", "www.baidu.com");
        Map<String, String> goodParam = new HashMap<String, String>();
        goodParam.put("goodsName", miniProgramPayParam.getGoodsName());
        goodParam.put("goodsDesc", miniProgramPayParam.getGoodsName());
        request.addParam("goodsParamExt", JSONObject.toJSONString(goodParam));

        StringBuffer signature = new StringBuffer();
        signature.append("parentMerchantNo=").append(parentMerchantNo)
                .append("&").append("merchantNo=").append(merchantNo)
                .append("&").append("orderId=").append(miniProgramPayParam.getOrderId())
                .append("&").append("orderAmount=").append(miniProgramPayParam.getOrderAmount())
                .append("&").append("notifyUrl=www.baidu.com");

        String hmac = encoderHmacSha256(signature.toString(), "27If74n392Xm670417a9a7XX6l023916l60r1Dvj71ifCFp32vs007QiH956");
        request.addParam("hmac", hmac);
    }


}
