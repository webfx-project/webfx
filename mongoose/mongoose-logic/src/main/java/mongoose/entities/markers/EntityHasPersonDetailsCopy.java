package mongoose.entities.markers;

/**
 * @author Bruno Salmon
 */
public interface EntityHasPersonDetailsCopy extends EntityHasPersonDetails {

    default Object getFirstNameField() { return "person_firstName";}

    default Object getLastNameField() { return "person_lastName";}

    default Object getAgeField() { return "person_age";}

    default Object getUnemployedField() { return "person_unemployed";}

    default Object getFacilityFeeField() { return "person_facilityFee";}

    default Object getWorkingVisitField() { return "person_workingVisit";}

    default Object getDiscoveryField() { return "person_discovery";}

    default Object getDiscoveryReducedField() { return "person_discoveryReduced";}

    default Object getGuestField() { return "person_guest";}

    default Object getResidentField() { return "person_resident";}

    default Object getResident2Field() { return "person_resident2";}

}
