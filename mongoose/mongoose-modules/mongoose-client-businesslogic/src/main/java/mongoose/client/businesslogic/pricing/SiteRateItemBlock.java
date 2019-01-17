package mongoose.client.businesslogic.pricing;

import mongoose.client.businessdata.workingdocument.WorkingDocument;
import mongoose.client.businessdata.workingdocument.WorkingDocumentLine;
import mongoose.shared.entities.Document;
import mongoose.shared.entities.Item;
import mongoose.shared.entities.Rate;
import mongoose.shared.entities.Site;
import webfx.framework.shared.orm.entity.Entities;
import webfx.platform.shared.util.Booleans;
import webfx.platform.shared.util.Objects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Bruno Salmon
 */
final class SiteRateItemBlock {

    private final WorkingDocument workingDocument;
    private final Site site;
    private final Item rateItem;

    private final List<AttendanceBlock> attendanceBlocks = new ArrayList<>();
    private int price;

    SiteRateItemBlock(WorkingDocument workingDocument, Site site, Item rateItem) {
        this.workingDocument = workingDocument;
        this.site = site;
        this.rateItem = rateItem;
    }

    void addWorkingDocumentLineAttendances(WorkingDocumentLine wdl) {
        for (LocalDate date : wdl.getDaysArray().localDateIterable())
            attendanceBlocks.add(new AttendanceBlock(wdl, date));
    }

