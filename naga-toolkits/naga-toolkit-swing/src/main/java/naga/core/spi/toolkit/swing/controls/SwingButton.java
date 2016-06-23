package naga.core.spi.toolkit.swing.controls;

import naga.core.spi.toolkit.events.ActionEvent;
import naga.core.spi.toolkit.controls.Button;
import naga.core.spi.toolkit.swing.events.SwingActionEvent;
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
