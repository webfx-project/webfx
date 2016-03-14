package naga.core.spi.gui;

import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.node.DisplayNode;
import naga.core.spi.gui.node.Node;
import naga.core.spi.gui.node.NodeFactory;
import naga.core.spi.gui.node.ToolkitNodeWrapper;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.platform.ServiceLoaderHelper;
import rx.Observable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class GuiToolkit {

    private Map<Class<? extends Node>, NodeFactory> nodeFactories = new HashMap<>();
    private Map<Class, ToolkitNodeWrapper> nodeWrappers = new HashMap<>();
    private Map<Class<? extends Node>, ToolkitNodeWrapper> reversedNodeWrappers = new HashMap<>();

    protected <T extends Node> void registerNodeFactory(Class<T> nodeInterface, NodeFactory nodeFactory) {
        nodeFactories.put(nodeInterface, nodeFactory);
    }

    public <T extends Node> T createNode(Class<T> nodeInterface) {
        return (T) nodeFactories.get(nodeInterface).createNode();
    }

    public void registerToolkitNodeWrapper(ToolkitNodeWrapper wrapper, Class tookitNodeClass, Class<? extends Node> nagaNodeClass) {
        nodeWrappers.put(tookitNodeClass, wrapper);
    }

    public <T extends Node> T wrapFromToolkitNode(Object toolkitNode, Class<T> nodeInterface) {
        return (T) wrapFromToolkitNode(toolkitNode);
    }

    public Node wrapFromToolkitNode(Object toolkitNode) {
        return nodeWrappers.get(toolkitNode.getClass()).convert(toolkitNode);
    }

    public abstract void setRootNode(Node rootNode);

    public void observeAndDisplay(Observable<DisplayResult> displayResultObservable, DisplayNode displayNode) {
        observeOnUiThread(displayResultObservable.map(this::transformDisplayResultForGui)).subscribe(displayNode.getDisplayResultSubscriber());
    }

    protected DisplayResult transformDisplayResultForGui(DisplayResult displayResult) {
        return displayResult;
    }

    public Scheduler scheduler() {
        return Platform.get().scheduler();
    }

    public static boolean isUiThread() {
        return get().scheduler().isUiThread();
    }

    public abstract <T> Observable<T> observeOnUiThread(Observable<T> observable);

    private static GuiToolkit TOOLKIT;

    public static GuiToolkit get() {
        if (TOOLKIT == null)
            TOOLKIT = ServiceLoaderHelper.loadService(GuiToolkit.class);
        return TOOLKIT;
    }

}
