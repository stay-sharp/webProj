package com.ruiyang.du.remote;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.client.YopRsaClient;
import org.junit.Test;

import java.io.IOException;

public class TestYOP {

    private static final String APP_KEY = "OPR:10015386823";
    private static final String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDWzZwAbaZo5MImGUjsP8NkRCf3uwDGAEzaFf+Am3gXdbYF+zQ3RJG1mXJCf6YZkQTyDPYITwFmcfAFlsyxrPZVDEMJA3foT1PKsO93vgbD3VczBmvZvwCttiwxAB3t3Ado6DGo58NBuVa9W1zU8N92qjprMpAtFDngynost4lr2k63L9oWopKgyoWCxyYT1Sc+vz3tLYb9sgg8gQEw10/Gk2D5xNSNkU1sWvKK5eKSsAFUy781rbecexo+eZ2dDiZg2hPkoArQQM8gTWsD1JfbEiXsCkxFuKWGokUwrn6g0W3/tFpQUgjGnZpjOVNJDPG5dV6ZiUG5a3VnEQcyOY3/AgMBAAECggEAf79IR/juPvPNg1sGQ3qoebkPDAXrL9Z93jW2/oV3pnwbBPOg2j2tapMXoPTJk+rrRgjvSYGzE3ovhSNqGUEEPxBLzdCCP2buLl2MDWq2G0ZH1dC704QEo0r0fS/UnOF+hqWQ4yLyvOO88mCjlQCr5abXl2k61ZRx136TqoR89eaIEbKgcv7mijO1F6HUojjG5srevAHY9gYoY3cPvSEjoMX/wxnF++gYlmvrCJKpsxphP/h6KNUiesdww2d9SMbCmqJus6YMPcOwsTa/z5lC1+gl7gOf8mvBy/MvrkkNfBwCWTLEikRLNfbGZeTjvyP5CjyANuw71aUMgcS7t8BkAQKBgQD3t3ZXYahi2iVtPyEQuiCmLFot2oeB2HmGuhArJxi0Srf+WpwMxPvndX38LPc295ZyXj9bKOBYkWSATje3IoDtq5ViVS/Msjp8nxjvWEv9PiN0jjV2XKfbiUW8E4TDccOzZ+leAfQTjDsmlSe4e3DaAeuupH+JuYIRs6RBL7QkWQKBgQDd/GWHgREjhpPdVtW2vhVWaHO9N20iNCY7CSuPfrRNtFUTiLZh6ejqY0ln8KdMkPv/3f+bvUDldFLY6+T+9ogaOQS8w9jpz1AAL9D0sUEFe3EE33M4Vx+UPOiLao6lSoZh97TudCQ6NHyJ5VMeI7urzQuCpAlip2t6JTrEWftaFwKBgDjf54zigC3KFkPB9c6IXYyprD7Q6MHYOPbE64ds35Atg5cjqKaJ56hZ4oYusMfvCpzRpMxCU1gicae/zaz/gAiRl6fPFT1zQlhwkuIJLNFFHhDZwc6eMXAHUXqAJMBW827SkdMisa53SPFv14HU3bFANaOQYzP04rc7vp+02zBpAoGBAIGUg+/Fd6u1MztRJQ+3hd0au/UIFbyRlH+BqtvoqndxfPgaJdyiHkQ0EPWLlR0sxO1nOX4/y1IlxPa7PMFYR7qG7i0CZeyWB60UPHWZwsrgc+jURv3sXZK6u0Qmv33dMvSdFBDd2wfe+5PDOfGuCi8PUljASHo+II78jIFxxTehAoGBAOo4PyOpkiHxL35OYWDhAkQ2rUVBwNUtnLp1h1p6JYOsGjhlzjJO9YJv8miof/kdZqBCkAmPaO7G+JBF9EdOOgnqkTkzwFkol+YZZri0E5G1dSWj5EabpJzpON936aRVjnIxvkQ1YzOZ62XYGBdgzQUP+zPq98XZ9uQ/pmpz6fMA";
    private static final String PARENT_MERCHANT_NO = "10015386823";
    private static final String MERCHANT_NO = "10027789831";

