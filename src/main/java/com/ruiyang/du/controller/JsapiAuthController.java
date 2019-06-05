package com.ruiyang.du.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruiyang.du.utils.HttpsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;


@RestController
@RequestMapping(value = "/jsapi")
public class JsapiAuthController {

    Logger logger = LoggerFactory.getLogger(JsapiAuthController.class);

    private static final String WX_APP_ID = "";
    private static final String WX_APP_SECRET = "";
    /*微信授权地址*/
    private static final String WX_AUTH_SERVER_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=";
    /*微信获取openid地址*/
    private static final String WX_OPENID_SERVER_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=";
    /*测试页面域名*/
    private static final String LOCAL_HOST = "https://www.codershaw.com/wallet";
    private static final String WX_AUTH_CALLBACK_URL = "/jspay/wx/authcallback";


    @RequestMapping(value = "/wx/userauth")
    public Object wechatUserAuth(HttpServletRequest request) {
        try {
            String ua = request.getHeader("User-Agent");
            //检查UA，确保是在微信内，否则提示
            if (ua.indexOf("MicroMessenger") < 0) {
                return "公众号测试，请在微信浏览器打开";
            }
            //拼接微信静默授权地址，并访问
            StringBuilder requestUrl = new StringBuilder(WX_AUTH_SERVER_URL);
            String redirectUrl = URLEncoder.encode(LOCAL_HOST + WX_AUTH_CALLBACK_URL, "UTF-8");
            requestUrl.append(WX_APP_ID).append("&redirect_uri=").append(redirectUrl).append(WX_AUTH_CALLBACK_URL);
            requestUrl.append("&response_type=code&scope=snsapi_base#wechat_redirect");

            return new RedirectView(requestUrl.toString());
        } catch (Exception e) {
            logger.error("wechatUserAuth() 异常： ", e);
            return "授权失败";
        }
    }

    @RequestMapping(value = "/wx/authcallback")
    public Object wechatAuth2CallBack(HttpServletRequest request) {
        String code = request.getParameter("code");
        String ua = request.getHeader("User-Agent");
        //拼接微信获取openid接口地址，并访问
        StringBuilder requestUrl = new StringBuilder(WX_OPENID_SERVER_URL);
        requestUrl.append(WX_APP_ID).append("&secret=").append(WX_APP_SECRET).append("&grant_type=authorization_code&code=").append(code);
        String jsonString = HttpsHelper.doGet(ua, requestUrl.toString(), "utf-8");
        //解析返回信息
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        logger.info("获取微信openId地址返回结果" + jsonObject.toJSONString());
        String openId = jsonObject.getString("openid");
        return openId;
    }

}
