package com.lkm.menu.service;

import com.lkm.menu.po.Order;
import com.lkm.menu.po.OrderReportAll;
import com.lkm.menu.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Linkm on 2022/5/15.
 */
public interface OrderJPA extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order>, Serializable {


    List<Order> findByUserId(Integer userId);

    @Query(value = "select sum(g.goods_price * ob.num) from menu.order o left join order_b ob on o.id = ob.order_id left join goods g on ob.goods_id = g.id where o.id=:orderId", nativeQuery = true)
    BigDecimal findTotalAmountById(Integer orderId);

    //查找某个人当天的订单
    @Query(value = "select * from menu.order  where user_id=:userId and TO_DAYS(ts) = TO_DAYS(NOW()) order by ts desc limit 1 ",nativeQuery = true)
    Order findTodayOrderByUserId(Integer userId);

    //按日期查找订单报表
    @Query(value = "select gt.name,g.goods_name,g.id as goods_id,count(1) as count from menu.order o \n" +
            "left join order_b ob on o.id = ob.order_id\n" +
            "left join goods g on g.id = ob.goods_id\n" +
            "left join goods_type gt on gt.id = g.type_id \n" +
            "where o.ts >:dateBegin and o.ts <:dateEnd \n" +
            "group by gt.name,g.goods_name,g.id order by g.id ",nativeQuery = true)
    List<OrderReportAll> findOrderReportByDate(String dateBegin, String dateEnd);

}
