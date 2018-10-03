package mongoose.shared.entities;

import mongoose.shared.entities.markers.EntityHasArrivalSiteAndItem;
import webfx.framework.shared.orm.entity.Entity;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public interface Rate extends Entity, EntityHasArrivalSiteAndItem {

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

    default void setAge1Max(Integer age1Max) {
        setFieldValue("age1_max", age1Max);
    }

    default Integer getAge1Max() {
        return getIntegerFieldValue("age1_max");
    }

    default void setAge1Price(Integer price) {
        setFieldValue("age1_price", price);
    }

    default Integer getAge1Price() {
        return getIntegerFieldValue("age1_price");
    }

    default void setAge1Discount(Integer discount) {
        setFieldValue("age1_discount", discount);
    }

    default Integer getAge1Discount() {
        return getIntegerFieldValue("age1_discount");
    }

    default void setAge2Max(Integer age2Max) {
        setFieldValue("age2_max", age2Max);
    }

    default Integer getAge2Max() {
        return getIntegerFieldValue("age2_max");
    }

    default void setAge2Price(Integer price) {
        setFieldValue("age2_price", price);
    }

    default Integer getAge2Price() {
        return getIntegerFieldValue("age2_price");
    }

    default void setAge2Discount(Integer discount) {
        setFieldValue("age2_discount", discount);
    }

    default Integer getAge2Discount() {
        return getIntegerFieldValue("age2_discount");
    }

    default void setAge3Max(Integer age3Max) {
        setFieldValue("age3_max", age3Max);
    }

    default Integer getAge3Max() {
        return getIntegerFieldValue("age3_max");
    }

    default void setAge3Price(Integer price) {
        setFieldValue("age3_price", price);
    }

    default Integer getAge3Price() {
        return getIntegerFieldValue("age3_price");
    }

    default void setAge3Discount(Integer discount) {
        setFieldValue("age3_discount", discount);
    }

    default Integer getAge3Discount() {
        return getIntegerFieldValue("age3_discount");
    }

    default void setWorkingVisitPrice(Integer price) {
        setFieldValue("workingVisit_price", price);
    }

    default Integer getWorkingVisitPrice() {
        return getIntegerFieldValue("workingVisit_price");
    }

    default void setWorkingVisitDiscount(Integer discount) {
        setFieldValue("workingVisit_discount", discount);
    }

    default Integer getWorkingVisitDiscount() {
        return getIntegerFieldValue("workingVisit_discount");
    }

    default void setGuestPrice(Integer price) {
        setFieldValue("guest_price", price);
    }

    default Integer getGuestPrice() {
        return getIntegerFieldValue("guest_price");
    }

    default void setGuestDiscount(Integer discount) {
        setFieldValue("guest_discount", discount);
    }

    default Integer getGuestDiscount() {
        return getIntegerFieldValue("guest_discount");
    }

    default void setResidentPrice(Integer price) {
        setFieldValue("resident_price", price);
    }

    default Integer getResidentPrice() {
        return getIntegerFieldValue("resident_price");
    }

    default void setResidentDiscount(Integer discount) {
        setFieldValue("resident_discount", discount);
    }

    default Integer getResidentDiscount() {
        return getIntegerFieldValue("resident_discount");
    }

    default void setResident2Price(Integer price) {
        setFieldValue("resident2_price", price);
    }

    default Integer getResident2Price() {
        return getIntegerFieldValue("resident2_price");
    }

    default void setResident2Discount(Integer discount) {
        setFieldValue("resident2_discount", discount);
    }

    default Integer getResident2Discount() {
        return getIntegerFieldValue("resident2_discount");
    }

    default void setDiscoveryPrice(Integer price) {
        setFieldValue("discovery_price", price);
    }

    default Integer getDiscoveryPrice() {
        return getIntegerFieldValue("discovery_price");
    }

    default void setDiscoveryDiscount(Integer discount) {
        setFieldValue("discovery_discount", discount);
    }

    default Integer getDiscoveryDiscount() {
        return getIntegerFieldValue("discovery_discount");
    }

    default void setDiscoveryReducedPrice(Integer price) {
        setFieldValue("discoveryReduced_price", price);
    }

    default Integer getDiscoveryReducedPrice() {
        return getIntegerFieldValue("discoveryReduced_price");
    }

    default void setDiscoveryReducedDiscount(Integer discount) {
        setFieldValue("discoveryReduced_discount", discount);
    }

    default Integer getDiscoveryReducedDiscount() {
        return getIntegerFieldValue("discoveryReduced_discount");
    }

    default void setUnemployedPrice(Integer price) {
        setFieldValue("unemployed_price", price);
    }

    default Integer getUnemployedPrice() {
        return getIntegerFieldValue("unemployed_price");
    }

    default void setUnemployedDiscount(Integer discount) {
        setFieldValue("unemployed_discount", discount);
    }

    default Integer getUnemployedDiscount() {
        return getIntegerFieldValue("unemployed_discount");
    }

    default void setFacilityFeePrice(Integer price) {
        setFieldValue("facilityFee_price", price);
    }

    default Integer getFacilityFeePrice() {
        return getIntegerFieldValue("facilityFee_price");
    }

    default void setFacilityFeeDiscount(Integer discount) {
        setFieldValue("facilityFee_discount", discount);
    }

    default Integer getFacilityFeeDiscount() {
        return getIntegerFieldValue("facilityFee_discount");
    }
}
