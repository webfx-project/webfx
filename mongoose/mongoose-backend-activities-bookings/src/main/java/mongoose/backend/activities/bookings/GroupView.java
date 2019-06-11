package mongoose.backend.activities.bookings;

import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import mongoose.shared.entities.Document;
import webfx.framework.client.ui.filter.StringFilter;
import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.expression.builder.ReferenceResolver;
import webfx.framework.shared.expression.builder.ThreadLocalReferenceResolver;
import webfx.framework.shared.expression.terms.As;
import webfx.framework.shared.expression.terms.ExpressionArray;
import webfx.framework.shared.expression.terms.function.Call;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.fxkit.extra.controls.displaydata.SelectableDisplayResultControl;
import webfx.fxkit.extra.controls.displaydata.chart.AreaChart;
import webfx.fxkit.extra.controls.displaydata.chart.BarChart;
import webfx.fxkit.extra.controls.displaydata.chart.PieChart;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.extra.displaydata.*;
import webfx.fxkit.extra.type.PrimType;
import webfx.fxkit.extra.util.ImageStore;

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

    private ReferenceResolver referenceResolver;

    public void setReferenceResolver(ReferenceResolver referenceResolver) {
        this.referenceResolver = referenceResolver;
    }

    Node buildUi() {
        return new TabPane(
                createGroupTab("pie",   "images/s16/pieChart.png", new PieChart()),
                createGroupTab("table", "images/s16/table.png",    new DataGrid()),
                createGroupTab("bar",   "images/s16/barChart.png", new BarChart()),
                createGroupTab("area",  "images/s16/barChart.png", new AreaChart())
        );
    }

    private Tab createGroupTab(String text, String iconPath, SelectableDisplayResultControl control) {
        Tab tab = new Tab(text, control);
        tab.setGraphic(ImageStore.createImageView(iconPath));
        tab.setClosable(false);
        if (control instanceof DataGrid) {
            control.displayResultProperty().bind(groupDisplayResultProperty());
            groupDisplaySelectionProperty().bind(control.displaySelectionProperty());
        } else if (control != null)
            groupDisplayResultProperty().addListener((observable, oldValue, rs) ->
                    control.setDisplayResult(toSingleSeriesChartDisplayResult(rs, control instanceof PieChart))
            );
        return tab;
    }

    private DisplayResult toSingleSeriesChartDisplayResult(DisplayResult rs, boolean pie) {
        DisplayResult result = null;
        if (rs != null) {
            int rowCount = rs.getRowCount();
            int colCount = rs.getColumnCount();
            int nameCol = 0;
            int valCol = colCount - 1; // The last column contains the number
            DisplayResultBuilder rsb = new DisplayResultBuilder(rowCount, new DisplayColumn[]{new DisplayColumnBuilder(null, PrimType.STRING).setRole(pie ? "series" : null).build(), DisplayColumn.create(null, PrimType.INTEGER)});
            for (int row = 0; row < rowCount; row++) {
                Object nameValue = rs.getValue(row, nameCol);
                if (nameValue instanceof Object[])  // probably icon, name => picking up name
                    nameValue = Arrays.stream((Object[]) nameValue).filter(x -> x instanceof String).map(x -> (String) x).filter(s -> !s.startsWith("images/")).findFirst().orElse("?");
                if (nameValue instanceof String && ((String) nameValue).startsWith("images/") && row == 0 && nameCol < colCount - 1) {
                    nameCol++;
                    row--;
                    continue;
                }
                rsb.setValue(row, 0, nameValue);
                rsb.setValue(row, 1, rs.getValue(row, valCol));
            }
            result = rsb.build();
        }
        return result;
    }

    private void updateSelectedGroupCondition() {
        Document group = getSelectedGroup();
        String sf = null;
        String gsf = getGroupStringFilter();
        if (group != null && gsf != null) {
            StringBuilder sb = new StringBuilder();
            ThreadLocalReferenceResolver.executeCodeInvolvingReferenceResolver(() -> {
                ExpressionArray<Expression> columnsExpressionArray = group.getDomainClass().parseExpressionArray(new StringFilter(gsf).getColumns());
                for (Expression columnExpression : columnsExpressionArray.getExpressions()) {
                    if (isAggregateExpression(columnExpression))
                        continue;
                    if (sb.length() > 0)
                        sb.append(" and ");
                    Object value = group.evaluate(columnExpression);
                    if (value instanceof EntityId)
                        value = ((EntityId) value).getPrimaryKey();
                    if (value instanceof String)
                        value = "'" + value + "'";
                    if (columnExpression instanceof As)
                        columnExpression = ((As) columnExpression).getOperand();
                    sb.append(columnExpression).append('=').append(value);
                }
            }, referenceResolver);
            sf = "{where: `" + sb + "`}";
        }
        selectedGroupConditionStringFilterProperty().set(sf);
    }

    private boolean isAggregateExpression(Expression expression) {
        if (expression instanceof As)
            expression = ((As) expression).getOperand();
        if (expression instanceof Call)
            switch (((Call) expression).getFunctionName()) {
                case "count":
                case "sum":
                    return true;
        }
        return false;
    }
}
