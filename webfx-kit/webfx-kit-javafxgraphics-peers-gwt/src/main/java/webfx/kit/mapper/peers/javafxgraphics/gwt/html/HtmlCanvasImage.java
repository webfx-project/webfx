package webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.HTMLCanvasElement;
import javafx.scene.image.WritableImage;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;

/**
 * @author Bruno Salmon
 */
final class HtmlCanvasImage extends WritableImage {

    private final HTMLCanvasElement snapshotCanvasElement;

    HtmlCanvasImage(HTMLCanvasElement sourceCanvasElement) {
        super(null, sourceCanvasElement.width, sourceCanvasElement.height, false, false, false);
        snapshotCanvasElement = copyCanvas(sourceCanvasElement);
    }

    static HTMLCanvasElement copyCanvas(HTMLCanvasElement canvasSource) {
        HTMLCanvasElement canvasCopy = HtmlUtil.createElement("canvas");
        int width = canvasSource.width;
        int height = canvasSource.height;
        canvasCopy.width = width;
        canvasCopy.height = height;
        if (width > 0 && height > 0) { // Checking size because drawImage is raising an exception on zero sized canvas
            CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) (Object) canvasCopy.getContext("2d");
            ctx.drawImage(canvasSource, 0, 0);
        }
        return canvasCopy;
    }

    public HTMLCanvasElement getSnapshotCanvasElement() {
        return snapshotCanvasElement;
    }
}
