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

    private static final String ALI_APP_ID = "2021001132671251";
    private static final String ALI_APP_PRI_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCOv6yzh0cEz/ECie1DTZZMHz33iFjhDYFLEDM+WRn23gXOcRyXzktj+Ds35YZKdvcz8FEkBu68qr6x+DocUVBtkMoek5C9JhXiUUKsc+42erLy/K9qHP8ObMIB35z+hQNW9ooFn3Z0rSD8WvhPfy1TxNE65cRNjcP6gOq7b4EKCKj8TtXxaQ3DM1HA69RP9Lzh/0Vwpo3as98H9d7qwTsCYgSsIeiNA5znWcq/qI3V950UQdWy6OVn6Bii/obAWg70dg8RYobaogTPnM1/MuXZ4xoLgSwAa2dc74hOW6ALib8Xa4UQVzSadh1+SKgoXdFVZU2OLoCunBG+yBj5CvLnAgMBAAECggEAJPemdee2kJx5QbsOYruZDKWampwIyF+C+Dl9y8uvWastvcS1LusRwxXp4BPkrywX4K9/k+DnupxNPjGWevbj6Tvx2/FDLnjKeg2pQpfTPPCRnzOpCcXwl3+6zllmgtNXhhHatJITTyv2uuOkIpJA3sy9ko8mQx9PfXQdVeUOedZVKxV/YRlwMEa3YjhCrmZqfEM8mr7GX5Z08ilE1nIBmgOi4wI9jcFsdEYAA4XMa1irbEuf0t2Yn+1crelxOsDep8diQw8FFj1aN7fKrhDQ8kdyh8lK8mgrP/1BKL7blAgPY+9rwlsGyNF+2wpZ7GuM1sdJRmXGgocIwN2nZnt3QQKBgQD+1Yu7AUkCb5fyV9g8JOYPwN/D0mLQJbhJK7ht2lJHIzi+8vp/bGjB9bXul+SKd7x7Q6tCz2k37INxEdbjW29v5r0cSCkDsdVI6nmVV3COej/c/WqqGpU0+6NcXqyxc8eAjCG/AhQeMTVGzwJa2+3h+gBkTdsHumM99u7bD3OfrwKBgQCPZtuQXtOzkvmPbDgkxKcg7nnvVktybxpJpfc6Kfjv8x0Cxa/CLm/dFyGX1WGrJDuiIdAhmloJWUYtKK77W/SOJm3NCj8AJ2AQd9dXrqzRFQ7dA4gycQrInGr8PeB2Pe0HwqIMQisOoVw8R7jc6eN09pK0T/eRr8d4IkEDcjS2SQKBgDPIJsWQJErHbEPFT94gzVLkKVP8OzH6iDDuHmLMGy3zXUOH0hj0aXEIQbjCyqDMZdNSU8mWpdXETI08R8MZQFKnu0O08gibKTDQ3HXZN7jr5HA+Wl9IWYtTmsVZQ5QnZ6GaV/0dKgNckmvaAqt9xx/FEjFPlLNET4g21iBTSnZnAoGAaF0jTl61E+vTVF4ePiQgvgbRFy5mmlHhf9gZpWsGkRsN8gaNc3T4VJ+RAz94ZWVpQQX6w3niLQub1yHUt3qk8Q9mueLLFwGRGGsffeDSRRheSNtZM9yxTG8KLamawnMvHoa4CNPfIjatOfG4yI4vy9/nxPKt6olTUksbpbZoRoECgYEAy4JJBIqEXE3Xoh5qSuo74l3o8qcpAqIYQB5XDrDyw9sd2dt3PeXhNZfC5oZ4bvHB9xhNiul82PlGWdPqAT2HudsQp/lAOqgKh90hXHYMHfbbL/cBy5YYoxVxgDZ3WlPBgwVXKkjNSotVNdCxCu5ue9NRJNoNJAyI4QbefxvPR4o=";
    private static final String ALI_PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiqD8rkQMmhR1z0N+s3zQ4o2TiEeXe+UwVm69LnnKYzVnLZsX2KZvV9ktasWP5QlHlloDf3SQd5iQAkjdoWb3V65drDUyVRDc40KrS9Ztl/TKgNHF6Yq5vEpqCt2Q4rLltKZTJ68AocYO1OOrGpg60HpjSeeoUdfAP/oyGtA84EpUFq31EAVZbk6vH3lkq6PkooIfqsCFA8f+NYh8DxN1aUFFatgyTKPuZYvM+HKajWdN5rVLUtJ2JuKEWQZOIxVPd+CNB15zoaM/8hn3g1UhCtPto981dj/HFHf/1fK/qjczcGjgZt2FWbxOue9Cw8e7Npo/oUlyHT6p0KC8+zGsZQIDAQAB";


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
