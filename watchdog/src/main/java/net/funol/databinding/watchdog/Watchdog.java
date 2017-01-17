package net.funol.databinding.watchdog;

/**
 * Created by ZHAOWEIWEI on 2017/1/6.
 */
@SuppressWarnings("unchecked")
public class Watchdog {

    private Object beWatched;
    private Object beNotified;

    private Watchdog(Object beWatched, Object beNotified) {
        this.beWatched = beWatched;
        this.beNotified = beNotified;
        wakeup();
    }

    protected void wakeup() {
        String injectorName = beWatched.getClass().getPackage().getName()
                + Util.WATCHDOG_PACKAGE_NAME_SUFFIX
                + "." + Util.getInjectorClassName(beWatched.getClass().getSimpleName());
        try {
            Class clazz = Class.forName(injectorName);
            WatchdogInjector injector = (WatchdogInjector) clazz.newInstance();
            injector.inject(beWatched, beNotified);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
                throw new IllegalArgumentException("beWatched can not be null.");
            }
            if (beNotified == null) {
                throw new IllegalArgumentException("beNotified can not be null.");
            }

            String callbackInterfaceName = beWatched.getClass().getPackage().getName()
                    + Util.WATCHDOG_PACKAGE_NAME_SUFFIX + "."
                    + Util.getCallbackInterfaceName(beWatched.getClass().getSimpleName());

            try {
                Class callback = Class.forName(callbackInterfaceName);
                if (!callback.isInstance(beNotified)) {
                    throw new RuntimeException(beNotified.getClass().getSimpleName() + " must implement " + callback.getSimpleName());
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
