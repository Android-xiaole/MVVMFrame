package com.lee.mvvmframe;

import android.app.Application;

import com.blankj.utilcode.util.ToastUtils;
import com.lee.base.mvvm.BaseError;
import com.lee.base.mvvm.BaseViewModel;
import com.lee.base.mvvm.binding.command.BindingAction;
import com.lee.base.mvvm.binding.command.BindingCommand;
import com.lee.base.mvvm.eventbus.EventMessage;
import com.lee.base.mvvm.rxjava.MyObserver;
import com.lee.base.mvvm.rxjava.ThreadTransformer;
import com.lee.mvvmframe.data.AppRepository;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

/**
 * Author ：lee
 * Date ：2022/5/21 15:35
 * Description ：
 */
public class MainViewModel extends BaseViewModel<AppRepository> {
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> imgUrl = new ObservableField<>();
    public int imgPlaceholderRes;


    public MainViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }

    public BindingCommand changeClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            showProgress();
            model.visitBaiDu()
                    .compose(ThreadTransformer.child2main())
                    .subscribe(new MyObserver<String>(MainViewModel.this) {
                        @Override
                        protected void onSuccess(String result) {
                            name.set(result);
                        }

                        @Override
                        protected void onFailure(BaseError error) {
                            name.set(error.getMessage());
                            ToastUtils.showShort(error.getMessage());
                        }

                        @Override
                        protected void onEnd() {
                            super.onEnd();
                            dismissProgress();
                        }
                    });
        }
    });

    @Override
    public void onReceiveMessage(EventMessage<?> message) {
        super.onReceiveMessage(message);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

}
