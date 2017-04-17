package io.auxo.databinding.watchdog;

public class Utils {

    public static final String PACKAGE_NAME_SUFFIX = ".watchdog";

    public static String getWatchdogPackage(Class clazz) {
        return getWatchdogPackage(clazz.getPackage().getName());
    }

    public static String getWatchdogPackage(String packageName) {
        return packageName + PACKAGE_NAME_SUFFIX;
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
