package com.lee.base.mvvm;

import com.lee.base.mvvm.eventbus.EventMessage;
import com.lee.base.mvvm.rxjava.IDisposableContainer;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * Author ：lee
 * Date ：2022/5/18 17:47
 * Description ：ViewModel接口类
 */
interface IBaseViewModel extends LifecycleEventObserver, IDisposableContainer {
    /**
     * 生命周期发生任何变化都会调用，这里定义这些生命周期方法是为了外部调用的时候是空方法
     * 因为onCreate里面执行了初始化EventBus的逻辑，防止外部调用的时候没写super，导致出现问题
     *
     * @param owner 生命周期持有者（Activity）
     * @param event 生命周期类型
     */
    default void onAny(LifecycleOwner owner, Lifecycle.Event event) {
    }

    default void onCreate() {
    }


    default void onStart() {
    }


    default void onResume() {
    }


    default void onPause() {
    }


    default void onStop() {
    }


    default void onDestroy() {
    }


    /**
     * 是否使用EventBus
     *
     * @return true:使用 false:不使用，默认不使用，如需使用请重写方法并返回true
     */
    default boolean useEventBus() {
        return false;
    }

    /**
     * 接受EventBus消息
     *
     * @param message 消息
     */
    void onReceiveMessage(EventMessage<?> message);
}
