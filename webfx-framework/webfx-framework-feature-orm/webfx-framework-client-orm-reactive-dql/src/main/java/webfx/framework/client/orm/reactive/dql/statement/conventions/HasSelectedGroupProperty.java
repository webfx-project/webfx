package webfx.framework.client.orm.reactive.dql.statement.conventions;

import javafx.beans.property.ObjectProperty;
import webfx.framework.shared.orm.entity.Entity;

public interface HasSelectedGroupProperty<E extends Entity> {

    ObjectProperty<E> selectedGroupProperty();
    default void setSelectedGroup(E selectedGroup) { selectedGroupProperty().set(selectedGroup); }
    default E getSelectedGroup() { return selectedGroupProperty().get(); }

}
