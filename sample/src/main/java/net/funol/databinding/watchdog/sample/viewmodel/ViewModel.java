package net.funol.databinding.watchdog.sample.viewmodel;

import android.databinding.ObservableField;
import android.text.TextUtils;

import net.funol.databinding.watchdog.annotations.WatchThis;

/**
 * Created by ZHAOWEIWEI on 2017/3/31.
 */

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
