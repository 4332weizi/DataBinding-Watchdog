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
        Field[] fields = beWatched.getClass().getFields();
        for (Field field : fields) {
            WatchThis fieldAnnotation = field.getAnnotation(WatchThis.class);
            if (fieldAnnotation == null) {
                continue;
            }
            try {
                if (!(field.get(beWatched) instanceof Observable)) {
                    throw new RuntimeException("field " + field.getName() + " must be Observable(DataBinding) ");
                }
                addOnPropertyChangedCallback(field, beNotified.getClass().getMethod(field.getName(), field.get(beWatched).getClass(), int.class));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("method " + field.getName() + " must be declare in " + beNotified.getClass().getName());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("field " + field.getName() + " must be declare as public access");
            }
        }
    }

    protected void addOnPropertyChangedCallback(final Field field, final Method method) throws IllegalAccessException {
        ((Observable) field.get(beWatched)).addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int fieldId) {
                try {
                    method.invoke(beNotified, observable, fieldId);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("method " + method.getName() + " must be declare as public access");
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e.getCause());
                }
            }
        });
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
            return new Watchdog(beWatched, beNotified);
        }
    }
}
