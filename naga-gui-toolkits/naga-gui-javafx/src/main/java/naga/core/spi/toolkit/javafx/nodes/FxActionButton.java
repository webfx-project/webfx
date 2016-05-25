package naga.core.spi.toolkit.javafx.nodes;

import javafx.scene.control.Button;
import naga.core.spi.toolkit.event.ActionEvent;
import naga.core.spi.toolkit.javafx.event.FxActionEvent;
import naga.core.spi.toolkit.nodes.ActionButton;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
public class FxActionButton extends FxButtonBase<Button> implements ActionButton<Button> {


    public FxActionButton() {
        this(new Button());
    }

    public FxActionButton(Button button) {
        super(button);
        button.setOnAction(event -> actionEventObservable.onNext(new FxActionEvent(event)));
    }

    private final BehaviorSubject<ActionEvent> actionEventObservable = BehaviorSubject.create();
    @Override
    public Observable<ActionEvent> actionEventObservable() {
        return actionEventObservable;
    }
}
