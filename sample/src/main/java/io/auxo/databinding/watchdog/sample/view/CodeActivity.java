package io.auxo.databinding.watchdog.sample.view;

import android.os.Bundle;
import android.webkit.WebView;

import java.util.Map;

import io.auxo.databinding.watchdog.sample.BR;
import io.auxo.databinding.watchdog.sample.R;
import io.auxo.databinding.watchdog.sample.databinding.ActivityCodeBinding;
import io.auxo.databinding.watchdog.sample.library.view.BaseActivity;
import io.auxo.databinding.watchdog.sample.library.viewmodel.ViewModel;
import io.auxo.databinding.watchdog.sample.viewmodel.BrowserViewModel;

public class CodeActivity extends BaseActivity<ActivityCodeBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Code");
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_code;
    }

    @Override
    protected Map<Integer, ViewModel> getDataBindingVariables(Map<Integer, ViewModel> variables) {
        variables.put(BR.webview, new BrowserViewModel("https://github.com/4332weizi/DataBinding-Watchdog"));
        return variables;
    }
}
