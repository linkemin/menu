package com.lkm.menu.po;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by Linkm on 2022/5/22.
 */
public class UserOrderDetail {

    private Integer userId;
    private Integer orderId;
    private Timestamp ts;
    private String userName;
    private String type;
    private String goodsName;
    private Integer goodsId;
    private BigDecimal goodsPrice;//单价
    private String num;//数量


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTs() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(ts);
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
