package mongooses.core.activities.sharedends.logic.ui.calendargraphic.impl;

import mongooses.core.activities.sharedends.logic.ui.calendargraphic.HasDayTimeMinuteInterval;

/**
 * @author Bruno Salmon
 */
interface VerticalDayTimePositioned extends HasDayTimeMinuteInterval {

    void setYAndHeight(double y, double height);
}
