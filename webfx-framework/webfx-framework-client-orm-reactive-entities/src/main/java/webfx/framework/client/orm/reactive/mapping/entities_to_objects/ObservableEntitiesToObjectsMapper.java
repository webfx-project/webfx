package webfx.framework.client.orm.reactive.mapping.entities_to_objects;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import webfx.framework.shared.orm.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public final class ObservableEntitiesToObjectsMapper<E extends Entity, T> {

    private final ObservableList<E> observableEntities;
    private final ObservableList<T> observableObjects = new OptimizedObservableListWrapper<>();

    public ObservableEntitiesToObjectsMapper(Function<E, T> objectCreator, BiConsumer<E, T> objectUpdater, BiConsumer<E, T> objectDeleter) {
        this (new OptimizedObservableListWrapper<>(), objectCreator, objectUpdater, objectDeleter);
    }

    public ObservableEntitiesToObjectsMapper(ObservableList<E> observableEntities, Function<E, T> objectCreator, BiConsumer<E, T> objectUpdater, BiConsumer<E, T> objectDeleter) {
        this.observableEntities = observableEntities;
        observableEntities.addListener((ListChangeListener<E>) change -> {
            while (change.next()) {
                if (change.wasPermutated()) {
                    List<T> copy = new ArrayList<>(observableObjects);
                    for (int index1 = change.getFrom(); index1 < change.getTo(); index1++) {
                        int index2 = change.getPermutation(index1);
                        T e1 = copy.get(index1);
                        T e2 = copy.set(index2, e1);
                        copy.set(index1, e2);
                        objectUpdater.accept(change.getList().get(index1), e2);
                        objectUpdater.accept(change.getList().get(index2), e1);
                    }
                    observableObjects.setAll(copy);
                } else if (change.wasUpdated() || change.wasReplaced()) {
                    for (int i = change.getFrom(); i < change.getTo(); i++)
                        objectUpdater.accept(change.getList().get(i), observableObjects.get(i));
                } else {
                    if (change.wasRemoved()) {
                        List<? extends E> removed = change.getRemoved();
                        int n = removed.size();
                        if (objectDeleter != null) {
                            for (int i = 0; i < n; i++)
                                objectDeleter.accept(removed.get(i), observableObjects.get(change.getFrom() + i));
                        }
                        observableObjects.remove(change.getFrom(), change.getFrom() + n);
                    }
                    if (change.wasAdded()) {
                        List<T> copy = new ArrayList<>(observableObjects);
                        for (int i = change.getFrom(); i < change.getTo(); i++)
                            copy.add(i, objectCreator.apply(change.getList().get(i)));
                        observableObjects.setAll(copy);
                    }
                }
            }
        });
    }

    public ObservableList<T> getObservableObjects() {
        return observableObjects;
    }

    public void updateFromEntities(List<E> entities) {
        observableEntities.setAll(entities);
    }

}

