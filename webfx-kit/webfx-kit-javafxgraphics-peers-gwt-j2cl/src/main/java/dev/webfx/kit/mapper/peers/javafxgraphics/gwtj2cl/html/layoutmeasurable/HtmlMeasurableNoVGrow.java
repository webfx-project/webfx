package dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.layoutmeasurable;

/**
 * @author Bruno Salmon
 */
public interface HtmlMeasurableNoVGrow extends HtmlMeasurable {

    @Override
    default double minHeight(double width) {
        return measureHeight(width);
    }

    @Override
    default double maxHeight(double width) {
        return measureHeight(width);
    }


}
