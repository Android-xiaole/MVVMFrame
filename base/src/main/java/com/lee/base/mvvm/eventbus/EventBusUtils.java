package com.lee.base.mvvm.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * Author ：lee
 * Date ：2022/5/21 18:02
 * Description ：发送消息工具类
 */
public class EventBusUtils {
    public static <T> void sendMessage(EventMessage<T> message) {
        EventBus.getDefault().post(message);
    }
} 
