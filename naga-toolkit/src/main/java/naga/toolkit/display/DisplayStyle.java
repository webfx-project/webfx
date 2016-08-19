package naga.toolkit.display;

import naga.toolkit.display.impl.DisplayStyleImpl;

/**
 * @author Bruno Salmon
 */
public interface DisplayStyle {

    Double getPrefWidth();

    String getTextAlign();

    DisplayStyle NO_STYLE = new DisplayStyleImpl();

}
