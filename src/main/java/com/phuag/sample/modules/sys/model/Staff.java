package com.phuag.sample.modules.sys.model;

import com.phuag.sample.common.persistence.DataEntity;

public class Staff extends DataEntity<Staff> {

    private String name;

    private String sex;

    private Byte age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }
}