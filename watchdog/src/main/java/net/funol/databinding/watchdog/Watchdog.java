package net.funol.databinding.watchdog;

import android.databinding.Observable;

import net.funol.databinding.watchdog.annotations.WatchThis;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ZHAOWEIWEI on 2017/1/6.
 */

public class Watchdog {

    private Object beWatched;
    private Object beNotified;

    private Watchdog(Object beWatched, Object beNotified) {
        this.beWatched = beWatched;
        this.beNotified = beNotified;
        wakeup();
    }

    protected void wakeup() {
        String injectorName = Util.getInjectorClassName(beWatched.getClass().getSimpleName());
        try {
            Class clazz = Class.forName(injectorName);
            WatchdogInjector injector = (WatchdogInjector) clazz.newInstance();
            injector.inject(beWatched, beNotified);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private Object beWatched;
        private Object beNotified;


        public Builder() {
        }

        public Builder watch(Object beWatched) {
            this.beWatched = beWatched;
            return this;
        }

        public Builder notify(Object beNotified) {
            this.beNotified = beNotified;
            return this;
        }

        public Watchdog build() {
            if (beWatched == null) {
                throw new IllegalArgumentException("beWatched required.");
            }
            if (beNotified == null) {
                throw new IllegalArgumentException("beNotified required.");
            }

            String beWatchedPackageName = beWatched.getClass().getName().replace("." + beWatched.getClass().getSimpleName(), "");
            String callbackInterfaceName = Util.getCallbackInterfaceName(beWatched.getClass().getSimpleName());
            String callbackInterfacePackageName = beWatchedPackageName + Util.WATCHDOG_PACKAGE_NAME_SUFFIX;

            Class callback;

            try {
                callback = Class.forName(callbackInterfacePackageName + "." + callbackInterfaceName);
                if (!callback.isInstance(beNotified)) {
                    System.out.println(beNotified.getClass().getSimpleName() + " was suggest to implement " + callbackInterfaceName);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return new Watchdog(beWatched, beNotified);
        }
    }


    public static class Util {

        private static final String WATCHDOG_PACKAGE_NAME_SUFFIX = ".watchdog";

        public static String getCallbackInterfaceName(String className) {
            return "I" + className + "Callbacks";
        }

        public static String getInjectorClassName(String className) {
            return className + "$$WatchdogInjector";
        }
    }

}
