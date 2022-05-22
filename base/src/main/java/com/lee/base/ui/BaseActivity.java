package com.lee.base.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.lee.base.mvvm.BaseViewModel;
import com.lee.base.mvvm.IBaseView;
import com.lee.base.view.BaseProgress;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/**
 * Author ：le
 * Date ：2019-10-21 16:43
 * Description ：BaseActivity用来处理一些必须的逻辑
 */
public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity implements IBaseView, BackHandledFragment.BackHandledInterface {
    protected V binding;
    protected VM viewModel;
    private int viewModelId;
    private BaseProgress dialog;
    private BackHandledFragment mBackHandedFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 页面接受的参数方法
        initParam();
        // 私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState);
        // 私有的ViewModel与View的契约事件回调逻辑
        registerUIChangeLiveDataCallBack();
        // 页面数据初始化方法
        initData();
        // 页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
    }

    /**
     * 初始化页面跳转参数
     */
    protected abstract void initParam();

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    protected abstract int initContentView(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    protected abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    protected VM initViewModel() {
        return null;
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化被被观察者
     */
    protected abstract void initViewObservable();

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        // DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState));
        viewModelId = initVariableId();
        viewModel = initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                // 如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(modelClass);
        }
        // 关联ViewModel
        binding.setVariable(viewModelId, viewModel);
        // 支持LiveData绑定xml，数据改变，UI自动会更新
        binding.setLifecycleOwner(this);
        // 让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
    }

    // 刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
        }
    }

    private void registerUIChangeLiveDataCallBack() {
        // 加载对话框显示
        viewModel.getUC().getShowProgressEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String title) {
                showProgress();
            }
        });
        // 加载对话框消失
        viewModel.getUC().getDismissProgressEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                dismissProgress();
            }
        });
        // 跳入新页面
        viewModel.getUC().getStartActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterField.CLASS);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startActivity(clz, bundle);
            }
        });
        // 跳入ContainerActivity
        viewModel.getUC().getStartContainerActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                String canonicalName = (String) params.get(BaseViewModel.ParameterField.CANONICAL_NAME);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startContainerActivity(canonicalName, bundle);
            }
        });
        // 关闭界面
        viewModel.getUC().getFinishEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                finish();
            }
        });
        // 关闭上一层
        viewModel.getUC().getOnBackPressedEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                onBackPressed();
            }
        });
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName, null);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public void startContainerActivity(String canonicalName, Bundle bundle) {
        Intent intent = new Intent(this, ContainerActivity.class);
        intent.putExtra(ContainerActivity.FRAGMENT, canonicalName);
        if (bundle != null) {
            intent.putExtra(ContainerActivity.BUNDLE, bundle);
        }
        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment.isVisible() && fragment instanceof BaseFragment) {
                if (((BaseFragment) fragment).onInterceptTouchEvent()) {
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void onBackPressed() {
        // 如果当前存在BackHandedFragment，并且其不捕获onBackPressed事件
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    /**
     * 判断当前页面是否载有BaseFragment
     *
     * @return true:有；false:没有
     */
    public boolean hasFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                // 注：这里的fragment资料显示有可能为null,但不影响instanceof判断
                if (fragment instanceof BaseFragment) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setShowFragment(BackHandledFragment showFragment) {
        mBackHandedFragment = showFragment;
    }

    @Override
    public void showProgress() {
        if (dialog == null) {
            dialog = new BaseProgress();
        }
        dialog.show(getSupportFragmentManager());
    }

    @Override
    public void dismissProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
