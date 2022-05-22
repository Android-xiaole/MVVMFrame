package com.lee.base.mvvm.eventbus;

/**
 * Author ：lee
 * Date ：2022/5/21 18:00
 * Description ：通用的EventBus消息类
 */
public class EventMessage<T> {
    int code; // 标识消息类型
    T data; // 消息数据

    public EventMessage(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
