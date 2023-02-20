package com.lkm.menu.dao;

import com.lkm.menu.po.OrderReportAll;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linkm on 2022/5/21.
 */

public class OrderDAO {
    @PersistenceContext
    private EntityManager entityManager;



    public List<OrderReportAll> getUserList(String username, String jobNumer) {
        String sql="select t1.user_id userId,t1.user_name userName,t1.postid postId,t1.jobnumer jobNumer,t1.dept_id deptId,t4.dept_name deptName " +
                " from sys_user t1" ;
        List<Object> params = new ArrayList<>();
        if(!StringUtils.isEmpty(username)){
            sql+=" and t1.user_name like ?";
            params.add("%"+username+"%");
        }
        if(!StringUtils.isEmpty(jobNumer)){
            sql+=" and t1.jonumer like ?";
            params.add("%"+jobNumer+"%");
        }
        Query contentQuery = entityManager.createNativeQuery(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            contentQuery.setParameter(i + 1, params.get(i));
        }
        contentQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(OrderReportAll.class));
        List<OrderReportAll> results = contentQuery.getResultList();
        return results;
    }
}
