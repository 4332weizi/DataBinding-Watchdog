package io.auxo.databinding.watchdog.sample.view;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

import io.auxo.databinding.watchdog.Watchdog;
import io.auxo.databinding.watchdog.sample.BR;
import io.auxo.databinding.watchdog.sample.R;
import io.auxo.databinding.watchdog.sample.databinding.ActivityMainBinding;
import io.auxo.databinding.watchdog.sample.viewmodel.MainViewModel;
import io.auxo.databinding.watchdog.sample.viewmodel.ViewModel;
import io.auxo.databinding.watchdog.sample.viewmodel.watchdog.IMainViewModelCallbacks;

import java.util.Map;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements IMainViewModelCallbacks {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected Map<Integer, ViewModel> getDataBindingVariables(Map<Integer, ViewModel> variables) {
        variables.put(BR.watchdog, new MainViewModel());
        Watchdog.watch(variables.get(BR.watchdog))
                .addWatcher(this);
        return variables;
    }

    @Override
    public void onUserNameChanged(ObservableField<String> observableField, int fieldId) {
        showToast("用户名：" + observableField.get());
    }

    @Override
    public void onPasswordChanged(ObservableField<String> observableField, int fieldId) {
        showToast("密码：" + observableField.get());
    }

    @Override
    public void onLoginSuccess(BaseObservable observableField, int fieldId) {
        showToast("登录成功");
    }

}
