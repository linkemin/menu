package com.lkm.menu.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lkm.menu.po.Httpinfo;
import com.lkm.menu.service.HttpInfoJPA;
import com.lkm.menu.service.MailService;
import com.lkm.menu.util.DateFormatUtil;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Linkm on 2023/1/9.
 */
@RestController
@RequestMapping("/daka")
public class DakaController {

    @Autowired
    HttpInfoJPA httpInfoJPA;

    /**
     * 通过用户Id获取打卡信息
     * @param userId
     * @return
     */
    @GetMapping("/getHttpInfo/{userId}")
    public Object getHttpInfo(@PathVariable("userId")Integer userId){
        List<Httpinfo> byUserId = httpInfoJPA.findByUserId(userId);
        if (byUserId.size() > 0){
            return byUserId.get(0);
        }else {
            Httpinfo httpinfo = new Httpinfo();
            httpinfo.setUserId(userId);
            httpinfo = httpInfoJPA.save(httpinfo);
            return httpinfo;
        }
    }


    /**
     * 保存httpinfo信息
     * @param userId
     * @param httpInfo
     */
    @PostMapping("/save")
    public void save(Integer userId, String httpInfo){
        List<Httpinfo> httpinfos = httpInfoJPA.findByUserId(userId);
        if (httpinfos.size() > 0){
            Httpinfo httpinfovo = httpinfos.get(0);
            httpinfovo.setHttpinfo(httpInfo);
            httpInfoJPA.save(httpinfovo);
        }
    }

    @PostMapping("/saveScheduled")
    public void saveScheduled(Integer userId, String scheduled){
        List<Httpinfo> httpinfos = httpInfoJPA.findByUserId(userId);
        if (httpinfos.size() > 0){
            Httpinfo httpinfovo = httpinfos.get(0);
            httpinfovo.setScheduled("true".equals(scheduled)?"Y":"N");
            httpInfoJPA.save(httpinfovo);
        }
    }

