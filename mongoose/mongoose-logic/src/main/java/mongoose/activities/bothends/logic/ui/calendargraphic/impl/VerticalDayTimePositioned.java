package mongoose.activities.bothends.logic.ui.calendargraphic.impl;

import mongoose.activities.bothends.logic.ui.calendargraphic.HasDayTimeMinuteInterval;

/**
 * @author Bruno Salmon
 */
interface VerticalDayTimePositioned extends HasDayTimeMinuteInterval {

    void setYAndHeight(double y, double height);
}
