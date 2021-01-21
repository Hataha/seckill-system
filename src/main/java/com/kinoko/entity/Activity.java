package com.kinoko.entity;


import com.baomidou.mybatisplus.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@TableName("activity")
public class Activity implements Serializable {
    @TableId
    private int id;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "start_time")
    private Date startTime;
    private String detail;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date start;
    private int rest;
    @TableField(fill = FieldFill.INSERT)
    @Version
    private int version;

    public Activity(String name, Date startTime, String detail, Date start, int rest, int version) {
        this.name = name;
        this.startTime = startTime;
        this.detail = detail;
        this.start = start;
        this.rest = rest;
        this.version = version;
    }

    public Activity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", detail='" + detail + '\'' +
                ", start=" + start +
                ", rest=" + rest +
                ", version=" + version +
                '}';
    }
}
