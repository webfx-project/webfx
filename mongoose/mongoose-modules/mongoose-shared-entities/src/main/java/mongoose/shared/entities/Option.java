package mongoose.shared.entities;

import mongoose.shared.businessdata.time.DateTimeRange;
import mongoose.shared.businessdata.time.DayTimeRange;
import mongoose.shared.entities.markers.*;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface Option extends Entity,
        EntityHasParent<Option>,
        EntityHasEvent,
        EntityHasName,
        EntityHasLabel,
        EntityHasArrivalSiteAndItem,
        EntityHasItemFamily,
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

    default boolean isNotObligatory() {
        return !isObligatory();
    }

    default ItemFamily findItemFamily() {
        Item item = getItem();
        if (item != null)
            return item.getFamily();
        ItemFamily itemFamily = getItemFamily();
        if (itemFamily != null)
            return itemFamily;
        Option parent = getParent();
        return parent == null ? null : parent.findItemFamily();
    }

    default String getItemFamilyCode() {
        ItemFamily itemFamily = findItemFamily();
        return itemFamily == null ? null : itemFamily.getCode();
    }

    @Override
    default ItemFamilyType getItemFamilyType() {
        ItemFamily itemFamily = findItemFamily();
        return itemFamily == null ? ItemFamilyType.UNKNOWN : itemFamily.getItemFamilyType();
    }

    default boolean isConcrete() {
        return !isFolder() && hasSiteAndItem();
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
