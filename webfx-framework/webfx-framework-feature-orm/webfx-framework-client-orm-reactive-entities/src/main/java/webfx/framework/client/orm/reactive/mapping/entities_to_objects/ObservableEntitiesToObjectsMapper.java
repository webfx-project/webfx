package webfx.framework.client.orm.reactive.mapping.entities_to_objects;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import webfx.framework.client.orm.reactive.mapping.dql_to_entities.OptimizedObservableListWrapper;
import webfx.framework.shared.orm.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
final class ObservableEntitiesToObjectsMapper<E extends Entity, T> {

    private final ObservableList<E> observableEntities;
    private final OptimizedObservableListWrapper<T> mappedObjects = new OptimizedObservableListWrapper<>();

    ObservableEntitiesToObjectsMapper(ObservableList<E> observableEntities, Function<E, T> mappedObjectCreator, BiConsumer<E, T> mappedObjectUpdater, BiConsumer<E, T> mappedObjectDeleter) {
        this.observableEntities = observableEntities;
        observableEntities.addListener((ListChangeListener<E>) change -> mappedObjects.runInTransaction(() -> {
            while (change.next()) {
                ObservableList<? extends E> list = change.getList();
                int from = change.getFrom();
                int to = change.getTo();
                if (change.wasPermutated()) {
                    List<T> copy = new ArrayList<>(mappedObjects);
                    for (int index1 = from; index1 < to; index1++) {
                        int index2 = change.getPermutation(index1);
                        T mappedObject1 = copy.get(index1);
                        T mappedObject2 = copy.set(index2, mappedObject1);
                        copy.set(index1, mappedObject2);
                        mappedObjectUpdater.accept(list.get(index1), mappedObject2);
                        mappedObjectUpdater.accept(list.get(index2), mappedObject1);
                    }
                    mappedObjects.setAll(copy);
                } else if (change.wasUpdated()) {
                    for (int i = from; i < to; i++)
                        mappedObjectUpdater.accept(list.get(i), mappedObjects.get(i));
                } else {
                    int addedSize = change.getAddedSize();
                    int removedSize = change.getRemovedSize();
                    if (removedSize > addedSize) {
                        List<? extends E> removed = change.getRemoved();
                        for (int i = 0; i < removedSize; i++) {
                            int listIndex = from + i;
                            T mappedObject = mappedObjects.get(listIndex);
                            if (i < addedSize)
                                mappedObjectUpdater.accept(list.get(listIndex), mappedObject);
                            else
                                mappedObjectDeleter.accept(removed.get(i), mappedObject);
                        }
                        mappedObjects.remove(from + addedSize, from + removedSize);
                    } else {
                        List<T> copy = new ArrayList<>(mappedObjects);
                        for (int i = 0; i < addedSize; i++) {
                            int listIndex = from + i;
                            E entity = list.get(listIndex);
                            if (i < removedSize)
                                mappedObjectUpdater.accept(entity, mappedObjects.get(listIndex));
                            else
                                copy.add(listIndex, mappedObjectCreator.apply(entity));
                        }
                        mappedObjects.setAll(copy);
                    }
                }
            }
        }));
    }

    ObservableList<E> getObservableEntities() {
        return observableEntities;
    }

    ObservableList<T> getMappedObjects() {
        return mappedObjects;
    }

}

