package naga.framework.activity.view.impl;

import javafx.scene.Node;
import naga.commons.util.async.Future;
import naga.framework.activity.uiroute.impl.UiRouteActivityBase;
import naga.framework.activity.view.ViewActivity;
import naga.framework.activity.view.ViewActivityContext;
import naga.framework.activity.view.ViewActivityContextMixin;
import naga.fx.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
public abstract class ViewActivityBase
        <C extends ViewActivityContext<C>>

        extends UiRouteActivityBase<C>
        implements ViewActivity<C>,
        ViewActivityContextMixin<C> {

    private Node viewNode;

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
        super.onResume();
        if (viewNode == null)
            viewNode = buildUi();
        setNode(viewNode);
    }
}
