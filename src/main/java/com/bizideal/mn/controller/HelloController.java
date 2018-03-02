package com.bizideal.mn.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.plugin2.message.Message;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author : liulq
 * @date: 创建时间: 2017/10/23 15:21
 * @version: 1.0
 * @Description:
 */
@Controller
public class HelloController {

    private Logger logger = LoggerFactory.getLogger(HelloController.class);

    /**
     * 测试hello
     *
     * @return
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(Model model) {
        model.addAttribute("name", "Dear");
        return "hello";
    }

    @RequestMapping("/checkToken")
    @ResponseBody
    public String checkToken(String signature, String timestamp
            , String nonce, String echostr) throws NoSuchAlgorithmException {
        String token = "liulq";
        String tmpStr = getSHA1(token, timestamp, nonce);

        System.out.println("+++++++++++++++++++++tmpStr   " + tmpStr);
        System.out.println("---------------------signature   " + signature);

        if (tmpStr.equals(signature)) {
            return echostr;
        } else {
            return null;
        }
    }

    @RequestMapping("/getMessageOnSubscribe")
    @ResponseBody
    public String get(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String s = result.toString("UTF-8");
        MessageDto messageDto1 = JSONObject.parseObject(s, MessageDto.class);
        System.out.println(messageDto1);
        return "success";
    }

    /**
     * 用SHA1算法生成安全签名
     *
     * @param token     票据
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @return 安全签名
     * @throws NoSuchAlgorithmException
     */
    public String getSHA1(String token, String timestamp, String nonce) throws NoSuchAlgorithmException {
        String[] array = new String[]{token, timestamp, nonce};
        StringBuffer sb = new StringBuffer();
        // 字符串排序
        Arrays.sort(array);
        for (int i = 0; i < 3; i++) {
            sb.append(array[i]);
        }
        String str = sb.toString();
        // SHA1签名生成
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(str.getBytes());
        byte[] digest = md.digest();

        StringBuffer hexstr = new StringBuffer();
        String shaHex = "";
        for (int i = 0; i < digest.length; i++) {
            shaHex = Integer.toHexString(digest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexstr.append(0);
            }
            hexstr.append(shaHex);
        }
        return hexstr.toString();
    }

    static class MessageDto implements Serializable {

        private static final long serialVersionUID = -5230258930971849513L;
        private String URL;
        private String ToUserName;
        private String FromUserName;
        private long CreateTime;
        private String MsgType;
        private String Event;
        private String Latitude;
        private String Longitude;
        private String Precision;
        private long MsgId;
        private String EventKey;
        private String Ticket;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"URL\":\"")
                    .append(URL).append('\"');
            sb.append(",\"ToUserName\":\"")
                    .append(ToUserName).append('\"');
            sb.append(",\"FromUserName\":\"")
                    .append(FromUserName).append('\"');
            sb.append(",\"CreateTime\":")
                    .append(CreateTime);
            sb.append(",\"MsgType\":\"")
                    .append(MsgType).append('\"');
            sb.append(",\"Event\":\"")
                    .append(Event).append('\"');
            sb.append(",\"Latitude\":\"")
                    .append(Latitude).append('\"');
            sb.append(",\"Longitude\":\"")
                    .append(Longitude).append('\"');
            sb.append(",\"Precision\":\"")
                    .append(Precision).append('\"');
            sb.append(",\"MsgId\":")
                    .append(MsgId);
            sb.append(",\"EventKey\":\"")
                    .append(EventKey).append('\"');
            sb.append(",\"Ticket\":\"")
                    .append(Ticket).append('\"');
            sb.append('}');
            return sb.toString();
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public String getToUserName() {
            return ToUserName;
        }

        public void setToUserName(String toUserName) {
            ToUserName = toUserName;
        }

        public String getFromUserName() {
            return FromUserName;
        }

        public void setFromUserName(String fromUserName) {
            FromUserName = fromUserName;
        }

        public long getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(long createTime) {
            CreateTime = createTime;
        }

        public String getMsgType() {
            return MsgType;
        }

        public void setMsgType(String msgType) {
            MsgType = msgType;
        }

        public String getEvent() {
            return Event;
        }

        public void setEvent(String event) {
            Event = event;
        }

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String latitude) {
            Latitude = latitude;
        }

        public String getLongitude() {
            return Longitude;
        }

        public void setLongitude(String longitude) {
            Longitude = longitude;
        }

        public String getPrecision() {
            return Precision;
        }

        public void setPrecision(String precision) {
            Precision = precision;
        }

        public long getMsgId() {
            return MsgId;
        }

        public void setMsgId(long msgId) {
            MsgId = msgId;
        }

        public String getEventKey() {
            return EventKey;
        }

        public void setEventKey(String eventKey) {
            EventKey = eventKey;
        }

        public String getTicket() {
            return Ticket;
        }

        public void setTicket(String ticket) {
            Ticket = ticket;
        }
    }
}
