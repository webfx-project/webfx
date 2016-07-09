package naga.providers.toolkit.javafx.nodes.controls;

import naga.toolkit.spi.events.ActionEvent;
import naga.providers.toolkit.javafx.events.FxActionEvent;
import naga.toolkit.spi.nodes.controls.Button;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
public class FxButton extends FxButtonBase<javafx.scene.control.Button> implements Button<javafx.scene.control.Button> {


    public FxButton() {
        this(createButton());
    }

    public FxButton(javafx.scene.control.Button button) {
        super(button);
        button.setOnAction(event -> actionEventObservable.onNext(new FxActionEvent(event)));
    }

    private static javafx.scene.control.Button createButton() {
        return new javafx.scene.control.Button();
    }

    private final BehaviorSubject<ActionEvent> actionEventObservable = BehaviorSubject.create();
    @Override
    public Observable<ActionEvent> actionEventObservable() {
        return actionEventObservable;
    }
}
