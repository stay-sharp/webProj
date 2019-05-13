package com.ruiyang.du.controller;

import com.ruiyang.du.utils.HttpsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

@RestController
@RequestMapping(value = "/jspay")
public class JspayController {

    Logger logger = LoggerFactory.getLogger(JspayController.class);
    private static final String UPOP_USER_AUTH_URL = "https://qr.95516.com/qrcGtwWeb-web/api/userAuth?version=1.0.0&redirectUrl=";
    private static final String UPOP_AUTH_CALLBACK_URL = "http://172.18.166.197:8080/jspay/upop/authcallback";


    @RequestMapping(value = "/upop/userauth")
    public String upopUserAuth(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        logger.info("upopUserAuth() ua = " + ua);
        try {
            String jsonString = HttpsHelper.doGet(ua, UPOP_USER_AUTH_URL + URLEncoder.encode(UPOP_AUTH_CALLBACK_URL, "utf-8"), "utf-8");
            logger.info("upopUserAuth() 调用银联授权接口响应 = " + jsonString);
            return jsonString;
        } catch (Exception e) {
            logger.info("upopUserAuth() 调用银联授权接口失败，异常： ", e);
            return "auth faild";
        }
    }

    @RequestMapping(value = "/upop/authcallback")
    public String auth2CallBack(HttpServletRequest request, String userAuthCode, String respCode) {
        logger.info("auth2CallBack() 接收到银联用户授权回调，userAuthCode = " + userAuthCode + "，respCode = " + respCode);
        if("00".equals(respCode)){
            logger.info("auth2CallBack() 接收到银联用户授权回调，获取userAuthCode成功，userAuthCode = " + userAuthCode);
        }else if("34".equals(respCode)){
            logger.info("auth2CallBack() 接收到银联用户授权回调，不支持获取userAuthCode，respCode = " + respCode);
        }else {
            logger.info("auth2CallBack() 接收到银联用户授权回调，获取userAuthCode失败");
        }
        return userAuthCode;
    }

}
