package com.lee.mvvmframe.data;

import com.lee.base.BaseConstants;
import com.lee.base.BuildConfig;
import com.lee.base.mvvm.BaseApi;
import com.lee.base.mvvm.HttpProvider;
import com.lee.base.mvvm.RequestHandler;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;

/**
 * Author ：lee
 * Date ：2022/5/22 14:50
 * Description ：
 */
public class AppApi extends BaseApi {
    private static volatile AppApiService instance;

    private AppApi() {
    }

    public static AppApiService getInstance() {
        if (instance == null) {
            synchronized (AppApi.class) {
                if (instance == null) {
                    instance = new AppApi().getRetrofit().create(AppApiService.class);
                }
            }
        }
        return instance;
    }

    @Override
    public HttpProvider configHttpProvider() {
        return new HttpProvider() {
            @Override
            public String configBaseUrl() {
                return BaseConstants.BASE_URL;
            }

            @Override
            public Interceptor[] configInterceptors() {
                return new Interceptor[0];
            }

            @Override
            public Converter.Factory[] configConverterFactories() {
                return new Converter.Factory[0];
            }

            @Override
            public void configOkHttpClient(OkHttpClient.Builder builder) {

            }

            @Override
            public CookieJar configCookie() {
                return null;
            }

            @Override
            public RequestHandler configHandler() {
                return null;
            }

            @Override
            public long configConnectTimeoutMills() {
                return 0;
            }

            @Override
            public long configReadTimeoutMills() {
                return 0;
            }

            @Override
            public long configWriteTimeoutMills() {
                return 0;
            }

            @Override
            public boolean configLogEnable() {
                return BuildConfig.DEBUG;
            }
        };
    }
}
