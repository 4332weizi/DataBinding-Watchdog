package net.funol.databinding.watchdog.sample.view;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

import net.funol.databinding.watchdog.Watchdog;
import net.funol.databinding.watchdog.sample.BR;
import net.funol.databinding.watchdog.sample.R;
import net.funol.databinding.watchdog.sample.databinding.ActivityMainBinding;
import net.funol.databinding.watchdog.sample.viewmodel.MainViewModel;
import net.funol.databinding.watchdog.sample.viewmodel.ViewModel;
import net.funol.databinding.watchdog.sample.viewmodel.watchdog.IMainViewModelCallbacks;

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
