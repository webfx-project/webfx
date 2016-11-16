package naga.providers.toolkit.gwt.nodes.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.gwt.events.GwtActionEvent;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.controls.Image;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
public class GwtButton extends GwtButtonBase<com.google.gwt.user.client.ui.Button> implements naga.toolkit.spi.nodes.controls.Button {

    public GwtButton() {
        this(new com.google.gwt.user.client.ui.Button());
    }

    public GwtButton(com.google.gwt.user.client.ui.Button button) {
        super(button);
        button.addClickHandler(event -> actionEventObservable.onNext(new GwtActionEvent(event)));
    }

    private final BehaviorSubject<ActionEvent> actionEventObservable = BehaviorSubject.create();
    @Override
    public Observable<ActionEvent> actionEventObservable() {
        return actionEventObservable;
    }

    private final Property<Image> imageProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Image> imageProperty() {
        return imageProperty;
    }
}
