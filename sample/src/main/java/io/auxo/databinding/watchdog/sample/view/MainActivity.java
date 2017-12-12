package io.auxo.databinding.watchdog.sample.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;

import java.util.Map;

import io.auxo.databinding.watchdog.Watchdog;
import io.auxo.databinding.watchdog.sample.BR;
import io.auxo.databinding.watchdog.sample.ObservableEvent;
import io.auxo.databinding.watchdog.sample.R;
import io.auxo.databinding.watchdog.sample.databinding.ActivityMainBinding;
import io.auxo.databinding.watchdog.sample.library.view.BaseActivity;
import io.auxo.databinding.watchdog.sample.library.viewmodel.ViewModel;
import io.auxo.databinding.watchdog.sample.viewmodel.MainViewModel;
import io.auxo.databinding.watchdog.sample.viewmodel.watchdog.IMainViewModelCallbacks;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements IMainViewModelCallbacks {

    public static final String EXTRA_USERNAME = "username";

    public static void start(Context context, String username) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        context.startActivity(intent);
    }

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
    public void onNavigateCode(ObservableEvent<Void> observableField, int fieldId) {
        BrowserActivity.browse(this, "Code", "https://github.com/4332weizi/DataBinding-Watchdog/");
    }

    @Override
    public void onNavigateIssues(ObservableEvent<Void> observableField, int fieldId) {
        BrowserActivity.browse(this, "Issues", "https://github.com/4332weizi/DataBinding-Watchdog/issues");
    }

    @Override
    public void onNavigatePulls(ObservableEvent<Void> observableField, int fieldId) {
        BrowserActivity.browse(this, "Pull Requests", "https://github.com/4332weizi/DataBinding-Watchdog/pulls");
    }

    @Override
    public void onNavigatePulse(ObservableEvent<Void> observableField, int fieldId) {
        BrowserActivity.browse(this, "Pulse", "https://github.com/4332weizi/DataBinding-Watchdog/pulse");
    }
}
