package mongoose.shared.entities.markers;

import mongoose.shared.entities.ItemFamilyType;

/**
 * @author Bruno Salmon
 */
public interface HasItemFamilyType {

    ItemFamilyType getItemFamilyType();

    default boolean isAccommodation() {
        return getItemFamilyType() == ItemFamilyType.ACCOMMODATION;
    }

    default boolean isMeals() {
        return getItemFamilyType() == ItemFamilyType.MEALS;
    }

    default boolean isDiet() {
        return getItemFamilyType() == ItemFamilyType.DIET;
    }

    default boolean isTeaching() {
        return getItemFamilyType() == ItemFamilyType.TEACHING;
    }

    default boolean isTranslation() {
        return getItemFamilyType() == ItemFamilyType.TRANSLATION;
    }

    default boolean isTransport() {
        return getItemFamilyType() == ItemFamilyType.TRANSPORT;
    }

    default boolean isTax() {
        return getItemFamilyType() == ItemFamilyType.TAX;
    }

}
