package com.lkm.menu.service;

import com.lkm.menu.po.GoodsType;
import com.lkm.menu.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Linkm on 2022/5/15.
 */
public interface GoodsTypeJPA extends JpaRepository<GoodsType, Integer>, JpaSpecificationExecutor<GoodsType>, Serializable {

    List<GoodsType> findAllByOrderByShowOrder();
}
