package io.auxo.databinding.watchdog.sample.library.viewmodel;

import android.databinding.ObservableField;
import android.text.TextUtils;

import io.auxo.databinding.watchdog.annotations.WatchThis;

public class ViewModel {

    @WatchThis(method = "showToast")
    public ObservableField<String> mToastText = new ObservableField<>();

    public void showToast(String message) {
        if (TextUtils.equals(message, mToastText.get())) {
            mToastText.notifyChange();
        } else {
            mToastText.set(message);
        }
    }

}
