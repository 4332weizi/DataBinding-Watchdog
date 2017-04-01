package net.funol.databinding.watchdog;

/**
 * Created by ZHAOWEIWEI on 2017/4/1.
 */
@SuppressWarnings("unchecked")
public class Watchable<W, N extends Watcher> {

    protected W mWatchable;
    // protected List<Watcher> mWatchers;

    protected WatchdogInjector<W, N> mInjector;

    protected Watchable(W watchable) {
        if (watchable == null) {
            throw new IllegalArgumentException("watchable can not be null.");
        }
        mWatchable = watchable;
        mInjector = getInjector(mWatchable);
    }

    public Watchable<W, N> addWatcher(N watcher) {
        // mWatchers.add(watcher);
        mInjector.inject(mWatchable, watcher);
        return this;
    }

    private WatchdogInjector<W, N> getInjector(W watchable) {
        String injectorName = Watchdog.getWatchdogPackage(watchable.getClass())
                + "." + Watchdog.getInjectorClassName(watchable.getClass());
        try {
            return (WatchdogInjector<W, N>) Class.forName(injectorName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
