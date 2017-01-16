package net.funol.databinding.watchdog;

/**
 * Created by zhaoweiwei on 2017/1/17.
 */

public abstract class WatchdogInjector {
    public abstract void inject(Object beWatched, Object beNotified);
}
