package naga.providers.toolkit.html.nodes.controls;

import elemental2.HTMLButtonElement;
import elemental2.Node;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.html.HtmlUtil;
import naga.providers.toolkit.html.events.HtmlActionEvent;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.Image;
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
        imageProperty.addListener((observable, oldValue, newValue) -> updateHtmlContent());
    }

    @Override
    protected void updateHtmlContent() {
        super.updateHtmlContent();
        Image image = getImage();
        if (image != null)
            HtmlUtil.appendFirstChild((Node) image.unwrapToNativeNode(), node);
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
