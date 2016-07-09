package naga.providers.toolkit.gwt.nodes.controls;

import naga.toolkit.spi.events.ActionEvent;
import naga.providers.toolkit.gwt.events.GwtActionEvent;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
public class GwtButton extends GwtButtonBase<com.google.gwt.user.client.ui.Button> implements naga.toolkit.spi.nodes.controls.Button<com.google.gwt.user.client.ui.Button> {

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
