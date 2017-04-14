package io.auxo.databinding.watchdog.sample.view;

import android.content.Intent;
import android.databinding.BaseObservable;

import java.util.Map;

import io.auxo.databinding.watchdog.Watchdog;
import io.auxo.databinding.watchdog.sample.BR;
import io.auxo.databinding.watchdog.sample.R;
import io.auxo.databinding.watchdog.sample.databinding.ActivityMainBinding;
import io.auxo.databinding.watchdog.sample.library.view.BaseActivity;
import io.auxo.databinding.watchdog.sample.library.viewmodel.ViewModel;
import io.auxo.databinding.watchdog.sample.viewmodel.MainViewModel;
import io.auxo.databinding.watchdog.sample.viewmodel.watchdog.IMainViewModelCallbacks;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements IMainViewModelCallbacks {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected Map<Integer, ViewModel> getDataBindingVariables(Map<Integer, ViewModel> variables) {
        String username = getIntent().getStringExtra("username");
        variables.put(BR.watchdog, new MainViewModel(username));
        Watchdog.watch(variables.get(BR.watchdog))
                .addWatcher(this);
        return variables;
    }

    @Override
    public void onNavigateCode(BaseObservable observableField, int fieldId) {
        startActivity(new Intent(this, CodeActivity.class));
    }

    @Override
    public void onNavigateIssues(BaseObservable observableField, int fieldId) {
        startActivity(new Intent(this, IssuesActivity.class));
    }
}
