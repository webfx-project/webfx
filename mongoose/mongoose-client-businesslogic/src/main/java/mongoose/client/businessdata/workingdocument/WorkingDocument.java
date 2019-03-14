package mongoose.client.businessdata.workingdocument;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import mongoose.client.aggregates.event.EventAggregate;
import mongoose.client.businesslogic.pricing.WorkingDocumentPricing;
import mongoose.client.businesslogic.workingdocument.BusinessLines;
import mongoose.client.businesslogic.workingdocument.BusinessType;
import mongoose.client.businesslogic.workingdocument.WorkingDocumentLogic;
import mongoose.shared.entities.*;
import mongoose.shared.entities.markers.EntityHasPersonalDetails;
import mongoose.shared.entities.markers.HasPersonalDetails;
import mongoose.shared.businessdata.time.DateTimeRange;
import webfx.framework.shared.orm.entity.*;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
public final class WorkingDocument {

    private final EventAggregate eventAggregate;
    private final Document document;
    private final ObservableList<WorkingDocumentLine> workingDocumentLines;
    private Integer computedPrice;
    private UpdateStore updateStore;
    private WorkingDocument loadedWorkingDocument;
    private boolean changedSinceLastApplyBusinessRules = true;
    private Predicate<Option> isOptionSelectedInOptionTreePredicate;

    public WorkingDocument(EventAggregate eventAggregate, List<WorkingDocumentLine> workingDocumentLines) {
        this(eventAggregate, eventAggregate.getPersonAggregate().getPreselectionProfilePerson(), workingDocumentLines);
    }

    public WorkingDocument(EventAggregate eventAggregate, WorkingDocument wd, List<WorkingDocumentLine> workingDocumentLines) {
        this(eventAggregate, createDocument(wd.getDocument()), workingDocumentLines);
        loadedWorkingDocument = wd.loadedWorkingDocument;
    }

    public WorkingDocument(EventAggregate eventAggregate, Person person, List<WorkingDocumentLine> workingDocumentLines) {
        this(eventAggregate, createDocument(person), workingDocumentLines);
    }

    public WorkingDocument(EventAggregate eventAggregate, Document document, List<WorkingDocumentLine> lines) {
        this.eventAggregate = eventAggregate;
        this.document = document;
        workingDocumentLines = FXCollections.observableArrayList(lines);
        Collections.forEach(workingDocumentLines, wdl -> wdl.setWorkingDocument(this));
        workingDocumentLines.addListener((ListChangeListener<WorkingDocumentLine>) c -> {
            clearLinesCache();
            clearComputedDateTimeRange();
            clearComputedPrice();
            markAsChangedForBusinessRules();
        });
    }

    // Constructor used to make a copy (that can be changed) of a loaded working document (that shouldn't be changed).
    // When submitting this copy, some decisions are made by comparison with the original loaded document.
    public WorkingDocument(WorkingDocument loadedWorkingDocument) {
        this(loadedWorkingDocument.getEventAggregate(), createDocument(loadedWorkingDocument.getDocument()), new ArrayList<>(loadedWorkingDocument.getWorkingDocumentLines()));
        this.loadedWorkingDocument = loadedWorkingDocument;
    }

    public EventAggregate getEventAggregate() {
        return eventAggregate;
    }

    public WorkingDocument getLoadedWorkingDocument() {
        return loadedWorkingDocument;
    }

    public Document getDocument() {
        return document;
    }

    public List<WorkingDocumentLine> getWorkingDocumentLines() {
        return workingDocumentLines;
    }


    public Predicate<Option> getIsOptionSelectedInOptionTreePredicate() {
        return isOptionSelectedInOptionTreePredicate;
    }

    public void setIsOptionSelectedInOptionTreePredicate(Predicate<Option> isOptionSelectedInOptionTreePredicate) {
        this.isOptionSelectedInOptionTreePredicate = isOptionSelectedInOptionTreePredicate;
    }

