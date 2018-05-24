package com.demo.common;

import java.io.Serializable;

public class RespDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int status;

    private String message;

    private T data;


    private RespDTO() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public static <T> RespDTO<T> success(T data) {
        RespDTO<T> resp = new RespDTO<>();
        resp.status = 0;
        resp.message = "操作成功";
        resp.data = data;
        return resp;
    }


    public static <T> RespDTO<T> success() {
        return success(null);
    }

    public static <T> RespDTO<T> fail(String message) {
        RespDTO<T> resp = new RespDTO<>();
        resp.status = -1;
        resp.message = message;
        return resp;
    }


}