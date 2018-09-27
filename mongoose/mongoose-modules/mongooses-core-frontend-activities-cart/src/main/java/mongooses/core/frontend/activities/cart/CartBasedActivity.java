package mongooses.core.frontend.activities.cart;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongooses.core.sharedends.activities.generic.MongooseButtonFactoryMixin;
import mongooses.core.sharedends.activities.generic.MongooseSectionFactoryMixin;
import mongooses.core.sharedends.aggregates.CartAggregate;
import mongooses.core.shared.entities.Event;
import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityBase;
import webfx.framework.services.i18n.I18n;
import webfx.fxkits.core.util.properties.Properties;

/**
 * @author Bruno Salmon
 */
public abstract class CartBasedActivity
        extends ViewDomainActivityBase
        implements MongooseButtonFactoryMixin, MongooseSectionFactoryMixin {

    private final Property<Object> cartUuidProperty = new SimpleObjectProperty<>();

    protected Object getCartUuid() {
        return cartUuidProperty.getValue();
    }

    @Override
    protected void updateModelFromContextParameters() {
        cartUuidProperty.setValue(getParameter("cartUuid"));
    }

    @Override
    public void onStart() {
        super.onStart();
        startLogic();
    }

    protected void startLogic() {
        Properties.runOnPropertiesChange(this::onCartUuidChange, cartUuidProperty);
        Properties.runOnPropertiesChange(this::onDictionaryChange, I18n.dictionaryProperty());
    }

    protected void onCartUuidChange() {
        loadCart();
    }

    protected void onDictionaryChange() {
        if (cartService().isLoaded())
            onCartLoaded();
    }

    private void loadCart() {
        cartService().onCartDocuments().setHandler(ar -> {
            if (ar.succeeded())
                onCartLoaded();
        });
    }

    protected void reloadCart() {
        cartService().unload();
        loadCart();
    }

    protected abstract void onCartLoaded();

    protected CartAggregate cartService() {
        return CartAggregate.getOrCreate(getCartUuid(), getDataSourceModel());
    }

    protected Event getEvent() {
        return cartService().getEventAggregate().getEvent();
    }

    protected Object getEventId() {
        return getEvent().getPrimaryKey();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Automatically loading the cart if not yet loading or loaded
        if (getCartUuid() != null && !cartService().isLoaded() && !cartService().isLoading())
            loadCart();
    }

    @Override
    protected void refreshDataOnActive() {
        reloadCart();
    }
}
