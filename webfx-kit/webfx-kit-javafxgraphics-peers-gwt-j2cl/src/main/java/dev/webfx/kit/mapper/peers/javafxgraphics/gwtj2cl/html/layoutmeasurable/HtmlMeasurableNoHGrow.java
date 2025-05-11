package dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.layoutmeasurable;

/**
 * @author Bruno Salmon
 */
public interface HtmlMeasurableNoHGrow extends HtmlMeasurable {

    @Override
    default double minWidth(double height) {
        return measureWidth(height);
    }

    @Override
    default double maxWidth(double height) {
        return measureWidth(height);
    }

}
