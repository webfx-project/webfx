package mongoose.entities.markers;

import naga.framework.orm.entity.Entity;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public interface EntityHasDate extends Entity, HasDate {

    @Override
    default void setDate(LocalDate date) {
        setFieldValue("date", date);
    }

    @Override
    default LocalDate getDate() {
        return getLocalDateFieldValue("date");
    }

}
