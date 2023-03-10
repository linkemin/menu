package com.lkm.menu.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lkm.menu.po.User;
import com.lkm.menu.util.DateFormatUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Linkm on 2022/6/2.
 */
public class Tests {

    private static List<String> FILE_TYPE_LIST =  new ArrayList<String>();

    interface MyInterface2{
        public int method();
    }

    static {
        FILE_TYPE_LIST.add("jpg");
        FILE_TYPE_LIST.add("png");
        FILE_TYPE_LIST.add("gif");
        FILE_TYPE_LIST.add("tif");
        FILE_TYPE_LIST.add("bmp");
        FILE_TYPE_LIST.add("dwg");
        FILE_TYPE_LIST.add("html");
        FILE_TYPE_LIST.add("rtf");
        FILE_TYPE_LIST.add("xml");
        FILE_TYPE_LIST.add("zip");
        FILE_TYPE_LIST.add("rar");
        FILE_TYPE_LIST.add("psd");
        FILE_TYPE_LIST.add("eml");
        FILE_TYPE_LIST.add("dbx");
        FILE_TYPE_LIST.add("pst");
        FILE_TYPE_LIST.add("xls");
        FILE_TYPE_LIST.add("doc");
        FILE_TYPE_LIST.add("mdb");
        FILE_TYPE_LIST.add("wpd");
        FILE_TYPE_LIST.add("eps");
        FILE_TYPE_LIST.add("ps");
        FILE_TYPE_LIST.add("pdf");
        FILE_TYPE_LIST.add("qdf");
        FILE_TYPE_LIST.add("pwl");
        FILE_TYPE_LIST.add("wav");
        FILE_TYPE_LIST.add("avi");
        FILE_TYPE_LIST.add("ram");
        FILE_TYPE_LIST.add("rm");
        FILE_TYPE_LIST.add("mpg");
        FILE_TYPE_LIST.add("mov");
        FILE_TYPE_LIST.add("asf");
        FILE_TYPE_LIST.add("mid");
    }

    public int LENGTH = 50;
    final public  int LENGTH2 = 50;



