package dev.webfx.kit.launcher.spi.impl.elemental2;

import com.sun.javafx.application.ParametersImpl;
import dev.webfx.kit.launcher.spi.impl.base.WebFxKitLauncherProviderBase;
import dev.webfx.kit.mapper.WebFxKitMapper;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.CanvasElementHelper;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.Context2DHelper;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.HtmlNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.UserInteraction;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.DragboardDataTransferHolder;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlFonts;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.useragent.UserAgent;
import dev.webfx.platform.util.Numbers;
import dev.webfx.platform.util.Strings;
import dev.webfx.platform.util.collection.Collections;
import dev.webfx.platform.util.elemental2.Elemental2Util;
import dev.webfx.platform.util.function.Factory;
import elemental2.dom.*;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

import java.util.Map;


/**
 * @author Bruno Salmon
 */
public final class Elemental2WebFxKitLauncherProvider extends WebFxKitLauncherProviderBase {

    private Application application;
    private HostServices hostServices;

    public Elemental2WebFxKitLauncherProvider() {
        super(false);
    }

    @Override
    public HostServices getHostServices() {
        if (hostServices == null)
            hostServices = uri -> {
                // Note: Safari is blocking (on macOS) or ignoring (on iOS) window.open() when not called during a user
                // interaction. If we are in that case, it's better to postpone the window opening to the next user
                // interaction (which we hope will happen soon, such as a key or mouse release).
                if (UserAgent.isSafari() && !UserInteraction.isUserInteracting()) {
                    UserInteraction.runOnNextUserInteraction(() ->
                            DomGlobal.window.open(uri, "_blank")
                        , true);
                } else {
                    // For other browsers, or with Safari but during a user interaction (ex: mouse click), it's ok to
                    // open the browser window straightaway.
                    DomGlobal.window.open(uri, "_blank");
                }
            };
        return hostServices;
    }

    @Override
    public javafx.scene.input.Clipboard getSystemClipboard() {
        return new javafx.scene.input.Clipboard() {
            @Override
            public boolean setContent(Map<DataFormat, Object> content) {
                setClipboardContent((String) content.get(DataFormat.PLAIN_TEXT));
                super.setContent(content);
                return true;
            }

            @Override
            public Object getContentImpl(DataFormat dataFormat) {
                if (dataFormat == DataFormat.PLAIN_TEXT)
                    return getClipboardContent();
                return super.getContent(dataFormat);
            }
        };
    }

    private static void setClipboardContent(String text) {
        Clipboard.writeText(text);
    }

    private static String getClipboardContent() {
        String[] content = {null};
        Clipboard.readText().then(text -> {
            content[0] = text;
            return null;
        });
        return content[0]; // Assuming the promise was instantly fulfilled
    }

    @Override
    public Dragboard createDragboard(Scene scene) {
        return new Dragboard(scene) {
            @Override
            public boolean setContent(Map<DataFormat, Object> content) {
                boolean result = false;
                DataTransfer dragBoardDataTransfer = DragboardDataTransferHolder.getDragboardDataTransfer();
                dragBoardDataTransfer.clearData();
                if (content != null)
                    for (Map.Entry<DataFormat, Object> entry : content.entrySet()) {
                        String value = Strings.toString(entry.getValue());
                        for (String formatIdentifier : entry.getKey().getIdentifiers())
                            if (dragBoardDataTransfer.setData(formatIdentifier, value))
                                result = true;
                    }
                return result;
            }

            @Override
            public Object getContentImpl(DataFormat dataFormat) {
                DataTransfer dataTransfer = DragboardDataTransferHolder.getDragboardDataTransfer();
                if (dataFormat == DataFormat.FILES)
                    return dataTransfer.files;
                String jsType = Collections.first(dataFormat.getIdentifiers());
                return dataTransfer.getData(jsType);
            }
        };
    }

