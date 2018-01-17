package mongoose.activities.shared.logic.work.sync;

import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.entities.Attendance;
import mongoose.entities.Document;
import mongoose.entities.DocumentLine;
import mongoose.services.EventService;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.mapping.QueryResultSetToEntityListGenerator;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.platform.services.query.spi.QueryService;
import naga.util.async.Batch;
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
        DataSourceModel dataSourceModel = eventService.getDataSourceModel();
        Object dataSourceId = dataSourceModel.getId();
        DomainModel domainModel = dataSourceModel.getDomainModel();
        SqlCompiled sqlCompiled1 = domainModel.compileSelect(DOCUMENT_LINE_LOAD_QUERY);
        SqlCompiled sqlCompiled2 = domainModel.compileSelect(ATTENDANCE_LOAD_QUERY);
        Object[] documentPkParameter = {documentPk};
        Future<Batch<QueryResultSet>> queryBatchFuture;
        return Future.allOf(eventService.onEventOptions(), queryBatchFuture = QueryService.executeQueryBatch(
                new Batch<>(new QueryArgument[]{
                        new QueryArgument(sqlCompiled1.getSql(), documentPkParameter, dataSourceId),
                        new QueryArgument(sqlCompiled2.getSql(), documentPkParameter, dataSourceId)
                })
        )).compose(v -> {
            Batch<QueryResultSet> b = queryBatchFuture.result();
            EntityStore store = EntityStore.createAbove(eventService.getEventStore());
            EntityList<DocumentLine> dls = QueryResultSetToEntityListGenerator.createEntityList(b.getArray()[0], sqlCompiled1.getQueryMapping(), store, "dl");
            EntityList<Attendance> as = QueryResultSetToEntityListGenerator.createEntityList(b.getArray()[1], sqlCompiled2.getQueryMapping(), store, "a");
            List<WorkingDocumentLine> wdls = new ArrayList<>();
            for (DocumentLine dl : dls)
                wdls.add(new WorkingDocumentLine(dl, Collections.filter(as, a -> a.getDocumentLine() == dl), eventService));
            WorkingDocument loadedWorkingDocument = new WorkingDocument(eventService, store.getEntity(Document.class, documentPk), wdls);
            return Future.succeededFuture(new WorkingDocument(loadedWorkingDocument));
        });
    }

}
