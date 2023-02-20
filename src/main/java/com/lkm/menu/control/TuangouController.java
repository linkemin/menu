package com.lkm.menu.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lkm.menu.po.TuangouBVO;
import com.lkm.menu.po.TuangouVO;
import com.lkm.menu.util.DateFormatUtil;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 团购订单
 * Created by Linkm on 2022/7/20.
 */
@RestController
@RequestMapping("/tuangou")
public class TuangouController {

    @GetMapping("/mtAndXs")
    public Object mtAndXs(){
        Map<String, Object> map = new HashMap<>();
        try {
            ExecutorService executor = Executors.newFixedThreadPool(5);
            CompletableFuture<Object> XS_LMQ = CompletableFuture.supplyAsync(() -> {
                return XS_LMQ();
            }, executor);
            CompletableFuture<Object> MT_LMQ = CompletableFuture.supplyAsync(() -> {
                return MT_LMQ();
            }, executor);
            CompletableFuture<Object> XS_LKM = CompletableFuture.supplyAsync(() -> {
                return XS_LKM();
            }, executor);
            CompletableFuture<Object> MT_LKM = CompletableFuture.supplyAsync(() -> {
                return MT_LKM();
            }, executor);

            CompletableFuture<Void> cfResult = CompletableFuture.allOf(XS_LMQ, MT_LMQ, XS_LKM, MT_LKM);
            CompletableFuture<Map<String, Object>> result = cfResult.thenApply(v -> {
                //这里的join并不会阻塞，因为传给thenApply的函数是在CF3、CF4、CF5全部完成时，才会执行 。
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("xs_lmq", XS_LMQ.join());
                resultMap.put("mt_lmq", MT_LMQ.join());
                resultMap.put("xs_lkm", XS_LKM.join());
                resultMap.put("mt_lkm", MT_LKM.join());
                //根据result3、result4、result5组装最终result;
                return resultMap;
            });
            try {
                map = result.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    
    @GetMapping("/tccAndPdd")
    public Object getGoodsType(){
        Map<String, Object> map = new HashMap<>();
        try {
            ExecutorService executor = Executors.newFixedThreadPool(5);
            CompletableFuture<Object> TCC_LMQ = CompletableFuture.supplyAsync(() -> {
                return "TCC_LMQ()";
            }, executor);
            CompletableFuture<Object> PDD_LMQ = CompletableFuture.supplyAsync(() -> {
                return PDD_LMQ();
            }, executor);
            CompletableFuture<Object> TCC_LKM = CompletableFuture.supplyAsync(() -> {
                return TCC_LKM();
            }, executor);
            CompletableFuture<Object> PDD_LKM = CompletableFuture.supplyAsync(() -> {
                return PDD_LKM();
            }, executor);

            CompletableFuture<Void> cfResult = CompletableFuture.allOf(TCC_LMQ, PDD_LMQ, TCC_LKM, PDD_LKM);
            CompletableFuture<Map<String, Object>> result = cfResult.thenApply(v -> {
                //这里的join并不会阻塞，因为传给thenApply的函数是在CF3、CF4、CF5全部完成时，才会执行 。
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("tcc_lmq", TCC_LMQ.join());
                resultMap.put("pdd_lmq", PDD_LMQ.join());
                resultMap.put("tcc_lkm", TCC_LKM.join());
                resultMap.put("pdd_lkm", PDD_LKM.join());
                //根据result3、result4、result5组装最终result;
                return resultMap;
            });
            try {
                map = result.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    //美团-林克敏
    public static Object MT_LKM(){
        String url = "https://bi-mall.meituan.com/api/c/mallorder/list?uuid=181b990be338e-15167bacbcc733-0-3d10d-181b990be34c8&utm_medium=wxapp&brand=meituanyouxuan&tenantId=1&utm_term=5.103.0&device_model=iPhone%208%20(GSM%2BCDMA)%3CiPhone10%2C1%3E&optimus_risk_level=71&optimus_code=10&offset=0&limit=10&type=0&isNeedToken=true&poiIdStr=tlRz_zUs-4qr4In3gcHi-gE&poi=0&stockPois=0&ci=4&bizId=4&fp_user_id=2420341450&fp_open_id_cipher=AwQAAABJAgAAAAEAAAAyAAAAPLgC95WH3MyqngAoyM%2Fhf1hEoKrGdo0pJ5DI44e1wGF9AT3PH7Wes03actC2n%2FGVnwfURonD78PewMUppAAAADiBxl6VnbN93A7Q45%2FW1XUDwdhMLIFbNLcaySl1fn35SMrmT4bCGqHRV9YYmWvltdvUo%2FPMUP5GuA%3D%3D&openId=oRRr90HOZexjxVINyN6XT_HIJ9Cw&unionId=oNQu9t95ajVa1lJ5Dzrar7msCBwg&superId=A1-A2-A3-A5-A7-A2-A4-A7&routeId=pages%2FuserCenter%2Findex%2Cpages%2ForderList%2Fnew&sysName=iOS&sysVerion=14.1&app_tag=youxuan";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Host","bi-mall.meituan.com");
        httpGet.addHeader("Connection","keep-alive");
        httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat");
        httpGet.addHeader("Content-Type","multipart/form-data");
        httpGet.addHeader("mtgsig","{\"a1\":\"1.1\",\"a2\":1659055755044,\"a3\":\"z7x111u7x1925uxzy4643v49u6w09w05818y009xxv187978y791075x\",\"a4\":\"669093ad4b573dc7ad939066c73d574b895cfd214e61aa6a\",\"a5\":\"54/Y17Xh2kToJquD6KsD2yohA/HAcAI1jk2KskAr/g/fvKaCwUujuejYGTzA8AEMJ2QpyIt/yTcGh+jXNwvCyUBkP1NH/mzhQL8dGibKbaWrekORoZLrynXI/K3FS7Jk16u6gi+KRYVfYGlilkDLAAMQq5rKJ04dPTZWVRJ+GLMqd11y83002nb7TOka8YK+lssGCbODbhN5fRpeGmiNNXhBrat3D9NxRrml8Ws5Z4Jj2SHPIOu5qkXB1nnr3cvWCMheMDgPgZ==\",\"a6\":\"w1.1D67dbXpPPJ0Zv+qLUGxmwVKX175L0rOeYWhyoGtSpkqHHj9crIHvXn75k85W+C9UxupuFFH4vuqOHwaqhahJOSq5Tal/PkfznIjxFyXcXBn6OopKNaHfLdBR9MsWQbIhbXwOA1Mp9lQsFJcs9oGDB3mA6VGCgFYr+ev3vkAi1rlrmoham2qkPPMIUvb0wgS4XLnNHhalmIsYZNNjseSMrxH1LMKvLqXbx/Sx9wmkb8Mgp0EcBJX+qktsEwBYlHUxmGWYwFOjnukpCYFxCyb87TFptZ3B7eP0hxcHpP3qImfy4bAQNBS77S2JRPac8jj38IdLhkGG7X6YY99UI9+XAl+Xm/xpaO0IMxu4/IFoaUICcY/uq0yLADxmOgnAphN024OwIodFRjiKvkdloShi9O+Uv9N9PAymaELHiX60E/10URQQhONnk13mb5VLddTfCS46T3/on0HIW4d7vYXT6jObJf+m6lKsFLRtS4MsoByFZXAodDcVmaFEi1heW8MSX96pEWKntnHDqeGzHjvrECIcr6jd17XAa/5abGhI+5lSskw4/Hdor8qJ8FZ7JFQ1nP5LiJGroTXJBEJev4Pwr5YgKizD/+cp8IVFnXwPERYdlKU5A7+iSJpzonWAJ7IhLzG5sroe+lp+2rnQzVSssbFBe/qQrVZ5kjGR3k2M9JhlT/WGH9g7rJ14wkEf3HufgGJMeZTSz/o1OQ1xpei4FOMsk8xKAld0j4booO8Usjq9bg4poZPJyNjAbBv4n2u3\",\"a7\":\"wx77af438b3505c00e\",\"x0\":3,\"d1\":\"0d728b4e896af56c836fb2cd4b1b4715\"}");
        httpGet.addHeader("t","KwMKY9x8CEapU4fep2RFEm2hrhgAAAAAkBIAAElc5HP-ii1IR_MbMrkPnD2seEe9GCeDaHJQdi_rrD7kTumi0x3USTs9bypDdDamJg");
        httpGet.addHeader("Referer","https://servicewechat.com/wx77af438b3505c00e/919/page-frame.html");
        httpGet.addHeader("Accept-Encoding","gzip, deflate, br");

        try {
            HttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstMT(body);
        } catch (Exception e) {
            return "美团数据解析出错";
        }
    }

    //美团-李梦茜
    public static Object MT_LMQ(){
        String url = "https://bi-mall.meituan.com/api/c/mallorder/list?uuid=180d0cc1e8dc8-211214c70d4048-0-5a900-180d0cc1e8dc8&utm_medium=wxapp&brand=meituanyouxuan&tenantId=1&utm_term=5.102.1&device_model=iPhone%2011%3CiPhone12%2C1%3E&optimus_risk_level=71&optimus_code=10&offset=0&limit=10&type=0&isNeedToken=true&poiIdStr=AvkQoE2h0iPpWb7lq_dE6AE&poi=0&stockPois=0&ci=4&bizId=4&fp_user_id=117373234&fp_open_id_cipher=AwQAAABJAgAAAAEAAAAyAAAAPLgC95WH3MyqngAoyM%2Fhf1hEoKrGdo0pJ5DI44e1wGF9AT3PH7Wes03actC2n%2FGVnwfURonD78PewMUppAAAADi5IlvAPPPfEaIlDgyZmX3ORATS8UOANLg1Vbc%2FyLC%2FFxujSAfwEh9QFgHuecM2y4sTArB5HS%2FQVw%3D%3D&openId=oRRr90IcLUX5ON1b16Ph-oe4JBtU&unionId=oNQu9t57LEMYI2C0hIcees-GQNgE&superId=A1-A2-A3-A5-A7&routeId=pages%2FuserCenter%2Findex%2Cpages%2ForderList%2Fnew&sysName=iOS&sysVerion=15.5&app_tag=youxuan";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Host", "bi-mall.meituan.com");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("t", "i50RChmp79lazkg9gXT1p_TIUXgAAAAA0BEAAMdvVCzjuWsCCAeLcOSyXbhmfglFtnX_os7UYjGDPNv1ocTvjhAStdJsG09Z6QTvAA");
        httpGet.addHeader("content-type", "multipart/form-data");
        httpGet.addHeader("Accept-Encoding", "gzip,compress,br,deflate");
        httpGet.addHeader("mtgsig", "{\"a1\":\"1.1\",\"a2\":1658758893566,\"a3\":\"36yw566u80uy57181x002z2vz17z4y39819w9xx781787978216x1vy0\",\"a4\":\"5b9ec36457e9e0b364c39e5bb3e0e9579acc226e292ccb39\",\"a5\":\"F3hy0ZziaZuM6Z5XrQCBneO4fSv14+RfQcHkc7Kp6gJfv92mtzoPrt+R57psTub5ajuxQxNtWqI+/nvG6UNJeX/gXtSYYvEynQFK+Cv4KZPFdRz394JnmD9jTZvWHr1amvNL5bnkX6nu/PSFNcUmkthyrka7G3lU8Lgty9xK1QnFH/hiW0MEvuk1w5W/TWhhO3WnLciZkDWHGlN2n4TC9LTq6TSSta9FSW9NVwPGjOyL5N5NJxWGOEj8IKB/htKa+Ui17l3H4I==\",\"a6\":\"w1.1HXq1gIirhvWA66O3lYJ67fkF09+UPJIQ3F8t3uFjuoOkpJyKRAMRXLZFzpnaNdjd5FfFjcYLfsonxEcP3GM/ysTP8rCRR4zxF/7IA0vhY1sqGLLbBRdJRu6Skz/rSlrlk1gsoq0thEeSgKVQCC9yEqtdhdKSE731RmbOSuyclw2kslhsIbsRNQGg9rzF2wgVpm3U6J1fT1a7L4OxqZcjqoSd7QQQBFsIhAPGYwx836gmZb2TN3RICYou231sc50K6khIk7rG3Wmmk+cD8WEzGgxi+R/b+AQpcTY8XUGsDkcgw8OAkjBmUR9HXKDuBVhiQY4rNM/1z+NHTZwpzcX7db7CLbEaDFJXjm4Y5rpubL91wQr88AuU2NyIUhnis2gRPBhf3Q1jNhqF+oI+R3t17kN7F7ZIZKtnhFatRdMSkifTnmzazLRbRupmUVSFt4jEKfg/EZL//Zck5gfrrJLd6081blHyoTHOHE4chOb72wV6KWByki3UInklebnMtwbAzBv/L5Yb7BMFyswLiebC0LlzC893wEVqieY75YnxtcHCBNOanA5ehaDEPus6jtyO+67AGWDjGf3+kEHklA0bdtV3F/Ru0vDFesoNs5gl3vY=\",\"a7\":\"wx77af438b3505c00e\",\"x0\":3,\"d1\":\"b0b25cc0c70a069dbc2abfb501b6a622\"}");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 15_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.25(0x1800192a) NetType/WIFI Language/zh_CN");
        httpGet.addHeader("Referer", "https://servicewechat.com/wx77af438b3505c00e/917/page-frame.html");

        try {
            HttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstMT(body);
        } catch (Exception e) {
            return "美团数据解析出错";
        }
    }

    //兴盛-林克敏
    public static Object XS_LKM(){
        String url = "https://trade.xsyxsc.com/tradeorder/order/v1/getOrderUserList";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Host", "trade.xsyxsc.com");
        httpPost.addHeader("Connection", "keep-alive");
//        httpPost.addHeader("Content-Length", "83");
        httpPost.addHeader("Accept", "application/json, text/plain, */*");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat");
        httpPost.addHeader("X-Feature-Tag", "item_product_home");
        httpPost.addHeader("clientType", "MINI_PROGRAM");
        httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
        httpPost.addHeader("fuzzy", "true");
        httpPost.addHeader("preBuy", "true");
        httpPost.addHeader("source", "applet");
        httpPost.addHeader("userKey", "e55c0872-2002-406e-b54c-5422e9a7b5fc");//userkey需动态调整，每登录一次调整一次
        httpPost.addHeader("userRecommend", "true");
        httpPost.addHeader("version", "2.4.28");
        httpPost.addHeader("Referer", "https://servicewechat.com/wx6025c5470c3cb50c/662/page-frame.html");
        httpPost.addHeader("Accept-Encoding", "gzip, deflate, br");

        List<BasicNameValuePair> param = new ArrayList<>(4);
        param.add(new BasicNameValuePair("clientType", "MINI_PROGRAM"));
        param.add(new BasicNameValuePair("userKey", "6882d37a-977c-4e91-9fd5-3884c801b77f"));
        param.add(new BasicNameValuePair("page", "1"));
        param.add(new BasicNameValuePair("rows", "10"));

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(param, StandardCharsets.UTF_8);
        httpPost.setEntity(formEntity);
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);//执行请求
            String body = EntityUtils.toString(response.getEntity());//获取响应内容
            return converstXS(body);
        } catch (Exception e) {
            return "兴盛数据解析出错";
        }
    }

    //兴盛-林克敏
    public static Object XS_LMQ(){
        String url = "https://trade.xsyxsc.com/tradeorder/order/v1/getOrderUserList";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Host", "trade.xsyxsc.com");
        httpPost.addHeader("Connection", "keep-alive");
//        httpPost.addHeader("Content-Length", "83");
        httpPost.addHeader("Accept", "application/json, text/plain, */*");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat");
        httpPost.addHeader("X-Feature-Tag", "item_product_home");
        httpPost.addHeader("clientType", "MINI_PROGRAM");
        httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
        httpPost.addHeader("fuzzy", "true");
        httpPost.addHeader("preBuy", "true");
        httpPost.addHeader("source", "applet");
        httpPost.addHeader("userKey", "1e984ae1-c99b-4ed7-9a60-9d6c753c1b0d");//userkey需动态调整，每登录一次调整一次
        httpPost.addHeader("userRecommend", "true");
        httpPost.addHeader("version", "2.4.33");
        httpPost.addHeader("Referer", "https://servicewechat.com/wx6025c5470c3cb50c/667/page-frame.html\n");
        httpPost.addHeader("Accept-Encoding", "gzip, deflate, br");

        List<BasicNameValuePair> param = new ArrayList<>(4);
        param.add(new BasicNameValuePair("clientType", "MINI_PROGRAM"));
        param.add(new BasicNameValuePair("userKey", "1e984ae1-c99b-4ed7-9a60-9d6c753c1b0d"));
        param.add(new BasicNameValuePair("page", "1"));
        param.add(new BasicNameValuePair("rows", "10"));

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(param, StandardCharsets.UTF_8);
        httpPost.setEntity(formEntity);
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);//执行请求
            String body = EntityUtils.toString(response.getEntity());//获取响应内容
            return converstXS(body);
        } catch (Exception e) {
            return "兴盛数据解析出错";
        }
    }



    //淘菜菜-李梦茜
    public static Object TCC_LMQ(){
        String url = "https://taocaicai.m.taobao.com/render/app/mmc-youxuan/taocaicai-trade/orderlist.html?scene=h%3Dn-5-1-7-1.2.1-1cd7l-l1aa6oggg27qkzr363p&pc_i=WDU2VGVwdjk2WW9EQU1TNHV6QWxjQ1lj&ps_i=632c913a55194a2580e7ffe79035c00a&status=1&switchOldTab=false&spm=a21bcv.8238533.order_manager.all_order&spmUrl=a21bcv.8200897.navigateto.clk&pgUrl=1658759364845.1658759893625&rst=1658759898357&prismChannel=tcc&disableNav=YES&status_bar_transparent=true&ttid=201200@taobao_iphone_10.14.0";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Host", "taocaicai.m.taobao.com");
        httpGet.addHeader("Accept-Encoding", "gzip, deflate, br");
        httpGet.addHeader("Cookie", "WAPFDFDTGFG=%2B4cMKKP%2B8PI%2BuhR95lySIoNk9FlxXvbFRvM%3D; _w_tb_nick=%E6%A2%A6%E6%A2%A6%E6%A2%A6%E6%A2%A6%E8%8C%9C; imewweoriw=3%2FrKsvyBP7XGLZ1TKNw%2BY89iJ6sONl5o%2BgGg08zyrlA%3D; ockeqeudmj=txX2gB0%3D; _cc_=URm48syIZQ%3D%3D; _l_g_=Ug%3D%3D; _nk_=%5Cu68A6%5Cu68A6%5Cu68A6%5Cu68A6%5Cu831C; _tb_token_=eeef1374373ee; cancelledSubSites=empty; cookie1=WvcUJJSzj3GEs0iKRTDO%2FnqjS0qlMC%2FxnGsdbk%2BE7uc%3D; cookie17=UonZBGTWkeehQw%3D%3D; cookie2=1bc9ca89c7c87d43ef10975de74ea3ac; csg=e7d95835; dnk=%5Cu68A6%5Cu68A6%5Cu68A6%5Cu68A6%5Cu831C; lgc=%5Cu68A6%5Cu68A6%5Cu68A6%5Cu68A6%5Cu831C; munb=1823323207; sg=%E8%8C%9C76; sgcookie=W100GEJ%2B%2BBcW%2FYtaUakukalw5qMBQvLRTME9GgbqKiwYcAEmkRtJfjDEFQxoy9rYOtz10XeE9atHMo7ZsS7c5JeQOvgxB%2FFwQ9%2FbaoPqeHZ%2F4Gx8Dv3Jkb2pxIST1pjqhJrK; skt=26a3a88ca85f206a; t=f128dc644772fdf40abbcc507d4239c5; tracknick=%5Cu68A6%5Cu68A6%5Cu68A6%5Cu68A6%5Cu831C; uc1=cookie14=UoexOtswU6QGgA%3D%3D&cookie15=VFC%2FuZ9ayeYq2g%3D%3D&existShop=false&cookie21=UIHiLt3xSift4ZS1LW77rg%3D%3D; uc3=vt3=F8dCv4JkF2X2qkGV8uo%3D&lg2=WqG3DMC9VAQiUQ%3D%3D&id2=UonZBGTWkeehQw%3D%3D&nk2=oHTKLvcrO35EBw%3D%3D; uc4=id4=0%40UOEzS2wkZCQNXmgy9vRCHBdk11MN&nk4=0%40oibrfHBka%2F4UAW83vuXDbOgmsQmr; unb=1823323207; isg=BDs7_fb73ZyTAeJ997fSEfYHwBulkE-Sz2w9nS34cDoljElutWGs48GGoqrCt6eK; l=eBMZM-vcgmVjWm-zmOfahurza77tcCp4YuPzaNbMiOCPOQbB5mjRW6vUyRd6Cn1Rns_wR38ioSTpBXL38yzM5Q-Yh_e0NhXZWdv1.; tfstk=cIZRBA9ivEKUoYqOF8Qm8ntInjx5aE_-TCcDvkyWEf-PbgABps2zs5dsrGDjHVXA.; hng=CN%7Czh-CN%7CCNY%7C156; _m_h5_tk=c8d27f29691f5abf18d64b8f0f226da1_1658722346631; _m_h5_tk_enc=32124eb135fa1b605d7d6e4225979c09; xlly_s=1; cna=h7clGJ8efzMCAXhVTpzuDcG5; enc=C8QuOSLo%2Bzv8A%2FU1O1X46U1kvdFICk4VBzZzI8Cy5uh0Y2WGKAf2veTgnk0nKQfyfZygM13%2BFmk4P5OND1fNZQ%3D%3D; csa=11273521320_0_23.145531.113.412149_0_0_0_440106_109_0_233930488_236839048_440106016_0; sm4=440106; thw=xx; miid=455565021360541157");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("f-refer", "wv_h5");
        httpGet.addHeader("Device-ID", "J0QfNZmNLGCjVZJOcPmiC2AD1MsXGg6wKcoqU4H0HcYmgFE6vv43tbMBJelyzeLe");
        httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 15_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 AliApp(TB/10.14.0) WindVane/8.7.2 UT4Aplus/0.0.4 828x1792 WK");
        httpGet.addHeader("f-pTraceId", "WVNet_WV_22-22-456");
        httpGet.addHeader("Referer", "https://taocaicai.m.taobao.com/render/app/mmc-youxuan/taocaicai-guide/my.html?scene=h%3Dn-5-1-7-1.2.1-1cd7l-l1aa6oggg27qkzr363p&pc_i=WDU2VGVwdjk2WW9EQU1TNHV6QWxjQ1lj&ps_i=632c913a55194a2580e7ffe79035c00a&shopId=658961161&disableNav=YES&status_bar_transparent=true&undefined=undefined&spm=a21bcv.8200897.navigateto.clk&rst=1658759363712&prismChannel=tcc&nextPageAnimation=false&ttid=201200@taobao_iphone_10.14.0");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstTCC(body);
        } catch (Exception e) {
            return "淘菜菜数据解析出错";
        }
    }

    //淘菜菜刷新token-林克敏
    public static Object TCC_LKM(){
        String url = "https://acs.m.hemaapi.cn/h5/mtop.wdk.youxuan.trade.order.queryorderlist/1.0/2.0/?jsv=2.4.12&appKey=12574478&t=1658903845315&sign=023608a54e61c7b517ddc7b00ea22f2c&c=409dc687b0abbd1f180a5a72fb5a8a6c_1658903408545%3Bfaf956f02851fc35dab6659f0c03ef6b&needLogin=true&needRetry=true&needLink2Login=true&api=mtop.wdk.youxuan.trade.order.queryOrderList&valueType=string&v=1.0&type=json&ttid=wap_SG_HMYX_H5%40yxwxhmxs_iPhone_4.51.0&dataType=json&data=%7B%22tabId%22%3A%221%22%2C%22page%22%3A1%2C%22pageSize%22%3A10%2C%22channelSource%22%3A%22wechat%22%2C%22source%22%3A%22wechat%22%2C%22scenarioGroup%22%3A%22HEMA_YOUXUAN%22%2C%22terminal%22%3A%22hmyx_wx_applet%22%2C%22channelCode%22%3A%22SG_WX_APPLETS%22%7D";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Host", "acs.m.hemaapi.cn");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("x-tap", "wx");
        httpGet.addHeader("content-type", "application/x-www-form-urlencoded");
        httpGet.addHeader("x-smallstc", "{\"loginSucResultAction\":\"loginResult\",\"st\":\"15354130e7660798f0ee6c914ff49478\",\"loginType\":\"snsLogin\",\"loginScene\":\"miniProgramLogin\",\"resultCode\":100,\"appEntrance\":\"weixin\",\"smartlock\":false,\"snsType\":\"weixin_mini_program\",\"sid\":\"15354130e7660798f0ee6c914ff49478\",\"cookie2\":\"15354130e7660798f0ee6c914ff49478\",\"munb\":1793903346,\"loginResult\":\"success\",\"csg\":\"d4b16f24\"}");
        httpGet.addHeader("Accept", "application/json");
        httpGet.addHeader("Accept-Encoding", "gzip,compress,br,deflate");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.26(0x18001a26) NetType/WIFI Language/zh_CN");
        httpGet.addHeader("Referer", "https://servicewechat.com/wx2ea1c42807a06e36/72/page-frame.html");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            //第一次的令牌是过期的，需重新生成令牌再次调接口
            JSONObject jsonObject = JSON.parseObject(body);
            //c的第一个下划线前面是token
            //11205d94d9e27781f3106cfd3d47b57d_1658913449678;f9d6d1c64da421382aa97f235563d0e8
            return TCC2_LKM(jsonObject.getString("c"));
        } catch (Exception e) {
            return "淘菜菜数据解析出错";
        }
    }

