package mongoose.entities.markers;

/**
 * @author Bruno Salmon
 */
public interface HasPersonDetails {

    void setFirstName(String FirstName);

    String getFirstName();

    void setLastName(String LastName);

    String getLastName();

    void setAge(Integer age);

    Integer getAge();

    void setUnemployed(Boolean unemployed);

    Boolean isUnemployed();

    void setFacilityFee(Boolean facilityFee);

    Boolean isFacilityFee();

    void setWorkingVisit(Boolean workingVisit);

    Boolean isWorkingVisit();

    void setDiscovery(Boolean discovery);

    Boolean isDiscovery();

    void setDiscoveryReduced(Boolean discoveryReduced);

    Boolean isDiscoveryReduced();

    void setGuest(Boolean guest);

    Boolean isGuest();

    void setResident(Boolean resident);

    Boolean isResident();

    void setResident2(Boolean resident2);

    Boolean isResident2();

}
