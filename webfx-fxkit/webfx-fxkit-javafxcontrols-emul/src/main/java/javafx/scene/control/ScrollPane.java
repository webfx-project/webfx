package javafx.scene.control;

import com.sun.javafx.scene.control.behaviour.ScrollPaneBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.Node;
import webfx.javafxcontrols.registry.JavaFxControlsRegistry;

/**
 * @author Bruno Salmon
 */
public class ScrollPane extends Control {

    private Runnable onChildrenLayout;

    public ScrollPane() {
        // The purpose of this code is to register the mouse handler so it captures focus on mouse click
        new BehaviorSkinBase<ScrollPane, ScrollPaneBehavior>(this, new ScrollPaneBehavior(this)) {};
    }

    public ScrollPane(Node content) {
        this();
        setContent(content);
    }

    @Override
    protected void sceneToLocal(com.sun.javafx.geom.Point2D pt) {
        super.sceneToLocal(pt);
        Bounds viewportBounds = getViewportBounds();
        pt.x += viewportBounds.getMinX();
        pt.y += viewportBounds.getMinY();
    }

    @Override
    protected void localToScene(com.sun.javafx.geom.Point2D pt) {
        Bounds viewportBounds = getViewportBounds();
        pt.x -= viewportBounds.getMinX();
        pt.y -= viewportBounds.getMinY();
        super.localToScene(pt);
    }

    private Property<ScrollBarPolicy> hbarPolicyProperty = new SimpleObjectProperty<>(ScrollBarPolicy.AS_NEEDED);

    public Property<ScrollBarPolicy> hbarPolicyProperty() {
        return hbarPolicyProperty;
    }

    public final void setHbarPolicy(ScrollBarPolicy value) {
        hbarPolicyProperty.setValue(value);
    }

    public ScrollBarPolicy getHbarPolicy() {
        return hbarPolicyProperty.getValue();
    }

    private Property<ScrollBarPolicy> vbarPolicyProperty = new SimpleObjectProperty<>(ScrollBarPolicy.AS_NEEDED);

    public Property<ScrollBarPolicy> vbarPolicyProperty() {
        return vbarPolicyProperty;
    }

    public final void setvbarPolicy(ScrollBarPolicy value) {
        vbarPolicyProperty.setValue(value);
    }

    public ScrollBarPolicy getvbarPolicy() {
        return vbarPolicyProperty.getValue();
    }
    /**
     * The node used as the content of this ScrollPane.
     */
    private ObjectProperty<Node> content;

    public final void setContent(Node value) {
        contentProperty().set(value);
        getChildren().setAll(value);
    }

    public final Node getContent() {
        return content == null ? null : content.get();
    }

    public final ObjectProperty<Node> contentProperty() {
        if (content == null) {
            content = new SimpleObjectProperty<Node>(this, "content");
        }
        return content;
    }

    /**
     * The current horizontal scroll position of the ScrollPane. This value
     * may be set by the application to scroll the view programatically.
     * The ScrollPane will update this value whenever the viewport is
     * scrolled or panned by the user. This value must always be within
     * the range of {@link #hminProperty hmin} to {@link #hmaxProperty hmax}. When {@link #hvalueProperty hvalue}
     * equals {@link #hminProperty hmin}, the contained node is positioned so that
     * its layoutBounds {@link javafx.geometry.Bounds#getMinX minX} is visible. When {@link #hvalueProperty hvalue}
     * equals {@link #hmaxProperty hmax}, the contained node is positioned so that its
     * layoutBounds {@link javafx.geometry.Bounds#getMaxX maxX} is visible. When {@link #hvalueProperty hvalue} is between
     * {@link #hminProperty hmin} and {@link #hmaxProperty hmax}, the contained node is positioned
     * proportionally between layoutBounds {@link javafx.geometry.Bounds#getMinX minX} and
     * layoutBounds {@link javafx.geometry.Bounds#getMaxX maxX}.
     */
    private DoubleProperty hvalue;

    public final void setHvalue(double value) {
        hvalueProperty().set(value);
    }

    public final double getHvalue() {
        return hvalue == null ? 0.0 : hvalue.get();
    }

    public final DoubleProperty hvalueProperty() {
        if (hvalue == null) {
            hvalue = new SimpleDoubleProperty(this, "hvalue");
        }
        return hvalue;
    }
    /**
     * The current vertical scroll position of the ScrollPane. This value
     * may be set by the application to scroll the view programatically.
     * The ScrollPane will update this value whenever the viewport is
     * scrolled or panned by the user. This value must always be within
     * the range of {@link #vminProperty vmin} to {@link #vmaxProperty vmax}. When {@link #vvalueProperty vvalue}
     * equals {@link #vminProperty vmin}, the contained node is positioned so that
     * its layoutBounds {@link javafx.geometry.Bounds#getMinY minY} is visible. When {@link #vvalueProperty vvalue}
     * equals {@link #vmaxProperty vmax}, the contained node is positioned so that its
     * layoutBounds {@link javafx.geometry.Bounds#getMaxY maxY} is visible. When {@link #vvalueProperty vvalue} is between
     * {@link #vminProperty vmin} and {@link #vmaxProperty vmax}, the contained node is positioned
     * proportionally between layoutBounds {@link javafx.geometry.Bounds#getMinY minY} and
     * layoutBounds {@link javafx.geometry.Bounds#getMaxY maxY}.
     */
    private DoubleProperty vvalue;

