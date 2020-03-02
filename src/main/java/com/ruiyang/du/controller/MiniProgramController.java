package com.ruiyang.du.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.ruiyang.du.bo.Merchant;
import com.ruiyang.du.param.CreateOrderRespDTO;
import com.ruiyang.du.param.MiniProgramPayParam;
import com.ruiyang.du.param.OrderPayRespDTO;
import com.ruiyang.du.utils.HttpsHelper;
import com.ruiyang.du.utils.MapToObject;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.client.YopRsaClient;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.ruiyang.du.utils.EncoderHmacSha256.encoderHmacSha256;

@Controller
@RequestMapping(value = "/miniprogram")
public class MiniProgramController {


    private static final String WX_APP_ID = "wxc2b60cc7983dce85";
    private static final String WX_APP_SECRET = "7488d0bd780da4b43d5f647bb478010c";

    private static final String ALI_APP_ID = "2021001135646782";
    private static final String ALI_APP_PRI_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCz0b8/lX1ImAQa" +
            "xRMaB41zPcmp4HAJ8qpcKFu0hreVswOxuo1ODZTTD2oRMNwPUbEuYqrs8jLMyVOA" +
            "IzYRqWXPK8BLADlUxfU10MuVqNdbqi631l4tRHb8p8nC5DnF5q6qkLVkEtEnRjdB" +
            "m3FMiTBJ++ye5Mk5fDmyWIVan/XsPjQJGDmfnMfeJTY+QQzlE6C8Fq7fvZ1a94uD" +
            "rSGyxSxfzHnSNxPD+ixnvDc6uMzui21JOnW4PttwFlWCB148xN+O3v4zKTkFjWdL" +
            "BDiVErBMQzthw7dU8we2wvRocD2FIaT136dir+CEebg+TpsM4rc7NOmpqAXEBxf0" +
            "3erjDguTAgMBAAECggEAbihMFNOrUFFwEkYwkX2W9cYgSGUpva5fFt3ScCEnThnA" +
            "ZZx2/B3t0e5bd/SKmwfclyHyu/0Tici70n+/m8o+iE+xGUEl3fz3XuRuE1y/sXPr" +
            "8srsHqcWjWjsfOv7SHUtA2hq9sdgQcMODq7/FXflkS03o49chk7Hua3o47+ik/nw" +
            "Tc8I2c8GzH4WJeH0X67r5N3nzZENPH5vZq5fzYSrvNO0k0o+M7+Lz/8UPXjjtKWz" +
            "59VToigip+qlro/3c59bKmiMVNbSpterlPo4pxaU1LEA/5YsN9RZq81ZfCLYOE/Q" +
            "zqxLNdCmeb3qeXbnZZOqbAU11G6SenZM+NyTJwJb4QKBgQDbWbsCmH0OjLzAgPGI" +
            "cv8QZCYLBd1dg0aR4U1ffOYVltmbXsBI0/Xnip4S+tL/2S+GCD3aVf6rFcIb5xIc" +
            "f0wQBAUE0HQQpXTU6Ce7f5H5srlt0xfcW5YsLscnmJO0OWAdHG2dkPMf0Q9a/tEB" +
            "JEX8CfpTlMiLErge45STPcyoVwKBgQDR3SakNkjcUJYSmWxtJgbryFTzDKb4rAcb" +
            "umofo2O8W6KYAsmmp+avbd6Hb/57qN/kNXnRR82JMEmUOBZzyOfyS5ltbWZfw1LW" +
            "M5Z9owNNeF6I3ewuixKgaH2J+kz9BTUJ3Hk+k7VhN9ANxsOBs2NRbCYyCbl1EySj" +
            "HnACQV2hJQKBgAteok/nIYxQhTQhOuPMuiXaWxJ1Ml03gb/sAruxWKfD0vG3vpXH" +
            "ztY6nCj1sVwroMf8w1/+1x19GjuiBZFOk928p5nxSCLYwK6Mfgq8+Q8NUYk8yN7W" +
            "LHmLzyzhPr63vqCjqZbiPYfnqTkZPjIl9C61QbF1Y9O5m6uWNkxPhr0vAoGBAMX2" +
            "5GWQ44gX6Ijg+xu2/F5cWm3vKG548Dei0ErJ+OIMWsMkFg5EYeJoNLrz1U3XDmQa" +
            "YbAeGG9qAm5zvhI4mTvBPpiv0xtQiAqRHUukgxAIFLztAscg31A7YPjQCOHAkLid" +
            "pi12bmmnmXX1YiUuD+kVqK4Y6D3c8fmBU8181ZKVAoGBAL8i+k4ZuUTPoBJAwy9p" +
            "ZERNsAzSWwqDc91j3zUC3JVNz9kFWK4Dt0BcfK7xNTG4xW5Qd5tEi6552NogFDOi" +
            "kNGwQxAhwzmRVbOHFINv52XVYmRxWCvITq2lbF4UlVM4v+THVk9hy5U77oAW0dwx" +
            "znWwCxW1oodVQJSGIR39ULvi";
    private static final String ALI_PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqmYPmFUgOgB2yu91SC9CgLnz07wQyk1qp9mMjo5EVHtSI8Qe15bnokJF9omiqzjVjWQ9gs0Nsbz7VAAHrJ+1Vu1ur1qX0oeP8EjcJwEqAUZf/SECx4z1I/rWKgLPCUvjhsSLPd/eoDwGo0IIf1eyW+SIa+IdIaMxkqCNwHGkZBsHEGjxgS8S/fyTE12GzhDFoJpE+nzitcttkqmDluK4ndTkupFMVFC83ClJBEL6rAx6UG0qPjDBDoTWfbGNGOv5njE2ffwycufCO6aJUwE9yWiOAL/yumqLYjO7jw75K1P+fO0EGyd2ycvoe0mXH0enpxjvPfLgJWOKFAU38eKzpwIDAQAB";


