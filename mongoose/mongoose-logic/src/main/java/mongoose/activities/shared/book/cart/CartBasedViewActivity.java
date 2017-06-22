package mongoose.activities.shared.book.cart;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.services.CartService;
import naga.framework.activity.view.impl.ViewActivityImpl;
import naga.fx.properties.Properties;

/**
 * @author Bruno Salmon
 */
public abstract class CartBasedViewActivity extends ViewActivityImpl {

    private final Property<Object> cartUuidProperty = new SimpleObjectProperty<>();

    protected Object getCartUuid() {
        return cartUuidProperty.getValue();
    }

    @Override
    protected void fetchRouteParameters() {
        cartUuidProperty.setValue(getParameter("cartUuid"));
    }

    @Override
    public void onStart() {
        super.onStart();
        startLogic();
    }

    protected void startLogic() {
        Properties.runOnPropertiesChange(p -> onCartUuidChange(), cartUuidProperty);
        Properties.runOnPropertiesChange(p -> onDictionaryChange(), getI18n().dictionaryProperty());
    }

    protected void onCartUuidChange() {
        loadCart();
    }

    protected void onDictionaryChange() {
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

    protected CartService cartService() {
        return CartService.getOrCreate(getCartUuid(), getDataSourceModel());
    }

    @Override
    protected void refreshDataOnActive() {
        reloadCart();
    }
}
