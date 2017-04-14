package io.auxo.databinding.watchdog.sample.viewmodel;

import android.databinding.ObservableField;
import android.view.View;

import io.auxo.databinding.watchdog.annotations.WatchThis;
import io.auxo.databinding.watchdog.sample.library.viewmodel.ViewModel;

public class LoginViewModel extends ViewModel {

    @WatchThis(method = "onUserNameChanged")
    public final ObservableField<String> username = new ObservableField<>();
    @WatchThis(method = "onPasswordChanged")
    public final ObservableField<String> password = new ObservableField<>();

    @WatchThis
    public final ObservableField<String> onLoginSuccess = new ObservableField<>();

    public View.OnClickListener onLoginBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (username.get() == null || username.get().equals("")) {
                showToast("please input your username");
                return;
            }
            if (password.get() == null || password.get().equals("")) {
                showToast("please input your password");
                return;
            }
            onLoginSuccess.set(username.get());
        }
    };

}
