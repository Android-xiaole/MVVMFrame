package com.lee.mvvmframe.data;

import io.reactivex.rxjava3.core.Observable;

/**
 * Author ：lee
 * Date ：2022/5/22 14:56
 * Description ：
 */
public class HttpDataSourceImpl implements HttpDataSource{
    private volatile static HttpDataSourceImpl INSTANCE = null;
    private AppApiService apiService;

    public static HttpDataSourceImpl getInstance(AppApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    private HttpDataSourceImpl(AppApiService apiService) {
        this.apiService = apiService;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<String> visitBaiDu() {
        return apiService.visitBaiDu();
    }
}
