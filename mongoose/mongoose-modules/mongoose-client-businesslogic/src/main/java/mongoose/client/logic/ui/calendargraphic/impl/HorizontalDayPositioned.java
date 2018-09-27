package mongoose.client.logic.ui.calendargraphic.impl;

import mongoose.client.logic.ui.calendargraphic.HasEpochDay;

/**
 * @author Bruno Salmon
 */
interface HorizontalDayPositioned extends HasEpochDay {

    void setXAndWidth(double x, double width);
}