    private static final String CREATE_URL = "/rest/v1.0/sys/trade/order";
    private static final String PAY_URL = "/rest/v1.0/nccashierapi/api/pay";

    private static final Logger logger = LoggerFactory.getLogger(MiniProgramController.class);

    private static Map<String, Merchant> merchantMap = new HashedMap();
    private static AlipayClient alipayClient = null;
    static {
        Merchant merchant1=new Merchant();
        merchant1.setMerchantNo("10028170395");
        merchant1.setHmacKey("1j9gmQIk3549fNlf7n8i9GqAyM733mEo9x80SYTW05IiFuB37VZD699v8UC8");
        merchantMap.put("10028170395",merchant1);
        Merchant merchant2=new Merchant();
        merchant2.setMerchantNo("10027785415");
        merchant2.setHmacKey("27If74n392Xm670417a9a7XX6l023916l60r1Dvj71ifCFp32vs007QiH956");
        merchantMap.put("10027785415",merchant2);

        alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",ALI_APP_ID,ALI_APP_PRI_KEY,"json","GBK",ALI_PUB_KEY,"RSA2");
    }

    /**
     * 小程序-用户授权
     * @param jsCode
     * @return
     */
    @RequestMapping(value = "/openid")
    @ResponseBody
    public String miniProgramUserAuth(HttpServletRequest request, String jsCode) {
        Map<String, String> map = new HashedMap();
        try {
            if (StringUtils.isBlank(jsCode)) {
                map.put("msg", "jsCode is null");
                return JSONObject.toJSONString(map);
            }
            String platform = getHttpEnv(request);
            if ("wechat".equalsIgnoreCase(platform)) {
                return oauthForWXMiniProgram(WX_APP_ID, WX_APP_SECRET, jsCode);
            } else if ("alipay".equalsIgnoreCase(platform)) {
                return oauthForAliMiniProgram(jsCode);
            } else {
                map.put("msg", "check env failed");
                return JSONObject.toJSONString(map);
            }
        } catch (Exception e) {
            logger.error("wxMiniProgramUserAuth() error", e);
            map.put("msg", "get wx openId failed");
            return JSONObject.toJSONString(map);
        }
    }

