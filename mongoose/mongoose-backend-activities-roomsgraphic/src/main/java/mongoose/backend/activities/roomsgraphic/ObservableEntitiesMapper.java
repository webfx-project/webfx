package mongoose.backend.activities.roomsgraphic;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import webfx.framework.shared.orm.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ObservableEntitiesMapper<E extends Entity, T> {

    private final ObservableList<E> observableList;
    private final ObservableList<T> mappedObservableList = FXCollections.observableArrayList();

    public ObservableEntitiesMapper(Function<E, T> creator, BiConsumer<E, T> updater, BiConsumer<E, T> deleter) {
        this (FXCollections.observableArrayList(), creator, updater, deleter);
    }

    public ObservableEntitiesMapper(ObservableList<E> observableList, Function<E, T> creator, BiConsumer<E, T> updater, BiConsumer<E, T> deleter) {
        this.observableList = observableList;
        observableList.addListener((ListChangeListener<E>) c -> {
            while (c.next()) {
                if (c.wasPermutated()) {
                    List<T> copy = new ArrayList<>(mappedObservableList);
                    for (int i = c.getFrom(); i < c.getTo(); i++)
                        copy.set(i, mappedObservableList.get(c.getPermutation(i)));
                    mappedObservableList.setAll(copy);
                } else if (c.wasUpdated() || c.wasReplaced()) {
                    for (int i = c.getFrom(); i < c.getTo(); i++)
                        updater.accept(c.getList().get(i), mappedObservableList.get(i));
                } else {
                    if (c.wasAdded()) {
                        List<T> copy = new ArrayList<>(mappedObservableList);
                        for (int i = c.getFrom(); i < c.getTo(); i++)
                            copy.add(i, creator.apply(c.getList().get(i)));
                        mappedObservableList.setAll(copy);
                    }
                    if (c.wasRemoved()) {
                        List<? extends E> removed = c.getRemoved();
                        for (int i = c.getFrom(); i < c.getTo(); i++)
                            deleter.accept(removed.get(i - c.getFrom()), mappedObservableList.get(i));
                        mappedObservableList.remove(c.getFrom(), c.getTo());
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

