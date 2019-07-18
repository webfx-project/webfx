package webfx.framework.client.ui.action.impl;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public final class SeparatorAction extends ReadOnlyAction {

    public SeparatorAction() {
        super(new SimpleStringProperty("----"), new SimpleObjectProperty<>(), new SimpleBooleanProperty(true), new SimpleBooleanProperty(true), null);
    }
}
