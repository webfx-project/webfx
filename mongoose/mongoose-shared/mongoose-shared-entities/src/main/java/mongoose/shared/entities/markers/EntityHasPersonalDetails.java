package mongoose.shared.entities.markers;

import mongoose.shared.entities.Country;
import mongoose.shared.entities.Organization;
import webfx.framework.shared.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface EntityHasPersonalDetails extends EntityHasOrganization, HasPersonalDetails {

    default Object getFirstNameField() { return "firstName";}

    default void setFirstName(String firstName) {
        setFieldValue(getFirstNameField(), firstName);
    }

    default String getFirstName() {
        return getStringFieldValue(getFirstNameField());
    }

    default Object getLastNameField() { return "lastName";}

    default void setLastName(String lastName) {
        setFieldValue(getLastNameField(), lastName);
    }

    default String getLastName() {
        return getStringFieldValue(getLastNameField());
    }

    default Object getLayNameField() { return "layName";}

    default void setLayName(String layName) {
        setFieldValue(getLayNameField(), layName);
    }

    default String getLayName() {
        return getStringFieldValue(getLayNameField());
    }

    default Object getMaleField() { return "male";}

    default void setMale(Boolean male) {
        setFieldValue(getMaleField(), male);
    }

    default Boolean isMale() {
        return getBooleanFieldValue(getMaleField());
    }

    default Object getOrdainedField() { return "ordained";}

    default void setOrdained(Boolean ordained) {
        setFieldValue(getOrdainedField(), ordained);
    }

    default Boolean isOrdained() {
        return getBooleanFieldValue(getOrdainedField());
    }

    default Object getAgeField() { return "age";}

    default void setAge(Integer age) {
        setFieldValue(getAgeField(), age);
    }

    default Integer getAge() {
        return getIntegerFieldValue(getAgeField());
    }

    default Object getCarer1NameField() { return "carer1Name";}

    default void setCarer1Name(String carer1Name) {
        setFieldValue(getCarer1NameField(), carer1Name);
    }

    default String getCarer1Name() {
        return getStringFieldValue(getCarer1NameField());
    }

    default Object getCarer2NameField() { return "carer2Name";}

    default void setCarer2Name(String carer2Name) {
        setFieldValue(getCarer2NameField(), carer2Name);
    }

    default String getCarer2Name() {
        return getStringFieldValue(getCarer2NameField());
    }

    default Object getEmailField() { return "email";}

    default void setEmail(String email) {
        setFieldValue(getEmailField(), email);
    }

    default String getEmail() {
        return getStringFieldValue(getEmailField());
    }

    default Object getPhoneField() { return "phone";}

    default void setPhone(String phone) {
        setFieldValue(getPhoneField(), phone);
    }

    default String getPhone() {
        return getStringFieldValue(getPhoneField());
    }

    default Object getStreetField() { return "street";}

    default void setStreet(String street) {
        setFieldValue(getStreetField(), street);
    }

    default String getStreet() {
        return getStringFieldValue(getStreetField());
    }

    default Object getPostCodeField() { return "postCode";}

    default void setPostCode(String postCode) {
        setFieldValue(getPostCodeField(), postCode);
    }

    default String getPostCode() {
        return getStringFieldValue(getPostCodeField());
    }

    default Object getCityNameField() { return "cityName";}

    default void setCityName(String cityName) {
        setFieldValue(getCityNameField(), cityName);
    }

    default String getCityName() {
        return getStringFieldValue(getCityNameField());
    }

    default Object getAdmin1NameField() { return "admin1Name";}

    default void setAdmin1Name(String admin1Name) {
        setFieldValue(getAdmin1NameField(), admin1Name);
    }

    default String getAdmin1Name() {
        return getStringFieldValue(getAdmin1NameField());
    }

    default Object getAdmin2NameField() { return "admin2Name";}

    default void setAdmin2Name(String admin2Name) {
        setFieldValue(getAdmin2NameField(), admin2Name);
    }

    default String getAdmin2Name() {
        return getStringFieldValue(getAdmin2NameField());
    }

    default Object getCountryNameField() { return "countryName";}

    default void setCountryName(String countryName) {
        setFieldValue(getCountryNameField(), countryName);
    }

    default String getCountryName() {
        return getStringFieldValue(getCountryNameField());
    }

    default Object getCountryField() { return "country";}

    default void setCountry(Object country) {
        setForeignField(getCountryField(), country);
    }

    default Country getCountry() {
        return getForeignEntity(getCountryField());
    }

    default EntityId getCountryId() {
        return getForeignEntityId(getCountryField());
    }

    default Object getOrganizationField() { return "organization";}

    default void setOrganization(Object organization) {
        setForeignField(getOrganizationField(), organization);
    }

    default Organization getOrganization() {
        return getForeignEntity(getOrganizationField());
    }

    default EntityId getOrganizationId() {
        return getForeignEntityId(getOrganizationField());
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