    public static void main(String[] args) {
        System.out.println(DateFormatUtil.getDateStr(DateFormatUtil.getDefOfDay(new Date(), "09:00:00"), "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtil.getDateStr(DateFormatUtil.getDefOfDay(new Date(), "18:00:00"), "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(DateFormatUtil.comparedate(DateFormatUtil.getDateFormatStr("yyyy-MM-dd HH:mm:ss"), DateFormatUtil.getDateFormatStr("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//        Date disabledate;
//        try {
//            try {
//                disabledate = df.parse("2022-10-11 12:12:12");
//                System.out.println(disabledate.toString());
//            } catch (java.text.ParseException e) {
//                e.printStackTrace();
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        CompletableFuture<Void> future1 = CompletableFuture.runAsync(()->{
//            System.out.println("fu1???"+Thread.currentThread().getName());
//        }, executor);
//        CompletableFuture<Void> future2 = CompletableFuture.runAsync(()->{
//            System.out.println("fu2???"+Thread.currentThread().getName());
//        }, executor);
//        CompletableFuture<Void> future3 = CompletableFuture.runAsync(()->{
//            System.out.println("fu3???"+Thread.currentThread().getName());
//        }, executor);
//        CompletableFuture<Void> future4 = CompletableFuture.runAsync(()->{
//            System.out.println("fu4???"+Thread.currentThread().getName());
//        }, executor);


//        System.out.println("get result: " + future.join());

//        try {
//            getDetail();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    public static void getDetail() throws IOException {
        String url = "https://mall.meituan.com/api/c/poi/10000039/sku/search/v2?uuid=181b40a438cc8-2ac381106f093-0-3d10d-181b40a438cc8&xuuid=181b40a438cc8-2ac381106f093-0-3d10d-181b40a438cc8&__reqTraceID=f3f9d41f-5a20-e90c-9cdd-71ee85a257b2&platform=ios&utm_medium=wxapp&brand=xiaoxiangmaicai&tenantId=1&utm_term=5.37.1&isClosed=false&offset=0&limit=30&poiId=10000039&keyword=%E5%90%90%E5%8F%B8&quickSearchWord=false&order=1&from=wri&last=0&poi=10000039&stockPois=10000039&ci=4&bizId=2&deliveryRegionKey=6667&openId=oV_5G44V8wczpt0vLyDtamkpkI3I&address_id=1950000004&sysName=iOS&sysVerion=14.1&app_tag=union&uci=4&userid=2420341450";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Host", "mall.meituan.com");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("openId", "oV_5G44V8wczpt0vLyDtamkpkI3I");
        httpGet.addHeader("content-type", "multipart/form-data");
        httpGet.addHeader("traceids", "7ea45ace#f0afefac");
        httpGet.addHeader("req_of_maicai", "1");
        httpGet.addHeader("location", "113.3650718858507,23.135308973524307");
        httpGet.addHeader("deliveryAddrLocation", ",");
        httpGet.addHeader("t", "dW81SuDrrhgdpPEiLWrJObyBUk8AAAAAkBIAAKr50R7-bpMHxaeIZxbVTeOF-wd0F7psjXR0apasyoXVEaBjf2RwJrs1AuzisvxiLg");
        httpGet.addHeader("mtgsig", "{\"a1\":\"1.1\",\"a2\":1658820932401,\"a3\":\"73v3vw1y6ux155050u0v90w3318uu2v6818y59z515w87978u01115vy\",\"a4\":\"3e823196dd3fe2a39631823ea3e23fdda782ec070460d3da\",\"a5\":\"PHerFmCbsgIEJ3Ply8j5OenjKsOpZmToMVpWw7JRVtAqm/stfqRmbdROj3Xej6ICKjFXxsccfD6cEl6PLb8gQGkTaqvhwz+wEPGgWtNqWSQB0eF+XZ27LPqZNJeRvt+GUnvN3uT+2sCBfPh0b3RnXFnZcVHvVaLQeJ/+XG/eRKro6aNFAl310rMZpyHhohnUcnb7w18lfFqMHeNXDsXgZuMOcQsxIHf9lOi6SbyZhy3/B6IhkwQPk9eitEE6kwjBU6Xqaqe5xRlRTfr=\",\"a6\":\"w1.1ulcXC2eK1/T1D2xwdxttMA9iX7CcYozru6rlBwtAvII5OdHbPjQqXZNB0Dj9YmD4QX25r7Z+36FJn4j9KWs/Ssptsz5puJjcHctIcfiBOiFxQBqY42P7uBQ/UHawSGB2WBamlZzpjj3gf7Ynror8t9flPOE02ypUDuW2jGg97j500tgCBEjN75QPvS75EiFtz0rGba0LsT+okr06F2Xdk6k1RKjGOrGjAM1eMNXzyC612/ji6FuOGcK8GFbbABsKjs5SzMJWOb53AjDZTUZ6MDZ0xBmlHksszs4+toar/GFklf2eFpBuNoH8lecg3YoJogaEZepY7oSzuWeYXZAfbwtnRSu69nZR5gpRrxqPK9CMR1t6QI++NVA4HIHfW2L7sVjbgDOAToMG182bWeVlCKeSyrzsnapInDbwcJ7ebitrY4FZGwjNCvCioGnDQD5SnSJdyfGGcd01uTfuM7/ktuZrghyswg5HY5UfSgzG2hfEkvGZE3Omj2owozN2zFOZrBuQ3bbwuccSEgg9/tDuWhqzvILYmAylk/kh0Rpk8w/an4IMXue7rNK3MRsxaS1U2EZUJiSS+Mg3WlAKD3HCSXi6/SEBZQR6yqLIMUkTQWxLX5n3dXEntnRurZ37d5rpyc8sVsczwfhaIf+k98PsHsaSOaAZgEViC0rpFbTCe+Rd68GJnWxlnxkF4v4q6cTdlFDKJD7+CK399oSE7f8UsqvFBwVDrXI41qJ+EmVJabE5pLYOfJ906xuM3wQ2+wY3\",\"a7\":\"wx92916b3adca84096\",\"x0\":3,\"d1\":\"bfcc5a9246f5fd763a183ad34598814c\"}");
        httpGet.addHeader("openIdCipher", "AwQAAABJAgAAAAEAAAAyAAAAPLgC95WH3MyqngAoyM/hf1hEoKrGdo0pJ5DI44e1wGF9AT3PH7Wes03actC2n/GVnwfURonD78PewMUppAAAADiS+ePxSn14CYSI4ooBbI2Pvcxrp8wycNscmEJhXqogAtJGVaXQrZfA+KyU83oxqQO09bTBPGp9pg==");
        httpGet.addHeader("Accept-Encoding", "gzip,compress,br,deflate");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.26(0x18001a25) NetType/WIFI Language/zh_CN");
        httpGet.addHeader("Referer", "https://servicewechat.com/wx92916b3adca84096/247/page-frame.html");
        HttpResponse response = httpClient.execute(httpGet);
        String body = EntityUtils.toString(response.getEntity());
        System.out.println("?????????????????????????????????"+ body);
    }


    public static void postTest() throws IOException {
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
        httpPost.addHeader("userKey", "e55c0872-2002-406e-b54c-5422e9a7b5fc");
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
        CloseableHttpResponse response = httpClient.execute(httpPost);//????????????
        //System.out.println("??????????????????" + response.getStatusLine());
        String body = EntityUtils.toString(response.getEntity());//??????????????????
        System.out.println("Login?????????????????????" + body);

        JSONObject jsonObject = JSON.parseObject(body);//?????????json??????
        String data = jsonObject.getString("data");//?????????data???       //?????????????????? "\""+    +"\""
        System.out.println(data);

    }
}
