package com.lkm.menu.po;

import java.util.List;

/**
 * Created by Linkm on 2022/7/20.
 */

public class TuangouVO {

    private String orderDate;//下单时间
    private String deliveryTime;//取货时间
    private String phone;
    private String storeName;
    private String orderTotal;
    private String totalCount;
    private String originName;
    private List<TuangouBVO> itemLists;

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }


    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<TuangouBVO> getItemLists() {
        return itemLists;
    }

    public void setItemLists(List<TuangouBVO> itemLists) {
        this.itemLists = itemLists;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }
}
