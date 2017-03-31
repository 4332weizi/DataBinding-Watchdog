package net.funol.databinding.watchdog.sample;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.view.View;

import net.funol.databinding.watchdog.annotations.WatchThis;

/**
 * Created by ZHAOWEIWEI on 2017/1/6.
 */

public class MainViewModel  extends ViewModel{

    @WatchThis(method = "onUserNameChanged")
    public final ObservableField<String> username = new ObservableField<>();
    @WatchThis(method = "onPasswordChanged")
    public final ObservableField<String> password = new ObservableField<>();

    @WatchThis
    public final BaseObservable onLoginSuccess = new BaseObservable();

    public View.OnClickListener onLoginBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onLoginSuccess.notifyChange();
        }
    };

}
