package com.demo.bean.model;

import com.demo.common.BaseEntity;

public class Organization extends BaseEntity{

    private String organunitName;

    private String organunitCode;

    private String province;

    private String city;

    public String getOrganunitName() {
        return organunitName;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getOrganunitCode() {
        return organunitCode;
    }
}
