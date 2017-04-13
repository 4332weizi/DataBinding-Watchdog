package net.funol.databinding.watchdog;

/**
 * Created by zhaoweiwei on 2017/1/17.
 */

public interface Injector<W, N> {
    void inject(W beWatched, N beNotified);
}
