package naga.fxdata.displaydata;

import naga.fxdata.displaydata.impl.DisplayStyleImpl;

/**
 * @author Bruno Salmon
 */
public interface DisplayStyle {

    Double getPrefWidth();

    String getTextAlign();

    DisplayStyle NO_STYLE = new DisplayStyleImpl();
    DisplayStyle CENTER_STYLE = new DisplayStyleImpl(null, "center");

}
