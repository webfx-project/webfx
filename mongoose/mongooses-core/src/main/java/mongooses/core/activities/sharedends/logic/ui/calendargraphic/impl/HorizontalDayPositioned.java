package mongooses.core.activities.sharedends.logic.ui.calendargraphic.impl;

import mongooses.core.activities.sharedends.logic.ui.calendargraphic.HasEpochDay;

/**
 * @author Bruno Salmon
 */
interface HorizontalDayPositioned extends HasEpochDay {

    void setXAndWidth(double x, double width);
}
