package com.lkm.menu.po;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Linkm on 2022/5/15.
 */
@Entity
@Table(name = "goods_type")
public class GoodsType {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="show_order")
    private String showOrder;

    @OneToMany
    @JoinColumn(name = "type_id", referencedColumnName = "id")//设置外键
    @OrderBy("goods_price,show_order ASC")
    @JSONField(serialize=false)
    private Set<Goods> goods;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(String showOrder) {
        this.showOrder = showOrder;
    }

    public Set<Goods> getGoods() {
        return goods;
    }

    public void setGoods(Set<Goods> goods) {
        this.goods = goods;
    }

    public GoodsType() {
    }
}
