package dev.webfx.kit.launcher.spi.impl.gwt;

import com.sun.javafx.application.ParametersImpl;
import dev.webfx.kit.launcher.spi.FastPixelReaderWriter;
import dev.webfx.kit.launcher.spi.impl.base.WebFxKitLauncherProviderBase;
import dev.webfx.kit.mapper.WebFxKitMapper;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.CanvasElementHelper;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.Context2DHelper;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.UserInteraction;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.DragboardDataTransferHolder;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlFonts;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.util.Strings;
import dev.webfx.platform.util.collection.Collections;
import dev.webfx.platform.util.function.Factory;
import elemental2.dom.*;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

import java.util.Map;


/**
 * @author Bruno Salmon
 */
public final class GwtWebFxKitLauncherProvider extends WebFxKitLauncherProviderBase {

    private static final boolean IS_SAFARI;
    static {
        String userAgent = DomGlobal.navigator.userAgent.toLowerCase();
        IS_SAFARI = userAgent.contains("safari") && !userAgent.contains("chrome") && !userAgent.contains("android");
    }

    private Application application;
    private HostServices hostServices;

    public GwtWebFxKitLauncherProvider() {
        super(false);
    }

    @Override
    public HostServices getHostServices() {
        if (hostServices == null)
            hostServices = uri -> {
                // Note: Safari is blocking (on macOS) or ignoring (on iOS) window.open() when not called during a user
                // interaction. If we are in that case, it's better to postpone the window opening to the next user
                // interaction (which we hope will happen soon, such as a key or mouse release).
                if (IS_SAFARI && !UserInteraction.isUserInteracting()) {
                    UserInteraction.runOnNextUserInteraction(() -> {
                        DomGlobal.window.open(uri, "_blank");
                    }, true);
                } else {
                    // For other browsers, or with Safari but during a user interaction (ex: mouse click), it's ok to
                    // open the browser window straightaway.
                    DomGlobal.window.open(uri, "_blank");
                }
            };
        return hostServices;
    }

    @Override
    public Clipboard getSystemClipboard() {
        return new Clipboard() {
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
        JsClipboard.writeText(text);
    }

    private static String getClipboardContent() {
        String[] content = { null };
        JsClipboard.readText().then(text -> {
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
    public FastPixelReaderWriter getFastPixelReaderWriter(Image image) {
        return new GwtFastPixelReaderWriter(image);
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
            // Applying an immediate mapping between the JavaFX and HTML canvas, otherwise the default behaviour of the
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
        return new BoundingBox(0, 0, textMetrics.width, (double) tm.get("actualBoundingBoxAscent") + (double) tm.get("actualBoundingBoxDescent"));
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

    private static native boolean supportsWebPJS() /*-{
        // Check FF, Edge by user agent
        var m = navigator.userAgent.match(/(Edge|Firefox)\/(\d+)\./)
        if (m) {
            return (m[1] === 'Firefox' && +m[2] >= 65)
                || (m[1] === 'Edge' && +m[2] >= 18)
        }

        // Use canvas hack for webkit-based browsers
        var e = document.createElement('canvas');
        return e.toDataURL && e.toDataURL('image/webp').indexOf('data:image/webp') == 0;
    }-*/;

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
}