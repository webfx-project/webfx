package javafx.scene;

/**
 * Interface that ScrollPane is implementing in the webfx-kit-javafxcontrols-emul module, but that is already accessible
 * in the webfx-kit-javafxgraphics-emul module to manage the special case of the shift introduced by the viewport in
 * localToParent() and parentToLocal() coordinates conversion.
 *
 * @author Bruno Salmon
 */
public interface IScrollPane {

    void localContentToParentViewport(com.sun.javafx.geom.Point2D pt);

    void parentViewportToLocalContent(com.sun.javafx.geom.Point2D pt);

}
