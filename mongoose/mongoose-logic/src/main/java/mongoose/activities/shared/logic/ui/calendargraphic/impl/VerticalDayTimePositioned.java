package mongoose.activities.shared.logic.ui.calendargraphic.impl;

import mongoose.activities.shared.logic.ui.calendargraphic.HasDayTimeMinuteInterval;

/**
 * @author Bruno Salmon
 */
interface VerticalDayTimePositioned extends HasDayTimeMinuteInterval {

    void setYAndHeight(double y, double height);
}
