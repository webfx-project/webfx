package naga.core.spi.toolkit.swing.nodes;

import naga.core.spi.toolkit.event.ActionEvent;
import naga.core.spi.toolkit.nodes.ActionButton;
import naga.core.spi.toolkit.swing.event.SwingActionEvent;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingActionButton extends SwingButtonBase<JButton> implements ActionButton<JButton> {


    public SwingActionButton() {
        this(new JButton());
    }

    public SwingActionButton(JButton button) {
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
