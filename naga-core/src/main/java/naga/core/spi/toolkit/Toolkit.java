package naga.core.spi.toolkit;

import javafx.collections.ObservableList;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.toolkit.charts.AreaChart;
import naga.core.spi.toolkit.charts.BarChart;
import naga.core.spi.toolkit.charts.LineChart;
import naga.core.spi.toolkit.charts.PieChart;
import naga.core.spi.toolkit.controls.*;
import naga.core.spi.toolkit.layouts.HBox;
import naga.core.spi.toolkit.layouts.VBox;
import naga.core.spi.toolkit.layouts.VPage;
import naga.core.spi.toolkit.layouts.Window;
import naga.core.spi.toolkit.node.GuiNode;
import naga.core.spi.toolkit.node.UnimplementedNode;
import naga.core.spi.toolkit.property.ConvertedObservableList;
import naga.core.util.function.Converter;
import naga.core.util.function.Factory;
import naga.core.util.serviceloader.ServiceLoaderHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class Toolkit {

    private Map<Class<? extends GuiNode>, Factory<GuiNode>> nodeFactories = new HashMap<>();
    private Map<Class<?>, Converter<?, GuiNode>> nativeNodeWrappers = new HashMap<>();
    private final Scheduler uiScheduler;
    private final Factory<Window> windowFactory;
    private Window applicationWindow;

    public Toolkit(Scheduler uiScheduler, Factory<Window> windowFactory) {
        this.uiScheduler = uiScheduler;
        this.windowFactory = windowFactory;
    }

    public <T extends GuiNode> void registerNodeFactory(Class<T> nodeInterface, Factory<GuiNode> nodeFactory) {
        nodeFactories.put(nodeInterface, nodeFactory);
    }

    public <T extends GuiNode> T createNode(Class<T> nodeInterface) {
        Factory<GuiNode> nodeFactory = nodeFactories.get(nodeInterface);
        if (nodeFactory != null)
            return (T) nodeFactory.create();
        Platform.log("WARNING: No factory node registered for " + nodeInterface + " in " + getClass());
        return (T) new UnimplementedNode<>();
    }

    public <N> void registerNativeNodeWrapper(Class<N> nativeNodeClass, Converter<N, GuiNode> nativeNodeWrapper) {
        nativeNodeWrappers.put(nativeNodeClass, nativeNodeWrapper);
    }

    public <T extends GuiNode, N> void registerNodeFactoryAndWrapper(Class<T> nodeInterface, Factory<GuiNode> nodeFactory, Class<N> nativeNodeClass, Converter<N, GuiNode> nativeNodeWrapper) {
        registerNodeFactory(nodeInterface, nodeFactory);
        registerNativeNodeWrapper(nativeNodeClass, nativeNodeWrapper);
    }

    public <T extends GuiNode, N> GuiNode wrapNativeNode(N toolkitNode) {
        Converter guiNodeConverter = nativeNodeWrappers.get(toolkitNode.getClass());
        return (T) guiNodeConverter.convert(toolkitNode);
    }

    public static <N> N unwrapToNativeNode(GuiNode<N> guiNode) {
        return guiNode == null ? null : guiNode.unwrapToNativeNode();
    }

    public <N> ObservableList<GuiNode<N>> wrapNativeObservableList(ObservableList<N> nativeList) {
        return ConvertedObservableList.create(nativeList, this::wrapNativeNode, Toolkit::unwrapToNativeNode);
    }

    public Window getApplicationWindow() {
        if (applicationWindow == null)
            applicationWindow = windowFactory.create();
        return applicationWindow;
    }

    public Scheduler scheduler() {
        return uiScheduler;
    }

    public static boolean isUiThread() {
        return get().scheduler().isUiThread();
    }

    private static Toolkit TOOLKIT;

    public static void register(Toolkit toolkit) {
        TOOLKIT = toolkit;
    }

    public static Toolkit get() {
        if (TOOLKIT == null) {
            //Platform.log("Getting toolkit");
            TOOLKIT = ServiceLoaderHelper.loadService(Toolkit.class);
            //Platform.log("Toolkit ok");
        }
        return TOOLKIT;
    }

    public Table createTable() {
        return createNode(Table.class);
    }

    public Button createButton() {
        return createNode(Button.class);
    }

    public CheckBox createCheckBox() {
        return createNode(CheckBox.class);
    }

    public SearchBox createSearchBox() {
        return createNode(SearchBox.class);
    }

    public Slider createSlider() {
        return createNode(Slider.class);
    }

    public VBox createVBox() {
        return createNode(VBox.class);
    }

    public HBox createHBox() {
        return createNode(HBox.class);
    }

    public VPage createVPage() {
        return createNode(VPage.class);
    }

    public LineChart createLineChart() {
        return createNode(LineChart.class);
    }

    public BarChart createBarChart() {
        return createNode(BarChart.class);
    }

    public AreaChart createAreaChart() {
        return createNode(AreaChart.class);
    }

    public PieChart createPieChart() {
        return createNode(PieChart.class);
    }
}
