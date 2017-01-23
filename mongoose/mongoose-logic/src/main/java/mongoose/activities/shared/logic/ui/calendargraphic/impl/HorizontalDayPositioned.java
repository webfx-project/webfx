package mongoose.activities.shared.logic.ui.calendargraphic.impl;

import mongoose.activities.shared.logic.ui.calendargraphic.HasEpochDay;

/**
 * @author Bruno Salmon
 */
interface HorizontalDayPositioned extends HasEpochDay {

    void setXAndWidth(double x, double width);
}
