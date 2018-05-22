package mongoose.activities.bothends.logic.work.sync;

import mongoose.activities.bothends.logic.time.DaysArray;
import mongoose.activities.bothends.logic.work.WorkingDocument;
import mongoose.activities.bothends.logic.work.WorkingDocumentLine;
import mongoose.entities.Attendance;
import mongoose.entities.Cart;
import mongoose.entities.Document;
import mongoose.entities.DocumentLine;
import mongoose.services.EventService;
import naga.framework.orm.entity.UpdateStore;
import naga.platform.services.update.UpdateArgument;
import naga.util.async.Future;
import naga.util.collection.Collections;
import naga.util.uuid.Uuid;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class WorkingDocumentSubmitter {

    public static Future<Document> submit(WorkingDocument wd, String comment) {
        UpdateStore store = wd.getUpdateStore();
        EventService eventService = wd.getEventService();
        WorkingDocument loadedWorkingDocument = wd.getLoadedWorkingDocument();
        Document du;
        if (loadedWorkingDocument != null)
            du = store.updateEntity(loadedWorkingDocument.getDocument());
        else {
            du = store.insertEntity(Document.class);
            du.setEvent(eventService.getEvent());
            Cart cart = eventService.getCurrentCart();
            if (cart == null) {
                cart = store.insertEntity(Cart.class);
                cart.setUuid(Uuid.randomUuid());
            }
            du.setCart(cart);
        }
        WorkingDocument.syncPersonDetails(wd.getDocument(), du);
        for (WorkingDocumentLine wdl : wd.getWorkingDocumentLines()) {
            List<Attendance> attendances = wdl.getAttendances();
            DocumentLine dl = wdl.getDocumentLine(), dlu;
            if (dl == null && loadedWorkingDocument != null) {
                WorkingDocumentLine oldWdl = loadedWorkingDocument.findSameWorkingDocumentLine(wdl);
                if (oldWdl != null) {
                    dl = oldWdl.getDocumentLine();
                    attendances = oldWdl.getAttendances();
                }
            }
            if (dl == null) {
                dlu = store.insertEntity(DocumentLine.class);
                dlu.setDocument(du);
            } else
                dlu = store.updateEntity(dl);
            dlu.setSite(wdl.getSite());
            dlu.setItem(wdl.getItem());

            DaysArray daysArray = wdl.getDaysArray();
            int j = 0, m = Collections.size(attendances), n = daysArray.getArray().length;
            if (m > 0 && n == 0) // means that all attendances have been removed
                removeLine(wd, dl);
            else {
                for (int i = 0; i < n; i++) {
                    LocalDate date = daysArray.getDate(i);
                    while (j < m && attendances.get(j).getDate().compareTo(date) < 0) // isBefore() doesn't work on Android
                        store.deleteEntity(attendances.get(j++));
                    if (j < m && attendances.get(j).getDate().equals(date))
                        j++;
                    else {
                        Attendance au = store.insertEntity(Attendance.class);
                        au.setDate(date);
                        au.setDocumentLine(dlu);
                    }
                }
                while (j < m)
                    store.deleteEntity(attendances.get(j++));
            }
        }
        if (loadedWorkingDocument != null)
            for (WorkingDocumentLine lastWdl : loadedWorkingDocument.getWorkingDocumentLines()) {
                if (wd.findSameWorkingDocumentLine(lastWdl) == null)
                    removeLine(wd, lastWdl.getDocumentLine());
            }
        return store.executeUpdate(new UpdateArgument[]{new UpdateArgument("select set_transaction_parameters(false)", store.getDataSourceId())})
                .map(batch -> du);
    }

    private static void removeLine(WorkingDocument wd, DocumentLine dl) {
        wd.getUpdateStore().deleteEntity(dl); // TODO: should probably be cancelled instead in some cases (and keep the non refundable part)
    }
}
