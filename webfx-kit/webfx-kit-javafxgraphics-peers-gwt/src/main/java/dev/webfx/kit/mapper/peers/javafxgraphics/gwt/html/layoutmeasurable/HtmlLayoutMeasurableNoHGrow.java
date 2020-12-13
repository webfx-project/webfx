package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable;

/**
 * @author Bruno Salmon
 */
public interface HtmlLayoutMeasurableNoHGrow extends HtmlLayoutMeasurable {

    @Override
    default double minWidth(double height) {
        return measureWidth(height);
    }

    @Override
    default double maxWidth(double height) {
        return measureWidth(height);
    }

}