    @Override
    public Screen getPrimaryScreen() {
        elemental2.dom.Screen screen = DomGlobal.screen;
        return Screen.from(toRectangle2D(screen.width, screen.height), toRectangle2D(screen.availWidth, screen.availHeight), DomGlobal.window.devicePixelRatio, DomGlobal.window.devicePixelRatio);
    }

    private static Rectangle2D toRectangle2D(double width, double height) {
        return new Rectangle2D(0, 0, width, height);
    }

    @Override
    public boolean supportsSvgImageFormat() {
        return true;
    }

    @Override
    public boolean supportsWebPImageFormat() {
        return supportsWebPJS();
    }

    @Override
    public GraphicsContext getGraphicsContext2D(Canvas canvas, boolean willReadFrequently) {
        return WebFxKitMapper.getGraphicsContext2D(canvas, willReadFrequently);
    }

    // HDPI management

    @Override
    public DoubleProperty canvasPixelDensityProperty(Canvas canvas) {
        String key = "webfx-canvasPixelDensityProperty";
        DoubleProperty canvasPixelDensityProperty = (DoubleProperty) canvas.getProperties().get(key);
        if (canvasPixelDensityProperty == null) {
            canvas.getProperties().put(key, canvasPixelDensityProperty = new SimpleDoubleProperty(getDefaultCanvasPixelDensity()));
            // Applying an immediate mapping between the JavaFX and HTML canvas, otherwise the default behavior of the
            // WebFX mapper (which is to postpone and process the mapping in the next animation frame) wouldn't work for
            // canvas. The application will indeed probably draw in the canvas just after it is initialized (and sized).
            // If we were to wait for the mapper to resize the canvas in the next animation frame, it would be too late.
            HTMLCanvasElement canvasElement = (HTMLCanvasElement) ((HtmlNodePeer) canvas.getOrCreateAndBindNodePeer()).getElement();
            FXProperties.runNowAndOnPropertiesChange(() ->
                    CanvasElementHelper.resizeCanvasElement(canvasElement, canvas),
                canvas.widthProperty(), canvas.heightProperty(), canvasPixelDensityProperty);

        }
        return canvasPixelDensityProperty;
    }

    private final HTMLCanvasElement MEASURE_CANVAS_ELEMENT = HtmlUtil.createElement("canvas");

    @Override
    public Bounds measureText(String text, Font font) {
        CanvasRenderingContext2D context = Context2DHelper.getCanvasContext2D(MEASURE_CANVAS_ELEMENT);
        context.font = HtmlFonts.getHtmlFontDefinition(font);
        TextMetrics textMetrics = context.measureText(text);
        JsPropertyMap<?> tm = Js.asPropertyMap(textMetrics);
        return new BoundingBox(0, 0, textMetrics.width,(double) tm.get("actualBoundingBoxAscent") + (double) tm.get("actualBoundingBoxDescent"));
    }

    private static final HTMLElement baselineSample = HtmlUtil.createSpanElement();
    private static final HTMLElement baselineLocator = HtmlUtil.createImageElement();

    static {
        baselineSample.appendChild(DomGlobal.document.createTextNode("Baseline text"));
        baselineSample.style.position = "absolute";
        baselineSample.style.lineHeight = CSSProperties.LineHeightUnionType.of("100%");
        baselineLocator.style.verticalAlign = "baseline";
        baselineSample.appendChild(baselineLocator);
    }

    @Override
    public double measureBaselineOffset(Font font) {
        if (font == null)
            return 0;
        if (!font.isBaselineOffsetSet()) {
            HtmlFonts.setHtmlFontStyleAttributes(font, baselineSample);
            DomGlobal.document.body.appendChild(baselineSample);
            double top = baselineSample.getBoundingClientRect().top;
            double baseLineY = baselineLocator.getBoundingClientRect().bottom;
            DomGlobal.document.body.removeChild(baselineSample);
            double baselineOffset = baseLineY - top;
            font.setBaselineOffset(baselineOffset);
        }
        return font.getBaselineOffset();
    }

