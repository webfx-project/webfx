package webfx.framework.activity.base.elementals.view.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import webfx.framework.activity.base.elementals.uiroute.impl.UiRouteActivityContextBase;
import webfx.framework.activity.base.elementals.view.ViewActivityContext;
import webfx.framework.activity.ActivityContext;
import webfx.framework.activity.ActivityContextFactory;

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
