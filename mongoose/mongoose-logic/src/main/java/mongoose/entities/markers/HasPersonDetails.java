package mongoose.entities.markers;

import mongoose.entities.Country;
import mongoose.entities.Organization;
import naga.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface HasPersonDetails {

    void setFirstName(String firstName);

    String getFirstName();

    void setLastName(String lastName);

    String getLastName();

    void setMale(Boolean male);

    Boolean isMale();

    void setAge(Integer age);

    Integer getAge();

    void setCarer1Name(String carer1Name);

    String getCarer1Name();

    void setCarer2Name(String carer2Name);

    String getCarer2Name();

    void setEmail(String email);

    String getEmail();

    void setPhone(String phone);

    String getPhone();

    void setStreet(String street);

    String getStreet();

    void setPostCode(String postCode);

    String getPostCode();

    void setCityName(String cityName);

    String getCityName();

    void setCountryName(String countryName);

    String getCountryName();

    void setCountry(Object country);

    Country getCountry();

    EntityId getCountryId();

    void setOrganization(Object organization);

    Organization getOrganization();

    EntityId getOrganizationId();

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
