package mongoose.entities.markers;

import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface EntityHasPersonDetails extends Entity, HasPersonDetails {

    default Object getFirstNameField() { return "firstName";}

    default void setFirstName(String FirstName) {
        setFieldValue(getFirstNameField(), FirstName);
    }

    default String getFirstName() {
        return getStringFieldValue(getFirstNameField());
    }

    default Object getLastNameField() { return "lastName";}

    default void setLastName(String LastName) {
        setFieldValue(getLastNameField(), LastName);
    }

    default String getLastName() {
        return getStringFieldValue(getLastNameField());
    }

    default Object getAgeField() { return "age";}

    default void setAge(Integer age) {
        setFieldValue(getAgeField(), age);
    }

    default Integer getAge() {
        return getIntegerFieldValue(getAgeField());
    }

    default Object getUnemployedField() { return "unemployed";}

    default void setUnemployed(Boolean unemployed) {
        setFieldValue(getUnemployedField(), unemployed);
    }

    default Boolean isUnemployed() {
        return getBooleanFieldValue(getUnemployedField());
    }

    default Object getFacilityFeeField() { return "facilityFee";}

    default void setFacilityFee(Boolean facilityFee) {
        setFieldValue(getFacilityFeeField(), facilityFee);
    }

    default Boolean isFacilityFee() {
        return getBooleanFieldValue(getFacilityFeeField());
    }

    default Object getWorkingVisitField() { return "workingVisit";}

    default void setWorkingVisit(Boolean workingVisit) {
        setFieldValue(getWorkingVisitField(), workingVisit);
    }

    default Boolean isWorkingVisit() {
        return getBooleanFieldValue(getWorkingVisitField());
    }

    default Object getDiscoveryField() { return "discovery";}

    default void setDiscovery(Boolean discovery) {
        setFieldValue(getDiscoveryField(), discovery);
    }

    default Boolean isDiscovery() {
        return getBooleanFieldValue(getDiscoveryField());
    }

    default Object getDiscoveryReducedField() { return "discoveryReduced";}

    default void setDiscoveryReduced(Boolean discoveryReduced) {
        setFieldValue(getDiscoveryReducedField(), discoveryReduced);
    }

    default Boolean isDiscoveryReduced() {
        return getBooleanFieldValue(getDiscoveryReducedField());
    }

    default Object getGuestField() { return "guest";}

    default void setGuest(Boolean guest) {
        setFieldValue(getGuestField(), guest);
    }

    default Boolean isGuest() {
        return getBooleanFieldValue(getGuestField());
    }

    default Object getResidentField() { return "resident";}

    default void setResident(Boolean resident) {
        setFieldValue(getResidentField(), resident);
    }

    default Boolean isResident() {
        return getBooleanFieldValue(getResidentField());
    }

    default Object getResident2Field() { return "resident2";}

    default void setResident2(Boolean resident2) {
        setFieldValue(getResident2Field(), resident2);
    }

    default Boolean isResident2() {
        return getBooleanFieldValue(getResident2Field());
    }

}