    private static final String ALIPAY_CHANNEL_ID = "233785908";
    private static final String ALIPAY_REPORT_MERCHANT_NO = "292972556";
    private static final String WECHAT_CHANNEL_ID = "259180610";
    private static final String WECHAT_REPORT_MERCHANT_NO = "279124270";

    @Test
    public void orderPayTest() {
        String url = "/rest/v1.0/trade-front/pay";
        String orderId = "test" + System.currentTimeMillis();
        System.out.println("商户订单号：" + orderId);

        YopRequest request = new YopRequest(APP_KEY, PRIVATE_KEY);
        /*商户类*/
        request.addParam("channelId", ALIPAY_CHANNEL_ID);
        request.addParam("parentMerchantNo", PARENT_MERCHANT_NO);
        request.addParam("merchantNo", MERCHANT_NO);
        request.addParam("reportMerchantNo", ALIPAY_REPORT_MERCHANT_NO);

        /*订单类*/
        request.addParam("orderId", orderId);
        request.addParam("goodsDesc", "yop test"); //商品描述
        request.addParam("orderAmount", "1"); //订单金额
        request.addParam("limitPay", ""); //信用卡控制
        request.addParam("orderReqTime", ""); //订单请求时间
        request.addParam("ypOrderExp", ""); //易宝订单有效期
        request.addParam("userIp", "127.0.0.1"); //支付方IP
        request.addParam("divideDetail", ""); //分账明细
        request.addParam("tradeDesc", "ttt");
        request.addParam("notifyUrl", "www.baidu.com"); //支付成功回调地址
        request.addParam("fundProcessType", ""); //资金处理类型
        request.addParam("payType", "ALIPAY_PASSIVE"); //支付方式

        /*公众号、生活号*/
        request.addParam("subAppId", "wx9e13bd68a8f1921e");
        request.addParam("subOpenId", "ouQ5y1n11oFn2KT2jX_DPYQwG6n4");

        /*被扫*/
        request.addParam("deviceInfo", "0000"); //终端号
        request.addParam("authCode", "134893864551325347"); //授权码

        try {
            System.out.println("请求参数:" + JSONObject.toJSONString(request.getParams()));
            YopResponse yopResponse = YopRsaClient.post(url, request);
            System.out.println("返回参数:" + yopResponse.getStringResult());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //支付查询
    @Test
    public void orderPayQueryTest() {
        String url = "/rest/v1.0/trade-front/pay/query";

        YopRequest request = new YopRequest(APP_KEY, PRIVATE_KEY);
        request.addParam("parentMerchantNo", PARENT_MERCHANT_NO);
        request.addParam("merchantNo", MERCHANT_NO);
        request.addParam("orderId", ""); //商户请求订单号

        try {
            System.out.println("请求参数:" + JSONObject.toJSONString(request.getParams()));
            YopResponse yopResponse = YopRsaClient.post(url, request);
            System.out.println("返回参数:" + yopResponse.getStringResult());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //退款
    @Test
    public void orderRefundTest() {
        String url = "/rest/v1.0/trade-front/refund-order/refund";

        YopRequest request = new YopRequest(APP_KEY, PRIVATE_KEY);
        request.addParam("parentMerchantNo", PARENT_MERCHANT_NO);
        request.addParam("merchantNo", MERCHANT_NO);
        request.addParam("orderId", ""); //商户请求订单号
        request.addParam("refundOrderId", "");
        request.addParam("refundAmount", "");
        request.addParam("refundDesc", "");
        request.addParam("notifyUrl", "");
        request.addParam("divideDetail", "");

        try {
            System.out.println("请求参数:" + JSONObject.toJSONString(request.getParams()));
            YopResponse yopResponse = YopRsaClient.post(url, request);
            System.out.println("返回参数:" + yopResponse.getStringResult());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //退款查询
    @Test
    public void orderRefundQueryTest() {
        String url = "/rest/v1.0/trade-front/refund-order/refund-query";
        YopRequest request = new YopRequest(APP_KEY, PRIVATE_KEY);
        request.addParam("parentMerchantNo", PARENT_MERCHANT_NO);
        request.addParam("merchantNo", MERCHANT_NO);
        request.addParam("orderId", ""); //商户请求订单号
        request.addParam("refundOrderId", "");

        try {
            System.out.println("请求参数:" + JSONObject.toJSONString(request.getParams()));
            YopResponse yopResponse = YopRsaClient.post(url, request);
            System.out.println("返回参数:" + yopResponse.getStringResult());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void hamcTest() throws IOException {
        String BASE_URL = "/rest/v1.0/sys/merchant/hmackeyquery";
        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCPssk8SXy12oTYyLOA1fQInwGqXlFSCO56b/ALb0n1FOQtyMDYP7oIbJsShf6Q0JTjxyfLhS4SxIyWtkgk9sv8Vvu7nbXHXRbQPUXFKnN/n8rouALPROcagkGe4/2R3sEABw6nKzUp62X8FWneGI5f8qAe3bX/e0NTPa44rJnDfGBJO0hirrlA7OxXDzw8bMwcOf7CEHTMUtBIafqSNMXxLToWUJ9iJ4lJDlCcV4jJ7Ul9O9yBZzvmhGS5L94q1+Rgbav7+BX51nnG9lZGBb7vdNxruPUnq90bUG3Io4PAtgqqaku3rNQzP8d+Z1jyrWoh+zOLngApaV/BB6+v/A/BAgMBAAECggEBAICfMjrROFx6ZPC57NpEJ/PTh6UcWUPJlNRo/37stVE2yuskR2EI4ZaKx98zquGqoaqkZ71mzw+jeIeqTzcu5PVQUnDjwILbkwD/50SNM+ane/MESheGgCmdL+lt/1ki/rPsnTQKm8KS3q1d6W4PotjFDoeyQiaVCXUnv5sg+10ayw8SnqU5X8VdQSnczYmoxD2fdg7c7zCefptC6UcJyBcSvAGpFiu0LE413yUJ7BenvYIKSlQTYBpyDlA1EGe9yLi42AmQSTIg8c8bUEnXnWWrti8k2WVDks9GvA+Lw2f213XVvowOwY3bDaOOFDtrUbRUE5el5UEZl8qhTH4YiVUCgYEA3Zk0yrIu6X+jgZyP+aFWEc34cowPzJK3YC5YIEuNGPoFlb81DatIaSMhisn3aWR/mA2gEkTSqRaX9E+fY8DjNdIHAj/SWlIxmAzXVnaCidsiyRNUEALkv9eknuOJ0MGIxGrDLu3kebmhBJP97IqTU1UpjyN5QbSJ6cQq+lKH6NcCgYEApgGqSdPiCavssz/58NoWRRyQVnmSQUUmyAIUCEX8rZ7lm+p5CPM2P+UeLB2drDjKDaZD0Da726LSAuz8f8CB3+PErLNjJ2E71tIvGLcxKpdfb0DDOekfCoEAT/TTKcQMEiAw/2AVdZPbzJ9/51XrORBXL+V6JiWFmvw5fxv/QScCgYEA1l6KgnejCEFwxEbXxO69W5X8fZtfAVEBUmsi14Me6QWdd4K0aRfEgej5XtEIpg0Rnd+4dVPc0rRHpZGNKKwFYSBfpV5mOPgAgA4UFtGocIHsAcbP8HlMHwYWe5q0zJ0cmJ6LWQ/LkbHwwYQFBdn8sYXWIWF4wQZM0Db0OJq0XZsCgYACob4gY4KO4rHi+Z2tytdViylipZTDAiSmQRouM2XZHs7HFQWMNcbjZm9/BY0tYM0bQEqM74E07zTjJPyvzc6BTSweDM4CntEDC9wBSU43PaUer2ko7uA2G2t02Q3L+La0RtxgABb3ATwX12OAuGT9R5wKP1obZrbiiSMkN75pOwKBgArkzsjmZWUuOTUjLayGn7HskEyE3FzYm0JrDM/DAzDZwX/twYnVJaoYWsvm53ypYIu/dfs0+P36pXIqY08Nn3mq8TWbUjJXrIGTElHgAlnl0CGz7yg4l43wynH8rZUinyOkN1hnZp8b02zvovvppqSd9nsZ3SFF+4k0mHESdTPa";
        YopRequest request = new YopRequest("OPR:10015386831", privateKey);
        request.addParam("parentMerchantNo","10015386831");
        request.addParam("merchantNo","10027782797");
        YopResponse response = YopRsaClient.post(BASE_URL,request);
        System.out.println(JSON.toJSONString(response.toString()));
    }
}
