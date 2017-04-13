package io.auxo.databinding.watchdog;

/**
 * Created by ZHAOWEIWEI on 2017/1/6.
 */
public class Watchdog {

    public static final String PACKAGE_NAME_SUFFIX = ".watchdog";

    public static <W, N extends Watcher> Watchable<W, N> watch(W beWatched) {
        return new Watchable<>(beWatched);
    }

    public static String getWatchdogPackage(Class clazz) {
        return getWatchdogPackage(clazz.getPackage().getName());
    }

    public static String getWatchdogPackage(String packageName) {
        return packageName + Watchdog.PACKAGE_NAME_SUFFIX;
    }

    public static String getCallbackInterfaceName(Class clazz) {
        return getCallbackInterfaceName(clazz.getSimpleName());
    }

    public static String getCallbackInterfaceName(String className) {
        return "I" + className + "Callbacks";
    }

    public static String getInjectorClassName(Class clazz) {
        return getInjectorClassName(clazz.getSimpleName());
    }

    public static String getInjectorClassName(String className) {
        return className + "$$WatchdogInjector";
    }
}
