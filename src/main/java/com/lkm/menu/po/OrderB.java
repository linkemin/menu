package com.lkm.menu.po;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Linkm on 2022/5/15.
 */
@Entity
@Table(name = "order_b")
public class OrderB {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="order_id")
    private String orderId;

    @ManyToOne
    @JoinColumn(name = "goods_id", referencedColumnName = "id",  insertable = false, updatable = false)//设置外键
    private Goods goods;

    @Column(name="goods_id")
    private String goodsId;

    @Column(name="num")
    private String num;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public OrderB(String orderId, String goodsId) {
        this.orderId = orderId;
        this.goodsId = goodsId;
    }

    public OrderB(String orderId, String goodsId, String num) {
        this.orderId = orderId;
        this.goodsId = goodsId;
        this.num = num;
    }

    public OrderB() {
    }

    public OrderB(Integer id, String orderId, Goods goods, String goodsId) {
        this.id = id;
        this.orderId = orderId;
        this.goods = goods;
        this.goodsId = goodsId;
    }

}
