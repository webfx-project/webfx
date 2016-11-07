package mongoose.activities.shared.logic.price;

import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.entities.Item;
import mongoose.entities.Site;
import naga.commons.util.Objects;
import naga.commons.util.collection.HashList;

/**
 * @author Bruno Salmon
 */
public class DocumentPricing {

    public static int computeDocumentPrice(WorkingDocument workingDocument) {
        HashList<SiteRateItemBlock> siteRateItemBlocks = new HashList<>();
        for (WorkingDocumentLine wdl : workingDocument.getWorkingDocumentLines()) {
            if (!wdl.isCancelled() && wdl.isConcrete()) {
                Site site = wdl.getSite();
                Item item = wdl.getItem();
                item = Objects.coalesce(item.getRateAliasItem(), item);
                SiteRateItemBlock block = new SiteRateItemBlock(workingDocument, site, item);
                SiteRateItemBlock existingBlock = siteRateItemBlocks.getExistingElement(block);
                if (existingBlock != null)
                    block = existingBlock;
                else
                    siteRateItemBlocks.add(block);
                block.addWorkingDocumentLineAttendances(wdl);
            }
        }
        int price = 0;
        for (SiteRateItemBlock block : siteRateItemBlocks)
            price += block.computePrice();
/*
        if (bill.document.cancelledDocumentLines) // adding price of cancelled document lines if any
            for (i = 0; i < bill.document.cancelledDocumentLines.length; i++)
                price += bill.document.cancelledDocumentLines[i].price_net;
*/
        return price;
    }
}
