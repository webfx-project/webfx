package naga.providers.toolkit.pivot.nodes.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.pivot.events.PivotActionEvent;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.controls.Image;
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

    //TODO: display image in the button
    private final Property<Image> imageProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Image> imageProperty() {
        return imageProperty;
    }

}
