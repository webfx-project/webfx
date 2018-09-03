package mongooses.core.aggregates;

import mongooses.core.activities.sharedends.logic.work.WorkingDocument;
import mongooses.core.entities.Cart;
import mongooses.core.entities.Document;
import mongooses.core.entities.MoneyTransfer;
import webfx.platforms.core.util.async.Future;
import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.framework.orm.entity.EntityList;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface CartAggregate {

    static CartAggregate get(Object cartIdOrUuid) {
        return CartAggregateImpl.get(cartIdOrUuid);
    }

    static CartAggregate getOrCreate(Object cartIdOrUuid, DataSourceModel dataSourceModel) {
        return CartAggregateImpl.getOrCreate(cartIdOrUuid, dataSourceModel);
    }

    static CartAggregate getOrCreateFromCart(Cart cart) {
        return CartAggregateImpl.getOrCreateFromCart(cart);
    }

    static CartAggregate getOrCreateFromDocument(Document document) {
        return CartAggregateImpl.getOrCreateFromDocument(document);
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

    EventAggregate getEventAggregate();

}
