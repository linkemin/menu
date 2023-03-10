package com.lkm.menu.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lkm.menu.po.*;
import com.lkm.menu.service.*;
import com.lkm.menu.util.DateFormatUtil;
import com.lkm.menu.util.WordUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.DateUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Linkm on 2022/5/14.
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    UserJPA userJPA;

    @Autowired
    OrderJPA orderJPA;

    @Autowired
    OrderBJPA orderBJPA;

    @Autowired
    GoodsTypeJPA goodsTypeJPA;

    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping("/")
    public String page(){
        return "menu";
    }

    @GetMapping("/getUser")
    public Object getUser(){
        return userJPA.findAllByOrderById();
    }

    @GetMapping("/test")
    public void test(HttpServletResponse response){
        try {
            WordUtils.generateWord2(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/getOrder/{userId}")
    public Object getOrder(@PathVariable("userId")Integer userId){
        return orderJPA.findByUserId(userId);
    }

    @GetMapping("/getGoodsType")
    public Object getGoodsType(){
        return goodsTypeJPA.findAllByOrderByShowOrder();
    }


    @PostMapping("/saveOrder")
    public Object saveOrder(String id, String orderData){
        Map<String, Object> resultMap = new HashMap<>();
        List<Map> orders = JSON.parseArray(orderData, Map.class);

        //1?????????????????????????????????????????????
        Order todayOrderByUserId = orderJPA.findTodayOrderByUserId(Integer.valueOf(id));


        Specification querySpecifi = new Specification<Order>(){
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                Date startTime = getStartTime();
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("userId"),id)));
                predicates.add(criteriaBuilder.between(root.get("ts"), startTime, tomorrow(startTime)));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        if (todayOrderByUserId != null){
            //?????????????????????????????????????????????
            resultMap.put("success", "true");
            resultMap.put("exists", "true");
            resultMap.put("order", JSON.toJSONString(todayOrderByUserId, SerializerFeature.DisableCircularReferenceDetect));
            return resultMap;
        }

        //????????????
        Order order = new Order();
        order.setUserId(Integer.valueOf(id));
        order.setTotalAmount("");
        Order result = orderJPA.save(order);

        //??????????????????
        Set<OrderB> orderBS = new HashSet<>();
        orders.forEach(map -> orderBS.add(new OrderB(String.valueOf(result.getId()), map.get("id").toString(), map.get("num").toString())));
        orderBJPA.saveAll(orderBS);

        //?????????????????????????????????
        BigDecimal totalAmount = orderJPA.findTotalAmountById(result.getId());
        result.setTotalAmount(totalAmount.toString());
        orderJPA.save(result);
        resultMap.put("success", "true");
        resultMap.put("exists", "false");
        return resultMap;
    }


    /**
     * ????????????
     * @param id
     * @param orderData
     * @return
     */
    @Transactional
    @PostMapping("/modifyOrder")
    public Object modifyOrder(String id, String orderData, String orderId){
        Map<String, Object> resultMap = new HashMap<>();
        List<Map> orders = JSON.parseArray(orderData, Map.class);

        //???????????????
        orderBJPA.deleteByOrderId(orderId);
        orderJPA.deleteById(Integer.valueOf(orderId));

        //???????????????
        Order order = new Order();
        order.setUserId(Integer.valueOf(id));
        order.setTotalAmount("");
        Order result = orderJPA.save(order);

        //??????????????????
        Set<OrderB> orderBS = new HashSet<>();
        orders.forEach(map -> orderBS.add(new OrderB(String.valueOf(result.getId()), map.get("id").toString(), map.get("num").toString())));
        orderBJPA.saveAll(orderBS);

        //?????????????????????????????????
        BigDecimal totalAmount = orderJPA.findTotalAmountById(result.getId());
        result.setTotalAmount(totalAmount.toString());
        orderJPA.save(result);
        resultMap.put("success", "true");
        return resultMap;
    }

    /**
     * ??????????????????
     * @param date
     * @return
     */
    @PostMapping("/orderReport")
    public Object orderReport(String date){
        String tomorrow = DateFormatUtil.getPreDayDateByDate(date, -1);
        date = date + " 06:00:00";
        tomorrow = tomorrow + " 06:00:00";
        Map<String, Object> map = new HashMap<>();
        List<OrderReportAll> orderReport = getOrderReport(date, tomorrow);
        List<OrderReportAll> orderReportDetail = getOrderReportDetail(date, tomorrow);
        map.put("orderReport", orderReport);
        map.put("orderReportDetail", orderReportDetail);
        return map;
    }

    /**
     * ???????????????????????????
     * @param date
     * @return
     */
    @PostMapping("/orderDgnReport")
    public Object orderDgnReport(String date){
        String tomorrow = DateFormatUtil.getPreDayDateByDate(date, -1);
        date = date + " 06:00:00";
        tomorrow = tomorrow + " 06:00:00";
        Map<String, Object> map = new HashMap<>();
        List<OrderReportAll> orderReport = getDgnOrderReport(date, tomorrow);
        List<OrderReportAll> orderReportDetail = getDgnOrderReportDetail(date, tomorrow);
        map.put("orderReport", orderReport);
        map.put("orderReportDetail", orderReportDetail);
        return map;
    }

    /**
     * ???????????????
     * @param userId
     * @return
     */
    @PostMapping("/userName")
    public Object userName(String userId){
        return userJPA.findById(Integer.valueOf(userId));
    }

    /**
     * ??????????????????
     * @param userId
     * @param userName
     * @param favorite
     * @param mobile
     * @return
     */
    @PostMapping("/modifyUser")
    public Object modifyUser(String userId, String userName, String favorite, String mobile){
        Map<String, Object> resultMap = new HashMap<>();
        User user = null;
        if (StringUtils.isEmpty(userId)){
            //???????????????
            user = new User();
            user.setUserName(userName);
            user.setMobile(mobile);
            user.setFavorite(favorite);
            user.setUserType("1");
            userJPA.save(user);
        }else {
            //??????
            Optional<User> userOptional = userJPA.findById(Integer.valueOf(userId));
            if (userOptional.isPresent()) {
                user = userOptional.get();
                user.setUserName(userName);
                user.setMobile(mobile);
                user.setFavorite(favorite);
                userJPA.save(user);
            }
        }
        resultMap.put("success", "true");
        resultMap.put("user", user);
        return resultMap;
    }


    /**
     * ??????????????????30??????????????????
     * @param userId
     * @return
     */
    @PostMapping("/userOrderDetail")
    public Object userOrderDetail(String userId){
        String today = DateFormatUtil.getDateStr(new Date(), "YYYY-MM-dd");
        String beford30Day = DateFormatUtil.getPreDayDateByDate(today, 30);
        beford30Day = beford30Day + " 06:00:00";
        Map<String, Object> map = new HashMap<>();
        List<UserOrderDetail> userOrderDetail = getUserOrderDetail(userId, beford30Day);
        map.put("userOrderDetail", userOrderDetail);
        return map;
    }

    /**
     * ???????????????????????????
     * @param userId
     * @return
     */
    @PostMapping("/todayUserOrderDetail")
    public Object todayUserOrderDetail(String userId){
        int hours = new Date().getHours();
        String today;
        String beford30Day;
        if (hours > 6){
            //6??????????????????
            today = DateFormatUtil.getDateStr(new Date(), "YYYY-MM-dd");
        }else {
            //6?????????????????????
            today = DateFormatUtil.getDateStr(new Date(), "YYYY-MM-dd");
            today = DateFormatUtil.getPreDayDateByDate(today, 1);
        }

        today = today + " 06:00:00";
        Map<String, Object> map = new HashMap<>();
        List<UserOrderDetail> userOrderDetail = getUserOrderDetail(userId, today);
        map.put("userOrderDetail", userOrderDetail);
        return map;
    }

    /**
     * ????????????????????????
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    public List<OrderReportAll> getOrderReport(String dateBegin, String dateEnd) {
        String sql="select gt.name as type,g.goods_name as goodsName,g.id as goodsId,sum(ob.num) as count, sum(g.goods_price * ob.num) as price  from menu.order o \n" +
                "inner join menu.user u on u.id = o.user_id and u.user_type = '0' \n" +
                "left join order_b ob on o.id = ob.order_id\n" +
                "left join goods g on g.id = ob.goods_id\n" +
                "left join goods_type gt on gt.id = g.type_id \n" +
                "where o.ts > ? and o.ts < ? \n" +
                "group by gt.name,g.goods_name,g.id order by g.id " ;
        List<Object> params = new ArrayList<>();
        params.add(dateBegin);
        params.add(dateEnd);
        Query contentQuery = entityManager.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            contentQuery.setParameter(i + 1, params.get(i));
        }
        contentQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(OrderReportAll.class));
        List<OrderReportAll> results = contentQuery.getResultList();
        return results;
    }

    /**
     * ???????????????????????????
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    public List<OrderReportAll> getDgnOrderReport(String dateBegin, String dateEnd) {
        String sql="select gt.name as type,g.goods_name as goodsName,g.id as goodsId,sum(ob.num) as count, sum(g.goods_price * ob.num) as price  from menu.order o \n" +
                "inner join menu.user u on u.id = o.user_id and u.user_type = '1' \n" +
                "left join order_b ob on o.id = ob.order_id\n" +
                "left join goods g on g.id = ob.goods_id\n" +
                "left join goods_type gt on gt.id = g.type_id \n" +
                "where o.ts > ? and o.ts < ? \n" +
                "group by gt.name,g.goods_name,g.id order by g.id " ;
        List<Object> params = new ArrayList<>();
        params.add(dateBegin);
        params.add(dateEnd);
        Query contentQuery = entityManager.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            contentQuery.setParameter(i + 1, params.get(i));
        }
        contentQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(OrderReportAll.class));
        List<OrderReportAll> results = contentQuery.getResultList();
        return results;
    }

    /**
     * ????????????????????????
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    public List<OrderReportAll> getOrderReportDetail(String dateBegin, String dateEnd) {
        String sql="select o.user_id as userId, u.user_name as userName, u.favorite, gt.name as type,g.goods_name as goodsName,g.id as goodsId, g.goods_price as goodsPrice, ob.num from menu.order o \n" +
                "left join order_b ob on o.id = ob.order_id\n" +
                "left join goods g on g.id = ob.goods_id\n" +
                "left join goods_type gt on gt.id = g.type_id \n" +
                "inner join menu.user u on u.id = o.user_id and u.user_type = '0'\n" +
                "where o.ts > ? and o.ts < ?\n" +
                " order by u.id,g.id \n" ;
        List<Object> params = new ArrayList<>();
        params.add(dateBegin);
        params.add(dateEnd);
        Query contentQuery = entityManager.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            contentQuery.setParameter(i + 1, params.get(i));
        }
        contentQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(OrderReportAll.class));
        List<OrderReportAll> results = contentQuery.getResultList();
        return results;
    }


    /**
     * ?????????????????????????????????
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    public List<OrderReportAll> getDgnOrderReportDetail(String dateBegin, String dateEnd) {
        String sql="select o.user_id as userId, u.user_name as userName, u.favorite, u.mobile, gt.name as type,g.goods_name as goodsName,g.id as goodsId, g.goods_price as goodsPrice, ob.num from menu.order o \n" +
                "left join order_b ob on o.id = ob.order_id\n" +
                "left join goods g on g.id = ob.goods_id\n" +
                "left join goods_type gt on gt.id = g.type_id \n" +
                "inner join menu.user u on u.id = o.user_id and u.user_type = '1'\n" +
                "where o.ts > ? and o.ts < ?\n" +
                " order by u.mobile, u.id,g.id \n" ;
        List<Object> params = new ArrayList<>();
        params.add(dateBegin);
        params.add(dateEnd);
        Query contentQuery = entityManager.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            contentQuery.setParameter(i + 1, params.get(i));
        }
        contentQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(OrderReportAll.class));
        List<OrderReportAll> results = contentQuery.getResultList();
        return results;
    }

    /**
     * ????????????????????????
     * @param userId
     * @param dateBegin
     * @return
     */
    public List<UserOrderDetail> getUserOrderDetail(String userId, String dateBegin) {
        String sql="select o.user_id as userId,\n" +
                " o.id as orderId,\n" +
                " o.ts,\n" +
                " u.user_name as userName,\n" +
                " gt.name as type,\n" +
                " ob.num as num,\n" +
                " g.goods_name as goodsName, " +
                " g.id as goodsId, " +
                " g.goods_price as goodsPrice " +
                " from menu.order o \n" +
                " left join order_b ob on o.id = ob.order_id\n" +
                " left join goods g on g.id = ob.goods_id\n" +
                " left join goods_type gt on gt.id = g.type_id \n" +
                " left join menu.user u on u.id = o.user_id\n" +
                " where u.id=? and o.ts > ? \n" +
                " order by o.ts,u.id,g.id " ;
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(dateBegin);
        Query contentQuery = entityManager.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            contentQuery.setParameter(i + 1, params.get(i));
        }
        contentQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(UserOrderDetail.class));
        List<UserOrderDetail> results = contentQuery.getResultList();
        return results;
    }


    /**
     * ????????????
     * @param orderId
     * @return
     */
    @Transactional
    @PostMapping("/cancelOrder")
    public Object cancelOrder(String orderId){
        Map<String, String> result = new HashMap<>();
        orderBJPA.deleteByOrderId(orderId);
        orderJPA.deleteById(Integer.valueOf(orderId));
        result.put("success", "true");
        //??????????????????
        return result;
    }

    /**
     * ????????????????????????
     * @return
     */
    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }


    /**
     * ????????????
     * @param today
     * @return
     */
    public static Date tomorrow(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        return calendar.getTime();
    }

}
