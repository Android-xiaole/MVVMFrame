package com.lee.mvvmframe.data;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

/**
 * Author ：lee
 * Date ：2022/5/22 14:49
 * Description ：
 */
public interface AppApiService {

    @GET("https://www.baidu.com")
    Observable<String> visitBaiDu();
} 
