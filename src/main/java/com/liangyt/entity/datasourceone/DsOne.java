package com.liangyt.entity.datasourceone;

import java.io.Serializable;

public class DsOne implements Serializable{
    private Integer id;

    private String one;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one == null ? null : one.trim();
    }
}