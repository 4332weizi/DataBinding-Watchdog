package io.auxo.databinding.watchdog;

@SuppressWarnings("unchecked")
public class Watchable<W, N extends Watcher> {

    protected W mWatchable;
    // protected List<Watcher> mWatchers;

    protected Injector<W, N> mInjector;

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

    private Injector<W, N> getInjector(W watchable) {
        String injectorName = Utils.getWatchdogPackage(watchable.getClass())
                + "." + Utils.getInjectorClassName(watchable.getClass());
        try {
            return (Injector<W, N>) Class.forName(injectorName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
