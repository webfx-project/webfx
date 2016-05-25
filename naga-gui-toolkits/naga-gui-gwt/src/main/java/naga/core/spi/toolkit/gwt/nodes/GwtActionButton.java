package naga.core.spi.toolkit.gwt.nodes;

import com.google.gwt.user.client.ui.Button;
import naga.core.spi.toolkit.event.ActionEvent;
import naga.core.spi.toolkit.gwt.event.GwtActionEvent;
import naga.core.spi.toolkit.nodes.ActionButton;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
public class GwtActionButton extends GwtButtonBase<Button> implements ActionButton<Button> {

    public GwtActionButton() {
        this(new Button());
    }

    public GwtActionButton(Button button) {
        super(button);
        button.addClickHandler(event -> actionEventObservable.onNext(new GwtActionEvent(event)));
    }

    private final BehaviorSubject<ActionEvent> actionEventObservable = BehaviorSubject.create();
    @Override
    public Observable<ActionEvent> actionEventObservable() {
        return actionEventObservable;
    }
}
