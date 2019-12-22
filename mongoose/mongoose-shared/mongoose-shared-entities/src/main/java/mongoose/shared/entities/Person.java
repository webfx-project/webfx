package mongoose.shared.entities;

import mongoose.shared.entities.markers.EntityHasPersonalDetails;
import webfx.framework.shared.orm.entity.Entity;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public interface Person extends Entity, EntityHasPersonalDetails {

    default Object getBirthDateField() { return "birthdate";}

    default void setBirthDate(LocalDate birthDate) {
        setFieldValue(getBirthDateField(), birthDate);
    }

    default LocalDate getBirthDate() {
        return getLocalDateFieldValue(getBirthDateField());
    }


}
