package io.auxo.databinding.watchdog.sample.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.webkit.WebView;

import io.auxo.databinding.watchdog.sample.library.viewmodel.ViewModel;

public class BrowserViewModel extends ViewModel {

    public final ObservableField<String> url = new ObservableField<>("");

    public BrowserViewModel(String url) {
        this.url.set(url);
    }

    @BindingAdapter("loadUrl")
    public static void loadUrl(WebView view,final String url) {
        if (!("".equals(url))) {
            view.loadUrl(url);
            view.getSettings().setJavaScriptEnabled(true);
        }
    }

}
