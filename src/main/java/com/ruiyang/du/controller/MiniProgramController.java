package com.ruiyang.du.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruiyang.du.utils.HttpsHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/newwap/miniprogram")
public class MiniProgramController {


    @RequestMapping(value = "/openid")
    public String miniProgramPreRoute(String appId, String appSecret, String jsCode, HttpServletRequest request) {
        try{
            String openId = oauthForOpenidForMiniProgram(appId, appSecret, jsCode);
            return openId;
        }catch (Exception e){
            return null;
        }
    }

    private String oauthForOpenidForMiniProgram(String appId, String appSecret, String jsCode) {
        try {
            // 获取微信小程序获取openID的API接口
            String openIdUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
            openIdUrl = openIdUrl.replace("APPID", appId).replace("SECRET", appSecret).replace("JSCODE", jsCode);
            String jsonString = HttpsHelper.doGet("",openIdUrl, "utf-8");
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            String openId = jsonObject.getString("openid");
            return openId;
        }catch (Throwable e){
            return null;
        }
    }


}
