package dev.webfx.kit.webgl.spi.impl.gwtj2cl;

import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.CanvasElementHelper;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.HtmlCanvasPeer;
import dev.webfx.kit.webgl.spi.WebGLProviderBase;
import elemental2.dom.HTMLCanvasElement;
import elemental2.webgl.WebGLRenderingContext;
import javafx.scene.canvas.Canvas;
import jsinterop.base.Js;

/**
 * @author Bruno Salmon
 */
public class GwtJ2clWebGLProvider extends WebGLProviderBase {

    @Override
    public dev.webfx.kit.webgl.WebGLRenderingContext getWebGLContext(Canvas canvas) {
        return getContext(canvas, "webgl");
    }

    private static dev.webfx.kit.webgl.WebGLRenderingContext getContext(Canvas canvas, String webglContextName) {
        String key = "webfx-" + webglContextName + "-context";
        Object gl = canvas.getProperties().get(key);
        if (gl instanceof dev.webfx.kit.webgl.WebGLRenderingContext)
            return (dev.webfx.kit.webgl.WebGLRenderingContext) gl;
        WebGLRenderingContext jsContext = getJsWebGLRenderingContext(canvas, webglContextName);
        if (jsContext == null)
            return null;
        dev.webfx.kit.webgl.WebGLRenderingContext gln = new GwtJ2clWebGLRenderingContext(jsContext);
        canvas.getProperties().put(key, gln);
        return gln;
    }

    private static WebGLRenderingContext getJsWebGLRenderingContext(Canvas canvas, String webglContextName) {
        NodePeer canvasPeer = canvas.getOrCreateAndBindNodePeer();
        if (canvasPeer instanceof HtmlCanvasPeer) {
            HTMLCanvasElement canvasElement = (HTMLCanvasElement) ((HtmlCanvasPeer) canvasPeer).getElement();
            CanvasElementHelper.resizeCanvasElement(canvasElement, canvas);
            return Js.cast(canvasElement.getContext(webglContextName));
        }
        return null;
    }

}
