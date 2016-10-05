package mongoose.entities;

import mongoose.entities.markers.EntityHasItem;
import mongoose.entities.markers.EntityHasSite;
import naga.framework.orm.entity.Entity;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public interface Rate extends Entity, EntityHasSite, EntityHasItem {

    default void setStartDate(LocalDate startDate) {
        setFieldValue("startDate", startDate);
    }

    default LocalDate getStartDate() {
        return getLocalDateFieldValue("startDate");
    }

    default void setEndDate(LocalDate endDate) {
        setFieldValue("endDate", endDate);
    }

    default LocalDate getEndDate() {
        return getLocalDateFieldValue("endDate");
    }

    default void setMinDay(Integer minDay) {
        setFieldValue("minDay", minDay);
    }

    default Integer getMinDay() {
        return getIntegerFieldValue("minDay");
    }

    default void setMaxDay(Integer maxDay) {
        setFieldValue("maxDay", maxDay);
    }

    default Integer getMaxDay() {
        return getIntegerFieldValue("maxDay");
    }

    default void setPerDay(Boolean perDay) {
        setFieldValue("perDay", perDay);
    }

    default Boolean isPerDay() {
        return getBooleanFieldValue("perDay");
    }

    default void setPrice(Integer price) {
        setFieldValue("price", price);
    }

    default Integer getPrice() {
        return getIntegerFieldValue("price");
    }

}
