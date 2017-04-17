package io.auxo.databinding.watchdog.sample.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.view.View;

import io.auxo.databinding.watchdog.annotations.WatchThis;
import io.auxo.databinding.watchdog.sample.library.viewmodel.ViewModel;

public class MainViewModel extends ViewModel {

    public final ObservableField<String> username = new ObservableField<>();

    @WatchThis
    public final BaseObservable onNavigateCode = new BaseObservable();
    @WatchThis
    public final BaseObservable onNavigateIssues = new BaseObservable();
    @WatchThis
    public final BaseObservable onNavigatePulls = new BaseObservable();
    @WatchThis
    public final BaseObservable onNavigatePulse = new BaseObservable();

    public MainViewModel(String username) {
        this.username.set(username);
    }

    public View.OnClickListener onCodeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onNavigateCode.notifyChange();
        }
    };

    public View.OnClickListener onIssuesClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onNavigateIssues.notifyChange();
        }
    };

    public View.OnClickListener onPullsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onNavigatePulls.notifyChange();
        }
    };

    public View.OnClickListener onPulseClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onNavigatePulse.notifyChange();
        }
    };

}
