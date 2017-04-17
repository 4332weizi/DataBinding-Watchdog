package io.auxo.databinding.watchdog;

public class Watchdog {
    public static <W, N extends Watcher> Watchable<W, N> watch(W beWatched) {
        return new Watchable<>(beWatched);
    }
}
