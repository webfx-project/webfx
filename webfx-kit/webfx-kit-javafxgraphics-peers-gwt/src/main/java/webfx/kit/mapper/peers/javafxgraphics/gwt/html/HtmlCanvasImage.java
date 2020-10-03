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
        super(null, sourceCanvasElement.clientWidth, sourceCanvasElement.clientHeight, false, false, false);
        snapshotCanvasElement = HtmlUtil.createElement("canvas");
        snapshotCanvasElement.width = sourceCanvasElement.clientWidth;
        snapshotCanvasElement.height = sourceCanvasElement.clientHeight;
        CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) (Object) snapshotCanvasElement.getContext("2d");
        ctx.drawImage(sourceCanvasElement, 0, 0);
    }

    public HTMLCanvasElement getSnapshotCanvasElement() {
        return snapshotCanvasElement;
    }
}