    @Override
    public ObservableList<Font> loadingFonts() {
        return Font.getLoadingFonts();
    }

    private static boolean supportsWebPJS() {
        // Check FF, Edge by user agent
        if (UserAgent.isFireFox())
            return UserAgent.getBrowserMajorVersion() >= 65;
        if (UserAgent.isEdge())
            return UserAgent.getBrowserMajorVersion() >= 18;
        // Use canvas hack for webkit-based browsers
        HTMLCanvasElement e = (HTMLCanvasElement) DomGlobal.document.createElement("canvas");
        return Js.asPropertyMap(e).has("toDataURL") && e.toDataURL("image/webp").startsWith("data:image/webp");
    }

    @Override
    public void launchApplication(Factory<Application> applicationFactory, String... args) {
        application = applicationFactory.create();
        if (application != null)
            try {
                ParametersImpl.registerParameters(application, new ParametersImpl(args));
                application.init();
                application.start(getPrimaryStage());
            } catch (Exception e) {
                Console.log("Error while launching the JavaFX application", e);
            }
    }

    @Override
    public Application getApplication() {
        return application;
    }

    private ObjectProperty<Insets> safeAreaInsetsProperty = null;

    @Override
    public ReadOnlyObjectProperty<Insets> safeAreaInsetsProperty() {
        if (safeAreaInsetsProperty == null) {
            safeAreaInsetsProperty = new SimpleObjectProperty<>(Insets.EMPTY);
            FXProperties.runNowAndOnPropertiesChange(this::updateSafeAreaInsets,
                getPrimaryStage().widthProperty(), getPrimaryStage().heightProperty());
            // Workaround for a bug observed in the Gmail internal browser on iPad where the window width/height
            // are still not final at the first opening. So we schedule a later update to get final values.
            UiScheduler.scheduleDelay(500, this::updateSafeAreaInsets); // 500 ms seem enough
        }
        return safeAreaInsetsProperty;
    }

    public void updateSafeAreaInsets() {
        /* The following code is relying on this CSS rule present in webfx-kit-javafxgraphics-web@main.css
        :root {
            --safe-area-inset-top:    env(safe-area-inset-top);
            --safe-area-inset-right:  env(safe-area-inset-right);
            --safe-area-inset-bottom: env(safe-area-inset-bottom);
            --safe-area-inset-left:   env(safe-area-inset-left);
        }
         */
        CSSStyleDeclaration computedStyle = Js.<ViewCSS>uncheckedCast(DomGlobal.window).getComputedStyle(DomGlobal.document.documentElement);
        String top = computedStyle.getPropertyValue("--safe-area-inset-top");
        String right = computedStyle.getPropertyValue("--safe-area-inset-right");
        String bottom = computedStyle.getPropertyValue("--safe-area-inset-bottom");
        String left = computedStyle.getPropertyValue("--safe-area-inset-left");
        safeAreaInsetsProperty.set(new Insets(
            Numbers.doubleValue(Strings.removeSuffix(top, "px")),
            Numbers.doubleValue(Strings.removeSuffix(right, "px")),
            Numbers.doubleValue(Strings.removeSuffix(bottom, "px")),
            Numbers.doubleValue(Strings.removeSuffix(left, "px"))
        ));
    }

    @Override
    public boolean isFullscreenEnabled() {
        return DomGlobal.document.fullscreenEnabled;
    }

    private Node fullscreenNode;
    private boolean fullscreenNodeWasManaged;

    @Override
    public boolean requestNodeFullscreen(Node node) {
        if (!isFullscreenEnabled())
            return false;
        NodePeer<?> nodePeer = node == null ? null : node.getOrCreateAndBindNodePeer();
        if (nodePeer instanceof HtmlNodePeer<?, ?, ?> htmlNodePeer) {
            fullscreenNode = node;
            htmlNodePeer.getElement().requestFullscreen();
            return true;
        }
        return false;
    }

