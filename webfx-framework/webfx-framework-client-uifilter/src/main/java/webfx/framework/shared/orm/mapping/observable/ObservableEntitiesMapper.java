package webfx.framework.shared.orm.mapping.observable;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import webfx.framework.shared.orm.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class ObservableEntitiesMapper<E extends Entity, T> {

    private final ObservableList<E> observableList;
    private final ObservableList<T> mappedObservableList = new ObservableListWrapperWithOptimizedChangeOnSetAll<>();

    public ObservableEntitiesMapper(Function<E, T> creator, BiConsumer<E, T> updater, BiConsumer<E, T> deleter) {
        this (new ObservableListWrapperWithOptimizedChangeOnSetAll<>(), creator, updater, deleter);
    }

    public ObservableEntitiesMapper(ObservableList<E> observableList, Function<E, T> creator, BiConsumer<E, T> updater, BiConsumer<E, T> deleter) {
        this.observableList = observableList;
        observableList.addListener((ListChangeListener<E>) c -> {
            while (c.next()) {
                if (c.wasPermutated()) {
                    List<T> copy = new ArrayList<>(mappedObservableList);
                    for (int index1 = c.getFrom(); index1 < c.getTo(); index1++) {
                        int index2 = c.getPermutation(index1);
                        T e1 = copy.get(index1);
                        T e2 = copy.set(index2, e1);
                        copy.set(index1, e2);
                        updater.accept(c.getList().get(index1), e2);
                        updater.accept(c.getList().get(index2), e1);
                    }
                    mappedObservableList.setAll(copy);
                } else if (c.wasUpdated() || c.wasReplaced()) {
                    for (int i = c.getFrom(); i < c.getTo(); i++)
                        updater.accept(c.getList().get(i), mappedObservableList.get(i));
                } else {
                    if (c.wasRemoved()) {
                        List<? extends E> removed = c.getRemoved();
                        int n = removed.size();
                        if (deleter != null) {
                            for (int i = 0; i < n; i++)
                                deleter.accept(removed.get(i), mappedObservableList.get(c.getFrom() + i));
                        }
                        mappedObservableList.remove(c.getFrom(), c.getFrom() + n);
                    }
                    if (c.wasAdded()) {
                        List<T> copy = new ArrayList<>(mappedObservableList);
                        for (int i = c.getFrom(); i < c.getTo(); i++)
                            copy.add(i, creator.apply(c.getList().get(i)));
                        mappedObservableList.setAll(copy);
                    }
                }
            }
        });
    }

    public ObservableList<T> getMappedObservableList() {
        return mappedObservableList;
    }

    public void updateFromEntities(List<E> entities) {
        observableList.setAll(entities);
    }

}

