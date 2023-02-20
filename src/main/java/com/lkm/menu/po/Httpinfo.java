package com.lkm.menu.po;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Linkm on 2023/1/9.
 */
@Entity
@Table(name = "httpinfo")
public class Httpinfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="httpinfo")
    private String httpinfo;

    @Column(name="lasttime",  insertable = false)
    private Timestamp lasttime;

    @Column(name="mail")
    private String mail;

    @Column(name="lastinfo")
    private String lastinfo;

    @Column(name="scheduled")
    private String scheduled;

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

    public String getHttpinfo() {
        return httpinfo;
    }

    public void setHttpinfo(String httpinfo) {
        this.httpinfo = httpinfo;
    }

    public String getLasttime() {
        if (lasttime == null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(lasttime.getTime()));
    }

    public void setLasttime(Timestamp lasttime) {
        this.lasttime = lasttime;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLastinfo() {
        return lastinfo;
    }

    public void setLastinfo(String lastinfo) {
        this.lastinfo = lastinfo;
    }

    public String getScheduled() {
        return scheduled;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }
}