    private DateTimeRange dateTimeRange;

    public DateTimeRange getDateTimeRange() {
        if (dateTimeRange == null)
            computeDocumentDateTimeRangeFromLines();
        return dateTimeRange;
    }

    private void computeDocumentDateTimeRangeFromLines() {
        dateTimeRange = DateTimeRange.merge(Collections.filterMap(getWorkingDocumentLines(), WorkingDocument::isWorkingDocumentLineToBeIncludedInWorkingDocumentDateTimeRange, WorkingDocumentLine::getDateTimeRange));
        //System.out.println(dateTimeRange.getText());
    }

    void clearComputedDateTimeRange() {
        dateTimeRange = null;
    }

    private static boolean isWorkingDocumentLineToBeIncludedInWorkingDocumentDateTimeRange(WorkingDocumentLine wdl) {
        return wdl.getDayTimeRange() != null; // Excluding lines with no day time range (ex: diet option)
    }

    public void markAsChangedForBusinessRules() {
        changedSinceLastApplyBusinessRules = true;
    }

    public WorkingDocument applyBusinessRules() {
        if (changedSinceLastApplyBusinessRules) {
            WorkingDocumentLogic.applyBusinessRules(this);
            changedSinceLastApplyBusinessRules = false;
        }
        return this;
    }

    public void clearComputedPrice() {
        computedPrice = null;
    }

    public int getComputedPrice() {
        if (computedPrice == null)
            computedPrice = computePrice();
        return computedPrice;
    }

    public int computePrice() {
        return computedPrice = WorkingDocumentPricing.computeDocumentPrice(this);
    }

    private final Map<BusinessType, BusinessLines> businessLinesMap = new HashMap<>();

    private void clearLinesCache() {
        businessLinesMap.clear();
    }

    public BusinessLines getBusinessLines(BusinessType businessType) {
        BusinessLines businessLines = businessLinesMap.get(businessType);
        if (businessLines == null)
            businessLinesMap.put(businessType, businessLines = new BusinessLines(businessType, this));
        return businessLines;
    }

    public boolean hasBusinessLines(BusinessType businessType) {
        return !getBusinessLines(businessType).isEmpty();
    }

    public void removeBusinessLines(BusinessType businessType) {
        getBusinessLines(businessType).removeAllLines();
    }

    @Deprecated
    public WorkingDocumentLine getBusinessLine(BusinessType businessType) {
        return Collections.first(getBusinessLines(businessType).getBusinessWorkingDocumentLines());
    }

    //// Accommodation line

    @Deprecated
    public WorkingDocumentLine getAccommodationLine() {
        return getBusinessLine(BusinessType.ACCOMMODATION);
    }

    public boolean hasAccommodation() {
        return hasBusinessLines(BusinessType.ACCOMMODATION);
    }

    //// Breakfast line

    public void removeBreakfast() {
        removeBusinessLines(BusinessType.BREAKFAST);
    }

    //// Lunch line

    public boolean hasLunch() {
        return hasBusinessLines(BusinessType.LUNCH);
    }

    //// Supper line

    public boolean hasSupper() {
        return hasBusinessLines(BusinessType.SUPPER);
    }

    public boolean hasMeals() {
        return hasLunch() || hasSupper();
    }


    //// Diet line

    @Deprecated
    public WorkingDocumentLine getDietLine() {
        return getBusinessLine(BusinessType.DIET);
    }

    public void removeDiet() {
        removeBusinessLines(BusinessType.DIET);
    }

    //// TouristTax line

    public boolean hasTouristTax() {
        return hasBusinessLines(BusinessType.TOURIST_TAX);
    }

    public void removeTouristTax() {
        removeBusinessLines(BusinessType.TOURIST_TAX);
    }

    //// Teaching line

    @Deprecated
    public WorkingDocumentLine getTeachingLine() {
        return getBusinessLine(BusinessType.TEACHING);
    }

