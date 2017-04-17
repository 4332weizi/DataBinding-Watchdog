package io.auxo.databinding.watchdog.sample.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.auxo.databinding.watchdog.annotations.WatchThis;
import io.auxo.databinding.watchdog.sample.library.viewmodel.ViewModel;

public class BrowserViewModel extends ViewModel {

    public final ObservableField<String> url = new ObservableField<>("");
    public final ObservableInt progress = new ObservableInt(0);

    @WatchThis(method = "setActionBarVisibility")
    public final ObservableBoolean progressVisible = new ObservableBoolean(true);

    public BrowserViewModel(String url) {
        this.url.set(url);
    }

    public WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progressVisible.set(true);
            view.loadUrl(url);
            return true;
        }
    };

    public WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progress.set(newProgress);
            if (newProgress == 100) {
                progressVisible.set(false);
            }
        }
    };
}
