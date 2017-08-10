package mongoose.entities;

import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.activities.shared.logic.time.TimeInterval;
import mongoose.entities.markers.*;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;

import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public interface Option extends Entity,
        EntityHasParent<Option>,
        EntityHasEvent,
        EntityHasName,
        EntityHasLabel,
        EntityHasSiteAndItem,
        HasItemFamilyType,
        EntityHasDateTimeRange {

    //// Domain fields

    default void setForceSoldout(Boolean forceSoldout) {
        setFieldValue("forceSoldout", forceSoldout);
    }

    default Boolean isForceSoldout() {
        return getBooleanFieldValue("forceSoldout");
    }

    default void setTimeRange(String timeRange) {
        setFieldValue("timeRange", timeRange);
    }

    default String getTimeRange() {
        return getStringFieldValue("timeRange");
    }

    default void setFolder(Boolean folder) {
        setFieldValue("folder", folder);
    }

    default Boolean isFolder() {
        return getBooleanFieldValue("folder");
    }

    default void setObligatory(Boolean obligatory) {
        setFieldValue("obligatory", obligatory);
    }

    default Boolean isObligatory() {
        return getBooleanFieldValue("obligatory");
    }

    default void setItemFamily(Object family) {
        setForeignField("itemFamily", family);
    }

    default EntityId getItemFamilyId() {
        return getForeignEntityId("itemFamily");
    }

    default ItemFamily getItemFamily() {
        return getForeignEntity("itemFamily");
    }

    default void setFloating(Boolean floating) {
        setFieldValue("floating", floating);
    }

    default Boolean isFloating() {
        return getBooleanFieldValue("floating");
    }

    default void setTopLabel(Object topLabel) {
        setForeignField("topLabel", topLabel);
    }

    default EntityId getTopLabelId() {
        return getForeignEntityId("topLabel");
    }

    default Label getTopLabel() {
        return getForeignEntity("topLabel");
    }

    default void setChildrenPromptLabel(Object childrenPromptLabel) {
        setForeignField("childrenPromptLabel", childrenPromptLabel);
    }

    default EntityId getChildrenPromptLabelId() {
        return getForeignEntityId("childrenPromptLabel");
    }

    default Label getChildrenPromptLabel() {
        return getForeignEntity("childrenPromptLabel");
    }

    default void setPromptLabel(Object promptLabel) {
        setForeignField("promptLabel", promptLabel);
    }

    default EntityId getPromptLabelId() {
        return getForeignEntityId("promptLabel");
    }

    default Label getPromptLabel() {
        return getForeignEntity("promptLabel");
    }

    default void setChildrenRadio(Boolean childrenRadio) {
        setFieldValue("childrenRadio", childrenRadio);
    }

    default Boolean isChildrenRadio() {
        return getBooleanFieldValue("childrenRadio");
    }

    default void setLayout(String layout) {
        setFieldValue("layout", layout);
    }

    default String getLayout() {
        return getStringFieldValue("layout");
    }
    
    //// Enriched fields and methods

    @Override
    default ItemFamilyType getItemFamilyType() {
        Item item = getItem();
        if (item != null)
            return item.getItemFamilyType();
        ItemFamily itemFamily = getItemFamily();
        if (itemFamily != null)
            return itemFamily.getItemFamilyType();
        Option parent = getParent();
        return parent == null ? ItemFamilyType.UNKNOWN : parent.getItemFamilyType();
    }

    default boolean isConcrete() {
        return !isFolder() && hasSiteAndItem();
    }

    default boolean isIncludedByDefault() {
        return isConcrete() ||
                hasItem() && hasTimeRange(); // Ex: Prayers -> to include in the working document so it is displayed in the calendar
    }

    default boolean isBreakfast() {
        return isMealsDayTimeRange(0, 10 * 60);
    }

    default boolean isLunch() {
        return isMealsDayTimeRange(10 * 60, 15 * 60);
    }

    default boolean isSupper() {
        return isMealsDayTimeRange(15 * 60, 24 * 60);
    }

    default boolean isMealsDayTimeRange(long startMinutes, long endMinutes) {
        if (!isMeals())
            return false;
        DayTimeRange dayTimeRange = DayTimeRange.parse(getTimeRangeOrParent());
        if (dayTimeRange == null)
            return false;
        TimeInterval dayTimeInterval = dayTimeRange.getDayTimeInterval(0, TimeUnit.DAYS);
        return dayTimeInterval.getIncludedStart() >= startMinutes && dayTimeInterval.getExcludedEnd() < endMinutes;
    }

    default boolean isTouristTax() {
        return isTax(); // The only tax for now is the tourist tax
    }

    default boolean isDependant() {
        return isDiet() || isBreakfast() || isTouristTax();
    }

    default boolean hasTimeRange() {
        return getTimeRangeOrParent() != null;
    }

    default String getTimeRangeOrParent() {
        return getStringFieldValueOrParent("timeRange");
    }

    default String getDateTimeRangeOrParent() {
        return getStringFieldValueOrParent("dateTimeRange");
    }

    DayTimeRange getParsedTimeRangeOrParent();

    DateTimeRange getParsedDateTimeRangeOrParent();
}
