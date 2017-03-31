package net.funol.databinding.watchdog.sample;

import android.databinding.ObservableBoolean;

import net.funol.databinding.watchdog.annotations.WatchThis;

/**
 * Created by ZHAOWEIWEI on 2017/3/31.
 */

public class ViewModel {
    @WatchThis
    public ObservableBoolean test = new ObservableBoolean();
}
