package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.layoutmeasurable;

/**
 * @author Bruno Salmon
 */
public interface HtmlLayoutMeasurableNoVGrow extends HtmlLayoutMeasurable {

    @Override
    default double minHeight(double width) {
        return measureHeight(width);
    }

    @Override
    default double maxHeight(double width) {
        return measureHeight(width);
    }


}
