package mongoose.activities.shared.book.event.shared;

import javafx.scene.layout.BorderPane;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.domainmodel.format.PriceFormatter;
import mongoose.entities.DocumentLine;
import mongoose.entities.Item;
import naga.commons.util.collection.Collections;
import naga.framework.expression.terms.function.Function;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;
import naga.framework.ui.filter.ExpressionColumn;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.mapping.EntityListToDisplayResultSetGenerator;
import naga.fx.properties.Properties;
import naga.fxdata.control.DataGrid;
import naga.fxdata.displaydata.DisplayResultSet;

import java.util.List;

import static naga.framework.ui.format.FormatterRegistry.registerFormatter;

/**
 * @author Bruno Salmon
 */
public class BookingOptionsPanel {

    private final I18n i18n;
    private final DataGrid dataGrid;
    private BorderPane optionsPanel;
    private EntityList<DocumentLine> lineEntities;

    public BookingOptionsPanel(I18n i18n) {
        this.i18n = i18n;
        this.dataGrid = new DataGrid();
        Properties.runOnPropertiesChange(p -> updateGrid(), i18n.dictionaryProperty());
    }

    public void syncUiFromModel(WorkingDocument workingDocument) {
        registerFormatter("priceWithCurrency", new PriceFormatter(workingDocument.getEventService().getEvent()));
        //Doesn't work on Android: syncUiFromModel(workingDocument.getWorkingDocumentLines().stream().map(BookingOptionsPanel::createDocumentLine).filter(Objects::nonNull).collect(Collectors.toList()), workingDocument.getDocument().getStore());
        syncUiFromModel(Collections.mapFilter(workingDocument.getWorkingDocumentLines(), BookingOptionsPanel::createDocumentLine, naga.commons.util.Objects::nonNull), workingDocument.getDocument().getStore());
    }

    public void syncUiFromModel(List<DocumentLine> documentLines, EntityStore entityStore) {
        syncUiFromModel(EntityList.create("bookingOptions", entityStore, documentLines));
    }

    public void syncUiFromModel(EntityList<DocumentLine> lineEntities) {
        this.lineEntities = lineEntities;
        updateGrid();
    }

    private void updateGrid() {
        if (lineEntities != null) {
            DisplayResultSet rs = generateDetailedResultSet();
            dataGrid.setDisplayResultSet(rs);
        }
    }

    private DisplayResultSet generateDetailedResultSet() {
        DomainModel domainModel = lineEntities.getStore().getDataSourceModel().getDomainModel();
        Function.register(new TranslateFunction(i18n));
        ExpressionColumn[] columns = ExpressionColumn.fromJsonArray("['item.icon,translate(item)',{expression: 'dates', textAlign: 'center'},{expression: 'price_net', format: 'priceWithCurrency'}]", domainModel, "DocumentLine");
        lineEntities.orderBy(domainModel.parseExpression("order by item.family.ord,item.name", "DocumentLine"));
        return EntityListToDisplayResultSetGenerator.createDisplayResultSet(lineEntities, columns, i18n);
    }

    public DataGrid getGrid() {
        return dataGrid;
    }

    public BorderPane getOptionsPanel() {
        if (optionsPanel == null) {
            optionsPanel = HighLevelComponents.createSectionPanel(null, null, "YourOptions", i18n);
            optionsPanel.setCenter(getGrid());
        }
        return optionsPanel;
    }

    private static DocumentLine createDocumentLine(WorkingDocumentLine wdl) {
        Item item = wdl.getItem();
        EntityStore store = item.getStore();
        EntityId documentLineId = EntityId.create(store.getDataSourceModel().getDomainModel().getClass("DocumentLine"));
        DocumentLine dl = store.getOrCreateEntity(documentLineId);
        dl.setSite(wdl.getSite());
        dl.setItem(item);
        dl.setFieldValue("price_net", wdl.getPrice());
        dl.setFieldValue("dates", wdl.getDaysArray().toSeries().toText("dd/MM"));
        return dl;
    }
}
