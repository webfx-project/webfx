package naga.core.spi.toolkit.javafx.controls;

import naga.core.spi.toolkit.events.ActionEvent;
import naga.core.spi.toolkit.javafx.events.FxActionEvent;
import naga.core.spi.toolkit.controls.Button;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
public class FxButton extends FxButtonBase<javafx.scene.control.Button> implements Button<javafx.scene.control.Button> {


    public FxButton() {
        this(new javafx.scene.control.Button());
    }

    public FxButton(javafx.scene.control.Button button) {
        super(button);
        button.setOnAction(event -> actionEventObservable.onNext(new FxActionEvent(event)));
    }

    private final BehaviorSubject<ActionEvent> actionEventObservable = BehaviorSubject.create();
    @Override
    public Observable<ActionEvent> actionEventObservable() {
        return actionEventObservable;
    }
}
