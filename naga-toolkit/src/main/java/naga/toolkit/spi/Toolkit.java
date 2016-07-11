package naga.toolkit.spi;

import javafx.collections.ObservableList;
import naga.commons.scheduler.Scheduler;
import naga.commons.util.function.Converter;
import naga.commons.util.function.Factory;
import naga.commons.util.serviceloader.ServiceLoaderHelper;
import naga.toolkit.properties.conversion.ConvertedObservableList;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.UnimplementedNode;
import naga.toolkit.spi.nodes.charts.*;
import naga.toolkit.spi.nodes.controls.*;
import naga.toolkit.spi.nodes.gauges.Gauge;
import naga.toolkit.spi.nodes.layouts.HBox;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;
import naga.toolkit.spi.nodes.layouts.Window;

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
        System.out.println("WARNING: No factory node registered for " + nodeInterface + " in " + getClass());
        return (T) new UnimplementedNode<>();
    }

    public <N> void registerNativeNodeWrapper(Class<N> nativeNodeClass, Converter<N, GuiNode> nativeNodeWrapper) {
        nativeNodeWrappers.put(nativeNodeClass, nativeNodeWrapper);
    }

    public <T extends GuiNode, N> void registerNodeFactoryAndWrapper(Class<T> nodeInterface, Factory<GuiNode> nodeFactory, Class<N> nativeNodeClass, Converter<N, GuiNode> nativeNodeWrapper) {
        registerNodeFactory(nodeInterface, nodeFactory);
        registerNativeNodeWrapper(nativeNodeClass, nativeNodeWrapper);
    }

    public <T extends GuiNode, N> T wrapNativeNode(N toolkitNode) {
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

    public TextField createTextField() {
        return nodeFactories.containsKey(TextField.class) ? createNode(TextField.class) : createSearchBox();
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

    public ScatterChart createScatterChart() {
        return createNode(ScatterChart.class);
    }

    public PieChart createPieChart() {
        return createNode(PieChart.class);
    }

    public Gauge createGauge() {
        return nodeFactories.containsKey(Gauge.class) ? createNode(Gauge.class) : createSlider();
    }
}
