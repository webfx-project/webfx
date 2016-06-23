package naga.core.spi.toolkit.gwt.controls;

import naga.core.spi.toolkit.events.ActionEvent;
import naga.core.spi.toolkit.gwt.events.GwtActionEvent;
import naga.core.spi.toolkit.controls.Button;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
public class GwtButton extends GwtButtonBase<com.google.gwt.user.client.ui.Button> implements Button<com.google.gwt.user.client.ui.Button> {

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
}
