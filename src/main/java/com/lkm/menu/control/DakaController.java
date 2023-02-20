package com.lkm.menu.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lkm.menu.po.Httpinfo;
import com.lkm.menu.service.HttpInfoJPA;
import com.lkm.menu.service.MailService;
import com.lkm.menu.util.DateFormatUtil;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
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
                if (!StringUtils.isEmpty(mail)){
                    List<String> mailList = new ArrayList<>();
                    mailList.add(mail);
                    try {
                        MailService.send(mailList, httpinfovo.getLastinfo());
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
        if (!StringUtils.isEmpty(mail)) {
            List<String> mailList = new ArrayList<>();
            mailList.add(mail);
            try {
                MailService.send(mailList, httpinfo.getLastinfo());
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

}
