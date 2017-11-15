package mongoose.services;

import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.activities.shared.logic.work.sync.WorkingDocumentLoader;
import mongoose.entities.*;
import naga.platform.services.log.spi.Logger;
import naga.platform.services.query.spi.QueryService;
import naga.util.Strings;
import naga.util.async.Batch;
import naga.util.async.Future;
import naga.util.collection.Collections;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.mapping.QueryResultSetToEntityListGenerator;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
class CartServiceImpl implements CartService {

    private final static Map<Object, CartService> services = new HashMap<>();

    private final EntityStore store;
    private Object id;
    private String uuid;
    private Cart cart;
    private List<Document> cartDocuments;
    private List<WorkingDocument> cartWorkingDocuments;
    private EntityList<MoneyTransfer> cartPayments;
    private EventService eventService;
    private boolean loading;

    public CartServiceImpl(Object cartIdOrUuid, EntityStore store) {
        id = cartIdOrUuid instanceof String ? null : cartIdOrUuid;
        uuid = cartIdOrUuid instanceof String ? (String) cartIdOrUuid : null;
        this.store = store;
    }

    static CartService get(Object cartIdOrUuid) {
        return services.get(toKey(cartIdOrUuid));
    }

    static CartService getOrCreate(Object cartIdOrUuid, EntityStore store) {
        cartIdOrUuid = toKey(cartIdOrUuid);
        CartService cartService = get(cartIdOrUuid);
        if (cartService == null)
            services.put(cartIdOrUuid, cartService = new CartServiceImpl(cartIdOrUuid, store));
        return cartService;
    }

    static CartService getOrCreate(Object cartIdOrUuid, DataSourceModel dataSourceModel) {
        return getOrCreate(cartIdOrUuid, EntityStore.create(dataSourceModel));
    }

    static CartService getOrCreateFromCart(Cart cart) {
        CartService service = getOrCreate(cart.getId(), cart.getStore());
        ((CartServiceImpl) service).setCart(cart);
        return service;
    }

    static CartService getOrCreateFromDocument(Document document) {
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
            services.put(id = toKey(cart.getId()), this);
        if (uuid == null)
            services.put(uuid = cart.getUuid(), this);
        if (eventService != null)
            eventService.setCurrentCart(cart);
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
        DataSourceModel dataSourceModel = store.getDataSourceModel();
        Object dataSourceId = dataSourceModel.getId();
        DomainModel domainModel = dataSourceModel.getDomainModel();
        String documentCondition = "document.cart." + (id != null ? "id=?" : "uuid=?");
        Object[] parameter = new Object[]{id != null ? id : uuid};
        SqlCompiled sqlCompiled1 = domainModel.compileSelect(Strings.replaceAll(WorkingDocumentLoader.DOCUMENT_LINE_LOAD_QUERY, "document=?", documentCondition));
        SqlCompiled sqlCompiled2 = domainModel.compileSelect(Strings.replaceAll(WorkingDocumentLoader.ATTENDANCE_LOAD_QUERY, "document=?", documentCondition));
        SqlCompiled sqlCompiled3 = domainModel.compileSelect(Strings.replaceAll(WorkingDocumentLoader.PAYMENT_LOAD_QUERY, "document=?", documentCondition));
        Future<Batch<QueryResultSet>> queryBatchFuture = QueryService.executeQueryBatch(
                new Batch<>(new QueryArgument[]{
                        new QueryArgument(sqlCompiled1.getSql(), parameter, dataSourceId),
                        new QueryArgument(sqlCompiled2.getSql(), parameter, dataSourceId),
                        new QueryArgument(sqlCompiled3.getSql(), parameter, dataSourceId)
                })
        );
        loading = true;
        return queryBatchFuture.compose(v -> {
            Batch<QueryResultSet> b = queryBatchFuture.result();
            EntityList<DocumentLine> dls = QueryResultSetToEntityListGenerator.createEntityList(b.getArray()[0], sqlCompiled1.getQueryMapping(), store, "dl");
            EntityList<Attendance> as = QueryResultSetToEntityListGenerator.createEntityList(b.getArray()[1], sqlCompiled2.getQueryMapping(), store, "a");
            cartPayments = QueryResultSetToEntityListGenerator.createEntityList(b.getArray()[2], sqlCompiled3.getQueryMapping(), store, "mt");
            cartDocuments = new ArrayList<>();
            cartWorkingDocuments = new ArrayList<>();
            if (dls.isEmpty()) {
                loading = false;
                return Future.succeededFuture();
            }
            eventService = EventService.getOrCreateFromDocument(dls.get(0).getDocument());
            return eventService.onEventOptions().compose(v2 -> {
                if (!cartDocuments.isEmpty()) {
                    Logger.log("Warning: CartService.onCart() has been called again before the first call is finished");
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
                    wdls.add(new WorkingDocumentLine(dl, Collections.filter(as, a -> a.getDocumentLine() == dl), eventService));
                }
                addWorkingDocument(currentDocument, wdls);
                setCart(cartDocuments.get(0).getCart());
                loading = false;
                return Future.succeededFuture(cart);
            });
        });
    }

    private void addWorkingDocument(Document document, List<WorkingDocumentLine> wdls) {
        cartWorkingDocuments.add(new WorkingDocument(new WorkingDocument(eventService, document, wdls)));
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
    public EventService getEventService() {
        return eventService;
    }
}
