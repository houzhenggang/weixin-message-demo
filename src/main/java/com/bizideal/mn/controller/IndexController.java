package com.bizideal.mn.controller;

import com.alibaba.fastjson.JSONObject;
import com.bizideal.mn.utils.HttpUtils;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @author : liulq
 * @date: 创建时间: 2017/10/23 16:41
 * @version: 1.0
 * @Description:
 */
@Controller
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    public static String accessToken = "";
    public static long lastTime = 0l;

    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.appsecret}")
    private String appsecret;

    @RequestMapping("")
    public String index() {
        return "index";
    }

    public static void main(String[] args) {
        System.out.println(org.apache.http.ssl.SSLContexts.class.getProtectionDomain());
    }

    @RequestMapping("/index")
    public String index1() {
        return "index";
    }

    // 获取access_token
    public void getAccesstoken() throws IOException {
        if (StringUtils.isNotBlank(accessToken) && System.currentTimeMillis() - lastTime < 1 * 60 * 60 * 1000)
            return;
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        url = url.replace("APPID", appid).replace("APPSECRET", appsecret);
        JSONObject jsonObject = HttpUtils.get(url);
        String access_token = jsonObject.getString("access_token");
        accessToken = access_token;
        lastTime = System.currentTimeMillis();
        logger.info("get access_token success . " + access_token);
    }

    // 获取关注者列表
    @RequestMapping("/getUsers")
    @ResponseBody
    public JSONObject getUsers() throws IOException {
        getAccesstoken();
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=";
        url = url.replace("ACCESS_TOKEN", accessToken);
        JSONObject jsonObject = HttpUtils.get(url);
        return jsonObject;
    }

    // 发送模板消息
    @RequestMapping("/sendMsg/{openid}")
    @ResponseBody
    public JSONObject sendMsg(@PathVariable("openid") String openid) throws IOException {
        getAccesstoken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", accessToken);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", openid); // 接收方的openid
        jsonObject.put("template_id", "mxznKzAIUvjnOOjjs43ACErzuo7SdTkUd6zRkG2gRBI"); // 模板id
        jsonObject.put("url", "www.baidu.com"); // 用户点击消息跳转的路径
        JSONObject firstObj = new JSONObject();
        firstObj.put("value", "恭喜您报名成功！\n");
        firstObj.put("color", "#173177");
        JSONObject realNameObj = new JSONObject();
        realNameObj.put("value", "刘立庆\n");
        realNameObj.put("color", "#173177");
        JSONObject phoneObj = new JSONObject();
        phoneObj.put("value", "17621216043");
        phoneObj.put("color", "#173177");
        JSONObject dataObj = new JSONObject();
        dataObj.put("first", firstObj);
        dataObj.put("realName", realNameObj);
        dataObj.put("phone", phoneObj);
        jsonObject.put("data", dataObj);
        JSONObject json = HttpUtils.postJson(url, jsonObject);
        return json;
    }

}
