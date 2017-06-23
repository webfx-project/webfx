package mongoose.services;

import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Cart;
import mongoose.entities.Document;
import mongoose.entities.MoneyTransfer;
import naga.commons.util.async.Future;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.EntityList;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface CartService {

    static CartService get(Object cartIdOrUuid) {
        return CartServiceImpl.get(cartIdOrUuid);
    }

    static CartService getOrCreate(Object cartIdOrUuid, DataSourceModel dataSourceModel) {
        return CartServiceImpl.getOrCreate(cartIdOrUuid, dataSourceModel);
    }

    static CartService getOrCreateFromCart(Cart cart) {
        return CartServiceImpl.getOrCreateFromCart(cart);
    }

    static CartService getOrCreateFromDocument(Document document) {
        return CartServiceImpl.getOrCreateFromDocument(document);
    }

    Future<Cart> onCart();

    Cart getCart();

    Future<List<Document>> onCartDocuments();

    List<Document> getCartDocuments();

    Future<List<WorkingDocument>> onCartWorkingDocuments();

    List<WorkingDocument> getCartWorkingDocuments();

    Future<EntityList> onCartPayments();

    EntityList<MoneyTransfer> getCartPayments();

    void unload();

    boolean isLoading();

    boolean isLoaded();

    EventService getEventService();

}
