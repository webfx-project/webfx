package webfx.framework.client.activity.impl.elementals.view.impl;

import javafx.scene.Node;
import webfx.kit.launcher.WebFxKitLauncher;
import webfx.platform.shared.util.async.Future;
import webfx.framework.client.activity.impl.elementals.uiroute.impl.UiRouteActivityBase;
import webfx.framework.client.activity.impl.elementals.view.ViewActivity;
import webfx.framework.client.activity.impl.elementals.view.ViewActivityContext;
import webfx.framework.client.activity.impl.elementals.view.ViewActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public abstract class ViewActivityBase
        <C extends ViewActivityContext<C>>

        extends UiRouteActivityBase<C>
        implements ViewActivity<C>,
        ViewActivityContextMixin<C> {

    protected Node uiNode;

    @Override
    public Future<Void> onResumeAsync() {
        if (WebFxKitLauncher.isReady())
            return Future.runAsync(this::onResume);
        Future<Void> future = Future.future();
        WebFxKitLauncher.onReady(() -> {
            onResume();
            future.complete();
        });
        return future;
    }

    @Override
    public void onResume() {
        super.onResume(); // will update context parameters from route and make the active property to true
        if (uiNode == null) {
            startLogic(); // The good place to start the logic (before building ui but after the above update)
            uiNode = buildUi();
        }
        setNode(uiNode);
    }

    protected void startLogic() {
    }
}
