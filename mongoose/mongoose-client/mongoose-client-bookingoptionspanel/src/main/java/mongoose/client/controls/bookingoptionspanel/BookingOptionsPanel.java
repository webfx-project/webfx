package mongoose.client.controls.bookingoptionspanel;

import javafx.scene.layout.BorderPane;
import mongoose.client.businessdata.workingdocument.WorkingDocument;
import mongoose.client.businessdata.workingdocument.WorkingDocumentLine;
import mongoose.client.controls.sectionpanel.SectionPanelFactory;
import mongoose.client.util.functions.TranslateFunction;
import mongoose.shared.entities.formatters.EventPriceFormatter;
import mongoose.shared.entities.DocumentLine;
import mongoose.shared.entities.Item;
import mongoose.shared.businessdata.time.DaysArray;
import mongoose.shared.businessdata.time.DaysArrayBuilder;
import webfx.framework.client.services.i18n.I18n;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.framework.shared.orm.expression.terms.function.AggregateFunction;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.EntitiesToVisualResultMapper;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.extras.visual.controls.grid.SkinnedVisualGrid;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.SelectionMode;
import webfx.extras.type.PrimType;
import webfx.kit.util.properties.Properties;
import webfx.platform.shared.util.Objects;
import webfx.platform.shared.util.collection.Collections;

import java.util.List;

import static webfx.framework.shared.orm.domainmodel.formatter.FormatterRegistry.registerFormatter;

/**
 * @author Bruno Salmon
 */
public final class BookingOptionsPanel {

    private final VisualGrid visualGrid;
    private BorderPane optionsPanel;
    private EntityList<DocumentLine> lineEntities;

    public BookingOptionsPanel() {
        visualGrid = new SkinnedVisualGrid(); // LayoutUtil.setMinMaxHeightToPref(new DataGrid());
        visualGrid.setHeaderVisible(false);
        visualGrid.setFullHeight(true);
        visualGrid.setSelectionMode(SelectionMode.DISABLED);
        new AggregateFunction<DocumentLine>("days_agg", PrimType.STRING) {
            @Override
            public Object evaluateOnAggregates(DocumentLine referrer, Object[] aggregates, Expression<DocumentLine> operand, DomainReader<DocumentLine> domainReader) {
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
        registerFormatter("priceWithCurrency", new EventPriceFormatter(workingDocument.getEventAggregate().getEvent()));
        workingDocument.getComputedPrice(); // ensuring the price has been computed
        //Doesn't work on Android: syncUiFromModel(workingDocument.getWorkingDocumentLines().stream().map(BookingOptionsPanel::createDocumentLine).filter(Objects::nonNull).collect(Collectors.toList()), workingDocument.getDocument().getStore());
        syncUiFromModel(Collections.mapFilter(workingDocument.getWorkingDocumentLines(), BookingOptionsPanel::createDocumentLine, Objects::nonNull), workingDocument.getDocument().getStore());
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
            VisualResult rs = generateGroupedLinesResult();
            visualGrid.setVisualResult(rs);
        }
    }

    private VisualResult generateDetailedLinesResult() {
        return EntitiesToVisualResultMapper.selectAndMapEntitiesToVisualResult(lineEntities,
                "select [" +
                        "'item.icon'," +
                        "'translate(item)'," +
                        "{expression: 'dates', textAlign: 'center'}," +
                        "{expression: 'price_net', format: 'priceWithCurrency'}" +
                        "] from DocumentLine where dates<>'' order by item.family.ord,item.name");
    }

    private VisualResult generateGroupedLinesResult() {
        return EntitiesToVisualResultMapper.selectAndMapEntitiesToVisualResult(lineEntities,
                "select [" +
                        // Displaying the actual item if only one is present for the item family, otherwise just displaying the item family (without further details)
                        "'item.family.icon, sum(1) != 1 ? translate(item.family) : string_agg(translate(item), `, ` order by item.name)'," +
                        "{expression: 'days_agg()', textAlign: 'center'}," +
                        "{expression: 'sum(price_net)', format: 'priceWithCurrency'}" +
                        "] from DocumentLine where dates<>'' group by item.family order by item.family.ord");
    }

    public VisualGrid getGrid() {
        return visualGrid;
    }

    public BorderPane getOptionsPanel() {
        if (optionsPanel == null) {
            optionsPanel = SectionPanelFactory.createSectionPanel("YourOptions");
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
