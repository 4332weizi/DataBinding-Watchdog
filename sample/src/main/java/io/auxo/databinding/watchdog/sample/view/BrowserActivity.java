package io.auxo.databinding.watchdog.sample.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;

import java.util.Map;

import io.auxo.databinding.watchdog.Watchdog;
import io.auxo.databinding.watchdog.sample.BR;
import io.auxo.databinding.watchdog.sample.R;
import io.auxo.databinding.watchdog.sample.databinding.ActivityBrowserBinding;
import io.auxo.databinding.watchdog.sample.library.view.BaseActivity;
import io.auxo.databinding.watchdog.sample.library.viewmodel.ViewModel;
import io.auxo.databinding.watchdog.sample.viewmodel.BrowserViewModel;
import io.auxo.databinding.watchdog.sample.viewmodel.watchdog.IBrowserViewModelCallbacks;

public class BrowserActivity extends BaseActivity<ActivityBrowserBinding> implements IBrowserViewModelCallbacks {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";

    public static void browse(Context context, String title, String url) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_browser;
    }

    @Override
    protected Map<Integer, ViewModel> getDataBindingVariables(Map<Integer, ViewModel> variables) {
        variables.put(BR.browser, new BrowserViewModel(getIntent().getStringExtra(EXTRA_URL)));
        Watchdog.watch(variables.get(BR.browser))
                .addWatcher(this);
        return variables;
    }

    @Override
    public void setActionBarVisibility(ObservableBoolean observableField, int fieldId) {
        if (observableField.get()) {
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
        }
    }
}
