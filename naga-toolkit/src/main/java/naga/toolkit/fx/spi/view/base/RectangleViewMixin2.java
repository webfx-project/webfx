package naga.toolkit.fx.spi.view.base;

/**
 * @author Bruno Salmon
 */
public interface RectangleViewMixin2 extends RectangleViewMixin {

    default void updateX(Double x) {}

    default void updateY(Double y) {}

    default void updateWidth(Double width) {}

    default void updateHeight(Double height) {}

    default void updateArcWidth(Double arcWidth) {}

    default void updateArcHeight(Double arcHeight) {}
}
