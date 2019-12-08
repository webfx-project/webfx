package mongoose.client.aggregates.cart;

import mongoose.client.aggregates.event.EventAggregate;
import mongoose.shared.entities.*;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.entity.*;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.Strings;
import webfx.platform.shared.util.async.Future;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class CartAggregateImpl implements CartAggregate {

    public final static String DOCUMENT_LINE_LOAD_QUERY = "select <frontend_cart>,document.<frontend_cart> from DocumentLine where site!=null and document=? order by document desc";
    public final static String ATTENDANCE_LOAD_QUERY = "select documentLine.id,date from Attendance where documentLine.document=? order by date";
    private final static String PAYMENT_LOAD_QUERY = "select <frontend_cart> from MoneyTransfer where document=? order by date desc";

    private final static Map<Object, CartAggregate> aggregates = new HashMap<>();

    private final EntityStore store;
    private Object id;
    private String uuid;
    private Cart cart;
    private List<Document> cartDocuments;
    private EntityList<DocumentLine> cartDocumentLines;
    private EntityList<Attendance> cartAttendances;
    private EntityList<MoneyTransfer> cartPayments;
    private EventAggregate eventAggregate;
    private boolean loading;

    public CartAggregateImpl(Object cartIdOrUuid, EntityStore store) {
        id = cartIdOrUuid instanceof String ? null : cartIdOrUuid;
        uuid = cartIdOrUuid instanceof String ? (String) cartIdOrUuid : null;
        this.store = store;
    }

    static CartAggregate get(Object cartIdOrUuid) {
        return aggregates.get(Entities.getPrimaryKey(cartIdOrUuid));
    }

    static CartAggregate getOrCreate(Object cartIdOrUuid, EntityStore store) {
        cartIdOrUuid = Entities.getPrimaryKey(cartIdOrUuid);
        CartAggregate cartAggregate = get(cartIdOrUuid);
        if (cartAggregate == null)
            aggregates.put(cartIdOrUuid, cartAggregate = new CartAggregateImpl(cartIdOrUuid, store));
        return cartAggregate;
    }

    static CartAggregate getOrCreate(Object cartIdOrUuid, DataSourceModel dataSourceModel) {
        return getOrCreate(cartIdOrUuid, EntityStore.create(dataSourceModel));
    }

    static CartAggregate getOrCreateFromCart(Cart cart) {
        CartAggregate service = getOrCreate(cart.getId(), cart.getStore());
        ((CartAggregateImpl) service).setCart(cart);
        return service;
    }

    static CartAggregate getOrCreateFromDocument(Document document) {
        return getOrCreateFromCart(document.getCart());
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        if (id == null)
            aggregates.put(id = Entities.getPrimaryKey((Object) cart.getId()), this);
        if (uuid == null)
            aggregates.put(uuid = cart.getUuid(), this);
        if (eventAggregate != null)
            eventAggregate.setActiveCart(cart);
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public List<Document> getCartDocuments() {
        return cartDocuments;
    }

    public EntityList<DocumentLine> getCartDocumentLines() {
        return cartDocumentLines;
    }

    public EntityList<Attendance> getCartAttendances() {
        return cartAttendances;
    }

    @Override
    public EntityList<MoneyTransfer> getCartPayments() {
        return cartPayments;
    }

    @Override
    public void unload() {
        cartDocuments = null;
        cartDocumentLines = null;
        cartAttendances = null;
        cartPayments = null;
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean isLoaded() {
        return cartDocuments != null && !loading;
    }

    @Override
    public Future<Cart> onCart() {
        if (isLoaded())
            return Future.succeededFuture(cart);
        loading = true;
        String documentCondition = "document.cart." + (id != null ? "id=?" : "uuid=?");
        Object[] parameter = new Object[]{id != null ? id : uuid};
        return store.executeQueryBatch(
              new EntityStoreQuery(Strings.replaceAll(DOCUMENT_LINE_LOAD_QUERY, "document=?", documentCondition), parameter)
            , new EntityStoreQuery(Strings.replaceAll(ATTENDANCE_LOAD_QUERY, "document=?", documentCondition), parameter)
            , new EntityStoreQuery(Strings.replaceAll(PAYMENT_LOAD_QUERY, "document=?", documentCondition), parameter)
        ).compose((entityLists, future) -> {
            cartDocuments = new ArrayList<>();
            cartDocumentLines = entityLists[0];
            cartAttendances = entityLists[1];
            cartPayments = entityLists[2];
            if (cartDocumentLines.isEmpty()) {
                loading = false;
                future.complete();
            }
            eventAggregate = EventAggregate.getOrCreateFromDocument(cartDocumentLines.get(0).getDocument());
            eventAggregate.onEventOptions().setHandler(ar -> {
                if (!cartDocuments.isEmpty()) {
                    Logger.log("Warning: CartAggregate.onCart() has been called again before the first call is finished");
                    cartDocuments.clear();
                }
                Document currentDocument = null;
                for (DocumentLine dl : cartDocumentLines) {
                    Document document = dl.getDocument();
                    if (document != currentDocument)
                        cartDocuments.add(currentDocument = document);
                }
                setCart(cartDocuments.get(0).getCart());
                loading = false;
                future.complete(cart);
            });
        });
    }

    @Override
    public Future<List<Document>> onCartDocuments() {
        return onCart().map(cart -> cartDocuments);
    }

    @Override
    public Future<EntityList> onCartPayments() {
        return onCart().map(cart -> cartPayments);
    }

    @Override
    public EventAggregate getEventAggregate() {
        return eventAggregate;
    }
}
