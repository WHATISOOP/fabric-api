package com.demo.controller;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.demo.common.RespDTO;

public class BaseController {
    protected final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

    protected String toJson(RespDTO<?> respDTO) {
        return gson.toJson(respDTO);
    }
}