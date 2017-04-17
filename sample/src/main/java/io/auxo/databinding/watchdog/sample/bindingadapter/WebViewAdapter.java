package io.auxo.databinding.watchdog.sample.bindingadapter;

import android.databinding.BindingAdapter;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewAdapter {

    @BindingAdapter("loadUrl")
    public static void loadUrl(WebView view, final String url) {
        if (!("".equals(url))) {
            view.loadUrl(url);
            view.getSettings().setJavaScriptEnabled(true);
        }
    }

    @BindingAdapter("webViewClient")
    public static void setWebViewClient(WebView view, final WebViewClient client) {
        view.setWebViewClient(client);
    }

    @BindingAdapter("webChromeClient")
    public static void setWebChromeClient(WebView view, final WebChromeClient client) {
        view.setWebChromeClient(client);
    }

}