    /**
     * 执行打卡
     * @param userId
     */
    @PostMapping("/daka")
    public Object daka(Integer userId){
        Map<String, String> map = new HashMap<>();
        List<Httpinfo> httpinfos = httpInfoJPA.findByUserId(userId);
        if (httpinfos.size() > 0){
            Httpinfo httpinfovo = httpinfos.get(0);
            if (StringUtils.isEmpty(httpinfovo.getHttpinfo())){
                map.put("success", "false");
                map.put("haveHttpInfo", "false");
                return map;
            }else {
                //打卡方法...
                String postMsg = post(httpinfovo);
                Date now = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String nowStr = df.format(now);
                httpinfovo.setLasttime(new Timestamp(now.getTime()));
                httpinfovo.setLastinfo(postMsg);
                httpInfoJPA.saveAndFlush(httpinfovo);
                String mail = httpinfovo.getMail();

                if (StringUtils.isEmpty(postMsg) && !StringUtils.isEmpty(mail)){
                    List<String> mailList = new ArrayList<>();
                    mailList.add(mail);
                    try {
                        MailService.sendFail(mailList, "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                map.put("success", "true");
                map.put("time", nowStr);
                map.put("postMsg", postMsg);
                return map;
            }
        }else {
            Httpinfo httpinfovo = new Httpinfo();
            httpinfovo.setUserId(userId);
            httpInfoJPA.save(httpinfovo);
            map.put("success", "false");
            map.put("haveHttpInfo", "false");
            return map;
        }
    }


    /**
     * 上班卡
     */
    @Scheduled(cron = "15/30 * 8 ? * MON-FRI")
    public void shangban(){
        List<Httpinfo> all = httpInfoJPA.findByScheduled("Y");
        Date now = new Date();
        String date0 = DateFormatUtil.getDateStr(DateFormatUtil.getStartOfDay(now), "yyyy-MM-dd HH:mm:ss");
        String date9 = DateFormatUtil.getDateStr(DateFormatUtil.getDefOfDay(now, "09:00:00"), "yyyy-MM-dd HH:mm:ss");
        for (Httpinfo httpinfo : all) {
            //已经打过则跳过
            String lasttime = httpinfo.getLasttime();
            if (!StringUtils.isEmpty(lasttime)){
                if (DateFormatUtil.comparedate(lasttime, date0, "yyyy-MM-dd HH:mm:ss") == 1 && DateFormatUtil.comparedate(lasttime, date9, "yyyy-MM-dd HH:mm:ss") == -1 ){
                    continue;
                }
            }
            double random = Math.random() * 100L;
            if (random < 3){
                autoPost(now, httpinfo);
            }else if(Integer.valueOf(DateFormatUtil.getDateFormatStr("mm")) > 50){
                //超过50分时间阈值则立刻打卡
                autoPost(now, httpinfo);
            }
        }
    }

    /**
     * 下班卡
     */
    @Scheduled(cron = "15/30 * 18 ? * MON-FRI")
    public void xiaban(){
        List<Httpinfo> all = httpInfoJPA.findByScheduled("Y");
        Date now = new Date();
        String date18 = DateFormatUtil.getDateStr(DateFormatUtil.getDefOfDay(now, "18:00:00"), "yyyy-MM-dd HH:mm:ss");
        for (Httpinfo httpinfo : all) {
            //已经打过则跳过
            String lasttime = httpinfo.getLasttime();
            if (!StringUtils.isEmpty(lasttime)){
                if (DateFormatUtil.comparedate(lasttime, date18, "yyyy-MM-dd HH:mm:ss") == 1 ){
                    continue;
                }
            }
            double random = Math.random() * 100L;
            if (random < 3){
                autoPost(now, httpinfo);
            }else if(Integer.valueOf(DateFormatUtil.getDateFormatStr("mm")) > 50){
                //超过50分时间阈值则立刻打卡
                autoPost(now, httpinfo);
            }
        }
    }

    /**
     * 加班卡
     */
    @Scheduled(cron = "15/30 * 20 ? * MON-FRI")
    public void jiaban(){
        List<Httpinfo> all = httpInfoJPA.findByScheduled("Y");
        Date now = new Date();
        String date18 = DateFormatUtil.getDateStr(DateFormatUtil.getDefOfDay(now, "20:00:00"), "yyyy-MM-dd HH:mm:ss");
        for (Httpinfo httpinfo : all) {
            //已经打过则跳过
            String lasttime = httpinfo.getLasttime();
            if (!StringUtils.isEmpty(lasttime)){
                if (DateFormatUtil.comparedate(lasttime, date18, "yyyy-MM-dd HH:mm:ss") == 1 ){
                    continue;
                }
            }
            double random = Math.random() * 100L;
            if (random < 3){
                autoPost(now, httpinfo);
            }else if(Integer.valueOf(DateFormatUtil.getDateFormatStr("mm")) > 50){
                //超过50分时间阈值则立刻打卡
                autoPost(now, httpinfo);
            }
        }
    }

    private void autoPost(Date now, Httpinfo httpinfo) {
        String postMsg = post(httpinfo);
        httpinfo.setLasttime(new Timestamp(now.getTime()));
        httpinfo.setLastinfo(postMsg);
        httpInfoJPA.saveAndFlush(httpinfo);
        String mail = httpinfo.getMail();
        if (StringUtils.isEmpty(postMsg) && !StringUtils.isEmpty(mail)){
            List<String> mailList = new ArrayList<>();
            mailList.add(mail);
            try {
                MailService.sendFail(mailList, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发起打卡请求
     * @param httpinfo
     * @return
     */
    public String post(Httpinfo httpinfo){
        String httpinfoStr = httpinfo.getHttpinfo();
        BufferedReader reader = new BufferedReader(new StringReader(httpinfoStr));
        String msg = "";
        String lineText;
        String part = "1";
        String url = "";
        Map<String, String> headers = new HashMap<>();
        Map<String, String> body = new HashMap<>();


        Pattern patternUrl = Pattern.compile("http.* ");

        try {
            while ((lineText = reader.readLine()) != null) {
                if ("1".equals(part)){
                    Matcher matcher = patternUrl.matcher(lineText);
                    if (matcher.find()) {
                        url = matcher.group();
                        url = url.trim();
                    }
                    part = "2";
                    continue;
                }else if ("2".equals(part)){
                    if ("".equals(lineText)){
                        part = "3";
                        continue;
                    }else {
                        String[] header = lineText.split(":");
                        headers.put(header[0], header[1].trim());
                    }
                }else if ("3".equals(part)){
                    String[] split = lineText.split("&");
                    for (String s : split) {
                        String[] split1 = s.split("=");
                        body.put(split1[0], split1[1]);
                    }
                    break;
                }

            }

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            headers.forEach((key, value) -> {
                if (!"Content-Length".equals(key)){
                    httpPost.addHeader(key, value);
                }
            });

            List<BasicNameValuePair> param = new ArrayList<>();
            body.forEach((key, value) -> {
                param.add(new BasicNameValuePair(key, value));
            });

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(param, StandardCharsets.UTF_8);
            httpPost.setEntity(formEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);//执行请求
            String result = EntityUtils.toString(response.getEntity());//获取响应内容
            System.out.println("打卡的响应内容为：" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public String post(String httpinfoStr){
        BufferedReader reader = new BufferedReader(new StringReader(httpinfoStr));
        String msg = "";
        String lineText;
        String part = "1";
        String url = "";
        Map<String, String> headers = new HashMap<>();
        Map<String, String> body = new HashMap<>();


        Pattern patternUrl = Pattern.compile("http.* ");

        try {
            while ((lineText = reader.readLine()) != null) {
                if ("1".equals(part)){
                    Matcher matcher = patternUrl.matcher(lineText);
                    if (matcher.find()) {
                        url = matcher.group();
                        url = url.trim();
                    }
                    part = "2";
                    continue;
                }else if ("2".equals(part)){
                    if ("".equals(lineText)){
                        part = "3";
                        continue;
                    }else {
                        String[] header = lineText.split(":");
                        headers.put(header[0], header[1].trim());
                    }
                }else if ("3".equals(part)){
                    String[] split = lineText.split("&");
                    for (String s : split) {
                        String[] split1 = s.split("=");
                        body.put(split1[0], split1[1]);
                    }
                    break;
                }

            }

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            headers.forEach((key, value) -> {
                if (!"Content-Length".equals(key)){
                    httpPost.addHeader(key, value);
                }
            });

            List<BasicNameValuePair> param = new ArrayList<>();
            body.forEach((key, value) -> {
                param.add(new BasicNameValuePair(key, value));
            });

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(param, StandardCharsets.UTF_8);
            httpPost.setEntity(formEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);//执行请求
            String result = EntityUtils.toString(response.getEntity());//获取响应内容
            System.out.println("打卡的响应内容为：" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public String httpget(String httpinfoStr){
        BufferedReader reader = new BufferedReader(new StringReader(httpinfoStr));
        String msg = "";
        String lineText;
        String part = "1";
        String url = "";
        Map<String, String> headers = new HashMap<>();
        Map<String, String> body = new HashMap<>();


        Pattern patternUrl = Pattern.compile("http.* ");

        try {
            while ((lineText = reader.readLine()) != null) {
                if ("1".equals(part)){
                    Matcher matcher = patternUrl.matcher(lineText);
                    if (matcher.find()) {
                        url = matcher.group();
                        url = url.trim();
                    }
                    part = "2";
                    continue;
                }else if ("2".equals(part)){
                    if ("".equals(lineText)){
                        part = "3";
                        continue;
                    }else {
                        String[] header = lineText.split(":", 2);
                        headers.put(header[0], header[1].trim());
                    }
                }else if ("3".equals(part)){
                    String[] split = lineText.split("&");
                    for (String s : split) {
                        String[] split1 = s.split("=");
                        body.put(split1[0], split1[1]);
                    }
                    break;
                }

            }

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            headers.forEach((key, value) -> {
                if (!"Content-Length".equals(key)){
                    httpGet.addHeader(key, value);
                }
            });

            List<BasicNameValuePair> param = new ArrayList<>();
            body.forEach((key, value) -> {
                param.add(new BasicNameValuePair(key, value));
            });

//            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(param, StandardCharsets.UTF_8);
//            httpGet.setEntity(formEntity);
            CloseableHttpResponse response = httpClient.execute(httpGet);//执行请求
            String result = EntityUtils.toString(response.getEntity());//获取响应内容
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static void main(String[] args) {

        String httpinfo = "GET https://mall.meituan.com/api/c/poi/10000521/sku/search/v2?uuid=1870c834fadb-20779350ebd618-0-3d10d-1870c834faec8&xuuid=1870c834fadb-20779350ebd618-0-3d10d-1870c834faec8&__reqTraceID=b1c7f0c7-a4fa-9169-3dab-d28b0c8afc97&platform=ios&utm_medium=wxapp&brand=xiaoxiangmaicai&tenantId=1&utm_term=5.46.2&isClosed=false&offset=0&limit=30&poiId=10000521&keyword=%E6%B1%89%E5%A0%A1&quickSearchWord=false&from=wri&last=0&personalRecommendClose=0&poi=10000521&stockPois=10000521&ci=4&bizId=2&deliveryRegionKey=6666&openId=oV_5G44V8wczpt0vLyDtamkpkI3I&address_id=1950000041&sysName=iOS&sysVerion=14.1&app_tag=union&uci=4&userid=2420341450 HTTP/1.1\n" +
                "Host: mall.meituan.com\n" +
                "Connection: keep-alive\n" +
                "openId: oV_5G44V8wczpt0vLyDtamkpkI3I\n" +
                "content-type: multipart/form-data\n" +
                "traceids: 5d768fef#bef47394\n" +
                "req_of_maicai: 1\n" +
                "location: 113.36511067708334,23.135461968315973\n" +
                "deliveryAddrLocation: 113.364489,23.135426\n" +
                "t: AgH0IeEQY79VChcaQ56Jd8taAGigyR9k2cVY95RaO7zlnW9G2w3ol5yqsIUp18qU9KQAFB1KcijnQwAAAABrFwAAzfcyzzur9P9hH8Z1Tz3CJfwLBAGQiDKUCdu_IQ6OPwz41RGeFsB9JzrW6KEynJrq\n" +
                "mtgsig: {\"a1\":\"1.1\",\"a2\":1679542173613,\"a3\":\"1679542144990UQYCWISae9035e21f6779e54461ecc6a87e45ea3458\",\"a4\":\"e6593a6ccd275e726c3a59e6725e27cdc185daeab3dbfd27\",\"a5\":\"RYmSa4sY8guPWvXTqeBZyYD0F/Pgpq0nRi9C288Cr6mKjc6td5POcCd0D36yZBKK4tBQoHRXN7Raz1tuiQT+oiL6Mp9o97rgVprFfO9nibLzNlKsgVlRKnZrJF947YUfyfYJmQSTOJuABrRGeKe+M28JqA8D2Acu2RhTkJO1Gp1eyNAcINAaE6Anwh45kEWJApSuJRrVtn3BTW6xDBiyJeSIffZQCNh=\",\"a6\":\"w1.1VITAlUG1siqVQ4gmRXka/OnfhjEv4gCqR1Cp4zfx21NYTzYKmH55WVjfUpkkuI0CTINIqyeDQnezE0Fbg5HUMCVCt9n16c8QZPMmeXGcHQgXokqcLomU3kSVPpo8D8e99QSTqzekUmUZcSyB61Z2lLEOZRYBktXwagutjGWDAi30ftcegYpGhXZYuHRjLijl4QnqeQ04boa0rxKj/QxMetSzNKuic9pWVgJ8BzbamXy+DGRlGjhHMSN53cYtxaqCDDvPqrLfABM/x5Q5fs+PYvXw+lcPGr2tLpu9KwWTAXN8sxiZ9DyFRdSc90OT/K3Yf1i+wAf7n8+jwoJhl7xVXAw9OXpTrQioFgfFN14cdDcH+rp7DVeXOJPIhTa982oDT/WTOy79gGKXqsPJS5LOMv9Q6fKm9vaf6w1vUOWnIhnYyfIGZ3KCN4FkqakS5OhXfYY34+pXyAO5CJUMNG9VKUcEdnK5tdknkeoRGc/T0SwuoefAfr5lo3E5k8yCWxPFnaceV6VL+P7mOC60aT3GOfXkUnxsOKcsW2mB+rzrOJ35D6KrA7DYW5E5KSsCopUb3/2U5PApGbT9+hzoMFvAatJlGEJDHj1lsM+YMze2IFM9oGYNyJFqHqXjQId8cJ+h\",\"a7\":\"wx92916b3adca84096\",\"x0\":3,\"d1\":\"aeab3b3e8ec36fc2dbb7eb3bb4458e00\"}\n" +
                "openIdCipher: AwQAAABJAgAAAAEAAAAyAAAAPLgC95WH3MyqngAoyM/hf1hEoKrGdo0pJ5DI44e1wGF9AT3PH7Wes03actC2n/GVnwfURonD78PewMUppAAAADg+Gu/5QAOZXbmGvFZVV6leU1Rg5QmdbkMafaBzg52eNV61t6boxV2xN3Wf6a85OPkJuBLN+T8WiQ==\n" +
                "Accept-Encoding: gzip,compress,br,deflate\n" +
                "User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.33(0x18002128) NetType/WIFI Language/zh_CN\n" +
                "Referer: https://servicewechat.com/wx92916b3adca84096/294/page-frame.html";
        String post = new DakaController().httpget(httpinfo);
        System.out.println(post);



    }
}
