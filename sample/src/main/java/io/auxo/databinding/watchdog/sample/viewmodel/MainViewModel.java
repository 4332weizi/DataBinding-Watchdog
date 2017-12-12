package io.auxo.databinding.watchdog.sample.viewmodel;

import android.databinding.ObservableField;
import android.view.View;

import io.auxo.databinding.watchdog.annotations.WatchThis;
import io.auxo.databinding.watchdog.sample.ObservableEvent;
import io.auxo.databinding.watchdog.sample.library.viewmodel.ViewModel;

public class MainViewModel extends ViewModel {

    public final ObservableField<String> username = new ObservableField<>();

    @WatchThis
    public final ObservableEvent<Void> onNavigateCode = new ObservableEvent<>();
    @WatchThis
    public final ObservableEvent<Void> onNavigateIssues = new ObservableEvent<>();
    @WatchThis
    public final ObservableEvent<Void> onNavigatePulls = new ObservableEvent<>();
    @WatchThis
    public final ObservableEvent<Void> onNavigatePulse = new ObservableEvent<>();

    public MainViewModel(String username) {
        this.username.set(username);
    }

    public View.OnClickListener onCodeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onNavigateCode.trigger();
        }
    };

    public View.OnClickListener onIssuesClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onNavigateIssues.trigger();
        }
    };

    public View.OnClickListener onPullsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onNavigatePulls.trigger();
        }
    };

    public View.OnClickListener onPulseClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onNavigatePulse.trigger();
        }
    };

}
