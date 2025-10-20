package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.HTMLCanvasElement;
import jsinterop.base.JsPropertyMap;

/**
 * @author Bruno Salmon
 */
public final class Context2DHelper {

    private static final JsPropertyMap<?> WILL_READ_FREQUENTLY_OPTION = JsPropertyMap.of("willReadFrequently", true);

    public static CanvasRenderingContext2D getCanvasContext2D(HTMLCanvasElement canvasElement) {
        return getCanvasContext2D(canvasElement, false);
    }

    public static CanvasRenderingContext2D getCanvasContext2D(HTMLCanvasElement canvasElement, boolean willReadFrequently) {
        // Note that canvasElement.getContext("2d") always returns the same context instance for the same canvasElement
        // instance (the context instance is created on first call). This means that the option willReadFrequently will
        // be considered only on first call, and then ignored on subsequent calls. So if the application code wants to
        // set willReadFrequently = true to a canvas context, the first thing it should do is to instantiate that
        // context with willReadFrequently = true using the WebFX API. Then all subsequent calls to get that canvas
        // context will return that instance with willReadFrequently set to true, even if willReadFrequently is false
        // in those subsequent calls (such as standard JavaFX calls to canvas.getGraphicsContext2D()).
        return (CanvasRenderingContext2D) (Object) canvasElement.getContext("2d", willReadFrequently ? WILL_READ_FREQUENTLY_OPTION : null);
    }

}