    //淘菜菜-林克敏
    public static Object TCC2_LKM(String c){
        long ts = System.currentTimeMillis();
        String sign = c.split("_")[0] + "&" + ts + "&12574478&{\"tabId\":\"1\",\"page\":1,\"pageSize\":10,\"channelSource\":\"wechat\",\"source\":\"wechat\",\"scenarioGroup\":\"HEMA_YOUXUAN\",\"terminal\":\"hmyx_wx_applet\",\"channelCode\":\"SG_WX_APPLETS\"}";
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        digest.update(sign.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        String signMd5 =builder.toString();

        String url = "https://acs.m.hemaapi.cn/h5/mtop.wdk.youxuan.trade.order.queryorderlist/1.0/2.0/?jsv=2.4.12&appKey=12574478&t=" + ts + "&sign=" + signMd5 + "&c=" + c + "&needLogin=true&needRetry=true&needLink2Login=true&api=mtop.wdk.youxuan.trade.order.queryOrderList&valueType=string&v=1.0&type=json&ttid=wap_SG_HMYX_H5%40yxwxhmxs_iPhone_4.51.0&dataType=json&data=%7B%22tabId%22%3A%221%22%2C%22page%22%3A1%2C%22pageSize%22%3A10%2C%22channelSource%22%3A%22wechat%22%2C%22source%22%3A%22wechat%22%2C%22scenarioGroup%22%3A%22HEMA_YOUXUAN%22%2C%22terminal%22%3A%22hmyx_wx_applet%22%2C%22channelCode%22%3A%22SG_WX_APPLETS%22%7D";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Host", "acs.m.hemaapi.cn");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("x-tap", "wx");
        httpGet.addHeader("content-type", "application/x-www-form-urlencoded");
        httpGet.addHeader("x-smallstc", "{\"loginSucResultAction\":\"loginResult\",\"st\":\"15354130e7660798f0ee6c914ff49478\",\"loginType\":\"snsLogin\",\"loginScene\":\"miniProgramLogin\",\"resultCode\":100,\"appEntrance\":\"weixin\",\"smartlock\":false,\"snsType\":\"weixin_mini_program\",\"sid\":\"15354130e7660798f0ee6c914ff49478\",\"cookie2\":\"15354130e7660798f0ee6c914ff49478\",\"munb\":1793903346,\"loginResult\":\"success\",\"csg\":\"d4b16f24\"}");
        httpGet.addHeader("Accept", "application/json");
        httpGet.addHeader("Accept-Encoding", "gzip,compress,br,deflate");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.26(0x18001a26) NetType/WIFI Language/zh_CN");
        httpGet.addHeader("Referer", "https://servicewechat.com/wx2ea1c42807a06e36/72/page-frame.html");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstTCC2(body);
        } catch (Exception e) {
            return "淘菜菜数据解析出错";
        }
    }


