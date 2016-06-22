package naga.core.spi.toolkit.javafx.nodes;

import naga.core.spi.toolkit.event.ActionEvent;
import naga.core.spi.toolkit.javafx.event.FxActionEvent;
import naga.core.spi.toolkit.nodes.Button;
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
