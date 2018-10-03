package webfx.framework.client.activity.impl.elementals.view.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import webfx.framework.client.activity.impl.elementals.uiroute.impl.UiRouteActivityContextBase;
import webfx.framework.client.activity.impl.elementals.view.ViewActivityContext;
import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public class ViewActivityContextBase
        <THIS extends ViewActivityContextBase<THIS>>

        extends UiRouteActivityContextBase<THIS>
        implements ViewActivityContext<THIS> {

    protected ViewActivityContextBase(ActivityContext parentContext, ActivityContextFactory<THIS> contextFactory) {
        super(parentContext, contextFactory);
    }

    private final Property<Node> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Node> nodeProperty() {
        return nodeProperty;
    }

    private final Property<Node> mountNodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Node> mountNodeProperty() {
        return mountNodeProperty;
    }

}
