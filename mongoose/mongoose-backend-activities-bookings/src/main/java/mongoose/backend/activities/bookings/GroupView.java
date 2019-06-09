package mongoose.backend.activities.bookings;

import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import mongoose.shared.entities.Document;
import webfx.framework.client.ui.filter.StringFilter;
import webfx.framework.shared.expression.builder.ReferenceResolver;
import webfx.framework.shared.expression.builder.ThreadLocalReferenceResolver;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.fxkit.extra.controls.displaydata.chart.PieChart;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.extra.displaydata.*;
import webfx.fxkit.extra.type.PrimType;
import webfx.fxkit.extra.util.ImageStore;
import webfx.platform.shared.util.Strings;

import java.util.Arrays;

final class GroupView {

    private final Property<DisplayResult> groupDisplayResultProperty = new SimpleObjectProperty<>();
    Property<DisplayResult> groupDisplayResultProperty() { return groupDisplayResultProperty; }

    private final Property<DisplaySelection> groupDisplaySelectionProperty = new SimpleObjectProperty<>();
    Property<DisplaySelection> groupDisplaySelectionProperty() { return groupDisplaySelectionProperty; }

    private final StringProperty groupStringFilterProperty = new SimpleStringProperty();
    StringProperty groupStringFilterProperty() { return groupStringFilterProperty; }
    String getGroupStringFilter() { return groupStringFilterProperty().get(); }

    private final ObjectProperty<Document> selectedGroupProperty = new SimpleObjectProperty<Document>() { // GWT doesn't compile <>
        @Override
        protected void invalidated() {
            updateSelectedGroupCondition();
        }
    };
    ObjectProperty<Document> selectedGroupProperty() {
        return selectedGroupProperty;
    }
    void setSelectedGroup(Document selectedGroup) {
        selectedGroupProperty.set(selectedGroup);
    }
    Document getSelectedGroup() {
        return selectedGroupProperty.get();
    }

    private final StringProperty selectedGroupConditionStringFilterProperty = new SimpleStringProperty();
    StringProperty selectedGroupConditionStringFilterProperty() { return selectedGroupConditionStringFilterProperty; }

    private final BooleanProperty visibleProperty = new SimpleBooleanProperty() {
        @Override
        protected void invalidated() {
            updateSelectedGroupCondition();
        }
    };
    BooleanProperty visibleProperty() {
        return visibleProperty;
    }

    private ReferenceResolver referenceResolver;

    public void setReferenceResolver(ReferenceResolver referenceResolver) {
        this.referenceResolver = referenceResolver;
    }

    Node buildUi() {
        DataGrid groupTable = new DataGrid();
        groupTable.displayResultProperty().bind(groupDisplayResultProperty());
        groupDisplaySelectionProperty().bind(groupTable.displaySelectionProperty());

        PieChart groupChart = new PieChart();
        groupDisplayResultProperty().addListener((observable, oldValue, rs) -> {
            DisplayResult pieDisplayResult = null;
            if (rs != null && visibleProperty.get()) {
                int rowCount = rs.getRowCount();
                int colCount = rs.getColumnCount();
                int nameCol = 0;
                int valCol = colCount - 1; // The last column contains the number
                DisplayResultBuilder rsb = new DisplayResultBuilder(rowCount, new DisplayColumn[]{new DisplayColumnBuilder(null, PrimType.STRING).setRole("series").build(), DisplayColumn.create(null, PrimType.INTEGER)});
                for (int row = 0; row < rowCount; row++) {
                    Object value = rs.getValue(row, nameCol);
                    if (value instanceof Object[])  // probably icon, name => picking up name
                        value = Arrays.stream((Object[]) value).filter(x -> x instanceof String).map(x -> (String) x).filter(s -> !s.startsWith("images/")).findFirst().orElse("?");
                    if (value instanceof String && ((String) value).startsWith("images/") && row == 0 && nameCol < colCount - 1) {
                        nameCol++;
                        row--;
                        continue;
                    }
                    rsb.setValue(row, 0, value);
                    rsb.setValue(row, 1, rs.getValue(row, valCol));
                }
                pieDisplayResult = rsb.build();
            }
            groupChart.setDisplayResult(pieDisplayResult);
        });

        Tab tableTab = new Tab("table", groupTable);
        tableTab.setGraphic(ImageStore.createImageView("images/s16/table.png"));
        tableTab.setClosable(false);
        Tab pieTab = new Tab("pie", groupChart);
        pieTab.setGraphic(ImageStore.createImageView("images/s16/pieChart.png"));
        pieTab.setClosable(false);
        return new TabPane(pieTab, tableTab);
    }

    private void updateSelectedGroupCondition() {
        Document group = getSelectedGroup();
        String sf = null;
        String gsf = getGroupStringFilter();
        if (group != null && gsf != null) {
            StringBuilder sb = new StringBuilder();
            ThreadLocalReferenceResolver.executeCodeInvolvingReferenceResolver(() -> {
                for (String groupToken : Strings.split(new StringFilter(gsf).getGroupBy(), ",")) {
                    if (sb.length() > 0)
                        sb.append(" and ");
                    Object value = group.evaluate(groupToken);
                    if (value instanceof EntityId)
                        value = ((EntityId) value).getPrimaryKey();
                    if (value instanceof String)
                        value = "'" + value + "'";
                    sb.append(groupToken).append('=').append(value);
                }
            }, referenceResolver);
            sf = "{where: `" + sb + "`}";
        }
        selectedGroupConditionStringFilterProperty().set(sf);
    }
}
