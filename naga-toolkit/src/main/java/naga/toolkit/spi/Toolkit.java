package naga.toolkit.spi;

import javafx.collections.ObservableList;
import naga.commons.keyobject.KeyObject;
import naga.commons.scheduler.Scheduler;
import naga.commons.util.Strings;
import naga.commons.util.function.Converter;
import naga.commons.util.function.Factory;
import naga.commons.util.serviceloader.ServiceLoaderHelper;
import naga.toolkit.properties.conversion.ConvertedObservableList;
import naga.toolkit.properties.markers.HasTextProperty;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.Parent;
import naga.toolkit.spi.nodes.UnimplementedNode;
import naga.toolkit.spi.nodes.charts.*;
import naga.toolkit.spi.nodes.controls.*;
import naga.toolkit.spi.nodes.gauges.Gauge;
import naga.toolkit.spi.nodes.layouts.*;
import naga.toolkit.util.ObservableLists;

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

    public boolean isReady() {
        return true;
    }

    public void onReady(Runnable runnable) {
        get().scheduler().runInUiThread(runnable);
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

    public static synchronized Toolkit get() {
        if (TOOLKIT == null) {
            //Platform.log("Getting toolkit");
            TOOLKIT = ServiceLoaderHelper.loadService(Toolkit.class);
            //Platform.log("Toolkit ok");
        }
        return TOOLKIT;
    }

    public static <P extends Parent> P setAllChildren(P parent, GuiNode... children) {
        ObservableLists.setAllNonNulls(parent.getChildren(), children);
        return parent;
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

    public Image createImage() {
        return createNode(Image.class);
    }

    public Image createImage(Object urlOrJson) {
        boolean isJson = urlOrJson instanceof KeyObject;
        KeyObject json = isJson ? (KeyObject) urlOrJson : null;
        String url = isJson ? json.getString("url") : Strings.toString(urlOrJson);
        Double width = isJson ? json.getDouble("width") : null;
        Double height = isJson ? json.getDouble("width") : null;
        return createImage(url, width, height);
    }

    public Image createImage(String url, Double width, Double height) {
        Image image = createImage();
        image.setWidth(width);
        image.setHeight(height);
        image.setUrl(url);
        return image;
    }

    public Image createImageOrNull(String url) {
        return url == null ? null : createImage(url);
    }

    public TextView createTextView() {
        return createNode(TextView.class);
    }

    public TextView createTextView(String text) {
        return setText(createTextView(), text);
    }

    private static <T extends HasTextProperty> T setText(T hasText, String text) {
        hasText.setText(text);
        return hasText;
    }

    public TextView createTextViewOrNull(String text) {
        return text == null ? null : createTextView(text);
    }

    public HtmlView createHtmlView() {
        return createNode(HtmlView.class);
    }

    public HtmlView createHtmlView(String text) {
        return setText(createHtmlView(), text);
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

    public VBox createVBox(GuiNode... children) {
        return setAllChildren(createVBox(), children);
    }

    public HBox createHBox() {
        return createNode(HBox.class);
    }

    public HBox createHBox(GuiNode... children) {
        return setAllChildren(createHBox(), children);
    }

    public FlowPane createFlowPane() {
        return createNode(FlowPane.class);
    }

    public FlowPane createFlowPane(GuiNode... children) {
        return setAllChildren(createFlowPane(), children);
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
