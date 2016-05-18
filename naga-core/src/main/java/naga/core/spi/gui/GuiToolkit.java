package naga.core.spi.gui;

import javafx.collections.ObservableList;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.spi.gui.nodes.Window;
import naga.core.spi.gui.property.MappedObservableList;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.platform.ServiceLoaderHelper;
import naga.core.util.function.Converter;
import naga.core.util.function.Factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class GuiToolkit {

    private Map<Class<? extends GuiNode>, Factory<GuiNode>> nodeFactories = new HashMap<>();
    private Map<Class<?>, Converter<?, GuiNode>> nativeNodeWrappers = new HashMap<>();
    private final Scheduler uiScheduler;
    private final Factory<Window> windowFactory;
    private Window applicationWindow;

    public GuiToolkit(Scheduler uiScheduler, Factory<Window> windowFactory) {
        this.uiScheduler = uiScheduler;
        this.windowFactory = windowFactory;
    }

    protected <T extends GuiNode> void registerNodeFactory(Class<T> nodeInterface, Factory<GuiNode> nodeFactory) {
        nodeFactories.put(nodeInterface, nodeFactory);
    }

    public <T extends GuiNode> T createNode(Class<T> nodeInterface) {
        return (T) nodeFactories.get(nodeInterface).create();
    }

    public <N> void registerNativeNodeWrapper(Class<N> nativeNodeClass, Converter<N, GuiNode> nativeNodeWrapper) {
        nativeNodeWrappers.put(nativeNodeClass, nativeNodeWrapper);
    }

    public <T extends GuiNode, N> void registerNodeFactoryAndWrapper(Class<T> nodeInterface, Factory<GuiNode> nodeFactory, Class<N> nativeNodeClass, Converter<N, GuiNode> nativeNodeWrapper) {
        registerNodeFactory(nodeInterface, nodeFactory);
        registerNativeNodeWrapper(nativeNodeClass, nativeNodeWrapper);
    }

    public <N> GuiNode wrapNativeNode(N toolkitNode) {
        Converter guiNodeConverter = nativeNodeWrappers.get(toolkitNode.getClass());
        return  (GuiNode) guiNodeConverter.convert(toolkitNode);
    }

    public static <N> N unwrapToNativeNode(GuiNode<N> guiNode) {
        return guiNode.unwrapToNativeNode();
    }

    public <N> ObservableList<GuiNode<N>> wrapNativeObservableList(ObservableList<N> nativeList) {
        return MappedObservableList.create(nativeList, this::wrapNativeNode, GuiToolkit::unwrapToNativeNode);
    }

    public Window getApplicationWindow() {
        if (applicationWindow == null)
            applicationWindow = windowFactory.create();
        return applicationWindow;
    }

    public DisplayResultSet transformDisplayResultForGui(DisplayResultSet displayResultSet) {
        return displayResultSet;
    }

    public Scheduler scheduler() {
        return uiScheduler;
    }

    public static boolean isUiThread() {
        return get().scheduler().isUiThread();
    }

    private static GuiToolkit TOOLKIT;

    public static void register(GuiToolkit toolkit) {
        TOOLKIT = toolkit;
    }

    public static GuiToolkit get() {
        if (TOOLKIT == null) {
            //Platform.log("Getting toolkit");
            TOOLKIT = ServiceLoaderHelper.loadService(GuiToolkit.class);
            //Platform.log("Toolkit ok");
        }
        return TOOLKIT;
    }

}
