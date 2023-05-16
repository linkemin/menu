package com.lkm.menu.po;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Linkm on 2022/5/15.
 */
@Entity
@Table(name = "[order]")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="total_amount")
    private String totalAmount;

    @Column(name="ts",  insertable = false, updatable = false)
    private Timestamp ts;

    @OneToMany(targetEntity=OrderB.class)
    @JoinColumn(name="order_id")
    private Set<OrderB> OrderBs = new HashSet<OrderB>(0);

    @Column(name="pickup_time")
    private String pickupTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    public Set<OrderB> getOrderBs() {
        return OrderBs;
    }

    public void setOrderBs(Set<OrderB> orderBs) {
        OrderBs = orderBs;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }
}
