package webfx.fxkit.extra.displaydata;

import webfx.fxkit.extra.displaydata.impl.DisplayStyleImpl;

/**
 * @author Bruno Salmon
 */
public interface DisplayStyle {

    Double getPrefWidth();

    String getTextAlign();

    DisplayStyle NO_STYLE = new DisplayStyleImpl();
    DisplayStyle CENTER_STYLE = new DisplayStyleImpl(null, "center");
    DisplayStyle RIGHT_STYLE = new DisplayStyleImpl(null, "right");

}
