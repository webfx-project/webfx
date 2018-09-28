package mongoose.client.calendargraphic.impl;

import mongoose.client.calendargraphic.HasEpochDay;

/**
 * @author Bruno Salmon
 */
interface HorizontalDayPositioned extends HasEpochDay {

    void setXAndWidth(double x, double width);
}