    //拼多多-林克敏
    public static Object PDD_LKM(){
        String url = "https://m.pinduoduo.net/vgt_order_list.html?_x_cid=ddmc_app_home&refer_page_name=fresh_mall_list&mx_img_nano=M5-D6ee51gPRHggrH37Iu3-1R_xYwgYqb8Do9mLnyxUirRUU2tPQ5Wv6xwp0F-S9K0Y4UNgHnCMb40owWq9TBPTn16YGJGS8GnLOWAmSINoUkeZTncELIBgi3oo35XZv9Ew6tKitKIPKl-HgjWmJtR4tC9FAqDsce1Qz_tDL1jfUgsUotdBm8apnK_zYnpKCbPkIdLuRXW4_nS25_7a5zFTHT4xnH0hKuBr7LQOvF6I&refer_page_sn=69254&_p_page=vgt_order_list&_t_timestamp=mobile-vegetables&refer_page_id=69254_1658311014739_i8xe3kzg7w";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Host", "m.pinduoduo.net");
        httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpGet.addHeader("p-uno-context", "{\"ls\":0,\"webp\":0}");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("Cookie", "dilx=mdFjpQBOjZ3sHNGxBEqS5;jrpl=ghUPyGM0VgOA9Fi0WO6KMzrmrZB0TQ57;njrpl=ghUPyGM0VgOA9Fi0WO6KMzrmrZB0TQ57;pdd_vds=gaeLuOebNNubGNxObOsbIiLGLiytlbwQNaInIQIGGGOGbNmPsQOOxyeiltLt;webp=1;ETag=aND5MLU7;PDDAccessToken=QJQH5L4IRXNYNUS3JXKNQ3STBAU4JPD2OTXIN5L4J6GQEGFA7QLQ1126a92;install_token=18A38DAA-A972-479C-B2C0-2DD87BD99570;pdd_user_id=8603322432106;pdd_user_uin=M34XTQWGTHMAWEB7FCNRWWUZ7A_GEXDA;13125-boutique_hub_monthly_card=t9oouudp8fdex4g2;_nano_fp=XpEyl0PxXqmjXqPbXC_7VeyYYMMPo6J4HK3gp3_1;api_uid=CkxUP2KTQq1oSgBkGK5QAg==");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 phh_ios_version/6.0.0 phh_ios_build/202202142026 pversion/2368");
        httpGet.addHeader("Accept-Language", "zh-cn");
        httpGet.addHeader("X-PDD-QUERIES", "width=750.000000&height=1334.000000&brand=apple&model=iPhone 8&osv=14.1&appv=6.0.0&pl=iOS&net=1");
        httpGet.addHeader("Accept-Encoding", "gzip, deflate, br");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstPDD(body, false);
        } catch (Exception e) {
            return "多多买菜数据解析出错";
        }
    }

    //拼多多-李梦茜
    public static Object PDD_LMQ(){
        String url = "https://m.pinduoduo.net/vgt_order_list.html?_x_cid=ddmc_app_home&refer_page_name=fresh_mall_list&mx_img_nano=KsBPSLcnzB5ES7iwkN6PRWBgaRjLZSffWDq9dVcaYPCCZmPsa3l0LMiaA73X76ipc5bMyCQjhl4V9ihCJaQwuwQcahQXQkp63M2XXwSRstwPzIwJ3EUxrwimvAGVd8kw4mpR7QzXmlAbZlRs7db96TtpzTyPqjKpdt6Ot9VRFKXrtwgZZ0bV_bKvH5_t7LvVZWTwo31I_ZvNrozhmav48HP2_dUVoNX9OdInkRZ0KQo&refer_page_sn=69254&_p_page=vgt_order_list&_t_timestamp=mobile-vegetables&refer_page_id=69254_1658760033178_0pn2calbr6";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Host", "m.pinduoduo.net");
        httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpGet.addHeader("p-uno-context", "{\"ls\":0,\"webp\":0}");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("Cookie", "dilx=6_F4XUbRTnmjRHmnqFWL9;jrpl=fEM4H1wqm0ezSdXQ7qFSArgWZrw2GtEr;njrpl=fEM4H1wqm0ezSdXQ7qFSArgWZrw2GtEr;pdd_vds=gaLLNOLGNtOniOEIiQQaotoOtaQNEnoPNQPiGbOttEtootaEbaIoybGoibPE;ETag=SGBtKplw;PDDAccessToken=UFT74R747TP3ZZ3LWQ6GQCOMGGHETS7V7G3RPWGPPZTMJVGFWKMA111e9c1;install_token=3FAE0947-F7D1-4580-9906-22EBA5562C57;pdd_user_id=3883644393;pdd_user_uin=XV42OPNW2S5HWIR32KOCFFX3SQ_GEXDA;webp=1;hub_medicine_goods_hub_medicine_goods=5l9q5hkultmyp862;13125-boutique_hub_monthly_card=84z7x6lpafbgaf5q;48767-boutique_hub_week_coupon=tfuile267rbemlwn;_nano_fp=XpEyl0E8XqdqnqEYn9_wZ~utsRvr_VilX58MWDH~;api_uid=CklYrGKW/PZVjABsJDnBAg==");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 15_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 phh_ios_version/6.19.0 phh_ios_build/202207122149 pversion/2446");
        httpGet.addHeader("Accept-Language", "zh-CN,zh-Hans;q=0.9");
        httpGet.addHeader("X-PDD-QUERIES", "width=828.000000&height=1792.000000&brand=apple&model=iPhone 11&osv=15.5&appv=6.19.0&pl=iOS&net=1");
        httpGet.addHeader("Accept-Encoding", "gzip, deflate, br");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstPDD(body, true);
        } catch (Exception e) {
            return "多多买菜数据解析出错";
        }
    }

    /**
     * 解析美团
     * @param body
     * @return
     */
    public static List<TuangouVO> converstMT(String body) throws Exception{
        JSONObject jsonObject = JSON.parseObject(body);
        List<TuangouVO> list = new ArrayList<>();
        if (jsonObject != null){
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null){
                JSONArray data1 = data.getJSONArray("orderList");
                data1.stream().forEach(x -> {
                    JSONObject x1 = (JSONObject) x;
                    JSONObject selfLiftInfo = x1.getJSONObject("selfLiftInfo");
                    String deliveryTime = selfLiftInfo.getString("selfLiftTimeLong");
                    String deliveryDate = DateFormatUtil.getFormatDate(Long.valueOf(deliveryTime), "yyyy-MM-dd");
                    Integer comparedate = DateFormatUtil.comparedate(deliveryDate, DateFormatUtil.getDateFormatStr(null));
                    if (comparedate >= -1){
                        TuangouVO tuangouVO = new TuangouVO();
                        tuangouVO.setOrderDate(DateFormatUtil.getFormatDate(Long.valueOf(x1.getString("orderTime")), "yyyy-MM-dd HH:mm:ss"));
                        tuangouVO.setPhone(selfLiftInfo.getString("selfLiftMobile"));
                        tuangouVO.setOrderTotal("" + Float.valueOf(x1.getString("cashPay"))/100f);
                        tuangouVO.setTotalCount(x1.getString("count"));
                        tuangouVO.setStoreName(selfLiftInfo.getString("selfLiftName"));
                        tuangouVO.setDeliveryTime(deliveryDate);//yyyy-MM-dd
                        tuangouVO.setOriginName("美团优选");
                        JSONArray orderDetailItemList = x1.getJSONArray("orderDetailItemList");
                        List<TuangouBVO> tuangouBVOS = new ArrayList<>();
                        orderDetailItemList.stream().forEach(y -> {
                            JSONObject y1 = (JSONObject) y;
                            TuangouBVO tuangouBVO = new TuangouBVO();
                            tuangouBVO.setProductName(y1.getString("skuName"));
                            tuangouBVO.setProductCount(y1.getString("count"));
                            tuangouBVO.setProductPrice("" + Float.valueOf(y1.getString("sellPrice"))/100f);
                            tuangouBVOS.add(tuangouBVO);
                        });
                        tuangouVO.setItemLists(tuangouBVOS);
                        list.add(tuangouVO);
                    }
                });
                return list;
            }
        }
        return null;
    }


    /**
     * 解析兴盛
     * @param body
     * @return
     */
    public static List<TuangouVO> converstXS(String body) throws Exception{
        JSONObject jsonObject = JSON.parseObject(body);
        List<TuangouVO> list = new ArrayList<>();
        if (jsonObject != null){
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null){
                JSONArray data1 = data.getJSONArray("data");
                data1.stream().forEach(x -> {
                    JSONObject x1 = (JSONObject) x;
                    JSONArray itemList = x1.getJSONArray("itemList");
                    String deliveryTime = itemList.getJSONObject(0).getString("deliveryTime");
                    String deliveryDate = DateFormatUtil.getFormatDate(Long.valueOf(deliveryTime), "yyyy-MM-dd");
                    Integer comparedate = DateFormatUtil.comparedate(deliveryDate, DateFormatUtil.getDateFormatStr(null));
                    if (comparedate >= -1){
                        TuangouVO tuangouVO = new TuangouVO();
                        tuangouVO.setOrderDate(DateFormatUtil.getFormatDate(Long.valueOf(x1.getString("orderDate")), "yyyy-MM-dd HH:mm:ss"));
                        tuangouVO.setPhone(x1.getString("phone"));
                        tuangouVO.setOrderTotal(x1.getString("orderTotal"));
                        tuangouVO.setTotalCount(x1.getString("totalQty"));
                        tuangouVO.setStoreName(x1.getString("storeName"));
                        tuangouVO.setDeliveryTime(deliveryDate);//yyyy-MM-dd
                        tuangouVO.setOriginName("兴盛优选");
                        List<TuangouBVO> tuangouBVOS = new ArrayList<>();
                        itemList.stream().forEach(y -> {
                            JSONObject y1 = (JSONObject) y;
                            TuangouBVO tuangouBVO = new TuangouBVO();
                            tuangouBVO.setProductName(y1.getString("productName"));
                            tuangouBVO.setProductCount(y1.getString("qty"));
                            tuangouBVO.setProductPrice(y1.getString("itemAdjustedPrice"));
                            tuangouBVOS.add(tuangouBVO);
                        });
                        tuangouVO.setItemLists(tuangouBVOS);
                        list.add(tuangouVO);
                    }
                });
                return list;
            }
        }
        return null;
    }

    /**
     * 解析淘菜菜
     * @param body
     * @return
     */
    public static List<TuangouVO> converstTCC(String body) throws Exception{
        Document doc = Jsoup.parse(body);
        JSONObject jsonObject = JSON.parseObject(doc.select("script[data-from=server]").get(0).html().replace("window.__INITIAL_DATA__=", ""));
        List<TuangouVO> list = new ArrayList<>();
        if (jsonObject != null){
            JSONObject data = jsonObject.getJSONObject("pageInitialProps");
            if (data != null){
                JSONArray data1 = data.getJSONObject("pageData").getJSONObject("model").getJSONArray("data");
                data1.stream().forEach(x -> {
                    JSONObject x1 = (JSONObject) x;
                    JSONObject grouponStation = x1.getJSONObject("grouponStation");
                    JSONObject paymentInfo = x1.getJSONObject("paymentInfo");
                    JSONArray itemList = x1.getJSONArray("subOrderList");
                    String deliveryDate = x1.getString("arriveTime").substring(0, 10);
                    Integer comparedate = DateFormatUtil.comparedate(deliveryDate, DateFormatUtil.getDateFormatStr(null));
                    if (comparedate >= -1){
                        TuangouVO tuangouVO = new TuangouVO();
                        tuangouVO.setOrderDate(DateFormatUtil.getFormatDate(Long.valueOf(x1.getString("payTime")), "yyyy-MM-dd HH:mm:ss"));
                        tuangouVO.setPhone(grouponStation.getString("receiverPhone"));
                        tuangouVO.setOrderTotal("" + Float.valueOf(paymentInfo.getString("totalFee"))/100f);
                        tuangouVO.setTotalCount("0");
                        tuangouVO.setStoreName(grouponStation.getString("name"));
                        tuangouVO.setDeliveryTime(deliveryDate);//yyyy-MM-dd
                        tuangouVO.setOriginName("淘菜菜");
                        List<TuangouBVO> tuangouBVOS = new ArrayList<>();
                        itemList.stream().forEach(y -> {
                            JSONObject y1 = (JSONObject) y;
                            TuangouBVO tuangouBVO = new TuangouBVO();
                            tuangouBVO.setProductName(y1.getString("title"));
                            tuangouBVO.setProductCount(y1.getString("buyAmount"));
                            tuangouBVO.setProductPrice("" + Float.valueOf(y1.getString("unitPromotionSalePrice"))/100f);
                            tuangouVO.setTotalCount("" + (Integer.valueOf(y1.getString("buyAmount")) + Integer.valueOf(tuangouVO.getTotalCount())));
                            tuangouBVOS.add(tuangouBVO);
                        });
                        tuangouVO.setItemLists(tuangouBVOS);
                        list.add(tuangouVO);
                    }
                });
                return list;
            }
        }
        return null;
    }

    /**
     * 解析淘菜菜
     * @param body
     * @return
     */
    public static List<TuangouVO> converstTCC2(String body) throws Exception{
        JSONObject jsonObject = JSON.parseObject(body);
        List<TuangouVO> list = new ArrayList<>();
        if (jsonObject != null){
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null){
                JSONArray data1 = data.getJSONArray("orderList");
                data1.stream().forEach(x -> {
                    JSONObject x1 = (JSONObject) x;
                    JSONObject selftakeStation = x1.getJSONObject("selftakeStation");
                    JSONObject paymentInfo = x1.getJSONObject("paymentInfo");
                    JSONArray itemList = x1.getJSONArray("subOrderList");
                    String deliveryDate = x1.getString("arriveTime").substring(0, 10);
                    Integer comparedate = DateFormatUtil.comparedate(deliveryDate, DateFormatUtil.getDateFormatStr(null));
                    if (comparedate >= -1){
                        TuangouVO tuangouVO = new TuangouVO();
                        tuangouVO.setOrderDate(x1.getString("payTime").replaceAll("/", "-"));
                        tuangouVO.setPhone(selftakeStation.getString("receiverPhone"));
                        tuangouVO.setOrderTotal("" + Float.valueOf(x1.getString("actualUserPay"))/100f);
                        tuangouVO.setTotalCount("0");
                        tuangouVO.setStoreName(selftakeStation.getString("name"));
                        tuangouVO.setDeliveryTime(deliveryDate);//yyyy-MM-dd
                        tuangouVO.setOriginName("淘菜菜");
                        List<TuangouBVO> tuangouBVOS = new ArrayList<>();
                        itemList.stream().forEach(y -> {
                            JSONObject y1 = (JSONObject) y;
                            TuangouBVO tuangouBVO = new TuangouBVO();
                            tuangouBVO.setProductName(y1.getString("title"));
                            tuangouBVO.setProductCount(y1.getString("buyAmount"));
                            tuangouBVO.setProductPrice("" + Float.valueOf(y1.getString("unitPromotionSalePrice"))/100f);
                            tuangouVO.setTotalCount("" + (Integer.valueOf(y1.getString("buyAmount")) + Integer.valueOf(tuangouVO.getTotalCount())));
                            tuangouBVOS.add(tuangouBVO);
                        });
                        tuangouVO.setItemLists(tuangouBVOS);
                        list.add(tuangouVO);
                    }
                });
                return list;
            }
        }
        return null;
    }

    public static List<TuangouVO> converstPDD(String body, boolean isLMQ) throws Exception{

        Document doc = Jsoup.parse(body);
        JSONObject jsonObject = null;
        Pattern pattern = Pattern.compile("window\\.rawData=.*;");
        Matcher matcher = pattern.matcher(doc.select("body script").get(2).html());
        while(matcher.find()){
            String a = matcher.group();
            jsonObject = JSON.parseObject(a.replace("window.rawData=", "").replace(";", ""));
        }
        List<TuangouVO> list = new ArrayList<>();
        if (jsonObject != null){
            JSONObject data = jsonObject.getJSONObject("store").getJSONObject("$state");
            if (data != null){
                JSONArray data1 = data.getJSONObject("10000").getJSONArray("list");
                data1.stream().forEach(x -> {
                    JSONObject x1 = (JSONObject) x;
                    JSONArray itemList = x1.getJSONArray("freshOrderResultList");
                    String deliveryTime = x1.getString("parentOrderTime").substring(0, 10);
                    String deliveryDate = DateFormatUtil.getFormatDate(Long.valueOf(deliveryTime) + 86400L, "yyyy-MM-dd");
                    Integer comparedate = DateFormatUtil.comparedate(deliveryDate, DateFormatUtil.getDateFormatStr(null));
                    if (comparedate >= -1){
                        TuangouVO tuangouVO = new TuangouVO();
                        tuangouVO.setOrderDate(DateFormatUtil.getFormatDate(Long.valueOf(x1.getString("parentOrderTime")) * 1000L, "yyyy-MM-dd HH:mm:ss"));
                        if (isLMQ){
                            tuangouVO.setPhone("15626065599");
                        }else {
                            tuangouVO.setPhone("13265060240");
                        }
                        tuangouVO.setOrderTotal("" + Float.valueOf(x1.getString("parentOrderAmount"))/100f);
                        tuangouVO.setTotalCount(x1.getString("totalGoodsNumber"));
                        tuangouVO.setStoreName("多多买菜不显示取货地址");
                        tuangouVO.setDeliveryTime(deliveryDate);//yyyy-MM-dd
                        tuangouVO.setOriginName("多多买菜");
                        List<TuangouBVO> tuangouBVOS = new ArrayList<>();
                        itemList.stream().forEach(y -> {
                            JSONObject y1 = (JSONObject) y;
                            TuangouBVO tuangouBVO = new TuangouBVO();
                            tuangouBVO.setProductName(y1.getString("goodsName"));
                            tuangouBVO.setProductCount(y1.getString("skuNumber"));
                            tuangouBVO.setProductPrice("" + Float.valueOf(y1.getString("goodsPrice"))/100f);
                            tuangouBVOS.add(tuangouBVO);
                        });
                        tuangouVO.setItemLists(tuangouBVOS);
                        list.add(tuangouVO);
                    }
                });
                return list;
            }
        }
        return null;
    }


    public static void main(String[] args) {
        try {
            PDD_LKM();
            XS_LKM();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
