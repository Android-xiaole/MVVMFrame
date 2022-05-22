package com.lee.mvvmframe;

import com.lee.mvvmframe.data.AppApi;
import com.lee.mvvmframe.data.AppRepository;
import com.lee.mvvmframe.data.HttpDataSource;
import com.lee.mvvmframe.data.HttpDataSourceImpl;
import com.lee.mvvmframe.data.LocalDataSource;
import com.lee.mvvmframe.data.LocalDataSourceImpl;

/**
 * Author ：lee
 * Date ：2022/5/22 14:47
 * Description ：
 */
public class Injection {
    public static AppRepository provideDemoRepository() {
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(AppApi.getInstance());
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return AppRepository.getInstance(httpDataSource, localDataSource);
    }
} 
