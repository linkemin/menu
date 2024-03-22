package com.lkm.menu.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lkm.menu.po.Httpinfo;
import com.lkm.menu.po.User;
import com.lkm.menu.service.HttpInfoJPA;
import com.lkm.menu.service.MailService;
import com.lkm.menu.service.UserJPA;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Linkm on 2023/1/9.
 */
@RestController
@RequestMapping("/daka")
public class DakaController {

    @Autowired
    HttpInfoJPA httpInfoJPA;

    @Autowired
    UserJPA userJPA;

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
     * 获取当天所有打卡信息
     * @return
     */
    @GetMapping("/getall")
    public Object getall(){
        List<Httpinfo> all = httpInfoJPA.findByScheduled("Y");
        List<User> userAll = userJPA.findAll();
//        HashMap<Integer, String> idNameMap = new HashMap<>();
        ArrayList<Map<Object, Object>> resultList = new ArrayList<>();
//        userAll.forEach(user -> {idNameMap.put(user.getId(), user.getUserName());});
        Map<Integer, String> idNameMap = userAll.stream().collect(Collectors.toMap(User::getId, User::getUserName));
        all.forEach(httpinfo -> {
            HashMap<Object, Object> obj = new HashMap<>();
            obj.put("name", idNameMap.get(httpinfo.getUserId()));
            obj.put("ts", httpinfo.getLasttime());
            if (StringUtils.isEmpty(httpinfo.getLastinfo())){
                obj.put("success", false);
            }else {
                obj.put("success", true);
            }
            resultList.add(obj);
        });
        return resultList;
    }


    /**
     * 保存httpinfo信息
     * @param userId
     * @param httpInfo
     */
    @PostMapping("/save")
    public void save(Integer userId, String httpInfo, String token, String a00){
        List<Httpinfo> httpinfos = httpInfoJPA.findByUserId(userId);
        if (httpinfos.size() > 0){
            Httpinfo httpinfovo = httpinfos.get(0);
            if (!StringUtils.isEmpty(httpInfo)){
                httpinfovo.setHttpinfo(httpInfo);
            }else {
                httpinfovo.setToken(token);
                httpinfovo.setA00(a00);
            }
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
                if (StringUtils.isEmpty(postMsg)){
                    httpinfovo.setA00("");
                    httpinfovo.setToken("");
                }
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
    @Scheduled(cron = "00/8 * 8 ? * MON-FRI")
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
            double random = Math.random() * 10000L;
            if (Double.valueOf(random).intValue() % 200 == 1){
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
    @Scheduled(cron = "00/7 * 18 ? * MON-FRI")
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
            if (random < 2){
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
//    @Scheduled(cron = "00/7 * 20 ? * MON-FRI")
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
            if (random < 2){
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
        if (StringUtils.isEmpty(postMsg)){
            httpinfo.setA00("");
            httpinfo.setToken("");
        }
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
        String token = httpinfo.getToken();
        String a00 = httpinfo.getA00();
        String oldToken = "";
        String oldA00 = "";
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
                    }else {
                        url = lineText.substring(5, lineText.length() - 9);
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
                        if ("yht_access_token".equals(header[0])){
                            oldToken = header[1].trim();
                        }
                        if ("a00".equals(header[0])){
                            oldA00 = header[1].trim();
                        }
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

            if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(a00)){
                //token不为空，且a00不为空，则覆盖
                url = url.replace(oldToken, token);
                Set<String> keys = headers.keySet();
                for (String key : keys) {
                    headers.put(key, headers.get(key).replaceAll(oldToken, token));
                    headers.put(key, headers.get(key).replaceAll(oldA00, a00));
                }
            }
            CloseableHttpClient httpClient = HttpClients.createDefault();
            if (!url.startsWith("https")){
                url = "https://" + headers.get("Host") + url;
            }
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
                    }else {
                        url = lineText.substring(5);
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
            if (!url.startsWith("https")){
                url = "https://" + headers.get("Host") + url;
            }
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

        double d = Math.random()*100000L;
        System.out.println(d);
        System.out.println(Double.valueOf(d).intValue());
        System.out.println(Double.valueOf(d).intValue()%50);



    }
}
