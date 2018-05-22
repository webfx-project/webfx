package mongoose.activities.bothends.logic.work.sync;

import mongoose.activities.bothends.logic.work.WorkingDocument;
import mongoose.activities.bothends.logic.work.WorkingDocumentLine;
import mongoose.entities.Attendance;
import mongoose.entities.Document;
import mongoose.entities.DocumentLine;
import mongoose.services.EventService;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.EntityStoreQuery;
import naga.util.async.Future;
import naga.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class WorkingDocumentLoader {

    public final static String DOCUMENT_LINE_LOAD_QUERY = "select <frontend_cart>,document.<frontend_cart> from DocumentLine where site!=null and document=? order by document desc";
    public final static String ATTENDANCE_LOAD_QUERY = "select documentLine.id,date from Attendance where documentLine.document=? order by date";
    public final static String PAYMENT_LOAD_QUERY = "select <frontend_cart> from MoneyTransfer where document=? order by date desc";

    public static Future<WorkingDocument> load(Document document) {
        return load(EventService.getOrCreateFromDocument(document), document.getPrimaryKey());
    }

    public static Future<WorkingDocument> load(EventService eventService, Object documentPk) {
        EntityStore store = EntityStore.createAbove(eventService.getEventStore());
        Future<EntityList[]> queryBatchFuture = store.executeQueryBatch(
              new EntityStoreQuery(DOCUMENT_LINE_LOAD_QUERY, new Object[]{documentPk})
            , new EntityStoreQuery(ATTENDANCE_LOAD_QUERY   , new Object[]{documentPk})
        );
        return Future.allOf(eventService.onEventOptions(), queryBatchFuture).map(v -> {
            EntityList[] entityLists = queryBatchFuture.result();
            EntityList<DocumentLine> dls = entityLists[0];
            EntityList<Attendance> as = entityLists[1];
            List<WorkingDocumentLine> wdls = new ArrayList<>();
            for (DocumentLine dl : dls)
                wdls.add(new WorkingDocumentLine(dl, Collections.filter(as, a -> a.getDocumentLine() == dl), eventService));
            WorkingDocument loadedWorkingDocument = new WorkingDocument(eventService, store.getEntity(Document.class, documentPk), wdls);
            return new WorkingDocument(loadedWorkingDocument);
        });
    }

}
