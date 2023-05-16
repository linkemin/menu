package com.lkm.menu.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Linkm on 2022/5/15.
 */
@Entity
@Table(name = "goods")
public class Goods {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;


    @Column(name="goods_code")
    private String goodsCode;

    @Column(name="goods_name")
    private String goodsName;

    @Column(name="show_order")
    private String showOrder;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "type_id")//设置外键
    private GoodsType goodsType;

    @Column(name="goods_price")
    private BigDecimal goodsprice;

    @Column(name="show_flag")
    private String showFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(String showOrder) {
        this.showOrder = showOrder;
    }

    public BigDecimal getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(BigDecimal goodsprice) {
        this.goodsprice = goodsprice;
    }

    public GoodsType getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(GoodsType goodsType) {
        this.goodsType = goodsType;
    }

    public String getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }

    public Goods() {
    }
}
