package mongoose.aggregates;

import mongoose.activities.bothends.logic.work.WorkingDocument;
import mongoose.activities.bothends.logic.work.WorkingDocumentLine;
import mongoose.activities.bothends.logic.work.sync.WorkingDocumentLoader;
import mongoose.entities.*;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.EntityStoreQuery;
import naga.platform.services.log.Logger;
import naga.util.Strings;
import naga.util.async.Future;
import naga.util.collection.Collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
class CartAggregateImpl implements CartAggregate {

    private final static Map<Object, CartAggregate> aggregates = new HashMap<>();

    private final EntityStore store;
    private Object id;
    private String uuid;
    private Cart cart;
    private List<Document> cartDocuments;
    private List<WorkingDocument> cartWorkingDocuments;
    private EntityList<MoneyTransfer> cartPayments;
    private EventAggregate eventAggregate;
    private boolean loading;

    public CartAggregateImpl(Object cartIdOrUuid, EntityStore store) {
        id = cartIdOrUuid instanceof String ? null : cartIdOrUuid;
        uuid = cartIdOrUuid instanceof String ? (String) cartIdOrUuid : null;
        this.store = store;
    }

    static CartAggregate get(Object cartIdOrUuid) {
        return aggregates.get(toKey(cartIdOrUuid));
    }

    static CartAggregate getOrCreate(Object cartIdOrUuid, EntityStore store) {
        cartIdOrUuid = toKey(cartIdOrUuid);
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

    private static Object toKey(Object id) {
        if (id instanceof EntityId)
            id = ((EntityId) id).getPrimaryKey();
        return id;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        if (id == null)
            aggregates.put(id = toKey(cart.getId()), this);
        if (uuid == null)
            aggregates.put(uuid = cart.getUuid(), this);
        if (eventAggregate != null)
            eventAggregate.setCurrentCart(cart);
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public List<Document> getCartDocuments() {
        return cartDocuments;
    }

    @Override
    public List<WorkingDocument> getCartWorkingDocuments() {
        return cartWorkingDocuments;
    }

    @Override
    public EntityList<MoneyTransfer> getCartPayments() {
        return cartPayments;
    }

    @Override
    public void unload() {
        cartDocuments = null;
        cartWorkingDocuments = null;
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
        String documentCondition = "document.cart." + (id != null ? "id=?" : "uuid=?");
        Object[] parameter = new Object[]{id != null ? id : uuid};
        return store.executeQueryBatch(
              new EntityStoreQuery(Strings.replaceAll(WorkingDocumentLoader.DOCUMENT_LINE_LOAD_QUERY, "document=?", documentCondition), parameter)
            , new EntityStoreQuery(Strings.replaceAll(WorkingDocumentLoader.ATTENDANCE_LOAD_QUERY, "document=?", documentCondition), parameter)
            , new EntityStoreQuery(Strings.replaceAll(WorkingDocumentLoader.PAYMENT_LOAD_QUERY, "document=?", documentCondition), parameter)
        ).compose((entityLists, future) -> {
            EntityList<DocumentLine> dls = entityLists[0];
            EntityList<Attendance> as = entityLists[1];
            cartPayments = entityLists[2];
            cartDocuments = new ArrayList<>();
            cartWorkingDocuments = new ArrayList<>();
            if (dls.isEmpty()) {
                loading = false;
                future.complete();
            }
            eventAggregate = EventAggregate.getOrCreateFromDocument(dls.get(0).getDocument());
            eventAggregate.onEventOptions().setHandler(ar -> {
                if (!cartDocuments.isEmpty()) {
                    Logger.log("Warning: CartAggregate.onCart() has been called again before the first call is finished");
                    cartDocuments.clear();
                    cartWorkingDocuments.clear();
                }
                Document currentDocument = null;
                List<WorkingDocumentLine> wdls = null;
                for (DocumentLine dl : dls) {
                    Document document = dl.getDocument();
                    if (document != currentDocument) {
                        if (currentDocument != null)
                            addWorkingDocument(currentDocument, wdls);
                        cartDocuments.add(currentDocument = document);
                        wdls = new ArrayList<>();
                    }
                    wdls.add(new WorkingDocumentLine(dl, Collections.filter(as, a -> a.getDocumentLine() == dl), eventAggregate));
                }
                addWorkingDocument(currentDocument, wdls);
                setCart(cartDocuments.get(0).getCart());
                loading = false;
                future.complete(cart);
            });
        });
    }

    private void addWorkingDocument(Document document, List<WorkingDocumentLine> wdls) {
        cartWorkingDocuments.add(new WorkingDocument(new WorkingDocument(eventAggregate, document, wdls)));
    }

    @Override
    public Future<List<Document>> onCartDocuments() {
        return onCart().map(cart -> cartDocuments);
    }

    @Override
    public Future<List<WorkingDocument>> onCartWorkingDocuments() {
        return onCart().map(cart -> cartWorkingDocuments);
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
