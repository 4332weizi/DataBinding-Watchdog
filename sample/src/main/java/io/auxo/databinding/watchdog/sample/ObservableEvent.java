package io.auxo.databinding.watchdog.sample;

import android.databinding.BaseObservable;

import java.io.Serializable;

public class ObservableEvent<T> extends BaseObservable implements Serializable {

    protected T mValue;

    public ObservableEvent() {
    }

    public ObservableEvent(T value) {
        mValue = value;
        notifyChange();
    }

    public void trigger(T value){
        mValue = value;
        notifyChange();
    }

    public void trigger(){
        notifyChange();
    }

    public T get() {
        return mValue;
    }
}
