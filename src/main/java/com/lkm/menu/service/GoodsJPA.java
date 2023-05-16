package com.lkm.menu.service;

import com.lkm.menu.po.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * Created by Linkm on 2023/5/15.
 */
public interface GoodsJPA extends JpaRepository<Goods, Integer>, JpaSpecificationExecutor<Goods>, Serializable {
}
