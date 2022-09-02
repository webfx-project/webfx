package dev.webfx.kit.launcher.spi.impl.gwt;

import com.google.gwt.core.client.JavaScriptObject;
import com.sun.javafx.application.ParametersImpl;
import dev.webfx.kit.launcher.spi.FastPixelReaderWriter;
import dev.webfx.kit.launcher.spi.impl.base.WebFxKitLauncherProviderBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.DragboardDataTransferHolder;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlFonts;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.util.Strings;
import dev.webfx.platform.util.collection.Collections;
import dev.webfx.platform.util.function.Factory;
import elemental2.dom.DataTransfer;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLCanvasElement;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.text.Font;
import javafx.stage.Screen;

import java.util.Map;


/**
 * @author Bruno Salmon
 */
public final class GwtWebFxKitLauncherProvider extends WebFxKitLauncherProviderBase {

    private Application application;
    private HostServices hostServices;

    public GwtWebFxKitLauncherProvider() {
        super(DomGlobal.navigator.userAgent);
    }

    @Override
    public HostServices getHostServices() {
        if (hostServices == null)
            hostServices = uri -> DomGlobal.window.open(uri);
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
        callClipboardCommand(text, "copy");
    }

    private static String getClipboardContent() {
        return callClipboardCommand(" ", "paste");
    }

    private static native String callClipboardCommand(String text, String command) /*-{
        var textArea = document.createElement('textarea');
        textArea.setAttribute('style','width:1px;border:0;opacity:0;');
        document.body.appendChild(textArea);
        textArea.value = text;
        textArea.select();
        document.execCommand(command);
        document.body.removeChild(textArea);
        return textArea.value;
    }-*/;

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
                return DragboardDataTransferHolder.getDragboardDataTransfer().getData(Collections.first(dataFormat.getIdentifiers()));
            }
        };
    }

    @Override
    public Screen getPrimaryScreen() {
        elemental2.dom.Screen screen = DomGlobal.screen;
        return Screen.from(toRectangle2D(screen.width, screen.height), toRectangle2D(screen.availWidth, screen.availHeight));
    }

    private static Rectangle2D toRectangle2D(double width, double height) {
        return new Rectangle2D(0, 0, width, height);
    }

    @Override
    public boolean supportsWebP() {
        return supportsWebPJS();
    }

    @Override
    public FastPixelReaderWriter getFastPixelReaderWriter(Image image) {
        return new GwtFastPixelReaderWriter(image);
    }

    private final HTMLCanvasElement canvas = HtmlUtil.createElement("canvas");


    @Override
    public Bounds measureText(String text, Font font) {
        JavaScriptObject textMetrics = getTextMetrics(canvas, text, HtmlFonts.getHtmlFontDefinition(font));
        return new BoundingBox(0, 0, getJsonWidth(textMetrics), getJsonHeight(textMetrics));
    }

    private native JavaScriptObject getTextMetrics(HTMLCanvasElement canvas, String text, String font)/*-{
        var context = canvas.getContext("2d");
        context.font = font;
        return { width: context.measureText(text).width, height: parseFloat(context.font.match(/\d+/)) };
    }-*/;

    private static native double getJsonWidth(JavaScriptObject json)/*-{
        return json.width;
    }-*/;

    private static native double getJsonHeight(JavaScriptObject json)/*-{
        return json.height;
    }-*/;

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