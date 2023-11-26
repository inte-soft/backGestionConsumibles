package com.intesoft.syncworks.interfaces.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseDto {
    private int statusCode;
    private Map<String, Object> data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setInfo(String messageContent, int statusCode, String keyData) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put(keyData, messageContent);
        this.statusCode = statusCode;
        this.setData(data);
    }
}
