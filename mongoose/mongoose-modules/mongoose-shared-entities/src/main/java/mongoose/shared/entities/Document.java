package mongoose.shared.entities;

import mongoose.shared.entities.markers.EntityHasCancelled;
import mongoose.shared.entities.markers.EntityHasEvent;
import mongoose.shared.entities.markers.EntityHasPerson;
import mongoose.shared.entities.markers.EntityHasPersonalDetailsCopy;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface Document extends Entity, EntityHasEvent, EntityHasCancelled, EntityHasPerson, EntityHasPersonalDetailsCopy {

    default void setRef(Integer ref) {
        setFieldValue("ref", ref);
    }

    default Integer getRef() {
        return getIntegerFieldValue("ref");
    }

    default void setCart(Object cart) {
        setForeignField("cart", cart);
    }

    default EntityId getCartId() {
        return getForeignEntityId("cart");
    }

    default Cart getCart() {
        return getForeignEntity("cart");
    }

    default void setPriceNet(Integer priceNet) {
        setFieldValue("price_net", priceNet);
    }

    default Integer getPriceNet() {
        return getIntegerFieldValue("price_net");
    }

    default void setPriceDeposit(Integer priceDeposit) {
        setFieldValue("price_deposit", priceDeposit);
    }

    default Integer getPriceDeposit() {
        return getIntegerFieldValue("price_deposit");
    }

    default void setPriceMinDeposit(Integer priceMinDeposit) {
        setFieldValue("price_minDeposit", priceMinDeposit);
    }

    default Integer getPriceMinDeposit() {
        return getIntegerFieldValue("price_minDeposit");
    }

    default void setConfirmed(Boolean confirmed) {
        setFieldValue("confirmed", confirmed);
    }

    default Boolean isConfirmed() {
        return getBooleanFieldValue("confirmed");
    }

}
