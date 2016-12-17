package naga.toolkit.spi;

import naga.commons.scheduler.UiScheduler;
import naga.commons.util.function.Factory;
import naga.commons.util.serviceloader.ServiceLoaderHelper;
import naga.toolkit.fx.spi.DrawingNode;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.Window;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class Toolkit {

    private Map<Class<? extends GuiNode>, Factory<GuiNode>> nodeFactories = new HashMap<>();
    private final UiScheduler uiScheduler;
    private final Factory<Window> windowFactory;
    private Window applicationWindow;

    public Toolkit(UiScheduler uiScheduler, Factory<Window> windowFactory) {
        this.uiScheduler = uiScheduler;
        this.windowFactory = windowFactory;
    }

    protected  <T extends GuiNode> void registerNodeFactory(Class<T> nodeInterface, Factory<GuiNode> nodeFactory) {
        nodeFactories.put(nodeInterface, nodeFactory);
    }

    private <T extends GuiNode> T createNode(Class<T> nodeInterface) {
        Factory<GuiNode> nodeFactory = nodeFactories.get(nodeInterface);
        if (nodeFactory != null)
            return (T) nodeFactory.create();
        System.out.println("WARNING: No factory node registered for " + nodeInterface + " in " + getClass());
        return null;
    }

    public <T extends GuiNode, N> T wrapNativeNode(N toolkitNode) {
        return null;
    }

    public static <N> N unwrapToNativeNode(GuiNode guiNode) {
        return guiNode == null ? null : guiNode.unwrapToNativeNode();
    }

    public Window getApplicationWindow() {
        if (applicationWindow == null)
            applicationWindow = windowFactory.create();
        return applicationWindow;
    }

    public boolean isReady() {
        return true;
    }

    public void onReady(Runnable runnable) {
        get().scheduler().runInUiThread(runnable);
    }

    public UiScheduler scheduler() {
        return uiScheduler;
    }

    public static boolean isUiThread() {
        return get().scheduler().isUiThread();
    }

    private static Toolkit TOOLKIT;

    public static synchronized Toolkit get() {
        if (TOOLKIT == null) {
            //Platform.log("Getting toolkit");
            TOOLKIT = ServiceLoaderHelper.loadService(Toolkit.class);
            //Platform.log("Toolkit ok");
        }
        return TOOLKIT;
    }

    public DrawingNode createDrawingNode() {
        return createNode(DrawingNode.class);
    }

}
