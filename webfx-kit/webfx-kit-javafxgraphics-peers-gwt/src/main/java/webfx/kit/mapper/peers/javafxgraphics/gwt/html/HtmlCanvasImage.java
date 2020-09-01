package webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import javafx.scene.image.WritableImage;

/**
 * @author Bruno Salmon
 */
final class HtmlCanvasImage extends WritableImage {

    private final HtmlCanvasPeer htmlCanvasPeer;

    HtmlCanvasImage(HtmlCanvasPeer htmlCanvasPeer) {
        super(null, htmlCanvasPeer.getElement().clientWidth, htmlCanvasPeer.getElement().clientHeight, false, false, false);
        this.htmlCanvasPeer = htmlCanvasPeer;
    }

    public HtmlCanvasPeer getHtmlCanvasPeer() {
        return htmlCanvasPeer;
    }
}
