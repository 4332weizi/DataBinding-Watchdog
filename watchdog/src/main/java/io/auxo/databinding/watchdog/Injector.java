package io.auxo.databinding.watchdog;

public interface Injector<W, N> {
    void inject(W beWatched, N beNotified);
}