    public boolean hasTeaching() {
        return hasBusinessLines(BusinessType.TEACHING);
    }

    //// Translation line

    @Deprecated
    public WorkingDocumentLine getTranslationLine() {
        return getBusinessLine(BusinessType.TRANSLATION);
    }

    public boolean hasTranslation() {
        return hasBusinessLines(BusinessType.TRANSLATION);
    }

    public void removeTranslation() {
        removeBusinessLines(BusinessType.TRANSLATION);
    }

    //// Hotel shuttle lines

    public void removeHotelShuttle() {
        removeBusinessLines(BusinessType.HOTEL_SHUTTLE);
    }


    public void syncPersonDetails(HasPersonalDetails p) {
        syncPersonDetails(p, document);
    }

    public UpdateStore getUpdateStore() {
        if (updateStore == null)
            updateStore = getUpdateStore(document);
        return updateStore;
    }

    private static UpdateStore getUpdateStore(Entity entity) {
        return getUpdateStore(entity.getStore());
    }

    private static UpdateStore getUpdateStore(EntityStore store) {
        return store instanceof UpdateStore ? (UpdateStore) store : UpdateStore.createAbove(store);
    }

    private static Document createDocument(EntityHasPersonalDetails personDetailsEntity) {
        UpdateStore store = getUpdateStore(personDetailsEntity);
        Document document;
        if (personDetailsEntity instanceof Document) // If from an original document, just making a copy
            document = store.copyEntity((Document) personDetailsEntity);
        else // otherwise creating a new document
            syncPersonDetails(personDetailsEntity, document = store.createEntity(Document.class));
        return document;
    }

    public static void syncPersonDetails(HasPersonalDetails p1, HasPersonalDetails p2) {
        p2.setFirstName(p1.getFirstName());
        p2.setLastName(p1.getLastName());
        p2.setLayName(p1.getLayName());
        p2.setMale(p1.isMale());
        p2.setOrdained(p1.isOrdained());
        p2.setAge(p1.getAge());
        p2.setCarer1Name(p1.getCarer1Name());
        p2.setCarer2Name(p1.getCarer2Name());
        p2.setEmail(p1.getEmail());
        p2.setPhone(p1.getPhone());
        p2.setStreet(p1.getStreet());
        p2.setPostCode(p1.getPostCode());
        p2.setCityName(p1.getCityName());
        p2.setAdmin1Name(p1.getAdmin1Name());
        p2.setAdmin2Name(p1.getAdmin2Name());
        p2.setCountryName(p1.getCountryName());
        p2.setCountry(p1.getCountry());
        p2.setOrganization(p1.getOrganization());
        p2.setUnemployed(p1.isUnemployed());
        p2.setFacilityFee(p1.isFacilityFee());
        p2.setWorkingVisit(p1.isWorkingVisit());
        p2.setDiscovery(p1.isDiscovery());
        p2.setDiscoveryReduced(p1.isDiscoveryReduced());
        p2.setGuest(p1.isGuest());
        p2.setResident(p1.isResident());
        p2.setResident2(p1.isResident2());
    }

    public WorkingDocumentLine findSameWorkingDocumentLine(WorkingDocumentLine wdl) {
        for (WorkingDocumentLine thisWdl : getWorkingDocumentLines()) {
            if (sameLine(thisWdl, wdl))
                return thisWdl;
        }
        return null;
    }

    private static boolean sameLine(WorkingDocumentLine wdl1, WorkingDocumentLine wdl2) {
        return wdl1 == wdl2 || wdl1 != null && (
                wdl1.getOption() != null && wdl2.getOption() != null ? wdl1.getOption() == wdl2.getOption() :
                Entities.sameId(wdl1.getSite(), wdl2.getSite()) &&
                Entities.sameId(wdl1.getItem(), wdl2.getItem())
        );
    }

    public void setEventActive() {
        ActiveWorkingDocumentsByEventStore.setEventActiveWorkingDocument(this);
    }

}
