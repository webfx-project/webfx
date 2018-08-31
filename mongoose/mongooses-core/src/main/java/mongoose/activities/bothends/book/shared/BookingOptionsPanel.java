package mongoose.activities.bothends.book.shared;

import javafx.scene.layout.BorderPane;
import mongoose.activities.bothends.generic.MongooseSectionFactoryMixin;
import mongoose.activities.bothends.logic.time.DaysArray;
import mongoose.activities.bothends.logic.time.DaysArrayBuilder;
import mongoose.activities.bothends.logic.work.WorkingDocument;
import mongoose.activities.bothends.logic.work.WorkingDocumentLine;
import mongoose.domainmodel.formatters.PriceFormatter;
import mongoose.entities.DocumentLine;
import mongoose.entities.Item;
import webfx.framework.expression.Expression;
import webfx.framework.expression.lci.DataReader;
import webfx.framework.expression.terms.function.AggregateFunction;
import webfx.framework.orm.entity.EntityList;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.services.i18n.I18n;
import webfx.framework.ui.mapping.EntityListToDisplayResultGenerator;
import webfx.fx.properties.Properties;
import webfx.fxdata.control.DataGrid;
import webfx.fxdata.control.SkinnedDataGrid;
import webfx.fxdata.displaydata.DisplayResult;
import webfx.fxdata.displaydata.SelectionMode;
import webfx.type.PrimType;
import webfx.util.collection.Collections;

import java.util.List;

import static webfx.framework.ui.formatter.FormatterRegistry.registerFormatter;

/**
 * @author Bruno Salmon
 */
public class BookingOptionsPanel implements MongooseSectionFactoryMixin {

    private final DataGrid dataGrid;
    private BorderPane optionsPanel;
    private EntityList<DocumentLine> lineEntities;

    public BookingOptionsPanel() {
        dataGrid = new SkinnedDataGrid(); // LayoutUtil.setMinMaxHeightToPref(new DataGrid());
        dataGrid.setHeaderVisible(false);
        dataGrid.setFullHeight(true);
        dataGrid.setSelectionMode(SelectionMode.DISABLED);
        new AggregateFunction<DocumentLine>("days_agg", PrimType.STRING) {
            @Override
            public Object evaluateOnAggregates(DocumentLine referrer, Object[] aggregates, Expression<DocumentLine> operand, DataReader<DocumentLine> dataReader) {
                DaysArrayBuilder daysArrayBuilder = new DaysArrayBuilder();
                for (Object dl : aggregates) {
                    DaysArray daysArray = (DaysArray) ((DocumentLine) dl).getFieldValue("daysArray");
                    daysArrayBuilder.addSeries(daysArray.toSeries(), null);
                }
                return daysArrayBuilder.build().toSeries().toText("dd/MM");
            }
        }.register();
        new TranslateFunction().register();
        Properties.runOnPropertiesChange(this::updateGrid, I18n.dictionaryProperty());
    }

    public void syncUiFromModel(WorkingDocument workingDocument) {
        registerFormatter("priceWithCurrency", new PriceFormatter(workingDocument.getEventAggregate().getEvent()));
        workingDocument.getComputedPrice(); // ensuring the price has been computed
        //Doesn't work on Android: syncUiFromModel(workingDocument.getWorkingDocumentLines().stream().map(BookingOptionsPanel::createDocumentLine).filter(Objects::nonNull).collect(Collectors.toList()), workingDocument.getDocument().getStore());
        syncUiFromModel(Collections.mapFilter(workingDocument.getWorkingDocumentLines(), BookingOptionsPanel::createDocumentLine, webfx.util.Objects::nonNull), workingDocument.getDocument().getStore());
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
            DisplayResult rs = generateGroupedLinesResult();
            dataGrid.setDisplayResult(rs);
        }
    }

    private DisplayResult generateDetailedLinesResult() {
        return EntityListToDisplayResultGenerator.select(lineEntities,
                "select [" +
                        "'item.icon'," +
                        "'translate(item)'," +
                        "{expression: 'dates', textAlign: 'center'}," +
                        "{expression: 'price_net', format: 'priceWithCurrency'}" +
                        "] from DocumentLine where dates<>'' order by item.family.ord,item.name");
    }

    private DisplayResult generateGroupedLinesResult() {
        return EntityListToDisplayResultGenerator.select(lineEntities,
                "select [" +
                        "'item.family.icon," +
                        "translate(item.family) + (item.family.code in (`teach`, `meals`) ? `` : `: ` + string_agg(translate(item), `, ` order by item.name))'," +
                        "{expression: 'days_agg()', textAlign: 'center'}," +
                        "{expression: 'sum(price_net)', format: 'priceWithCurrency'}" +
                        "] from DocumentLine where dates<>'' group by item.family order by item.family.ord");
    }

    public DataGrid getGrid() {
        return dataGrid;
    }

    public BorderPane getOptionsPanel() {
        if (optionsPanel == null) {
            optionsPanel = createSectionPanel("YourOptions");
            optionsPanel.setCenter(getGrid());
        }
        return optionsPanel;
    }

    private static DocumentLine createDocumentLine(WorkingDocumentLine wdl) {
        Item item = wdl.getItem();
        EntityStore store = item.getStore();
        DocumentLine dl = store.createEntity(DocumentLine.class);
        dl.setSite(wdl.getSite());
        dl.setItem(item);
        dl.setFieldValue("price_net", wdl.getPrice());
        dl.setFieldValue("daysArray", wdl.getDaysArray());
        dl.setFieldValue("dates", wdl.getDaysArray().toSeries().toText("dd/MM"));
        return dl;
    }
}
