package com.ruiyang.du.controller;

import com.alibaba.fastjson.JSONObject;
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

import java.util.HashMap;
import java.util.Map;

import static com.ruiyang.du.utils.EncoderHmacSha256.encoderHmacSha256;

@Controller
@RequestMapping(value = "/miniprogram")
public class MiniProgramController {


    private static final String APP_ID = "wxc2b60cc7983dce85";
    private static final String APP_SECRET = "7488d0bd780da4b43d5f647bb478010c";

    private static final String CREATE_URL = "/rest/v1.0/sys/trade/order";
    private static final String PAY_URL = "/rest/v1.0/nccashierapi/api/pay";

    private static final Logger logger = LoggerFactory.getLogger(MiniProgramController.class);

    private static Map<String, Merchant> merchantMap = new HashedMap();
    static {
        Merchant merchant1=new Merchant();
        merchant1.setMerchantNo("10027782797");
        merchant1.setHmacKey("p9183gnJ3b7848yGa31y3V6PN4NvykSx179v781M8vFG4VTybdj8891V0s3p");
        merchantMap.put("10027782797",merchant1);
        Merchant merchant2=new Merchant();
        merchant2.setMerchantNo("10027785415");
        merchant2.setHmacKey("27If74n392Xm670417a9a7XX6l023916l60r1Dvj71ifCFp32vs007QiH956");
        merchantMap.put("10027785415",merchant2);

    }

    @RequestMapping(value = "/openid")
    @ResponseBody
    public String miniProgramPreRoute(String jsCode) {
        Map<String,String> map = new HashedMap();
        try {
            if(StringUtils.isBlank(jsCode)){
                map.put("msg","jsCode is null");
                return JSONObject.toJSONString(map);
            }
            return oauthForOpenidForMiniProgram(APP_ID, APP_SECRET, jsCode);
        } catch (Exception e) {
            logger.error("miniProgramPreRoute() error" ,e);
            map.put("msg","get openId failed");
            return JSONObject.toJSONString(map);
        }
    }

    private String oauthForOpenidForMiniProgram(String appId, String appSecret, String jsCode) {
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
            logger.error("oauthForOpenidForMiniProgram() error" ,e);
            map.put("msg","request wechat oauth2 failed");
            return JSONObject.toJSONString(map);
        }
    }

    @RequestMapping(value = "/pay")
    @ResponseBody
    public String miniProgramPay(String openId, String orderAmount, String goodsName, String orderId, String merchantNo) {
        Map<String,String> map = new HashedMap();
        try {
            if(StringUtils.isBlank(openId) || StringUtils.isBlank(orderAmount) || StringUtils.isBlank(goodsName) ||
                    StringUtils.isBlank(orderId)){
                map.put("msg","param is null");
                return JSONObject.toJSONString(map);
            }
            MiniProgramPayParam miniProgramPayParam = new MiniProgramPayParam(orderId, orderAmount, goodsName, openId);
            Merchant merchant = merchantMap.get(StringUtils.isBlank(merchantNo) ? "10027785415" : merchantNo);
            String resultData = orderPay(miniProgramPayParam,merchant);
            return resultData;
        } catch (Exception e) {
            logger.error("miniProgramPay() error" ,e);
            map.put("msg","pay error");
            return JSONObject.toJSONString(map);
        }
    }

    private String orderPay(MiniProgramPayParam miniProgramPayParam, Merchant merchant) {
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
            this.buildOrderPay(request, miniProgramPayParam, createOrderRespDTO.getToken());
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

    private void buildOrderPay(YopRequest request, MiniProgramPayParam miniProgramPayParam, String token) {

        request.addParam("token", token);
        request.addParam("payTool", "MINI_PROGRAM");
        request.addParam("payType", "WECHAT");
        request.addParam("appId", APP_ID);
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


}
