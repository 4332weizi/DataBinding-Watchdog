package io.auxo.databinding.watchdog.sample.view;

import android.databinding.ObservableField;

import java.util.Map;

import io.auxo.databinding.watchdog.Watchdog;
import io.auxo.databinding.watchdog.sample.BR;
import io.auxo.databinding.watchdog.sample.ObservableEvent;
import io.auxo.databinding.watchdog.sample.R;
import io.auxo.databinding.watchdog.sample.databinding.ActivityLoginBinding;
import io.auxo.databinding.watchdog.sample.library.view.BaseActivity;
import io.auxo.databinding.watchdog.sample.library.viewmodel.ViewModel;
import io.auxo.databinding.watchdog.sample.viewmodel.LoginViewModel;
import io.auxo.databinding.watchdog.sample.viewmodel.watchdog.ILoginViewModelCallbacks;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements ILoginViewModelCallbacks {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected Map<Integer, ViewModel> getDataBindingVariables(Map<Integer, ViewModel> variables) {
        variables.put(BR.watchdog, new LoginViewModel());
        Watchdog.watch(variables.get(BR.watchdog))
                .addWatcher(this);
        return variables;
    }

    @Override
    public void onUserNameChanged(ObservableField<String> observableField, int fieldId) {
        showToast("username:" + observableField.get());
    }

    @Override
    public void onPasswordChanged(ObservableField<String> observableField, int fieldId) {
        showToast("password:" + observableField.get());
    }

    @Override
    public void onLoginSuccess(ObservableEvent<String> observableField, int fieldId) {
        showToast("login success");
        MainActivity.start(this, observableField.get());
        finish();
    }
}