    int computePrice() {
        Collections.sort(attendanceBlocks);
        price = 0;
        int blockLength = attendanceBlocks.size();
        int remainingDays = blockLength;
        if (remainingDays == 0)
            return price;
        int consumedDays = 0;
        //if (update)
            resetBlockWorkingDocumentLinesPrice();
        LocalDate firstDay = attendanceBlocks.get(0).getDate();
        LocalDate lastDay = attendanceBlocks.get(blockLength - 1).getDate();
        List<Rate> rates = workingDocument.getEventAggregate().selectRates(
                r -> Entities.sameId(r.getSite(), site) && Entities.sameId(r.getItem(), rateItem) && rateMatchesDocument(r) &&
                        dateNullOrBefore(r.getStartDate(), lastDay) && dateNullOrAfter(r.getEndDate(), firstDay));
        if (!rates.isEmpty()) {
            //rates.sort(function (r1, r2) { return (r1.perDay ? 1 : r1.maxDay ) - (r2.perDay ? 1 : r2.maxDay);});
            while (remainingDays > 0) {
                // selecting the cheapest rate for the next attendances
                RateInfo cheapest = null;
                RateInfo second = null;
                RateInfo cheapestFixedThreshold = null; // Used for Festivals while daily rates are variable
                for (Rate rate : rates) {
                    LocalDate startDate = rate.getStartDate();
                    LocalDate endDate = rate.getEndDate();
                    if (startDate != null || endDate != null) {
                        LocalDate date = attendanceBlocks.get(consumedDays).getDate();
                        if (startDate != null && date.compareTo(startDate) < 0 || endDate != null && date.compareTo(endDate) > 0)
                            continue;
                    }
                    int quantity = 1; //rate.perPerson && dl.share_owner && dl.capacity ? dl.capacity.capacity : 1;
                    int ratePrice = getRatePrice(rate) * quantity;
                    int minDay = Objects.coalesce(rate.getMinDay(), 1);
                    int maxDay = rate.isPerDay() ? 1 : Objects.coalesce(rate.getMaxDay(), 10000);
                    // When a rate defines a new lower daily price that applies after a minimum of days (ex: 30% discount when >= 14 days),
                    // we need to ensure that people approaching that number of days (ex: 12 or 13 days)
                    // don't pay more with the previous rate than people staying that minimum of days (ex: 14 days)
                    // In other words, we need to put an upper limit for such people, equals to the price that is applied at that minimum of days
                    if (blockLength < minDay && maxDay == 1) { // So if the block is less than the rate min day,
                        ratePrice = ratePrice * minDay; // we transform the daily rate into a fixed rate with the upper limit
                        maxDay = minDay; // that applies over that period
                    }
                    if (startDate == null && endDate == null && blockLength <= maxDay && (cheapestFixedThreshold == null || cheapestFixedThreshold.price > ratePrice))
                        cheapestFixedThreshold = new RateInfo(ratePrice / blockLength, ratePrice, blockLength);
                    int consumableDays = Math.min(remainingDays, maxDay);
                    int dailyPrice = ratePrice / consumableDays;
                    RateInfo memo = new RateInfo(dailyPrice, ratePrice, consumableDays);
                    if (cheapest == null)
                        cheapest = memo;
                    else if (dailyPrice < cheapest.dailyPrice) {
                        second = cheapest;
                        cheapest = memo;
                    } else if (second == null || dailyPrice < second.dailyPrice)
                        second = memo;
                }
                if (cheapest == null)
                    break;
                if (cheapestFixedThreshold != null && price + cheapest.price > cheapestFixedThreshold.price) {
                    cheapest = cheapestFixedThreshold;
                    price = 0;
                    consumedDays = 0;
                    remainingDays = blockLength;
                    second = null;
                    resetBlockWorkingDocumentLinesPrice();
                }
                // applying the found cheapest rate on the next consumable days (applicable for this rate)
                int remainingPrice = cheapest.price;
                if (second == null)
                    second = cheapest;
                int fullAttendancePrice = second.price / second.consumableDays;
                for (int i = 0; i < cheapest.consumableDays && remainingPrice > 0; i++) {
                    AttendanceBlock ba = attendanceBlocks.get(consumedDays + i);
                    ba.setPrice(i == cheapest.consumableDays - 1 ? remainingPrice : Math.min(fullAttendancePrice, remainingPrice));
                    //if (update)
                        ba.getWorkingDocumentLine().incrementPrice(ba.getPrice());
                    remainingPrice -= ba.getPrice();
                }
                // updating the block price
                price += cheapest.price;
                // marking progress
                consumedDays += cheapest.consumableDays;
                remainingDays -= cheapest.consumableDays;
            }
        }
/*
        var roundingFactor = bill.document.event.optionRoundingFactor;
        if (update && roundingFactor) { // rounding document lines prices if a rounding factor is specified for the event
            price = 0; // also recomputing the total block price
            for (i = 0; i < blockLength; i++) {
                dl = attendanceBlocks[i].documentLine;
                if (!dl.rounded) {
                    dl.price = Math.round(dl.price / roundingFactor) * roundingFactor;
                    price += dl.price;
                    dl.rounded = true;
                }
            }
        }
*/
        return price;
    }

    private void resetBlockWorkingDocumentLinesPrice() {
        for (AttendanceBlock block : attendanceBlocks) {
            WorkingDocumentLine dl = block.getWorkingDocumentLine();
            dl.setPrice(0);
            dl.setRounded(false);
        }
    }

    private boolean dateNullOrBefore(LocalDate date, LocalDate compareToDate) {
        return date == null || compareToDate == null || date.compareTo(compareToDate) <= 0;
    }

    private boolean dateNullOrAfter(LocalDate date, LocalDate compareToDate) {
        return date == null || compareToDate == null || date.compareTo(compareToDate) >= 0;
    }

    private boolean rateMatchesDocument(Rate rate) {
        return true;
    }

