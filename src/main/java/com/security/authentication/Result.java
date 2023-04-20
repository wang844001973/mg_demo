package com.security.authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Result {

    private String data_id = "";
    private String data_desc = "";
    private int errorCode = 0;
    private String errorInfo = "成功";
    private List<?> data;
    private String callback = "";

    public Result() {
    }

    public Result(List<?> list) {
        this.setData(list);
    }

    public Result(List<?> list, Map<String, Object> parameter) {
        this.setData(list);
        this.setCallback(parameter);
    }

    public Result(Object obj) {
        List<Object> list = new ArrayList<Object>();
        list.add(obj);
        this.setData(list);
    }

    public Result(Object obj, Map<String, Object> parameter) {
        List<Object> list = new ArrayList<Object>();
        list.add(obj);
        this.setData(list);
        this.setCallback(parameter);
    }

    public Result(int errorCode, String errorInfo) {
        this.errorCode = errorCode;
        this.errorInfo = errorInfo;
    }

    public Result(int errorCode, String errorInfo, List<?> list) {
        this.setErrorCode(errorCode);
        this.setErrorInfo(errorInfo);
        this.setData(list);
    }

    public Result(int errorCode, String errorInfo, List<?> list, Map<String, Object> callback) {
        this.setErrorCode(errorCode);
        this.setErrorInfo(errorInfo);
        this.setData(list);
        this.setCallback(callback);
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public String getData_id() {
        return data_id;
    }

    public void setData_id(String data_id) {
        this.data_id = data_id;
    }

    public String getData_desc() {
        return data_desc;
    }

    public void setData_desc(String data_desc) {
        this.data_desc = data_desc;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(Map<String, Object> parameter) {
        this.callback = parameter.get("callback") == null ? "" : parameter.get("callback") + "";
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

}
