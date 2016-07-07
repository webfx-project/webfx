package naga.toolkit.providers.pivot.nodes.controls;

import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.providers.pivot.events.PivotActionEvent;
import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.PushButton;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
public class PivotButton extends PivotButtonBase<PushButton> implements Button<PushButton> {

    public PivotButton() {
        this(new PushButton());
    }

    public PivotButton(PushButton button) {
        super(button);
        button.setAction(new Action() {
            @Override
            public void perform(Component source) {
                actionEventObservable.onNext(new PivotActionEvent(source));
            }
        });
    }

    private final BehaviorSubject<ActionEvent> actionEventObservable = BehaviorSubject.create();
    @Override
    public Observable<ActionEvent> actionEventObservable() {
        return actionEventObservable;
    }
}
