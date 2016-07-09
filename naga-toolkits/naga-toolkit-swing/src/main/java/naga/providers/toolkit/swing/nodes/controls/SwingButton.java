package naga.providers.toolkit.swing.nodes.controls;

import naga.providers.toolkit.swing.events.SwingActionEvent;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.controls.Button;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingButton extends SwingButtonBase<JButton> implements Button<JButton> {


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
    }

    private final BehaviorSubject<ActionEvent> actionEventObservable = BehaviorSubject.create();
    @Override
    public Observable<ActionEvent> actionEventObservable() {
        return actionEventObservable;
    }
}
