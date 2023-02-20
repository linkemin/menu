package com.lkm.menu.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lkm.menu.po.MeiTuanBVO;
import com.lkm.menu.po.MeiTuanVO;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 美团信息
 * Created by Linkm on 2022/7/27.
 */
@RestController
@RequestMapping("/meituan")
public class MeiTuanController {

    private static List<String> skuIdList = new ArrayList<>();

    {
//        skuIdList.add("18247");//曼可顿超醇原味
//        skuIdList.add("67577");//曼可顿超醇全麦
        skuIdList.add("67580");//曼可顿品醇
//        skuIdList.add("18249");//曼可顿高纤维全麦
        skuIdList.add("95668");//曼可顿芝麻仔汉堡
        skuIdList.add("66355");//嘉顿快餐汉堡包
        skuIdList.add("230678");//桃李全麦
        skuIdList.add("56596");//桃李醇熟
        skuIdList.add("220808");//象大厨吐司
        skuIdList.add("220907");//象大厨全麦吐司
        skuIdList.add("347239");//象大厨汉堡



    }

    @GetMapping("/mianbao")
    public Object maicai(){
        Map<String, Object> map = new HashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        CompletableFuture<Object> tangdong = CompletableFuture.supplyAsync(() -> {
            return tangdong();
        }, executor);
        CompletableFuture<Object> huangcun = CompletableFuture.supplyAsync(() -> {
            return huangcun();
        }, executor);
        CompletableFuture<Object> dongpu = CompletableFuture.supplyAsync(() -> {
            return dongpu();
        }, executor);
        CompletableFuture<Object> xintang = CompletableFuture.supplyAsync(() -> {
            return xintang();
        }, executor);
        CompletableFuture<Object> tangdong2 = CompletableFuture.supplyAsync(() -> {
            return tangdong2();
        }, executor);
        CompletableFuture<Object> huangcun2 = CompletableFuture.supplyAsync(() -> {
            return huangcun2();
        }, executor);
        CompletableFuture<Object> dongpu2 = CompletableFuture.supplyAsync(() -> {
            return dongpu2();
        }, executor);
        CompletableFuture<Object> xintang2 = CompletableFuture.supplyAsync(() -> {
            return xintang2();
        }, executor);
        CompletableFuture<Void> cf6 = CompletableFuture.allOf(tangdong, huangcun, dongpu, xintang, tangdong2, huangcun2, dongpu2, xintang2);
        CompletableFuture<Map<String, Object>> result = cf6.thenApply(v -> {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("tangdong", tangdong.join());
            resultMap.put("huangcun", huangcun.join());
            resultMap.put("dongpu", dongpu.join());
            resultMap.put("xintang", xintang.join());
            resultMap.put("tangdong2", tangdong2.join());
            resultMap.put("huangcun2", huangcun2.join());
            resultMap.put("dongpu2", dongpu2.join());
            resultMap.put("xintang2", xintang2.join());
            return resultMap;
        });
        try {
            map = result.get();
            map = merge(map);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String, Object> merge(Map<String, Object> map){
        Map<String, Object> returnMap = new HashMap<String, Object>();
        map.forEach((k, v) -> {
            if (!k.contains("2")){
                MeiTuanVO vo = (MeiTuanVO)map.get(k);
                if (vo != null){
                    MeiTuanVO vo2 = (MeiTuanVO)map.get(k + "2");
                    if (vo2 != null){
                        vo.getBvos().addAll(vo2.getBvos());
                    }
                }
                returnMap.put(k, v);
            }
        });
        return returnMap;
    }

    public static void main(String[] args) {
        try {
            System.out.println(huangcun2());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object tangdong(){

        try {
            String url = "https://mall.meituan.com/api/c/poi/10000521/sku/search/v2?uuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&xuuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&__reqTraceID=236017ce-c511-b9ae-4540-289bcb02203c&platform=ios&utm_medium=wxapp&brand=xiaoxiangmaicai&tenantId=1&utm_term=5.43.5&isClosed=false&offset=0&limit=30&poiId=10000521&keyword=%E5%90%90%E5%8F%B8&quickSearchWord=false&from=his&last=0&personalRecommendClose=0&poi=10000521&stockPois=10000521&ci=4&bizId=2&deliveryRegionKey=6666&openId=oV_5G44V8wczpt0vLyDtamkpkI3I&sysName=iOS&sysVerion=14.1&app_tag=union&uci=4&userid=2420341450";

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Host", "mall.meituan.com");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("openId", "oV_5G44V8wczpt0vLyDtamkpkI3I");
            httpGet.addHeader("content-type", "multipart/form-data");
            httpGet.addHeader("traceids", "f75d417d#99bfb12b");
            httpGet.addHeader("req_of_maicai", "1");
            httpGet.addHeader("location", "113.36496771918402,23.135313042534722");
            httpGet.addHeader("deliveryAddrLocation", "0,0");
            httpGet.addHeader("t", "AgFkIPWHaE1fUfyYcp5uUT9ciyaGwiLi7FwVln6VlQ5nAYQvpI1qsoXn-A59Zl3DERjTkY9polQ9jgAAAAABFgAAyEAwEk4ZBE98mqDzNhYt6AsEzmw7rCgekpPQ4tKHt0mJUO_ebyKR1qCA02BopAu5");
            httpGet.addHeader("mtgsig", "{\"a1\":\"1.1\",\"a2\":1672729140620,\"a3\":\"y4wxz46180495uv009426x2865u04wx6814241v686587978uw901713\",\"a4\":\"83d64913c1bb51b51349d683b551bbc19f54bbf52a050034\",\"a5\":\"EC4LXvGScmY9Jz9Pr84WYi89TryPkwH5WFyAwNzaAhwLYomYCEj+A4oGs2KhqCEBUqeodw2kKTxg51mEtPg3SMzEIX9Ve3MiUmyZmn99ebtoD1R5AMENP/+lOwJ4xgdBFittkLWHVK9YJVydWpwE7YUA9xi9Qqhrip/92E524hkmz5MJwdhJmB9fP0j4yJAkPHNxKUxQ5njcQl2ZrpIrHlKMX8nyt/59zOYBAAsVWE+gRYuwvmspJjZ4czA6xekhDAU00Id1NVVw3/h=\",\"a6\":\"w1.1Wtb2g84zCHmS6uI1A0HHgvuh+gTqL9HaVVvq2UIz+orZaod7UkuQObDnnswja756NwRWhsWOUdd13WdpK7L2mZI/wbSiWZGQZtH0BCM0ueTwu+J8O+bwCSfd0qBjKatOaztj3UhbjNU+IIROvxaXilYwfKfkPgO5juR31fLgoWHWrdPS5W/JNyZchXHZNhTPbPkoMIVwXWXc2Cj/3eQccWPopave5NvCzCj6nI5oHa6IPggm0uUGkzcYyxchTtxGp2RanjdrRkXJPsotPtjDfyaGSEubskPh9OwaHpUbopVwnSd4W5iy2bCTaklroDEMoejLdYnTr1k5XbLQ7pVQhv0P3FvvVPHRGjjnd2rKsLCZraBZf5ML1q1J21oWsRMnUxBGzb1zBSCd2dbhUlJY+e5jMjJssmoUtyydm/Gz9ceKOltnvlH1LSqe+nfdyfbpsCiccIGXtvTy83Aq2DdYdiPl53NXSFJkN56RE4jospGOZ4rEIF+u4E9byvTtBDODD4HEt2tJugnhnA7+jsKlDSxYCpXKpCNLDkhPOm1Hj63/MOxEfTSFn0w1PX7kS/d1pKV1T4QeiB4G4SxJhuPQV5dwdPkF+6jqOHsgUH/MxAItfa8GuccZOFc+uexflv49qIdNNG7Eb5DSKuafsljTfw==\",\"a7\":\"wx92916b3adca84096\",\"x0\":3,\"d1\":\"a9547f2cce11cdf64c99d9def8d3fe2e\"}");
            httpGet.addHeader("openIdCipher", "AwQAAABJAgAAAAEAAAAyAAAAPLgC95WH3MyqngAoyM/hf1hEoKrGdo0pJ5DI44e1wGF9AT3PH7Wes03actC2n/GVnwfURonD78PewMUppAAAADhguaCvY4tVQuqHuz0Af4TsZ1QLzTclEs11MGFzA9P1kNB4Uta9Xm40f/oo/DsmQuPx5ArmMMNPuA==");
            httpGet.addHeader("Accept-Encoding", "gzip,compress,br,deflate");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.29(0x18001d38) NetType/WIFI Language/zh_CN");
            httpGet.addHeader("Referer", "https://servicewechat.com/wx92916b3adca84096/281/page-frame.html");
            HttpResponse response = null;
            response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstVO(body, "棠东店");
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static Object dongpu(){
        try{
            String url = "https://mall.meituan.com/api/c/poi/10000263/sku/search/v2?uuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&xuuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&__reqTraceID=ef1d485c-9d36-8129-bfc1-4f96e3c3e198&platform=ios&utm_medium=wxapp&brand=xiaoxiangmaicai&tenantId=1&utm_term=5.43.5&isClosed=false&offset=0&limit=30&poiId=10000263&keyword=%E5%90%90%E5%8F%B8&quickSearchWord=false&from=wri&last=0&personalRecommendClose=0&poi=10000263&stockPois=10000263&ci=4&bizId=2&deliveryRegionKey=6666&openId=oV_5G44V8wczpt0vLyDtamkpkI3I&address_id=1950000025&sysName=iOS&sysVerion=14.1&app_tag=union&uci=4&userid=2420341450";

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Host", "mall.meituan.com");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("openId", "oV_5G44V8wczpt0vLyDtamkpkI3I");
            httpGet.addHeader("content-type", "multipart/form-data");
            httpGet.addHeader("traceids", "7874a8b5#56fee669");
            httpGet.addHeader("req_of_maicai", "1");
            httpGet.addHeader("location", "113.36477593315972,23.13528293185764");
            httpGet.addHeader("deliveryAddrLocation", "113.402232,23.120875");
            httpGet.addHeader("t", "AgGXJEQsvrI_0XgSAYWXEVn43521YlTazPzexhRk3Av2q2QOo8AVjLK8Dln3otZKhXv8J5K2qlZGUAAAAAABFgAAqnJH3dt5aq7q7Vb2f45UAC_WWRbQsGVs9BwZbs83iUvehKBXZuL5fcHkmTIb8nLj");
            httpGet.addHeader("mtgsig", "{\"a1\":\"1.1\",\"a2\":1672714552851,\"a3\":\"1672714530063GEKUKSO7dc99e6481ecdbaa6b3930138ede75cc2816\",\"a4\":\"cb08ba7b8d7b6d3f7bba08cb3f6d7b8d683c86be5751fd33\",\"a5\":\"H0vPppc+UcTM0iG/v8lIWkKvQ3B6z8MAETkbGWQ2+JI2Hd5lLclXrVXZTx8fyAz4pHLgXaSI+jVoRI2tHdaS9AfHCEVa92RO1i1O5/8lbUpJoDn+nRquuYGRm9+hzPr01223VcqGFgOge9BkWkJiPteocStVmw80ByTfQoIls9FRGsTicTamXdRRDhssQZ7lqJxgi157CX6zanMGElamCkKl/49FPrq=\",\"a6\":\"w1.1Wxqch49wL5kOeth6Qjw2icGUH+QmgDFKGNKeU8O097krW7nCHZK7cyDL88+rG7pWT8s7jH9HpY7QCrWiqb4kXLBQZK7BOcpHQHdBLmtMcSXoktFhBAF5Z1jZkM8rFSxC5bK5wHCJtrE5J30ZrDjG29aatSIP5GLIog5f083q58uTDMprRgUb98jOkZNwAMHA/PIj/pkb7I7juNrJm04bKZiBDVAo2Hu61ksANGChDswDUfQh38ksMQYZG/m0yfy2E6woXrIfPasrn5zDGVySyruoGcKwO5VrHh0gHIGpn2vW2T37900jw+H6agh/bjAYHbSpS9ATwbRTqX6Awam4yAIsA5h2r1L4AM4D4Ftx01pI2Te6f/3rfr51KfneRnc6Q1O5KxTrz8zmSrE2AEjF9jr8mFpDctaGrRtElCM1ouwz+Lf7080yCThFSvyC4xKIeNWPQEzOFeBsdkMcB6qsmnE7P0pidCUngtoFRWMkor0zFXotVayB/Zwbdg71HYwhOxtYdegMCMJTpiCEA62Bt1mw1uguSu3dyzkjMmr8tn6r0DcNylEEBjmqpvPutAL7DGHRahJ7+SGRTf1wg6Rt2AITZNDKveAvHSTgf8hwUMDCk0VJsXuj7FVtBN2Y7mnT\",\"a7\":\"wx92916b3adca84096\",\"x0\":3,\"d1\":\"f6b8a43a58e45d21f22b8028473dcf50\"}");
            httpGet.addHeader("openIdCipher", "AwQAAABJAgAAAAEAAAAyAAAAPLgC95WH3MyqngAoyM/hf1hEoKrGdo0pJ5DI44e1wGF9AT3PH7Wes03actC2n/GVnwfURonD78PewMUppAAAADhrLZaS24+PUi/xXnRkMd6J7Re19GHplFVARnfUO7+193PBuTS5mGSh8tD1Xd9IJK91UHSvieaZqQ==");
            httpGet.addHeader("Accept-Encoding", "gzip,compress,br,deflate");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.26(0x18001a26) NetType/WIFI Language/zh_CN");
            httpGet.addHeader("Referer", "https://servicewechat.com/wx92916b3adca84096/247/page-frame.html");
            HttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstVO(body, "东圃2店");
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }


    }

    public static Object huangcun(){
        try{
            String url = "https://mall.meituan.com/api/c/poi/10000213/sku/search/v2?uuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&xuuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&__reqTraceID=9cabd4a0-1af8-2cbb-8f65-06db508b217a&platform=ios&utm_medium=wxapp&brand=xiaoxiangmaicai&tenantId=1&utm_term=5.43.5&isClosed=false&offset=0&limit=30&poiId=10000213&keyword=%E5%90%90%E5%8F%B8&quickSearchWord=false&from=his&last=0&personalRecommendClose=0&poi=10000213&stockPois=10000213&ci=4&bizId=2&deliveryRegionKey=6666-6672&openId=oV_5G44V8wczpt0vLyDtamkpkI3I&address_id=1950000043&sysName=iOS&sysVerion=14.1&app_tag=union&uci=4&userid=2420341450";

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Host", "mall.meituan.com");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("openId", "oV_5G44V8wczpt0vLyDtamkpkI3I");
            httpGet.addHeader("content-type", "multipart/form-data");
            httpGet.addHeader("traceids", "f75d417d#13717c34");
            httpGet.addHeader("req_of_maicai", "1");
            httpGet.addHeader("location", "113.36501953125,23.13536187065972");
            httpGet.addHeader("deliveryAddrLocation", "113.407978,23.124671");
            httpGet.addHeader("t", "AgFkIPWHaE1fUfyYcp5uUT9ciyaGwiLi7FwVln6VlQ5nAYQvpI1qsoXn-A59Zl3DERjTkY9polQ9jgAAAAABFgAAyEAwEk4ZBE98mqDzNhYt6AsEzmw7rCgekpPQ4tKHt0mJUO_ebyKR1qCA02BopAu5");
            httpGet.addHeader("mtgsig", "{\"a1\":\"1.1\",\"a2\":1672729245518,\"a3\":\"y4wxz46180495uv009426x2865u04wx6814241v686587978uw901713\",\"a4\":\"fe94f0ff6aa7d87dfff094fe7dd8a76ab147fa88cc9f5de2\",\"a5\":\"7I532oBHGJE9J34wNfn2Abvo2oNeu0fatw67TMmX/6PBePEXCoXkLKmYNUr0xjJU57XP71rtVZXZVy7mB8+Pxdb2OcN6g/59pBpoBoz8mV9uOpWqnvDF5buHKdcYD7wVhYSYRu/Q2/qKwoed8FSgJLtYvFkL14wMdEYhhJ4/EvStRPvNhGme1EI+oxYQLHE1ZK/SpxZX3JUxrrr3xZd3dlLMMczyMfYfK933ZtEFBVg05PPGL8rL7e+dA8yJUfjAGpKK3RalK1kEhYZ=\",\"a6\":\"w1.1BGoeR0lkwiTQG14B6u/A8WomL6Gn4ViikPSsF0BaSh7O8hbmSZ8I91z0FZ/AdamneTejDPhLkLIlrPz8C3Y0+SuhqHdCQLHzrqILJMqZDJtYDSw1Q+psm1xtZT8lEzJsTFhx/RH2owRS4FuBD+sOuTQD86W3vyff91/2JhRu0bWp4s8IFmnW2kqGV7fjuPF9HDQUHAJ/05mTGB0uw4d8iGvF0ZblHk5HB3JKsWES2YMxSRFms9ymDpVbOkxGhIuprEwB1hXUhBN9acSPlNKuSq6u43114wnI35mxp4+rFLgArIdsBfLJbJ/Xxj5cIxM41A/nikeZtlprAhJTKfQArqXpeLaLMDiljTPp0nErF3J/842QHmOsRBiFf0MVUQLYvsqz9pBU9+I7i1YkSb3hMY+SOKTtep8bUvexlUMx3a15aFZJ3SwkHqNR4HRgGKsMJ0dUI7pn9pQHDCo6qMTssEFYZlQ6bWxptsGq3mcdaOMaAjhYxocp0a1/1Lso0ocAGZ2t26q7A+aEsTJ51gvQCcY9KPTAKClelG2pesiDcoRskEukVaB8t4xSnd8Ce4+x2edWz6dMe9/aISXcsAtZ+2RlslFSr/RLL3hhpVRxjUYzif2wBxW3arEa1KGpJStT3DwqhA4+2M4z9zoCfY+6uQgGHDFZw3Sle0IIAJtpW1t9O06xpQGGoAOCZiI8dpnnNbkK5SEM9y6ZsGdridZ7qLXEcwPGIVyAMkMJTFJaoD6QWMfpqPD+bboWG5yfxSS+FRSEBym3d1JMOQd9PPXnBSbl6n4mhJX1C05eA++LUSowH7gZ5qZKQN3ntNvBUFVnMo/+k7WBV3m2u0oDiyfATQ==\",\"a7\":\"wx92916b3adca84096\",\"x0\":3,\"d1\":\"e94caf3e8dad20de71b6455a40ec9e97\"}");
            httpGet.addHeader("openIdCipher", "AwQAAABJAgAAAAEAAAAyAAAAPLgC95WH3MyqngAoyM/hf1hEoKrGdo0pJ5DI44e1wGF9AT3PH7Wes03actC2n/GVnwfURonD78PewMUppAAAADhguaCvY4tVQuqHuz0Af4TsZ1QLzTclEs11MGFzA9P1kNB4Uta9Xm40f/oo/DsmQuPx5ArmMMNPuA==");
            httpGet.addHeader("Accept-Encoding", "gzip,compress,br,deflate");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.29(0x18001d38) NetType/WIFI Language/zh_CN");
            httpGet.addHeader("Referer", "https://servicewechat.com/wx92916b3adca84096/281/page-frame.html");
            HttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstVO(body, "黄村店");
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static Object xintang(){
        try{
            String url = "https://mall.meituan.com/api/c/poi/10000482/sku/search/v2?uuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&xuuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&__reqTraceID=cb91d25c-ca7c-f733-0a6c-29b9d8cfb54c&platform=ios&utm_medium=wxapp&brand=xiaoxiangmaicai&tenantId=1&utm_term=5.43.5&isClosed=false&offset=0&limit=30&poiId=10000482&keyword=%E5%90%90%E5%8F%B8&quickSearchWord=false&from=his&last=0&personalRecommendClose=0&poi=10000482&stockPois=10000482&ci=4&bizId=2&deliveryRegionKey=6666&openId=oV_5G44V8wczpt0vLyDtamkpkI3I&address_id=1950000039&sysName=iOS&sysVerion=14.1&app_tag=union&uci=4&userid=2420341450";

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Host", "mall.meituan.com");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("openId", "oV_5G44V8wczpt0vLyDtamkpkI3I");
            httpGet.addHeader("content-type", "multipart/form-data");
            httpGet.addHeader("traceids", "f75d417d#8a0ef82b");
            httpGet.addHeader("req_of_maicai", "1");
            httpGet.addHeader("location", "113.36503553602431,23.135323621961806");
            httpGet.addHeader("deliveryAddrLocation", "113.411856,23.156805");
            httpGet.addHeader("t", "AgFkIPWHaE1fUfyYcp5uUT9ciyaGwiLi7FwVln6VlQ5nAYQvpI1qsoXn-A59Zl3DERjTkY9polQ9jgAAAAABFgAAyEAwEk4ZBE98mqDzNhYt6AsEzmw7rCgekpPQ4tKHt0mJUO_ebyKR1qCA02BopAu5");
            httpGet.addHeader("mtgsig", "{\"a1\":\"1.1\",\"a2\":1672729341763,\"a3\":\"y4wxz46180495uv009426x2865u04wx6814241v686587978uw901713\",\"a4\":\"6a10460c1d3075a60c46106aa675301d4f69601ce91c5001\",\"a5\":\"N7o01aFF79T9qsReNu5T4fdbhThrCRczEsy1yLEUiKcEivRbplKUmJEUPdoMvkvoxF1Ili7U+6Q2SMxLVZsQyv9W0uNn8Uzb6Dz9qr0xglpMcq0o+OgCsnuAoAfbEtTFYJkmUF3DHoVXoip6ndhppoUdu7lelvFBkVHXtfFgie3ytDKukOLjCpmmNMNxpjKr6AOQcR2zpH8otQKATidV37AzTSvaxkgJyCFIcDEFiPfgWkpqph+q+XjAacl8V0BckCorzHG3VH55NNav\",\"a6\":\"w1.1UTeBPa9dwAisbSy0gpqvtZzzNB8pwA733XgLowL0RmRfVq3onvRwA82jJEGVnrk1PQNs5VGRyLZMEWteknD5DuNe/0tx0cyiyRU/iv6dJs13eEvxOO/wouXbJqk49jUO5yjzL/qevPv9mj0SRP+iMlEBOh5aRLWvknn2HsUGdOKQ0s7R9CkV2/2unAvTwYOb4OcwLHfIrZJ8Kimb+txLwMuXVoWoZSQE0wg81yb8TXFaGiRMDDZUsDgkbNSKlS0a0OrxyTrcvFgQfmqMNpfAyxwS62iQG5AW7tjT6bk6sElQ69A6qf92DyvQorowpnZmYdZfMFFFwwHY9CfKADtgyIM8h84ziuNTriQ95vMJOhNlmcKV7ysSBL5qLPllZmQxYxT+QaILWuPjyglFMYl8WazCPmRrI+quamr1V9VARDQxGQXWk+W5+Ah+fHCcyl+BpL1orq7kk+W7fsdiJIU+7H7FCCdkXOsLFCXUondnzBKEAG2bNBouyMaFyG17ewWTOQ/EahEMA76CpzCmdlGrqGwTLOo527AXnE4qK14TYcfx1d8KnCdzBi4cKwIDm8ASucxkFR0ilmGXQcmZnJGtYNSTyjyd5ccWBtuTRVfTCBtSUPCz4oO74og4C1a0lR4PxgsfDUudlj2IpJX8QDYpZ51mLZgORgYOTS8J/B4ly5xMStcyFJeioyxKOlDUdTZhZ/+UvEujDH0YV0GL/WsdEYWI70UVwZ+8dIUgqhnxkGEwQDzRgMgAEqu83MflzO+uIzo4b+8LKCXbv51J9IwVlg0+YfhfbT/dCYXMxNhfCNfBO65/nUfSBP2VGb4jPRoLsAOMo3iu/iaDz9kuIgNF5vr7/lOJILJIBQOzw8AxbAY2zuxz6Q7XvrXMQ2ebgmNYzeKCogON5gP/ULMhyb0G+g==\",\"a7\":\"wx92916b3adca84096\",\"x0\":3,\"d1\":\"963993c603a2e903b88e75df76b4ed9f\"}");
            httpGet.addHeader("openIdCipher", "AwQAAABJAgAAAAEAAAAyAAAAPLgC95WH3MyqngAoyM/hf1hEoKrGdo0pJ5DI44e1wGF9AT3PH7Wes03actC2n/GVnwfURonD78PewMUppAAAADhguaCvY4tVQuqHuz0Af4TsZ1QLzTclEs11MGFzA9P1kNB4Uta9Xm40f/oo/DsmQuPx5ArmMMNPuA==");
            httpGet.addHeader("Accept-Encoding", "gzip,compress,br,deflate");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.29(0x18001d38) NetType/WIFI Language/zh_CN");
            httpGet.addHeader("Referer", "https://servicewechat.com/wx92916b3adca84096/281/page-frame.html");
            HttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstVO(body, "新塘店");
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }



    public static Object tangdong2(){

        try {
            String url = "https://mall.meituan.com/api/c/poi/10000213/sku/search/v2?uuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&xuuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&__reqTraceID=b88da902-e18f-9388-10a1-62577c6674a3&platform=ios&utm_medium=wxapp&brand=xiaoxiangmaicai&tenantId=1&utm_term=5.43.5&isClosed=false&offset=0&limit=30&poiId=10000213&keyword=%E6%B1%89%E5%A0%A1&quickSearchWord=false&from=his&last=0&personalRecommendClose=0&poi=10000213&stockPois=10000213&ci=4&bizId=2&deliveryRegionKey=6666-6672&openId=oV_5G44V8wczpt0vLyDtamkpkI3I&address_id=1950000043&sysName=iOS&sysVerion=14.1&app_tag=union&uci=4&userid=2420341450";

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Host", "mall.meituan.com");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("openId", "oV_5G44V8wczpt0vLyDtamkpkI3I");
            httpGet.addHeader("content-type", "multipart/form-data");
            httpGet.addHeader("traceids", "f75d417d#13717c34");
            httpGet.addHeader("req_of_maicai", "1");
            httpGet.addHeader("location", "113.36501953125,23.13536187065972");
            httpGet.addHeader("deliveryAddrLocation", "113.407978,23.124671");
            httpGet.addHeader("t", "AgFkIPWHaE1fUfyYcp5uUT9ciyaGwiLi7FwVln6VlQ5nAYQvpI1qsoXn-A59Zl3DERjTkY9polQ9jgAAAAABFgAAyEAwEk4ZBE98mqDzNhYt6AsEzmw7rCgekpPQ4tKHt0mJUO_ebyKR1qCA02BopAu5");
            httpGet.addHeader("mtgsig", "{\"a1\":\"1.1\",\"a2\":1672729263455,\"a3\":\"y4wxz46180495uv009426x2865u04wx6814241v686587978uw901713\",\"a4\":\"698857e9238e342ce95788692c348e23b6aae61f9a9e683c\",\"a5\":\"6zoJhbT1sq+9J6MKW0qjeCe/fZ2s1js11TCPkV/fNV5fDi8kxlTn4CfFpMWKbyALVgq4Mkl/a/JHXio7rE9NkIB1xJEZvAsJABjAegLMzcgVIoB1SUVdFERk9RJpwCRPdVT+n1rnZKTEaHkV11fEgqpqLGroNgV9lsi8zOvw/cKrvK+OsTHtEz4YS5JwBQ+QUYT2MtfW5tyWACAMVgMXAoKIi13mdr+8KV1LMQz+9ERGSUpOSK58UwyEdmydPX4OyBijnnP1mN0Hkku=\",\"a6\":\"w1.1QUdQHo4sQ1YC4QxxEg1WvEzxpbavr3RLl7NGKSMgnGIHKTKrci+4rpKkcwIrYCZw7VkRZu02CpS9DIHK6HSAuVSkynTw/FmqByAu1XeJRkXl7/4DRtFhtB0u6FTTlWHm++sHGsVfzkNTRHrrKfXPsAKLFoONT+/u52gBEL9IgiwZqJZpmLzSCp90akXXeib92lsNrGLyHxcD77vV41JKE6+LcYp5X5KYsaqsKD+Ne7BEOa2XjmjSvCp6bc6F0Ft17E4mc//Qi2e/TQ4N7dmY0UluV2guxIkJCeEd3EJgApAbBQ99xi1qiBchfAhV+iuxtRIMuPF5hUnCCf0VSWw5rqK9+LlZk3RlKzwbtVP61LbYy+c/+pJ19+83BWhcv1N8jSo+5hgFLwJf4eVNgaydEQ3ziTwjT/GTiqKSTl6wCv4qHvlS+NR2ngkRVhLQtm3+O/pe3dPbs9OQwdGmbS6a2w6ZKiCBxWMexlrzOGwqSbg80AKciRs7uuLOzBins9S+bhzpUHiZ51csFtCTHKZXHcrtdB3W0r2WdDO9pQRTxZUoRgTyK+MoI4B7/HSCCKDLN+JMVuX/jeCLgTyYNMHznChENd++gj1yGtLUkqDCcOdtMRVHSy9piKH04MJxs2hFL6NK/iI9Tl+UHqTrlm4AKgr3fXi0lvFnkRrv7l9/snJNVrk13rlTkXdAmP/MMe8mtBJu46doMcV1pYGqMBcJc9PqNotoCdIsvpHHtUinuZWn+W4OoI1a4tl9/Gija/feGeC0dYf4RtS6yhR2s53CAM5mtrqFcc5DRywaND9VG8dC+YE73eExkfaB8YULW6TJgui0EPKu/9LfUpCdxn280Q==\",\"a7\":\"wx92916b3adca84096\",\"x0\":3,\"d1\":\"e451684c97a3ebd550d0322b2ab21417\"}");
            httpGet.addHeader("openIdCipher", "AwQAAABJAgAAAAEAAAAyAAAAPLgC95WH3MyqngAoyM/hf1hEoKrGdo0pJ5DI44e1wGF9AT3PH7Wes03actC2n/GVnwfURonD78PewMUppAAAADhguaCvY4tVQuqHuz0Af4TsZ1QLzTclEs11MGFzA9P1kNB4Uta9Xm40f/oo/DsmQuPx5ArmMMNPuA==");
            httpGet.addHeader("Accept-Encoding", "gzip,compress,br,deflate");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.29(0x18001d38) NetType/WIFI Language/zh_CN");
            httpGet.addHeader("Referer", "https://servicewechat.com/wx92916b3adca84096/281/page-frame.html");
            HttpResponse response = null;
            response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstVO(body, "棠东店");
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static Object dongpu2(){
        try{
            String url = "https://mall.meituan.com/api/c/poi/10000263/sku/search/v2?uuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&xuuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&__reqTraceID=c2f31663-40f0-01bb-8d5e-62a1099ec7e6&platform=ios&utm_medium=wxapp&brand=xiaoxiangmaicai&tenantId=1&utm_term=5.43.5&isClosed=false&offset=0&limit=30&poiId=10000263&keyword=%E6%B1%89%E5%A0%A1&quickSearchWord=false&from=his&last=0&personalRecommendClose=0&poi=10000263&stockPois=10000263&ci=4&bizId=2&deliveryRegionKey=6666&openId=oV_5G44V8wczpt0vLyDtamkpkI3I&address_id=1950000025&sysName=iOS&sysVerion=14.1&app_tag=union&uci=4&userid=2420341450";

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Host", "mall.meituan.com");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("openId", "oV_5G44V8wczpt0vLyDtamkpkI3I");
            httpGet.addHeader("content-type", "multipart/form-data");
            httpGet.addHeader("traceids", "f75d417d#7f720b52");
            httpGet.addHeader("req_of_maicai", "1");
            httpGet.addHeader("location", "113.36485405815972,23.13526068793403");
            httpGet.addHeader("deliveryAddrLocation", "113.403735,23.121515");
            httpGet.addHeader("t", "AgFkIPWHaE1fUfyYcp5uUT9ciyaGwiLi7FwVln6VlQ5nAYQvpI1qsoXn-A59Zl3DERjTkY9polQ9jgAAAAABFgAAyEAwEk4ZBE98mqDzNhYt6AsEzmw7rCgekpPQ4tKHt0mJUO_ebyKR1qCA02BopAu5");
            httpGet.addHeader("mtgsig", "{\"a1\":\"1.1\",\"a2\":1672729307788,\"a3\":\"y4wxz46180495uv009426x2865u04wx6814241v686587978uw901713\",\"a4\":\"706789a58a2554e7a5896770e754258a29230806ce772d8c\",\"a5\":\"bZLS9R0d0cS9JCxPM6kc0sazVPc8mK1cboMOAzn0qveutYT74utbe1Medv+JAPLscdNQiY4ADWWIwpTpa+VQPS+jjGFD14dTLx84HeFnb0rctX0UOzaykGaCfQ7Y3huLISo0KmOWaiu9XsFkznebSnHR54zfCglqTobRpuPGy/IwQ8XLZe8gV1yiNvDU9GVONRfEghtHIwotiivqEQ0/VFEMFBUHiqdJLi2aG+MGzUIhDfTjCxJTsD8N6PS/ss9EkK5wwOeuCuYFK3ln\",\"a6\":\"w1.1gx1wPj83Q/CVY422kMTTBNbqHEuIoQtuUwHGE4y637+/GcrfmwFQgGiW0I2EyrIWRil5Mr1OKvJl5aS33oOq8vFlwavUuFa4W68O72f1bqlc9ovauaRs0fb4cXu8USVt/vXCKRvxC1hpmaTy/GolWf3l5wWVE4je+9QBtIcjPCD/pB7xetCW4dAAJP1P1hE4mJFsy0A+6fPXivEDyilGxFnDOX7OhZCQVT0fNcTyARU9wXIH09/rD6dufLmhrJcuy9h8iukdVXtkjUvfa6jY7kLq264neoSQG2WjHOHXJ5se5FIOgk3Dm6c1ewNKqAVLlUVlO0qk/TjZzt5c6kwfG7tJGB2yXG/TBm6dTKsZ6RoPFhXXLC3gf/UGj+FvzlnmYb+ZuedWPqQX5Sw9E+f573v5xTClIy7WotorwPk3cs3Z/HoVXWcESHk0aUQMI8VCzvxxPKYtluaN6Z9qEBzJKD5y2Km2KPmnzsbFy5T2PhaSk3M02uXpMsBJ1jUBLM8e54q9phuIDsliKe7MvhDcDvslAQUbGjiyn2RVnko7SZfykmGqBJRmXhdsKi+71q7+PkOHZ/IfepFCKh2BpY6Zh0Vi3aQpf05d8tRVJ5ARkm58/PCxYLNxvEAOoxRfuc4f1z0pFsRwnuFIwbTZuZH1Jh1RZrIjBkuWzGroxDRHLLWosU+TFY0Y7OUP/RzWY8GHdMOgHhdCrcE+8q9CNYeh8QjOuhbDKRTI/M92eZLvYHWam0c8FbxuvBV9AaUfsH1YjMRw6py2a6MxI8XJuCjZsbqBlnDXMieZuxDt9xwy3hZHN3ZMWmttjwLFHR3DfnD9pTsFvnmpMNYZP/IpJTh/eJbRMFOiRyrdbAhcpkTQNJnR2Z4g8d9KcRTQe6vnhCdoJQIgY2uKVeA3QsrLns3VC32LES7SwDdFbaej78vyx1g=\",\"a7\":\"wx92916b3adca84096\",\"x0\":3,\"d1\":\"7d1000d1d0590b01a239bb9f6634fa43\"}");
            httpGet.addHeader("openIdCipher", "AwQAAABJAgAAAAEAAAAyAAAAPLgC95WH3MyqngAoyM/hf1hEoKrGdo0pJ5DI44e1wGF9AT3PH7Wes03actC2n/GVnwfURonD78PewMUppAAAADhguaCvY4tVQuqHuz0Af4TsZ1QLzTclEs11MGFzA9P1kNB4Uta9Xm40f/oo/DsmQuPx5ArmMMNPuA==");
            httpGet.addHeader("Accept-Encoding", "gzip,compress,br,deflate");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.29(0x18001d38) NetType/WIFI Language/zh_CN");
            httpGet.addHeader("Referer", "https://servicewechat.com/wx92916b3adca84096/281/page-frame.html");
            HttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstVO(body, "东圃2店");
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }


    }

    public static Object huangcun2(){
        try{
            String url = "https://mall.meituan.com/api/c/poi/10000213/sku/search/v2?uuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&xuuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&__reqTraceID=b88da902-e18f-9388-10a1-62577c6674a3&platform=ios&utm_medium=wxapp&brand=xiaoxiangmaicai&tenantId=1&utm_term=5.43.5&isClosed=false&offset=0&limit=30&poiId=10000213&keyword=%E6%B1%89%E5%A0%A1&quickSearchWord=false&from=his&last=0&personalRecommendClose=0&poi=10000213&stockPois=10000213&ci=4&bizId=2&deliveryRegionKey=6666-6672&openId=oV_5G44V8wczpt0vLyDtamkpkI3I&address_id=1950000043&sysName=iOS&sysVerion=14.1&app_tag=union&uci=4&userid=2420341450";

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Host", "mall.meituan.com");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("openId", "oV_5G44V8wczpt0vLyDtamkpkI3I");
            httpGet.addHeader("content-type", "multipart/form-data");
            httpGet.addHeader("traceids", "f75d417d#13717c34");
            httpGet.addHeader("req_of_maicai", "1");
            httpGet.addHeader("location", "113.36501953125,23.13536187065972");
            httpGet.addHeader("deliveryAddrLocation", "113.407978,23.124671");
            httpGet.addHeader("t", "AgFkIPWHaE1fUfyYcp5uUT9ciyaGwiLi7FwVln6VlQ5nAYQvpI1qsoXn-A59Zl3DERjTkY9polQ9jgAAAAABFgAAyEAwEk4ZBE98mqDzNhYt6AsEzmw7rCgekpPQ4tKHt0mJUO_ebyKR1qCA02BopAu5");
            httpGet.addHeader("mtgsig", "{\"a1\":\"1.1\",\"a2\":1672729263455,\"a3\":\"y4wxz46180495uv009426x2865u04wx6814241v686587978uw901713\",\"a4\":\"698857e9238e342ce95788692c348e23b6aae61f9a9e683c\",\"a5\":\"6zoJhbT1sq+9J6MKW0qjeCe/fZ2s1js11TCPkV/fNV5fDi8kxlTn4CfFpMWKbyALVgq4Mkl/a/JHXio7rE9NkIB1xJEZvAsJABjAegLMzcgVIoB1SUVdFERk9RJpwCRPdVT+n1rnZKTEaHkV11fEgqpqLGroNgV9lsi8zOvw/cKrvK+OsTHtEz4YS5JwBQ+QUYT2MtfW5tyWACAMVgMXAoKIi13mdr+8KV1LMQz+9ERGSUpOSK58UwyEdmydPX4OyBijnnP1mN0Hkku=\",\"a6\":\"w1.1QUdQHo4sQ1YC4QxxEg1WvEzxpbavr3RLl7NGKSMgnGIHKTKrci+4rpKkcwIrYCZw7VkRZu02CpS9DIHK6HSAuVSkynTw/FmqByAu1XeJRkXl7/4DRtFhtB0u6FTTlWHm++sHGsVfzkNTRHrrKfXPsAKLFoONT+/u52gBEL9IgiwZqJZpmLzSCp90akXXeib92lsNrGLyHxcD77vV41JKE6+LcYp5X5KYsaqsKD+Ne7BEOa2XjmjSvCp6bc6F0Ft17E4mc//Qi2e/TQ4N7dmY0UluV2guxIkJCeEd3EJgApAbBQ99xi1qiBchfAhV+iuxtRIMuPF5hUnCCf0VSWw5rqK9+LlZk3RlKzwbtVP61LbYy+c/+pJ19+83BWhcv1N8jSo+5hgFLwJf4eVNgaydEQ3ziTwjT/GTiqKSTl6wCv4qHvlS+NR2ngkRVhLQtm3+O/pe3dPbs9OQwdGmbS6a2w6ZKiCBxWMexlrzOGwqSbg80AKciRs7uuLOzBins9S+bhzpUHiZ51csFtCTHKZXHcrtdB3W0r2WdDO9pQRTxZUoRgTyK+MoI4B7/HSCCKDLN+JMVuX/jeCLgTyYNMHznChENd++gj1yGtLUkqDCcOdtMRVHSy9piKH04MJxs2hFL6NK/iI9Tl+UHqTrlm4AKgr3fXi0lvFnkRrv7l9/snJNVrk13rlTkXdAmP/MMe8mtBJu46doMcV1pYGqMBcJc9PqNotoCdIsvpHHtUinuZWn+W4OoI1a4tl9/Gija/feGeC0dYf4RtS6yhR2s53CAM5mtrqFcc5DRywaND9VG8dC+YE73eExkfaB8YULW6TJgui0EPKu/9LfUpCdxn280Q==\",\"a7\":\"wx92916b3adca84096\",\"x0\":3,\"d1\":\"e451684c97a3ebd550d0322b2ab21417\"}");
            httpGet.addHeader("openIdCipher", "AwQAAABJAgAAAAEAAAAyAAAAPLgC95WH3MyqngAoyM/hf1hEoKrGdo0pJ5DI44e1wGF9AT3PH7Wes03actC2n/GVnwfURonD78PewMUppAAAADhguaCvY4tVQuqHuz0Af4TsZ1QLzTclEs11MGFzA9P1kNB4Uta9Xm40f/oo/DsmQuPx5ArmMMNPuA==");
            httpGet.addHeader("Accept-Encoding", "gzip,compress,br,deflate");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.29(0x18001d38) NetType/WIFI Language/zh_CN");
            httpGet.addHeader("Referer", "https://servicewechat.com/wx92916b3adca84096/281/page-frame.html");
            HttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstVO(body, "黄村店");
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static Object xintang2(){
        try{
            String url = "https://mall.meituan.com/api/c/poi/10000482/sku/search/v2?uuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&xuuid=185758e2cc3c8-2184d998e16f40-0-3d10d-185758e2cc388&__reqTraceID=5206873e-eace-f1ca-6dcc-8667f72c9100&platform=ios&utm_medium=wxapp&brand=xiaoxiangmaicai&tenantId=1&utm_term=5.43.5&isClosed=false&offset=0&limit=30&poiId=10000482&keyword=%E6%B1%89%E5%A0%A1&quickSearchWord=false&from=his&last=0&personalRecommendClose=0&poi=10000482&stockPois=10000482&ci=4&bizId=2&deliveryRegionKey=6666&openId=oV_5G44V8wczpt0vLyDtamkpkI3I&address_id=1950000039&sysName=iOS&sysVerion=14.1&app_tag=union&uci=4&userid=2420341450";

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Host", "mall.meituan.com");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("openId", "oV_5G44V8wczpt0vLyDtamkpkI3I");
            httpGet.addHeader("content-type", "multipart/form-data");
            httpGet.addHeader("traceids", "f75d417d#8a0ef82b");
            httpGet.addHeader("req_of_maicai", "1");
            httpGet.addHeader("location", "113.36503553602431,23.135323621961806");
            httpGet.addHeader("deliveryAddrLocation", "113.411856,23.156805");
            httpGet.addHeader("t", "AgFkIPWHaE1fUfyYcp5uUT9ciyaGwiLi7FwVln6VlQ5nAYQvpI1qsoXn-A59Zl3DERjTkY9polQ9jgAAAAABFgAAyEAwEk4ZBE98mqDzNhYt6AsEzmw7rCgekpPQ4tKHt0mJUO_ebyKR1qCA02BopAu5");
            httpGet.addHeader("mtgsig", "{\"a1\":\"1.1\",\"a2\":1672729358791,\"a3\":\"y4wxz46180495uv009426x2865u04wx6814241v686587978uw901713\",\"a4\":\"be481b70805697b1701b48beb1975680b76a38c806fd6e48\",\"a5\":\"2tTfooE7bhY9qoooMxTf9ZPtXm7LUkfcrw5DcGcpH9FHP0iJolJhdMUeQohD3fQcrppgn+mNjcC8rQR0hQlcVEVaggcL/RrS0PfVtkwgbs0uVGO517C6aiE0xsUGSENvBlfdKDN1t/OG0cWRfQ3N7z80eqh+2HxtgY8Rvcv2zNl1udObFnsTuwuA2TmI4lHse4fYYEua2GEimG90PKmm28+cm9F1A0X0GJMx+tav5XB1CzfL4p6YMamHDl3nskIFkNo2QdnhlDDkm09W\",\"a6\":\"w1.1UonNW7Wd+GSPiFyU1qctzx0k5Grd7DqJAHcpAdzdFxsuXrkJWGnMbIXPBtojhKHO7uUUnTsMPYGnk1BMLCIu0I5z4GvhkfOPu68oZ1WiRo9aw9Num/On8w2HAXXy0+SLVEhxTguM96A2Uc4K4VJcECH0pCgudHOJUkOEEb1LYzlbjtyr7xuVab8hLpBPOCzF+hTF4k2Jm1aV8eaR8JD+Fs30Yte2K6tEh5e0u6/AH7VZPGdXoTv0VMmEytxOD6naKrQiTamQTjnSCI8iyMQlP4CeZE9zMoQ6enpudsSt9A9qB3r/+IkbzMf4oHWOOtePIQR3GI9Xnw8bZtnKrtaJBuxAhnZDNnx3j0mpD/2GjgypYqW/Szi5mha4ouCrUv+D9xzfz3GwE2mWZX+te4Tbvuken+8OpmIHt86FdCxvRCkeIIdTKqTH1PLm2C9VmIlpZxIyULA0TIH1Be7bBQlma4tJJTvCE6fd/kMtBM+ALvA0Izo6SwkdQQPtJKsxDSeR9x8oeFAzVae2dsGrenopfp6d6eBPKx+TPgrAn16E0TEAh41tfD7F5QLJQg1ZQdbjE/DxxtjJZGV+tNeLT4NcLRZK97Tn1a7dUICtdQE4mOmt56A/w5VNc09yiUxtj1T5D4NtkhFQFyH1p5WNewqoXgWWH6++x3RiBLy50k18LOZ9VWZmF5C7v2gkAdlzIa1reQWR8MzYbt3EiW58anhyVfWjslfFl2ofHi5ppWDguSGGOkuUp0JhL147RCFzCzMY96w8dW91pXn5Bx4iiEHdZLR8Po7x8tbEXgupffuvLR3oQkb2N8VHJj6hli5a0aZKQiPqdb+cXyNQAN0LIg7y6hEgC6BPjhRbJ7Aagoq5c/VFBwbUYeInwJfntM9lzvGh9f/K2K8ntjqFJkRfyOOhQg==\",\"a7\":\"wx92916b3adca84096\",\"x0\":3,\"d1\":\"8b1cb2f9608ba0fd4b82b80d21c82822\"}");
            httpGet.addHeader("openIdCipher", "AwQAAABJAgAAAAEAAAAyAAAAPLgC95WH3MyqngAoyM/hf1hEoKrGdo0pJ5DI44e1wGF9AT3PH7Wes03actC2n/GVnwfURonD78PewMUppAAAADhguaCvY4tVQuqHuz0Af4TsZ1QLzTclEs11MGFzA9P1kNB4Uta9Xm40f/oo/DsmQuPx5ArmMMNPuA==");
            httpGet.addHeader("Accept-Encoding", "gzip,compress,br,deflate");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.29(0x18001d38) NetType/WIFI Language/zh_CN");
            httpGet.addHeader("Referer", "https://servicewechat.com/wx92916b3adca84096/281/page-frame.html");
            HttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity());
            return converstVO(body, "新塘店");
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }



    /**
     * 解析美团
     * @param body
     * @return
     */
    public static MeiTuanVO converstVO(String body, String address) throws Exception{
        JSONObject jsonObject = JSON.parseObject(body);
        MeiTuanVO meiTuanVO = new MeiTuanVO();
        List<MeiTuanBVO> list = new ArrayList<>();
        if (jsonObject != null){
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null){
                JSONArray data1 = data.getJSONArray("skuListAreaVOs");
                data1.stream().forEach(x -> {
                    JSONObject x1 = (JSONObject) x;
                    JSONObject skuItem = x1.getJSONObject("skuItem");
                    if (skuItem != null && skuIdList.contains(skuItem.getString("skuId"))){
                        JSONObject skuTitle = skuItem.getJSONObject("skuTitle");
                        MeiTuanBVO meiTuanBVO = new MeiTuanBVO();
                        meiTuanBVO.setPrice(skuItem.getString("seqPrice"));
                        meiTuanBVO.setSkuId(skuItem.getString("skuId"));
                        meiTuanBVO.setName(skuTitle.getString("text"));
                        list.add(meiTuanBVO);
                    }

                });
                meiTuanVO.setAddress(address);
                meiTuanVO.setBvos(list);
                return meiTuanVO;
            }
        }
        return null;
    }
}
