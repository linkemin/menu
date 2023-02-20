package com.lkm.menu.service;

import com.lkm.menu.po.Httpinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Linkm on 2023/1/9.
 */
public interface HttpInfoJPA extends JpaRepository<Httpinfo, Integer>, JpaSpecificationExecutor<Httpinfo>, Serializable {

    List<Httpinfo> findByUserId(Integer userId);

    List<Httpinfo> findByScheduled(String scheduled);
}
