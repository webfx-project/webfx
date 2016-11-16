package naga.providers.toolkit.swing.nodes.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.swing.events.SwingActionEvent;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.Image;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingButton extends SwingButtonBase<JButton> implements Button {


    public SwingButton() {
        this(new JButton());
    }

    public SwingButton(JButton button) {
        super(button);
        button.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent event) {
                actionEventObservable.onNext(new SwingActionEvent(event));
            }
        });
        imageProperty.addListener((observable, oldValue, image) -> node.setIcon(((JLabel) Toolkit.unwrapToNativeNode(image)).getIcon()));
    }

    private final BehaviorSubject<ActionEvent> actionEventObservable = BehaviorSubject.create();
    @Override
    public Observable<ActionEvent> actionEventObservable() {
        return actionEventObservable;
    }

    private final Property<Image> imageProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Image> imageProperty() {
        return imageProperty;
    }
}