    private Integer getRatePrice(Rate rate) {
        Integer ratePrice = rate.getPrice();
        Document document = workingDocument.getDocument();
        Integer age = document.getAge();
        if (age != null) {
            Integer ageMax = rate.getAge1Max();
            if (ageMax != null && age <= ageMax) {
                Integer price = rate.getAge1Price();
                if (price != null)
                    return price;
                Integer discount = rate.getAge1Discount();
                if (discount != null)
                    return ratePrice * (100 - discount) / 100;
            }
            ageMax = rate.getAge2Max();
            if (ageMax != null && age <= ageMax) {
                Integer price = rate.getAge2Price();
                if (price != null)
                    return price;
                Integer discount = rate.getAge2Discount();
                if (discount != null)
                    return ratePrice * (100 - discount) / 100;
            }
            ageMax = rate.getAge3Max();
            if (ageMax != null && age <= ageMax) {
                Integer price = rate.getAge3Price();
                if (price != null)
                    return price;
                Integer discount = rate.getAge3Discount();
                if (discount != null)
                    return ratePrice * (100 - discount) / 100;
            }
        }
        if (Booleans.isTrue(document.isWorkingVisit())) {
            Integer price = rate.getWorkingVisitPrice();
            if (price != null)
                return price;
            Integer discount = rate.getWorkingVisitDiscount();
            if (discount != null)
                return ratePrice * (100 - discount) / 100;
        }
        if (Booleans.isTrue(document.isGuest())) {
            Integer price = rate.getGuestPrice();
            if (price != null)
                return price;
            Integer discount = rate.getGuestDiscount();
            if (discount != null)
                return ratePrice * (100 - discount) / 100;
        }
        if (Booleans.isTrue(document.isResident())) {
            Integer price = rate.getResidentPrice();
            if (price != null)
                return price;
            Integer discount = rate.getResidentDiscount();
            if (discount != null)
                return ratePrice * (100 - discount) / 100;
        }
        if (Booleans.isTrue(document.isResident2())) {
            Integer price = rate.getResident2Price();
            if (price != null)
                return price;
            Integer discount = rate.getResident2Discount();
            if (discount != null)
                return ratePrice * (100 - discount) / 100;
        }
        if (Booleans.isTrue(document.isDiscoveryReduced())) {
            Integer price = rate.getDiscoveryReducedPrice();
            if (price != null)
                return price;
            Integer discount = rate.getDiscoveryReducedDiscount();
            if (discount != null)
                return ratePrice * (100 - discount) / 100;
        }
        if (Booleans.isTrue(document.isDiscovery())) {
            Integer price = rate.getDiscoveryPrice();
            if (price != null)
                return price;
            Integer discount = rate.getDiscoveryDiscount();
            if (discount != null)
                return ratePrice * (100 - discount) / 100;
        }
        if (Booleans.isTrue(document.isUnemployed())) {
            Integer price = rate.getUnemployedPrice();
            if (price != null)
                return price;
            Integer discount = rate.getUnemployedDiscount();
            if (discount != null)
                return ratePrice * (100 - discount) / 100;
        }
        if (Booleans.isTrue(document.isFacilityFee())) {
            Integer price = rate.getFacilityFeePrice();
            if (price != null)
                return price;
            Integer discount = rate.getFacilityFeeDiscount();
            if (discount != null)
                return ratePrice * (100 - discount) / 100;
        }
        return ratePrice;
    }

    static final class RateInfo {
        final int dailyPrice;
        final int price;
        final int consumableDays;

        RateInfo(int dailyPrice, int price, int consumableDays) {
            this.dailyPrice = dailyPrice;
            this.price = price;
            this.consumableDays = consumableDays;
        }
    }

    //// equals() and hashCode() implementation for use as key in HashList<SiteRateItemBlock>
    //// => the key is made only of site and rateItem fields

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteRateItemBlock that = (SiteRateItemBlock) o;

        return site == that.site && rateItem == that.rateItem;
    }

    @Override
    public int hashCode() {
        int result = site.hashCode();
        result = 31 * result + rateItem.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SiteRateItemBlock[" + site.getId() + " - " + rateItem.getId() + " -");
        for (AttendanceBlock ab : attendanceBlocks) {
            sb.append(" ").append(ab.getDate());
        }
        return sb.append("]").toString();
    }
}
