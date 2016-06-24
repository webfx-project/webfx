package naga.core.spi.toolkit.pivot.controls;

import naga.core.spi.toolkit.controls.Button;
import naga.core.spi.toolkit.events.ActionEvent;
import naga.core.spi.toolkit.pivot.events.PivotActionEvent;
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
