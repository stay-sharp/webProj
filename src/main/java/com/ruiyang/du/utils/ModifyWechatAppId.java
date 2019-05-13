package com.ruiyang.du.utils;

import com.alibaba.fastjson.JSON;
import com.ruiyang.du.bo.ModifyAppidVO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModifyWechatAppId {

    private static final String EXCEL_PATH = "/Users/yp-tc-m-7122/Desktop/source.xlsx";
    private static Map<String, String> failMap = new HashMap<String, String>();

    /*服务商后台-首页*/
    private static final String WX_SP_BOSS_URL = "https://pay.weixin.qq.com/index.php/partner/public/home";
    /*关闭设置预留信息弹窗*/
    private static final String WX_HTML_CLASS_CLOSE_TIP = "ico-cls close-d-set-modify-reserved-info";
    /*服务商功能-超链接*/
    private static final String WX_HTML_SP_MENU_URL = "https://pay.weixin.qq.com/index.php/core/home/header?menu=6106";
    /*商户识别码-输入框*/
    private static final String WX_HTML_NAME_MER_NO_INPUT = "subMerchantId";
    /*商户识别码-查询按钮*/
    private static final String WX_HTML_TEXT_MER_NO_BTN = "查询";
    /*开发配置-超链接-需拼接参数*/
    private static final String WX_HTML_MER_CONFIG_URL = "https://pay.weixin.qq.com/index.php/extend/sub_dev_setting?sub_mchid=";
    /*推荐关注的前往配置-超链接-需置换参数*/
    private static final String WX_HTML_MER_FOCUS_CONFIG_URL = "https://pay.weixin.qq.com/index.php/extend/mktsubrecommendsetting?agency_mchid=100078877&sub_mchid=SUB_MCHID&isParentBank=";
    /*关注appid-输入框*/
    private static final String WX_HTML_FOCUS_INPUT_XPATH = "//*[@id=\"m_recommend_appid\"]";
    /*关注appid-确认按钮*/
    private static final String WX_HTML_FOCUS_INPUT_CONFIRM = "//*[@id=\"modify_confirm\"]";

    public static void main(String[] args) {
        WebDriver webDriver = null;
        try {
            webDriver = loginAndEnter2ModifyPage();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        operateFromExcel(webDriver);
        System.out.println("999---任务处理完毕，失败的数据有：" + JSON.toJSONString(failMap));
    }

    private static WebDriver loginAndEnter2ModifyPage() throws InterruptedException {
        WebDriver webDriver = SeleniumUtils.loadDriver();
        //打开微信服务商平台，等待登录10秒
        webDriver.get(WX_SP_BOSS_URL);

        System.out.println("1---等待登录中，请在30秒内扫码登录");
        Thread.sleep(30000l);

        //选择并点击服务商功能超链接
        webDriver.get(WX_HTML_SP_MENU_URL);
        System.out.println("2---跳过预留信息弹层，打开服务商功能");
        return webDriver;
    }

    private static void operateFromExcel(WebDriver webDriver) {
        try {
            XSSFWorkbook xssfWorkbook = ExcelOperation.readSourceFileToWorkBook(EXCEL_PATH);
            XSSFSheet sourceSheet = xssfWorkbook.getSheetAt(0);
            int totalRowNum = sourceSheet.getLastRowNum();
            System.out.println("3---读取excel成功，总计需处理数据行数为" + totalRowNum);
            for (int i = 0; i < totalRowNum; i++) {
                XSSFRow row = sourceSheet.getRow(i);
                loopForCells(row, failMap, webDriver);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环行内单元格
     *
     * @param sourceRow
     * @param failMap
     * @return
     */
    private static void loopForCells(XSSFRow sourceRow, Map<String, String> failMap, WebDriver webDriver) {
        if (sourceRow == null || sourceRow.getPhysicalNumberOfCells() != 3) {
            return;
        }
        ModifyAppidVO modifyAppidVO = new ModifyAppidVO();
        //要求格式：一共3列，分别为微信商户号、appid、关注appid
        String merchantNo = sourceRow.getCell(0).getStringCellValue();
        String appid = sourceRow.getCell(1).getStringCellValue();
        String focusAppid = sourceRow.getCell(2).getStringCellValue();
        modifyAppidVO.setWechatMerchantNo(merchantNo);
        modifyAppidVO.setAppid(appid);
        modifyAppidVO.setFocusAppid(focusAppid);
        System.out.println("4---读取excel行成功，当前商户数据为" + JSON.toJSONString(modifyAppidVO));
        //准备修改商编
        modifyForMerchant(modifyAppidVO, webDriver);
    }

    private static void modifyForMerchant(ModifyAppidVO modifyAppidVO, WebDriver webDriver) {
        try {
            //选择并输入商户识别码
            WebElement input = webDriver.findElement(By.name(WX_HTML_NAME_MER_NO_INPUT));
            input.sendKeys(modifyAppidVO.getWechatMerchantNo());
            input.submit();
            //选择并点击查询按钮
            webDriver.findElement(By.linkText(WX_HTML_TEXT_MER_NO_BTN)).submit();
            //选择并点击开发配置超链接
            webDriver.get(WX_HTML_MER_CONFIG_URL+modifyAppidVO.getWechatMerchantNo());
            //选择并点击推荐关注的前往配置
            webDriver.get(WX_HTML_MER_FOCUS_CONFIG_URL.replace("SUB_MCHID",modifyAppidVO.getWechatMerchantNo()));

            //检索appid项，找到对应的修改按钮，并点击
            findModifyBtnByAppid(modifyAppidVO.getAppid(),webDriver);
            //输入内容
            WebElement inputFocusAppid = webDriver.findElement(By.xpath(WX_HTML_FOCUS_INPUT_XPATH));
            inputFocusAppid.sendKeys(modifyAppidVO.getFocusAppid());
            //点击确定
            WebElement confirm = webDriver.findElement(By.xpath(WX_HTML_FOCUS_INPUT_CONFIRM));
            confirm.submit();
            //选择并点击服务商功能超链接
            webDriver.get(WX_HTML_SP_MENU_URL);
        } catch (Exception e) {
            System.out.println("修改商户异常,微信商编 = " + modifyAppidVO.getWechatMerchantNo());
            failMap.put(modifyAppidVO.getWechatMerchantNo(), e.getMessage());
            e.printStackTrace();
        }
    }

    private static void findModifyBtnByAppid(String appid, WebDriver webDriver){
        String xpathExpress = "//*[@appid=\"APP_ID\"]".replace("APP_ID",appid);
        WebElement element = webDriver.findElement(By.xpath(xpathExpress));
        element.submit();
    }

}
