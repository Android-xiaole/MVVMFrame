package com.lee.base.mvvm.rxjava;

import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Author ：lee
 * Date ：2022/5/22 18:03
 * Description ：
 */
public interface IDisposableContainer {

    void addDisposable(Disposable disposable);

    void clearDisposable();
}
