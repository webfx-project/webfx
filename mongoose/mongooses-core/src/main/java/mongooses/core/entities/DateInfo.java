package mongooses.core.entities;

import mongooses.core.entities.markers.*;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface DateInfo extends Entity, EntityHasEvent, EntityHasLabel, EntityHasDateTimeRange, EntityHasMinDateTimeRange, EntityHasMaxDateTimeRange {

    Label getFeesBottomLabel();

    Label getFeesPopupLabel();

    Boolean isForceSoldout();

}
