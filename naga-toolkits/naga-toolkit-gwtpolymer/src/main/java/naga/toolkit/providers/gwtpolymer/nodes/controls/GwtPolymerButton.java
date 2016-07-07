package naga.toolkit.providers.gwtpolymer.nodes.controls;

import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.paper.widget.PaperButton;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.providers.gwt.events.GwtActionEvent;
import naga.toolkit.providers.gwt.nodes.GwtNode;
import naga.toolkit.spi.nodes.controls.Button;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerButton extends GwtNode<PaperButton> implements Button<PaperButton> {

    public GwtPolymerButton() {
        this(new PaperButton());
    }

    public GwtPolymerButton(PaperButton button) {
        super(button);
        Polymer.ready(button.getElement(), o -> {
            syncVisualText();
            textProperty.addListener((observable, oldValue, newValue) -> syncVisualText());
            button.addClickHandler(event -> actionEventObservable.onNext(new GwtActionEvent(event)));
            return null;
        });
    }

    private final BehaviorSubject<ActionEvent> actionEventObservable = BehaviorSubject.create();
    @Override
    public Observable<ActionEvent> actionEventObservable() {
        return actionEventObservable;
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

    private void syncVisualText() {
        node.getElement().setInnerHTML(getText());
    }
}