    @Override
    public boolean exitFullscreen() {
        if (DomGlobal.document.fullscreenElement == null && !getPrimaryStage().isFullScreen())
            return false;
        DomGlobal.document.exitFullscreen();
        return true;
    }

    {
        // Additional fullscreen management, as the 2 above methods are only covering programmatic calls, but the user
        // can also exit the fullscreen by other means such as the ESC button. So we detect here all fullscreen events
        // (entering or exiting) whatever their origin (programmatic calls or not). This is where we set the fullscreen
        // JavaFX property and manage the behavior about the JavaFX node being fullscreen.
        DomGlobal.document.addEventListener("fullscreenchange", e -> {
                Element fullscreenElement = DomGlobal.document.fullscreenElement;
                boolean fullscreen = fullscreenElement != null;
                // Setting the fullscreen JavaFX property
                Stage primaryStage = getPrimaryStage();
                primaryStage.fullScreenPropertyImpl().set(fullscreen);
                // Managing the fullscreen JavaFX node
                if (fullscreenNode != null) {
                    if (fullscreen) {
                        // The HTML element is now fullscreen, but JavaFX is not yet aware of that, so if we don't do
                        // anything special, it will continue resizing the node in the original scene graph on the next
                        // layout pass. To prevent this, we force the node to be unmanaged while it is fullscreen.
                        fullscreenNodeWasManaged = fullscreenNode.isManaged(); // to restore it when exiting fullscreen
                        fullscreenNode.setManaged(false); // asking its parent to keep it untouched during layout
                        // Now that it is unmanaged, we are responsible for resizing it to the fullscreen dimensions.
                        // In theory, we could use fullscreenElement.clientWidth and clientHeight to get that dimension,
                        // but it has been observed (on Chrome) that fullscreenElement.clientHeight is not returning the
                        // correct value for some reason, so we use the screen bounds instead.
                        // In addition, on mobiles, the screen bounds may change if the user switches from portrait to
                        // landscape or vice versa, so we need to do a binding to keep JavaFX dimensions correct.
                        if (fullscreenNode instanceof Region fullscreenRegion /* very likely a region */) {
                            // JavaFX doesn't provide Screen.boundsProperty() so we use the primary stage bounds instead,
                            // assuming the stage is always fullscreen (which should be the case during this fullscreen
                            // mode).
                            fullscreenRegion.widthPropertyImpl().bind(primaryStage.widthProperty());
                            fullscreenRegion.heightPropertyImpl().bind(primaryStage.heightProperty());
                        } else { // Desktops
                            Rectangle2D screenBounds = Screen.getPrimary().getBounds();
                            fullscreenNode.resize(screenBounds.getWidth(), screenBounds.getHeight());
                        }
                    } else {
                        // Re-establishing the previous state when it "comes back" to the normal scene graph
                        if (fullscreenNode instanceof Region fullscreenRegion /* very likely a region */) {
                            // Unbinding the JavaFX dimensions that we forced when entering fullscreen
                            fullscreenRegion.widthPropertyImpl().unbind();
                            fullscreenRegion.heightPropertyImpl().unbind();
                        }
                        // Re-establishing the previous managed state
                        fullscreenNode.setManaged(fullscreenNodeWasManaged);
                        // Now we can forget the node
                        fullscreenNode = null;
                    }
                }
            }
        );

        // PWA callbacks
        Elemental2Util.setPromptPwaInstallReadyCallback(appInstallPromptReadyProperty::set);
        Elemental2Util.setPwaInstalledCallback(appInstalledProperty::set);
    }

    @Override
    public boolean supportsAppInstall() {
        return Elemental2Util.supportsPwa();
    }

    @Override
    public void promptAppInstall() {
        Elemental2Util.promptPwaInstall();
    }

    @Override
    public boolean isRunningAsInstalledApp() {
        return Elemental2Util.isRunningAsPWA();
    }
}