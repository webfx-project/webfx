package webfx.framework.client.orm.reactive.dql.statement.conventions;

import javafx.beans.property.ObjectProperty;
import webfx.framework.shared.orm.entity.Entity;

public interface HasSelectedMasterProperty<E extends Entity> {

    ObjectProperty<E> selectedMasterProperty();
    default void setSelectedMaster(E selectedMaster) { selectedMasterProperty().set(selectedMaster); }
    default E getSelectedMaster() { return selectedMasterProperty().get(); }

}
