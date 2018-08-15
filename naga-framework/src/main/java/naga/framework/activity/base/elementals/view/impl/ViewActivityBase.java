package naga.framework.activity.base.elementals.view.impl;

import javafx.scene.Node;
import naga.util.async.Future;
import naga.framework.activity.base.elementals.uiroute.impl.UiRouteActivityBase;
import naga.framework.activity.base.elementals.view.ViewActivity;
import naga.framework.activity.base.elementals.view.ViewActivityContext;
import naga.framework.activity.base.elementals.view.ViewActivityContextMixin;
import naga.fx.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
public abstract class ViewActivityBase
        <C extends ViewActivityContext<C>>

        extends UiRouteActivityBase<C>
        implements ViewActivity<C>,
        ViewActivityContextMixin<C> {

    private Node uiNode;

    @Override
    public Future<Void> onResumeAsync() {
        if (Toolkit.get().isReady())
            return Future.runAsync(this::onResume);
        Future<Void> future = Future.future();
        Toolkit.get().onReady(() -> {
            onResume();
            future.complete();
        });
        return future;
    }

    @Override
    public void onResume() {
        super.onResume(); // will update context parameters from route and make the active property to true
        startLogic(); // The good place to start the logic (before building ui but after the above update)
        if (uiNode == null)
            uiNode = buildUi();
        setNode(uiNode);
    }

    protected void startLogic() {
    }
}
