package mongoose.activities.shared.logic.price;

import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.entities.Item;
import mongoose.entities.Rate;
import mongoose.entities.Site;
import naga.commons.util.Objects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Bruno Salmon
 */
class SiteRateItemBlock {

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
/*
        if (update)
            for (var i = 0; i < blockLength; i++) {
                var dl = attendanceBlocks[i].documentLine;
                dl.price = 0;
                dl.rounded = false;
            }
*/
        LocalDate firstDay = attendanceBlocks.get(0).getDate();
        LocalDate lastDay = attendanceBlocks.get(blockLength - 1).getDate();
        List<Rate> rates = workingDocument.getEventService().selectRates(
                r -> r.getSite() == site && r.getItem() == rateItem && rateMatchesDocument(r) &&
                        dateNullOrBefore(r.getStartDate(), lastDay) && dateNullOrAfter(r.getEndDate(), firstDay));
        if (!rates.isEmpty()) {
            //rates.sort(function (r1, r2) { return (r1.perDay ? 1 : r1.maxDay ) - (r2.perDay ? 1 : r2.maxDay);});
            while (remainingDays > 0) {
                // selecting the cheapest rate for the next attendances
                RateInfo cheapest = null;
                RateInfo second = null;
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
                // applying the found cheapest rate on the next consumable days (applyable for this rate)
                int remainingPrice = cheapest.price;
                if (second == null)
                    second = cheapest;
                int fullAttendancePrice = second.price / second.consumableDays;
                for (int i = 0; i < cheapest.consumableDays && remainingPrice > 0; i++) {
                    AttendanceBlock ba = attendanceBlocks.get(consumedDays + i);
                    ba.setPrice(i == cheapest.consumableDays - 1 ? remainingPrice : Math.min(fullAttendancePrice, remainingPrice));
/*
                    if (update)
                        ba.documentLine.price += ba.price;
*/
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

    private boolean dateNullOrBefore(LocalDate date, LocalDate compareToDate) {
        return date == null || compareToDate == null || date.compareTo(compareToDate) < 0;
    }

    private boolean dateNullOrAfter(LocalDate date, LocalDate compareToDate) {
        return date == null || compareToDate == null || date.compareTo(compareToDate) >= 0;
    }

    private boolean rateMatchesDocument(Rate rate) {
        return true;
    }

    private int getRatePrice(Rate rate) {
        int price = rate.getPrice();
/*
        var age = document.person_age || document.person && document.person.age;
        var unemployed = document.person_unemployed || document.person && document.person.unemployed;
        var facilityFee = document.person_facilityFee || document.person && document.person.facilityFee;
        var workingVisit = document.person_workingVisit || document.person && document.person.workingVisit;
        var guest = document.person_guest || document.person && document.person.guest;
        var resident = document.person_resident || document.person && document.person.resident;
        var resident2 = document.person_resident2 || document.person && document.person.resident2;
        var discoveryReduced = document.person_discoveryReduced || document.person && document.person.discoveryReduced;
        var discovery = document.person_discovery || document.person && document.person.discovery;
        if (age || age === 0) {
            if (rate.age1_max && age <= rate.age1_max)
                price = (rate.age1_price || rate.age1_price === 0) ? rate.age1_price : price * (100 - rate.age1_discount) / 100;
            else if (rate.age2_max && age <= rate.age2_max)
                price = (rate.age2_price || rate.age2_price === 0) ? rate.age2_price : price * (100 - rate.age2_discount) / 100;
            else if (rate.age3_max && age <= rate.age3_max)
                price = (rate.age3_price || rate.age3_price === 0) ? rate.age3_price : price * (100 - rate.age3_discount) / 100;
        } else if (workingVisit && (rate.workingVisit_price || rate.workingVisit_discount))
            price = (rate.workingVisit_price || rate.workingVisit_price === 0) ? rate.workingVisit_price : price * (100 - rate.workingVisit_discount) / 100;
        else if (guest && (rate.guest_price || rate.guest_discount))
            price = (rate.guest_price || rate.guest_price === 0) ? rate.guest_price : price * (100 - rate.guest_discount) / 100;
        else if (resident && (rate.resident_price || rate.resident_discount))
            price = (rate.resident_price || rate.resident_price === 0) ? rate.resident_price : price * (100 - rate.resident_discount) / 100;
        else if (resident2 && (rate.resident2_price || rate.resident2_discount))
            price = (rate.resident2_price || rate.resident2_price === 0) ? rate.resident2_price : price * (100 - rate.resident2_discount) / 100;
        else if (discoveryReduced && (rate.discoveryReduced_price || rate.discoveryReduced_discount))
            price = (rate.discoveryReduced_price || rate.discoveryReduced_price === 0) ? rate.discoveryReduced_price : price * (100 - rate.discoveryReduced_discount) / 100;
        else if (discovery && (rate.discovery_price || rate.discovery_discount))
            price = (rate.discovery_price || rate.discovery_price === 0) ? rate.discovery_price : price * (100 - rate.discovery_discount) / 100;
        else if (unemployed && (rate.unemployed_price || rate.unemployed_discount))
            price = (rate.unemployed_price || rate.unemployed_price === 0) ? rate.unemployed_price : price * (100 - rate.unemployed_discount) / 100;
        else if (facilityFee && (rate.facilityFee_price || rate.facilityFee_discount))
            price = (rate.facilityFee_price || rate.facilityFee_price === 0) ? rate.facilityFee_price : price * (100 - rate.facilityFee_discount) / 100;
*/
        return price;
    }

    static class RateInfo {
        int dailyPrice;
        int price;
        int consumableDays;

        public RateInfo(int dailyPrice, int price, int consumableDays) {
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

}
