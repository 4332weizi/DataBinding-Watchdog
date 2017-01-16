package net.funol.databinding.watchdog.sample;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    public void onLoginSuccess(BaseObservable observableField, int fieldId) {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
    }

}
