package com.lkm.menu.po;

import java.util.List;

/**
 * Created by Linkm on 2022/7/27.
 */
public class MeiTuanVO {

    private String address;
    private List<MeiTuanBVO> bvos;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<MeiTuanBVO> getBvos() {
        return bvos;
    }

    public void setBvos(List<MeiTuanBVO> bvos) {
        this.bvos = bvos;
    }
}
