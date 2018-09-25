package mongooses.core.sharedends.logic.ui.calendargraphic.impl;

import mongooses.core.sharedends.logic.ui.calendargraphic.HasEpochDay;

/**
 * @author Bruno Salmon
 */
interface HorizontalDayPositioned extends HasEpochDay {

    void setXAndWidth(double x, double width);
}
