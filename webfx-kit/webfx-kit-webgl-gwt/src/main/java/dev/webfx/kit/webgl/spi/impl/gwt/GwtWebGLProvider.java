package dev.webfx.kit.webgl.spi.impl.gwt;

import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlCanvasPeer;
import dev.webfx.kit.webgl.spi.WebGLProvider;
import elemental2.dom.HTMLCanvasElement;
import elemental2.webgl.WebGLRenderingContext;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import jsinterop.base.Js;

/**
 * @author Bruno Salmon
 */
public class GwtWebGLProvider implements WebGLProvider {

    @Override
    public boolean supportsWebGL() {
        return true;
    }

    @Override
    public Canvas createWebGLNode() {
        Canvas canvas = new Canvas();
        NodePeer canvasPeer = canvas.getOrCreateAndBindNodePeer();
        if (canvasPeer instanceof HtmlCanvasPeer) {
            HTMLCanvasElement canvasElement = (HTMLCanvasElement) ((HtmlCanvasPeer) canvasPeer).getElement();
            canvasElement.setAttribute("webgl", "true"); // See HtmlGraphicsContext.updateCanvasElementSize()
        }
        return canvas;
    }

    @Override
    public Canvas createWebGLNode(double width, double height) {
        Canvas canvas = createWebGLNode();
        canvas.setWidth(width);
        canvas.setHeight(height);
        return canvas;
    }

    @Override
    public double getWebGLNodeWidth(Node webglNode) {
        if (webglNode instanceof Canvas) {
            return ((Canvas) webglNode).getWidth();
        }
        return -1;
    }

    @Override
    public double getWebGLNodeHeight(Node webglNode) {
        if (webglNode instanceof Canvas) {
            return ((Canvas) webglNode).getHeight();
        }
        return -1;
    }

    @Override
    public dev.webfx.kit.webgl.WebGLRenderingContext getContext(Node webglNode) {
        Object gl = webglNode.getProperties().get("webglContext");
        if (gl instanceof dev.webfx.kit.webgl.WebGLRenderingContext)
            return (dev.webfx.kit.webgl.WebGLRenderingContext) gl;
        if (webglNode instanceof Canvas) {
            WebGLRenderingContext jsContext = getWebGLRenderingContext((Canvas) webglNode);
            if (jsContext == null)
                return null;
            dev.webfx.kit.webgl.WebGLRenderingContext gln = new GwtWebGLRenderingContext(jsContext);
            webglNode.getProperties().put("webglContext", gln);
            return gln;
        }
        return null;
    }

    private WebGLRenderingContext getWebGLRenderingContext(Canvas canvas) {
        NodePeer canvasPeer = canvas.getOrCreateAndBindNodePeer();
        if (canvasPeer instanceof HtmlCanvasPeer) {
            HTMLCanvasElement canvasElement = (HTMLCanvasElement) ((HtmlCanvasPeer) canvasPeer).getElement();
            return Js.cast(canvasElement.getContext("webgl"));
        }
        return null;
    }

}
