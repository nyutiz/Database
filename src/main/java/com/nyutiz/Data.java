package com.nyutiz;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private Map<String, String> dataMap;

    public Data() {
        this.dataMap = new HashMap<>();
    }

    public void setData(String key, String value) {
        dataMap.put(key, value);
    }

    public String getData(String key) {
        return dataMap.get(key);
    }

    public Map<String, String> getDataMap() {
        return dataMap;
    }
}
