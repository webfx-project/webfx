package webfx.extras.visual;

import webfx.extras.visual.impl.VisualStyleImpl;

/**
 * @author Bruno Salmon
 */
public interface VisualStyle {

    Double getPrefWidth();

    String getTextAlign();

    VisualStyle NO_STYLE = new VisualStyleImpl();
    VisualStyle CENTER_STYLE = new VisualStyleImpl(null, "center");
    VisualStyle RIGHT_STYLE = new VisualStyleImpl(null, "right");

}
