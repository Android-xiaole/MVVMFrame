package com.lee.base.mvvm.rxjava;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;
import com.lee.base.mvvm.BaseError;

import org.json.JSONException;

import java.net.UnknownHostException;

import io.reactivex.rxjava3.observers.ResourceObserver;


/**
 * Author ：le
 * Date ：2019-10-22 9:59
 * Description ：为Observable定制的ResourceObserver
 */
public abstract class MyObserver<T> extends ResourceObserver<T> {
    private boolean isNetWork = true; // 标记是否是网络请求，默认是网络请求
    private IDisposableContainer disposableContainer;

    // 网络请求成功的回调
    protected abstract void onSuccess(T t);

    // 网络请求失败回调
    protected abstract void onFailure(BaseError error);

    /**
     * 网络请求结束的回调（不管成功还是失败都会回调，这里一般可以去做progress dismiss的操作）
     * 这和onComplete不同，onComplete只会在onNext走完之后回调,该方法不需要可以不调用
     */
    protected void onEnd() {
    }

    public MyObserver() {
    }

    public MyObserver(boolean isNetWork) {
        this.isNetWork = isNetWork;
    }

    public MyObserver(IDisposableContainer disposableContainer) {
        this.disposableContainer = disposableContainer;
    }

    public MyObserver(boolean isNetWork, IDisposableContainer disposableContainer) {
        this.isNetWork = isNetWork;
        this.disposableContainer = disposableContainer;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isNetWork) {
            NetworkUtils.NetworkType networkType = NetworkUtils.getNetworkType();
            switch (networkType) {
                case NETWORK_NO:
                case NETWORK_UNKNOWN:
                    // 一定好主动调用下面这一句,取消本次Subscriber订阅
                    if (!isDisposed()) {
                        dispose();
                    }
                    onFailure(BaseError.netWorkError());
                    onEnd();
                    break;
                default:
                    if (disposableContainer != null) {
                        disposableContainer.addDisposable(this);
                    }
                    break;
            }
        } else {
            if (disposableContainer != null) {
                disposableContainer.addDisposable(this);
            }
        }
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        BaseError error;
        if (e != null) {
            if (!(e instanceof BaseError)) {
                if (e instanceof UnknownHostException) {
                    error = BaseError.netWorkError();
                } else if (e instanceof JSONException || e instanceof JsonParseException || e instanceof MalformedJsonException) {
                    error = BaseError.jsonParseError();
                } else {
                    error = new BaseError(e);
                }
            } else {
                error = (BaseError) e;
            }
        } else {
            // e=null，就设定为未知异常
            error = BaseError.unKnowError();
        }
        onFailure(error);
        onEnd();
    }

    @Override
    public void onComplete() {
        onEnd();
    }

}