    public final void setVvalue(double value) {
        vvalueProperty().set(value);
    }

    public final double getVvalue() {
        return vvalue == null ? 0.0 : vvalue.get();
    }

    public final DoubleProperty vvalueProperty() {
        if (vvalue == null) {
            vvalue = new SimpleDoubleProperty(this, "vvalue");
        }
        return vvalue;
    }

    /**
     * The minimum allowable {@link #hvalueProperty hvalue} for this ScrollPane.
     * Default value is 0.
     */
    private DoubleProperty hmin;

    public final void setHmin(double value) {
        hminProperty().set(value);
    }

    public final double getHmin() {
        return hmin == null ? 0.0F : hmin.get();
    }

    public final DoubleProperty hminProperty() {
        if (hmin == null) {
            hmin = new SimpleDoubleProperty(this, "hmin", 0.0);
        }
        return hmin;
    }
    /**
     * The minimum allowable {@link #hvalueProperty vvalue} for this ScrollPane.
     * Default value is 0.
     */
    private DoubleProperty vmin;

    public final void setVmin(double value) {
        vminProperty().set(value);
    }

    public final double getVmin() {
        return vmin == null ? 0.0F : vmin.get();
    }

    public final DoubleProperty vminProperty() {
        if (vmin == null) {
            vmin = new SimpleDoubleProperty(this, "vmin", 0.0);
        }
        return vmin;
    }
    /**
     * The maximum allowable {@link #hvalueProperty hvalue} for this ScrollPane.
     * Default value is 1.
     */
    private DoubleProperty hmax;

    public final void setHmax(double value) {
        hmaxProperty().set(value);
    }

    public final double getHmax() {
        return hmax == null ? 1.0F : hmax.get();
    }

    public final DoubleProperty hmaxProperty() {
        if (hmax == null) {
            hmax = new SimpleDoubleProperty(this, "hmax", 1.0);
        }
        return hmax;
    }
    /**
     * The maximum allowable {@link #hvalueProperty vvalue} for this ScrollPane.
     * Default value is 1.
     */
    private DoubleProperty vmax;

    public final void setVmax(double value) {
        vmaxProperty().set(value);
    }

    public final double getVmax() {
        return vmax == null ? 1.0F : vmax.get();
    }

    public final DoubleProperty vmaxProperty() {
        if (vmax == null) {
            vmax = new SimpleDoubleProperty(this, "vmax", 1.0);
        }
        return vmax;
    }

    /**
     * The actual Bounds of the ScrollPane Viewport.
     * This is the Bounds of the content node.
     */
    private ObjectProperty<Bounds> viewportBounds;

    public final void setViewportBounds(Bounds value) {
        viewportBoundsProperty().set(value);
    }

    public final Bounds getViewportBounds() {
        return viewportBounds == null ? new BoundingBox(0,0,0,0) : viewportBounds.get();
    }

    public final ObjectProperty<Bounds> viewportBoundsProperty() {
        if (viewportBounds == null) {
            viewportBounds = new SimpleObjectProperty<Bounds>(this, "viewportBounds", new BoundingBox(0,0,0,0));
        }
        return viewportBounds;
    }

    @Override
    protected void layoutChildren() {
        layoutInArea(getChildren().get(0), 0, 0, Double.MAX_VALUE, Double.MAX_VALUE, 0, Insets.EMPTY, false, false, HPos.LEFT, VPos.TOP, true);
        if (onChildrenLayout != null)
            onChildrenLayout.run();
    }

    public void setOnChildrenLayout(Runnable onChildrenLayout) {
        this.onChildrenLayout = onChildrenLayout;
    }

    /***************************************************************************
     *                                                                         *
     * Support classes                                                         *
     *                                                                         *
     **************************************************************************/

    /**
     * An enumeration denoting the policy to be used by a scrollable
     * Control in deciding whether to show a scroll bar.
     * @since JavaFX 2.0
     */
    public enum ScrollBarPolicy {
        /**
         * Indicates that a scroll bar should never be shown.
         */
        NEVER,
        /**
         * Indicates that a scroll bar should always be shown.
         */
        ALWAYS,
        /**
         * Indicates that a scroll bar should be shown when required.
         */
        AS_NEEDED
    }

    static {
        JavaFxControlsRegistry.registerScrollPane();
    }
}
