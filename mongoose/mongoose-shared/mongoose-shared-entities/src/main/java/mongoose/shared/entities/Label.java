package mongoose.shared.entities;

import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface Label extends Entity {

    default void setDe(String de) {
        setFieldValue("de", de);
    }

    default String getDe() {
        return getStringFieldValue("de");
    }

    default void setEn(String en) {
        setFieldValue("en", en);
    }

    default String getEn() {
        return getStringFieldValue("en");
    }

    default void setEs(String es) {
        setFieldValue("es", es);
    }

    default String getEs() {
        return getStringFieldValue("es");
    }

    default void setFr(String fr) {
        setFieldValue("fr", fr);
    }

    default String getFr() {
        return getStringFieldValue("fr");
    }

    default void setPt(String pt) {
        setFieldValue("pt", pt);
    }

    default String getPt() {
        return getStringFieldValue("pt");
    }

}
