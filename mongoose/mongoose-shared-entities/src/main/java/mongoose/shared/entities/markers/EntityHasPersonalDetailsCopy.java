package mongoose.shared.entities.markers;

/**
 * @author Bruno Salmon
 */
public interface EntityHasPersonalDetailsCopy extends EntityHasPersonalDetails {

    default Object getFirstNameField() { return "person_firstName";}

    default Object getLastNameField() { return "person_lastName";}

    default Object getLayNameField() { return "person_layName";}

    @Override
    default Object getMaleField() {
        return "person_male";
    }

    @Override
    default Object getOrdainedField() {
        return "person_ordained";
    }

    default Object getAgeField() { return "person_age";}

    @Override
    default Object getCarer1NameField() {
        return "person_carer1Name";
    }

    @Override
    default Object getCarer2NameField() {
        return "person_carer2Name";
    }

    @Override
    default Object getEmailField() {
        return "person_email";
    }

    @Override
    default Object getPhoneField() {
        return "person_phone";
    }

    @Override
    default Object getStreetField() {
        return "person_street";
    }

    @Override
    default Object getPostCodeField() {
        return "person_postCode";
    }

    @Override
    default Object getCityNameField() {
        return "person_cityName";
    }

    @Override
    default Object getAdmin1NameField() {
        return "person_admin1Name";
    }

    @Override
    default Object getAdmin2NameField() {
        return "person_admin2Name";
    }

    @Override
    default Object getCountryNameField() {
        return "person_countryName";
    }

    @Override
    default Object getCountryField() {
        return "person_country";
    }

    @Override
    default Object getOrganizationField() {
        return "person_organization";
    }

    default Object getUnemployedField() { return "person_unemployed";}

    default Object getFacilityFeeField() { return "person_facilityFee";}

    default Object getWorkingVisitField() { return "person_workingVisit";}

    default Object getDiscoveryField() { return "person_discovery";}

    default Object getDiscoveryReducedField() { return "person_discoveryReduced";}

    default Object getGuestField() { return "person_guest";}

    default Object getResidentField() { return "person_resident";}

    default Object getResident2Field() { return "person_resident2";}

}
