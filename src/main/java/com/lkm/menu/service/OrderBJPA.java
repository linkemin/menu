package com.lkm.menu.service;

import com.lkm.menu.po.Order;
import com.lkm.menu.po.OrderB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Linkm on 2022/5/15.
 */
public interface OrderBJPA extends JpaRepository<OrderB, Integer>, JpaSpecificationExecutor<OrderB>, Serializable {


    List<OrderB> findByOrderId(String orderId);

    Integer deleteByOrderId(String orderId);
}
