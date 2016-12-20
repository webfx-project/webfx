package naga.providers.toolkit.html.fx;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.html.fx.html.HtmlScene;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.stage.Window;

import static elemental2.Global.document;
import static elemental2.Global.window;

/**
 * @author Bruno Salmon
 */
public class HtmlWindow extends Window {

    // withProperty and heightProperty will be bound (to make them unmodifiable) to these final properties
    private final Property<Double> htmlWidthProperty = new SimpleObjectProperty<>(0d);
    private final Property<Double> htmlHeightProperty = new SimpleObjectProperty<>(0d);


    public HtmlWindow() {
        document.body.style.overflow = "hidden";
        // Binding withProperty and heightProperty (to make them unmodifiable)
        widthProperty().bind(htmlWidthProperty);
        heightProperty().bind(htmlHeightProperty);
        // Making their value to the actual browser window
        updateHtmlDimensions();
        window.onresize = a -> {
            updateHtmlDimensions();
            return null;
        };
    }

    private void updateHtmlDimensions() {
        htmlWidthProperty.setValue(window.innerWidth);
        htmlHeightProperty.setValue(window.innerHeight);
    }

    @Override
    protected void onTitleUpdate() {
        document.title = getTitle();
    }

    @Override
    protected void onSceneRootUpdate() {
        setWindowContent(((HtmlScene) getScene()).getSceneNode());
    }

    private void setWindowContent(elemental2.Node content) {
        HtmlUtil.setBodyContent(content);
    }

}
