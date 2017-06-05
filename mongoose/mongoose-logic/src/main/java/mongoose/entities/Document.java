package mongoose.entities;

import mongoose.entities.markers.EntityHasCancelled;
import mongoose.entities.markers.EntityHasEvent;
import mongoose.entities.markers.EntityHasPerson;
import mongoose.entities.markers.EntityHasPersonDetailsCopy;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public interface Document extends Entity, EntityHasEvent, EntityHasCancelled, EntityHasPerson, EntityHasPersonDetailsCopy {

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
}
