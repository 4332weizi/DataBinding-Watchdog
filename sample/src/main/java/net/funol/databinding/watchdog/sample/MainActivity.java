package net.funol.databinding.watchdog.sample;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import net.funol.databinding.watchdog.Watchdog;
import net.funol.databinding.watchdog.sample.databinding.ActivityMainBinding;
import net.funol.databinding.watchdog.sample.watchdog.IMainViewModelCallbacks;

public class MainActivity extends AppCompatActivity implements IMainViewModelCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainViewModel viewModel = new MainViewModel();
        binding.setWatchdog(viewModel);

        Watchdog.newBuilder()
                .watch(viewModel)
                .notify(this)
                .build();
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

    public void showToast(String content) {
        Toast toast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
