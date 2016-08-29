package naga.providers.toolkit.html.nodes.controls;

import elemental2.HTMLButtonElement;
import naga.providers.toolkit.html.HtmlUtil;
import naga.providers.toolkit.html.events.HtmlActionEvent;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.controls.Button;
import rx.Observable;
import rx.subjects.BehaviorSubject;


/**
 * @author Bruno Salmon
 */
public class HtmlButton extends HtmlButtonBase<HTMLButtonElement> implements Button<HTMLButtonElement> {

    public HtmlButton() {
        this(HtmlUtil.createButtonElement());
    }

    public HtmlButton(HTMLButtonElement button) {
        super(button);
        button.onclick = event -> {
            actionEventObservable.onNext(new HtmlActionEvent(event));
            return null;
        };
    }

    private final BehaviorSubject<ActionEvent> actionEventObservable = BehaviorSubject.create();
    @Override
    public Observable<ActionEvent> actionEventObservable() {
        return actionEventObservable;
    }
}
