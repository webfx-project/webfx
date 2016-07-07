package naga.toolkit.providers.swing.nodes.controls;

import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.providers.swing.events.SwingActionEvent;
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
