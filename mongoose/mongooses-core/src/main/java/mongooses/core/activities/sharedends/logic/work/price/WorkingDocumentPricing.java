package mongooses.core.activities.sharedends.logic.work.price;

import mongooses.core.activities.sharedends.logic.work.WorkingDocument;
import mongooses.core.activities.sharedends.logic.work.WorkingDocumentLine;
import mongooses.core.entities.Item;
import mongooses.core.entities.Site;
import webfx.platforms.core.util.Objects;
import webfx.platforms.core.util.collection.HashList;

/**
 * @author Bruno Salmon
 */
public class WorkingDocumentPricing {

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
        for (SiteRateItemBlock block : siteRateItemBlocks) {
            int blockPrice = block.computePrice();
            //Platform.log(block + " price = " + blockPrice);
            price += blockPrice;
        }
/*
        if (bill.document.cancelledDocumentLines) // adding price of cancelled document lines if any
            for (i = 0; i < bill.document.cancelledDocumentLines.length; i++)
                price += bill.document.cancelledDocumentLines[i].price_net;
*/
        return price;
    }
}
