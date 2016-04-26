package naga.core.spi.gui;

import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.spi.gui.nodes.Window;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.platform.ServiceLoaderHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class GuiToolkit {

    private Map<Class<? extends GuiNode>, GuiNodeFactory> nodeFactories = new HashMap<>();
    private Map<Class, ToolkitNodeWrapper> nodeWrappers = new HashMap<>();
    private Map<Class<? extends GuiNode>, ToolkitNodeWrapper> reversedNodeWrappers = new HashMap<>();

    protected <T extends GuiNode> void registerNodeFactory(Class<T> nodeInterface, GuiNodeFactory nodeFactory) {
        nodeFactories.put(nodeInterface, nodeFactory);
    }

    public <T extends GuiNode> T createNode(Class<T> nodeInterface) {
        return (T) nodeFactories.get(nodeInterface).createNode();
    }

    public void registerToolkitNodeWrapper(ToolkitNodeWrapper wrapper, Class tookitNodeClass, Class<? extends GuiNode> nagaNodeClass) {
        nodeWrappers.put(tookitNodeClass, wrapper);
    }

    public <T extends GuiNode> T wrapFromToolkitNode(Object toolkitNode, Class<T> nodeInterface) {
        return (T) wrapFromToolkitNode(toolkitNode);
    }

    public GuiNode wrapFromToolkitNode(Object toolkitNode) {
        return nodeWrappers.get(toolkitNode.getClass()).convert(toolkitNode);
    }

    public abstract Window getApplicationWindow();

    public DisplayResultSet transformDisplayResultForGui(DisplayResultSet displayResultSet) {
        return displayResultSet;
    }

    public Scheduler scheduler() {
        return Platform.get().scheduler();
    }

    public static boolean isUiThread() {
        return get().scheduler().isUiThread();
    }

    private static GuiToolkit TOOLKIT;

    public static void register(GuiToolkit toolkit) {
        TOOLKIT = toolkit;
    }

    public static GuiToolkit get() {
        if (TOOLKIT == null)
            TOOLKIT = ServiceLoaderHelper.loadService(GuiToolkit.class);
        return TOOLKIT;
    }

}
