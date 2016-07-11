package naga.framework.ui.activity;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import naga.platform.activity.ActivityContextFactory;
import naga.platform.activity.ApplicationContextImpl;
import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;
import naga.platform.spi.Platform;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class UiApplicationContextImpl<C extends UiApplicationContextImpl<C>> extends ApplicationContextImpl<C> implements UiApplicationContext<C> {

    protected UiApplicationContextImpl(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
        nodeProperty().addListener(new ChangeListener<GuiNode>() {
            @Override
            public void changed(ObservableValue<? extends GuiNode> observable, GuiNode oldValue, GuiNode newValue) {
                observable.removeListener(this);
                //Platform.log("Binding application window node property");
                Toolkit.get().getApplicationWindow().nodeProperty().bind(observable);
            }
        });
    }

    private History history = Platform.get().getBrowserHistory();
    @Override
    public History getHistory() {
        return history;
    }

    @Override
    public JsonObject getParams() {
        return null;
    }

    private final Property<GuiNode> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> nodeProperty() {
        return nodeProperty;
    }

    @Override
    public Property<GuiNode> mountNodeProperty() {
        return null;
    }
}
