package com.lee.base.mvvm.data;

/**
 * Author ：lee
 * Date ：2021/4/4 12:09
 * Description ：适用于返回数据结构：
 * {
 *     "code":200,
 *     "message":"ok",
 *     "data":{
 *         "key":"value"
 *     }
 * }
 */
class SingleData<T> extends BaseData {

    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
