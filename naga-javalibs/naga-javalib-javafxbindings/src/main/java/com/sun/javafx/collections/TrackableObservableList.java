package com.sun.javafx.collections;

import javafx.collections.ListChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class TrackableObservableList<T> extends ObservableListWrapper<T>{

    public TrackableObservableList(List<T> list) {
        super(list);
    }

    public TrackableObservableList() {
        super(new ArrayList<T>());
        addListener((ListChangeListener.Change<? extends T> c) -> {
            TrackableObservableList.this.onChanged((ListChangeListener.Change<T>)c);
        });
    }

    protected abstract void onChanged(ListChangeListener.Change<T> c);

}
