package mongooses.core.shared.entities;

import mongooses.core.shared.entities.markers.EntityHasPersonDetails;
import webfx.framework.orm.entity.Entity;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public interface Person extends Entity, EntityHasPersonDetails {

    default Object getBirthDateField() { return "birthdate";}

    default void setBirthDate(LocalDate birthDate) {
        setFieldValue(getBirthDateField(), birthDate);
    }

    default LocalDate getBirthDate() {
        return getLocalDateFieldValue(getBirthDateField());
    }


}
