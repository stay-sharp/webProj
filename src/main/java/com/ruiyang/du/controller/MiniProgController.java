package com.ruiyang.du.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.facade.opr.dto.order.ext.GoodsParamExtDTO;
import com.yeepay.g3.facade.opr.dto.order.ext.PaymentParamExtDTO;
import com.yeepay.g3.facade.opr.dto.yop.order.YopCreateOrderReqDTO;
import com.yeepay.g3.facade.opr.dto.yop.order.YopCreateOrderResDTO;
import com.yeepay.g3.facade.opr.facade.yop.YopOrderFacade;
import com.yeepay.g3.facade.yop.ca.dto.DigitalSignatureDTO;
import com.yeepay.g3.facade.yop.ca.enums.CertTypeEnum;
import com.yeepay.g3.facade.yop.ca.enums.DigestAlgEnum;
import com.yeepay.g3.facade.yop.ca.facade.DigitalSecurityFacade;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(value = "/miniprogram")
public class MiniProgController {


    private static final Logger logger = LoggerFactory.getLogger(MiniProgramController.class);


    /**
     * 小程序OPR下单
     * 供APP调用，会调用OPR下单，返回token及必要参数签名
     *
     * @param request
     * @param orderAmount
     * @param goodsName
     * @return
     */
    @RequestMapping(value = "/token")
    @ResponseBody
    public String miniProgramPay(HttpServletRequest request, String orderAmount, String goodsName) {
        Map<String, Object> result = new HashedMap();
        if (StringUtils.isBlank(orderAmount) || StringUtils.isBlank(goodsName)) {
            result.put("msg", "param is null");
            return JSONObject.toJSONString(result);
        }
        String orderId = "t_minipro_" + System.currentTimeMillis();

        //请求OPR下单
        YopCreateOrderResDTO createOrderRespDTO = null;
        try {
            createOrderRespDTO = oprOrder(orderId, orderAmount, goodsName);
        } catch (Exception e) {
            logger.error("调用OPR接口异常 error", e);
            result.put("msg", "opr error");
            return JSONObject.toJSONString(result);
        }
        if (createOrderRespDTO == null || StringUtils.isBlank(createOrderRespDTO.getToken())) {
            result.put("msg", "opr error");
            return JSONObject.toJSONString(result);
        }
        //签名
        String sign = null;
        try {
            StringBuilder toSign = new StringBuilder("appKey=");
            toSign.append("OPR:" + "parentMerchantNo").append("&token=").append(createOrderRespDTO.getToken());//todo 父商编
            sign = yopSign(toSign.toString());
        }catch (Exception e){
            result.put("msg", "sign error");
            return JSONObject.toJSONString(result);
        }
        //组装返回信息
        Map<String, String> displayOrderInfoMap = new HashedMap();
        displayOrderInfoMap.put("merchantOrderNo", orderId);
        displayOrderInfoMap.put("uniqueOrderNo", createOrderRespDTO.getUniqueOrderNo());
        displayOrderInfoMap.put("amount", orderAmount);
        result.put("displayOrderInfo", displayOrderInfoMap);
        result.put("msg", "success");
        result.put("token", createOrderRespDTO.getToken());
        result.put("appKey", "OPR:" + "parentMerchantNo");//todo 父商编
        result.put("sign", sign);
        return JSONObject.toJSONString(result);
    }

    private String yopSign(String plaintext) throws Exception {
        DigitalSecurityFacade digitalSecurityFacade = getRemoteFacade("http://ycenc.yeepay.com:30227/yop-hessian/hessian/", DigitalSecurityFacade.class);
        DigitalSignatureDTO digitalEnvelopeDTO = new DigitalSignatureDTO();
        digitalEnvelopeDTO.setAppKey("_YOP");
        digitalEnvelopeDTO.setCertType(CertTypeEnum.RSA2048);
        digitalEnvelopeDTO.setDigestAlg(DigestAlgEnum.SHA256);
        digitalEnvelopeDTO.setPlainText(plaintext);
        DigitalSignatureDTO sign = digitalSecurityFacade.sign(digitalEnvelopeDTO);
        return sign.getSignature();
    }


    private YopCreateOrderResDTO oprOrder(String orderId, String orderAmount, String goodsName) throws Exception{
        YopOrderFacade yopOrderFacade = getRemoteFacade("http://ycenc.yeepay.com:30134/opr-hessian/hessian/", YopOrderFacade.class);
        YopCreateOrderReqDTO yopCreateOrderReqDTO = buildCreateOrderParam(orderId, orderAmount, goodsName);
        YopCreateOrderResDTO order = yopOrderFacade.createOrder(yopCreateOrderReqDTO);
        return order;
    }

    private YopCreateOrderReqDTO buildCreateOrderParam(String orderId, String orderAmount, String goodsName) {
        YopCreateOrderReqDTO reqDTO = new YopCreateOrderReqDTO();
        reqDTO.setProductVersion("DSXTSB");
        reqDTO.setBizSystemNo("DS");
        reqDTO.setAppKey("OPR:10025454402");//todo 父商编
        reqDTO.setParentMerchantNo("10025454402");//todo 父商编
        reqDTO.setMerchantNo("10027719755");//todo 商编
        reqDTO.setOrderId(orderId);
        reqDTO.setOrderAmount(orderAmount);
        reqDTO.setTimeoutExpress(120);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        reqDTO.setRequestDate(format.format(new Date()));
        reqDTO.setRedirectUrl("");//reditectUrl API的应该没有用
        reqDTO.setNotifyUrl("http://www.notify.com");//这个应该是聚合前置平台的服务器回调地址
        GoodsParamExtDTO goodsParamExtDTO = new GoodsParamExtDTO();
        goodsParamExtDTO.setGoodsName(goodsName);
        //GoodsParamExtDTO 中另外2个参数无需传入
        reqDTO.setGoodsParamExt(JSON.toJSONString(goodsParamExtDTO));
        PaymentParamExtDTO paymentParamExtDto = new PaymentParamExtDTO();
        reqDTO.setPaymentParamExt(JSON.toJSONString(paymentParamExtDto));
        reqDTO.setSalesProductCode("DSXTSB");
        reqDTO.setCheckHmac(false);
        return reqDTO;
    }

    public static <T> T getRemoteFacade(String urlPerfix, Class<T> facade) throws Exception{
        HessianProxyFactory factory = new HessianProxyFactory();
        String url = urlPerfix + facade.getSimpleName();
        System.out.println(url);
        return (T) factory.create(facade, url);
    }

}
