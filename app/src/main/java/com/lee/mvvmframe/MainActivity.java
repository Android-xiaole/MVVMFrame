package com.lee.mvvmframe;

import android.os.Bundle;

import com.lee.base.ui.BaseActivity;
import com.lee.mvvmframe.databinding.ActivityMainBinding;

import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    protected void initParam() {

    }

    @Override
    protected int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    protected int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initData() {
        viewModel.name.set("结果显示");
        viewModel.imgPlaceholderRes = R.mipmap.ic_launcher;
        viewModel.imgUrl.set("https://img2.baidu.com/it/u=1873256083,931126228&fm=253&fmt=auto?w=130&h=170");
    }

    @Override
    protected void initViewObservable() {
    }

    @Override
    protected MainViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(this,factory).get(MainViewModel.class);
    }
}