    private String oauthForWXMiniProgram(String appId, String appSecret, String jsCode) {
        Map<String,String> map = new HashedMap();
        try {
            // 获取微信小程序获取openID的API接口
            String openIdUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
            openIdUrl = openIdUrl.replace("APPID", appId).replace("SECRET", appSecret).replace("JSCODE", jsCode);
            String jsonString = HttpsHelper.doGet("", openIdUrl, "utf-8");
            logger.info("oauthForOpenidForMiniProgram() 请求微信授权登录，返回：" ,jsonString);
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            String openId = jsonObject.getString("openid");
            if (openId != null) {
                map.put("openId",openId);
                map.put("msg","success");
                return JSONObject.toJSONString(map);
            }
            map.put("msg",jsonObject.getString("errmsg"));
            return JSONObject.toJSONString(map);
        } catch (Throwable e) {
            logger.error("oauthForWXMiniProgram() error" ,e);
            map.put("msg","request wechat oauth2 failed");
            return JSONObject.toJSONString(map);
        }
    }

    private String oauthForAliMiniProgram(String authCode) {
        Map<String,String> map = new HashedMap();
        try {
            AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
            request.setGrantType("authorization_code");
            request.setCode(authCode);
            AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
            logger.info("oauthForAliMiniProgram() 请求支付宝授权登录，返回：" ,JSONObject.toJSONString(response));
            if(response.isSuccess()){
                map.put("openId",response.getUserId());
                map.put("msg","success");
                return JSONObject.toJSONString(map);
            } else {
                map.put("msg",response.getMsg());
                return JSONObject.toJSONString(map);
            }
        } catch (Throwable e) {
            logger.error("oauthForAliMiniProgram() error" ,e);
            map.put("msg","request alipay oauth2 failed");
            return JSONObject.toJSONString(map);
        }
    }

    /**
     * @param openId
     * @param orderAmount
     * @param goodsName
     * @param orderId
     * @param merchantNo 选填，空则默认10027785415
     * @return
     */
    @RequestMapping(value = "/pay")
    @ResponseBody
    public String miniProgramPay(HttpServletRequest request, String openId, String orderAmount, String goodsName, String orderId, String merchantNo) {
        Map<String,String> map = new HashedMap();
        try {
            if(StringUtils.isBlank(openId) || StringUtils.isBlank(orderAmount) || StringUtils.isBlank(goodsName) ||
                    StringUtils.isBlank(orderId)){
                map.put("msg","param is null");
                return JSONObject.toJSONString(map);
            }
            String platform = getHttpEnv(request);
            if(!"wechat".equalsIgnoreCase(platform) && !"alipay".equalsIgnoreCase(platform)){
                map.put("msg", "check env failed");
                return JSONObject.toJSONString(map);
            }
            MiniProgramPayParam miniProgramPayParam = new MiniProgramPayParam(orderId, orderAmount, goodsName, openId);

            Merchant merchant = merchantMap.get(merchantNo);
            if (merchant == null) {
                merchant = merchantMap.get("10027785415");
            }
            String resultData = orderPay(miniProgramPayParam,merchant,platform);
            return resultData;
        } catch (Exception e) {
            logger.error("miniProgramPay() error" ,e);
            map.put("msg","pay error");
            return JSONObject.toJSONString(map);
        }
    }

