package com.lee.base.mvvm.data;

import java.util.List;

/**
 * Author ：lee
 * Date ：2021/4/4 12:10
 * Description ：适用于返回数据结构：
 * {
 *     "code":200,
 *     "message":"ok",
 *     "data":[
 *         {
 *             "key":"value"
 *         },
 *         {
 *             "key":"value"
 *         },
 *         {
 *             "key":"value"
 *         }
 *     ]
 * }
 */
class ListData<T> extends BaseData {

    List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
