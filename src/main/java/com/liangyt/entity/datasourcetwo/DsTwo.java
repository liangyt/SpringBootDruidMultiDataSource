package com.liangyt.entity.datasourcetwo;

import java.io.Serializable;

public class DsTwo implements Serializable {
    private Integer id;

    private String two;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two == null ? null : two.trim();
    }
}