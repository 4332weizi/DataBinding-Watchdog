package net.funol.databinding.watchdog.sample;

import android.databinding.BaseObservable;
import android.view.View;

import net.funol.databinding.watchdog.annotations.WatchThis;

/**
 * Created by ZHAOWEIWEI on 2017/1/6.
 */

public class MainViewModel {

    @WatchThis
    public final BaseObservable onLoginSuccess = new BaseObservable();

    public View.OnClickListener onLoginBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onLoginSuccess.notifyChange();
        }
    };

}