    private String orderPay(MiniProgramPayParam miniProgramPayParam, Merchant merchant, String platform) {
        Map<String,String> map = new HashedMap();
        CreateOrderRespDTO createOrderRespDTO = null;
        try {
            YopRequest request = new YopRequest("OPR:" + merchant.getParentMerchantNo(), merchant.getPrivateKey());
            this.buildCreateOrder(request, miniProgramPayParam, merchant);
            YopResponse response = YopRsaClient.post(CREATE_URL, request);
            Map<Object, Object> createOrderResult = (Map<Object, Object>) response.getResult();
            createOrderRespDTO = (CreateOrderRespDTO) MapToObject.mapToObject(createOrderResult, CreateOrderRespDTO.class);
            if(createOrderRespDTO==null || StringUtils.isBlank(createOrderRespDTO.getToken())){
                map.put("msg","opr error");
                return JSONObject.toJSONString(map);
            }
        } catch (Exception e) {
            logger.error("调用OPR接口异常 error" ,e);
            map.put("msg","opr error");
            return JSONObject.toJSONString(map);
        }
        try {
            YopRequest request = new YopRequest("OPR:" + merchant.getParentMerchantNo(), merchant.getPrivateKey());
            this.buildOrderPay(request, miniProgramPayParam, createOrderRespDTO.getToken(), platform);
            YopResponse response = YopRsaClient.post(PAY_URL, request);
            Map<Object, Object> orderPayResult = (Map<Object, Object>) response.getResult();
            OrderPayRespDTO orderPayRespDTO = (OrderPayRespDTO) MapToObject.mapToObject(orderPayResult, OrderPayRespDTO.class);
            if(orderPayRespDTO==null || StringUtils.isBlank(orderPayRespDTO.getResultData())){
                map.put("msg","cashier error");
                return JSONObject.toJSONString(map);
            }
            map.put("prepayCode",orderPayRespDTO.getResultData());
            map.put("msg","success");
            return JSONObject.toJSONString(map);
        } catch (Exception e) {
            logger.error("调用收银台接口异常 error" ,e);
            map.put("msg","cashier error");
            return JSONObject.toJSONString(map);
        }
    }

    private void buildOrderPay(YopRequest request, MiniProgramPayParam miniProgramPayParam, String token, String platform) {

        request.addParam("token", token);
        if("wechat".equalsIgnoreCase(platform)){
            request.addParam("payTool", "MINI_PROGRAM");
            request.addParam("payType", "WECHAT");
            request.addParam("appId", WX_APP_ID);
        }else {
            request.addParam("payTool", "ZFB_SHH");
            request.addParam("payType", "ALIPAY");
            request.addParam("appId", ALI_APP_ID);
        }
        request.addParam("openId", miniProgramPayParam.getOpenId());
        request.addParam("userIp", "127.0.1.0");
        request.addParam("version", "1.0");
    }

    private void buildCreateOrder(YopRequest request, MiniProgramPayParam miniProgramPayParam, Merchant merchant) {
        request.addParam("parentMerchantNo", merchant.getParentMerchantNo());
        request.addParam("merchantNo", merchant.getMerchantNo());
        request.addParam("orderId", miniProgramPayParam.getOrderId());
        request.addParam("orderAmount", miniProgramPayParam.getOrderAmount());
        request.addParam("notifyUrl", "http://www.baidu.com/back");
        Map<String, String> goodParam = new HashMap<String, String>();
        goodParam.put("goodsName", miniProgramPayParam.getGoodsName());
        goodParam.put("goodsDesc", miniProgramPayParam.getGoodsName());
        request.addParam("goodsParamExt", JSONObject.toJSONString(goodParam));

        StringBuffer signature = new StringBuffer();
        signature.append("parentMerchantNo=").append(merchant.getParentMerchantNo())
                .append("&").append("merchantNo=").append(merchant.getMerchantNo())
                .append("&").append("orderId=").append(miniProgramPayParam.getOrderId())
                .append("&").append("orderAmount=").append(miniProgramPayParam.getOrderAmount())
                .append("&").append("notifyUrl=http://www.baidu.com/back");

        String hmac = encoderHmacSha256(signature.toString(), merchant.getHmacKey());
        request.addParam("hmac", hmac);
    }

    private String getHttpEnv(HttpServletRequest request){
        String ua = request.getHeader("User-Agent");
        logger.info("getHttpEnv() ua = " + ua);
        if(ua.contains("MicroMessenger")){
            return "wechat";
        }else if(ua.contains("Alipay")){
            return "alipay";
        }else {
            return "unknown";
        }
    }
}
