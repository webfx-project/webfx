package javafx.scene;

import dev.webfx.kit.mapper.peers.javafxgraphics.markers.HasAutoSizeChildrenProperty;
import dev.webfx.kit.registry.javafxgraphics.JavaFxGraphicsRegistry;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.scene.layout.LayoutFlags;

/**
 * @author Bruno Salmon
 */
public class Group extends Parent implements
        HasAutoSizeChildrenProperty {

    public Group() {
    }

    public Group(Node... nodes) {
        super(nodes);
    }

    private final BooleanProperty autoSizeChildrenProperty = new SimpleBooleanProperty(true);
    @Override
    public BooleanProperty autoSizeChildrenProperty() {
        return autoSizeChildrenProperty;
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    @Override
    protected Bounds impl_computeLayoutBounds() {
        layout(); // Needs to done prematurely, as we otherwise don't know the bounds of the children
        return super.impl_computeLayoutBounds();
    }

    /**
     * Group defines the preferred width as simply being the width of its layout bounds, which
     * in turn is simply the sum of the positions & widths of all of its children. That is,
     * the preferred width is the one that it is at, because a Group cannot be resized.
     *
     * Note: as the layout bounds in autosize Group depend on the Group to be already laid-out,
     * this call will do the layout of the Group if necessary.
     *
     * @param height This parameter is ignored by Group
     * @return The layout bounds width
     */
    @Override
    protected double impl_prefWidth(double height) {
        if (isAutoSizeChildren())
            layout();
        return super.impl_prefWidth(height);
    }

    /**
     * Group defines the preferred height as simply being the height of its layout bounds, which
     * in turn is simply the sum of the positions & heights of all of its children. That is,
     * the preferred height is the one that it is at, because a Group cannot be resized.
     *
     * Note: as the layout bounds in autosize Group depend on the Group to be already laid-out,
     * this call will do the layout of the Group if necessary.
     *
     * @param width This parameter is ignored by Group
     * @return The layout bounds height
     */
    @Override
    protected double impl_prefHeight(double width) {
        if (isAutoSizeChildren())
            layout();
        return super.impl_prefHeight(width);
    }


    @Override
    protected double impl_minHeight(double width) {
        return impl_prefHeight(width);
    }

    @Override
    protected double impl_minWidth(double height) {
        return impl_prefWidth(height);
    }

    /**
     * Group implements layoutChildren such that each child is resized to its preferred
     * size, if the child is resizable. Non-resizable children are simply left alone.
     * If {@link #autoSizeChildren} is false, then Group does nothing in this method.
     */
    @Override
    protected void layoutChildren() {
        if (isAutoSizeChildren())
            super.layoutChildren();
    }

    @Override
    public void layout() { // Non-standard JavaFX optimization: layout is disabled on groups with autoSizeChildren = false
        if (isAutoSizeChildren())
            super.layout();
        else
            setLayoutFlag(LayoutFlags.CLEAN);
    }

    static {
        JavaFxGraphicsRegistry.registerGroup();
    }
}
