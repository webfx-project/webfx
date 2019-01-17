package mongoose.client.businessdata.workingdocument;

import mongoose.client.aggregates.cart.CartAggregateImpl;
import mongoose.client.aggregates.event.EventAggregate;
import mongoose.shared.entities.Attendance;
import mongoose.shared.entities.Document;
import mongoose.shared.entities.DocumentLine;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.EntityStoreQuery;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class WorkingDocumentLoader {

    public static Future<WorkingDocument> load(Document document) {
        return load(EventAggregate.getOrCreateFromDocument(document), document.getPrimaryKey());
    }

    public static Future<WorkingDocument> load(EventAggregate eventAggregate, Object documentPk) {
        EntityStore store = EntityStore.createAbove(eventAggregate.getEventStore());
        Future<EntityList[]> queryBatchFuture = store.executeQueryBatch(
              new EntityStoreQuery(CartAggregateImpl.DOCUMENT_LINE_LOAD_QUERY, new Object[]{documentPk})
            , new EntityStoreQuery(CartAggregateImpl.ATTENDANCE_LOAD_QUERY   , new Object[]{documentPk})
        );
        return Future.allOf(eventAggregate.onEventOptions(), queryBatchFuture).map(v -> {
            EntityList[] entityLists = queryBatchFuture.result();
            EntityList<DocumentLine> dls = entityLists[0];
            EntityList<Attendance> as = entityLists[1];
            List<WorkingDocumentLine> wdls = new ArrayList<>();
            for (DocumentLine dl : dls)
                wdls.add(new WorkingDocumentLine(dl, Collections.filter(as, a -> a.getDocumentLine() == dl), eventAggregate));
            WorkingDocument loadedWorkingDocument = new WorkingDocument(eventAggregate, store.getEntity(Document.class, documentPk), wdls);
            return new WorkingDocument(loadedWorkingDocument);
        });
    }

}
