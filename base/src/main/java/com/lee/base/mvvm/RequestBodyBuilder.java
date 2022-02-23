package com.lee.base.mvvm;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Author ：le
 * Date ：2019-10-24 10:26
 * Description ：okhttp构建json请求体的类
 */
public class RequestBodyBuilder {
    private final JsonObject jsonObject;

    public RequestBodyBuilder() {
        jsonObject = new JsonObject();
    }

    /**
     * 构建json对象
     *
     * @param key   jsonKey
     * @param value 值
     * @return this
     */
    public <T> RequestBodyBuilder addProperty(String key, T value) {
        if (value instanceof Number) {
            this.jsonObject.addProperty(key, (Number) value);
        } else if (value instanceof String) {
            this.jsonObject.addProperty(key, (String) value);
        } else if (value instanceof Boolean) {
            this.jsonObject.addProperty(key, (Boolean) value);
        } else if (value instanceof Character) {
            this.jsonObject.addProperty(key, (Character) value);
        } else if (value instanceof JsonElement) {
            this.jsonObject.add(key, (JsonElement) value);
        } else {
            this.jsonObject.add(key, new Gson().toJsonTree(value));
        }
        return this;

    }

    /**
     * 构建json数组
     *
     * @param key       jsonKey
     * @param listValue 值
     * @return this
     */
    public <T> RequestBodyBuilder addProperty(String key, List<T> listValue) {
        JsonArray jsonArray = new JsonArray();
        for (T t : listValue) {
            if (t instanceof Number) {
                jsonArray.add((Number) t);
            } else if (t instanceof String) {
                jsonArray.add((String) t);
            } else if (t instanceof Boolean) {
                jsonArray.add((Boolean) t);
            } else if (t instanceof Character) {
                jsonArray.add((Character) t);
            } else if (t instanceof JsonElement) {
                jsonArray.add((JsonElement) t);
            } else {
                jsonArray.add(new Gson().toJsonTree(t));
            }
        }
        this.jsonObject.add(key, jsonArray);
        return this;
    }

    public RequestBodyBuilder removeProperty(String property) {
        this.jsonObject.remove(property);
        return this;
    }

    public RequestBody build() {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }
}
