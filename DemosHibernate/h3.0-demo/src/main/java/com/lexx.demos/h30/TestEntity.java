package com.edifecs.hibertest;

import java.io.Serializable;

/**
 * Created by c-ionnmoro on 19-Nov-16.
 */
public class TestEntity implements Serializable {
    private long id;
    private String name;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}