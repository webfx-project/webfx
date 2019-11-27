package mongoose.backend.controls.masterslave.group;

import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import mongoose.backend.controls.masterslave.UiBuilder;
import mongoose.client.presentationmodel.*;
import webfx.extras.imagestore.ImageStore;
import webfx.extras.type.PrimType;
import webfx.extras.type.Type;
import webfx.extras.type.Types;
import webfx.extras.visual.*;
import webfx.extras.visual.controls.SelectableVisualResultControl;
import webfx.extras.visual.controls.charts.VisualAreaChart;
import webfx.extras.visual.controls.charts.VisualBarChart;
import webfx.extras.visual.controls.charts.VisualPieChart;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.framework.client.orm.dql.DqlStatement;
import webfx.framework.client.orm.entity.filter.table.EntityColumn;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.builder.ReferenceResolver;
import webfx.framework.shared.orm.expression.builder.ThreadLocalReferenceResolver;
import webfx.framework.shared.orm.expression.sqlcompiler.terms.ConstantSqlCompiler;
import webfx.framework.shared.orm.expression.terms.As;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;
import webfx.framework.shared.orm.expression.terms.function.Call;
import webfx.platform.shared.util.Numbers;

import java.util.Arrays;

public final class GroupView<E extends Entity> implements UiBuilder,
        HasGroupDqlStatementProperty,
        HasGroupVisualResultProperty,
        HasGroupVisualSelectionProperty,
        HasSelectedGroupProperty<E>,
        HasSelectedGroupConditionDqlStatementProperty {

    private final ObjectProperty<DqlStatement> groupDqlStatementProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DqlStatement> groupDqlStatementProperty() { return groupDqlStatementProperty; }

    private final ObjectProperty<VisualResult> groupVisualResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualResult> groupVisualResultProperty() { return groupVisualResultProperty; }

    private final ObjectProperty<VisualSelection> groupVisualSelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualSelection> groupVisualSelectionProperty() { return groupVisualSelectionProperty; }

    private final ObjectProperty<E> selectedGroupProperty = new SimpleObjectProperty<E/*GWT*/>() {
        @Override
        protected void invalidated() {
            updateSelectedGroupCondition();
        }
    };
    @Override public ObjectProperty<E> selectedGroupProperty() { return selectedGroupProperty; }

    private final ObjectProperty<DqlStatement> selectedGroupConditionDqlStatementProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DqlStatement> selectedGroupConditionDqlStatementProperty() { return selectedGroupConditionDqlStatementProperty; }

    private ReferenceResolver referenceResolver;

    public GroupView<E> setReferenceResolver(ReferenceResolver referenceResolver) {
        this.referenceResolver = referenceResolver;
        return this;
    }

    private final boolean tableOnly;

    public GroupView() {
        this(false);
    }

    public GroupView(boolean tableOnly) {
        this.tableOnly = tableOnly;
    }

    public static <E extends Entity> GroupView<E> createAndBind(HasGroupVisualResultProperty pm) {
        return createAndBind(false, pm);
    }

    public static <E extends Entity> GroupView<E> createTableOnlyAndBind(HasGroupVisualResultProperty pm) {
        return createAndBind(true, pm);
    }

    public static <E extends Entity> GroupView<E> createAndBind(boolean tableOnly, HasGroupVisualResultProperty pm) {
        GroupView<E> groupView = new GroupView<>(/*tableOnly*/true); // always table only for now due to performance issue (to be fixed)
        groupView.doDataBinding(pm);
        return groupView;
    }

    public void doDataBinding(HasGroupVisualResultProperty pm) {
        bindWithSourceGroupVisualResultProperty(pm.groupVisualResultProperty());
        if (pm instanceof HasGroupVisualSelectionProperty)
            bindWithTargetGroupVisualSelectionProperty(((HasGroupVisualSelectionProperty) pm).groupVisualSelectionProperty());
        if (pm instanceof HasGroupDqlStatementProperty)
            bindWithSourceGroupDqlStatementProperty(((HasGroupDqlStatementProperty) pm).groupDqlStatementProperty());
        if (pm instanceof HasSelectedGroupConditionDqlStatementProperty)
            bindWithTargetSelectedGroupConditionDqlStatementProperty(((HasSelectedGroupConditionDqlStatementProperty) pm).selectedGroupConditionDqlStatementProperty());
        if (pm instanceof HasSelectedGroupProperty)
            bindWithSourceSelectedGroupProperty(((HasSelectedGroupProperty) pm).selectedGroupProperty());
        if (pm instanceof HasSelectedGroupReferenceResolver)
            setReferenceResolver(((HasSelectedGroupReferenceResolver) pm).getSelectedGroupReferenceResolver());
    }

    public void bindWithSourceGroupVisualResultProperty(ObjectProperty<VisualResult> sourceGroupVisualResultProperty) {
        if (sourceGroupVisualResultProperty != null)
            groupVisualResultProperty.bind(sourceGroupVisualResultProperty);
    }

    public void bindWithTargetGroupVisualSelectionProperty(Property<VisualSelection> targetGroupVisualSelectionProperty) {
        if (targetGroupVisualSelectionProperty != null)
            targetGroupVisualSelectionProperty.bind(groupVisualSelectionProperty);
    }

    public void bindWithSourceGroupDqlStatementProperty(Property<DqlStatement> sourceGroupDqlStatementProperty) {
        if (sourceGroupDqlStatementProperty != null)
            groupDqlStatementProperty.bind(sourceGroupDqlStatementProperty);
    }

    public void bindWithTargetSelectedGroupConditionDqlStatementProperty(Property<DqlStatement> targetSelectedGroupConditionDqlStatementProperty) {
        if (targetSelectedGroupConditionDqlStatementProperty != null)
            targetSelectedGroupConditionDqlStatementProperty.bind(selectedGroupConditionDqlStatementProperty);
    }

    public void bindWithSourceSelectedGroupProperty(ObjectProperty<E> sourceSelectedGroupProperty) {
        if (sourceSelectedGroupProperty != null)
            selectedGroupProperty.bind(sourceSelectedGroupProperty);
    }

    @Override
    public Node buildUi() {
        Node ui = tableOnly ? bindControl(new VisualGrid()) : new TabPane(
                createGroupTab("table", "images/s16/table.png",    new VisualGrid()),
                createGroupTab("pie",   "images/s16/pieChart.png", new VisualPieChart()),
                createGroupTab("bar",   "images/s16/barChart.png", new VisualBarChart()),
                createGroupTab("area",  "images/s16/barChart.png", new VisualAreaChart())
        );
        ui.getProperties().put("groupView", this); // This is to avoid GC
        return ui;
    }

    private Tab createGroupTab(String text, String iconPath, SelectableVisualResultControl control) {
        Tab tab = new Tab(text, bindControl(control));
        tab.setGraphic(ImageStore.createImageView(iconPath));
        tab.setClosable(false);
        return tab;
    }

    private <C extends SelectableVisualResultControl> C bindControl(C control) {
        if (control instanceof VisualGrid) {
            control.visualResultProperty().bind(groupVisualResultProperty());
            groupVisualSelectionProperty().bind(control.visualSelectionProperty());
        } else if (control != null)
            groupVisualResultProperty().addListener((observable, oldValue, rs) ->
                    control.setVisualResult(toSingleSeriesChartVisualResult(rs, control instanceof VisualPieChart))
            );
        return control;
    }

    private VisualResult toSingleSeriesChartVisualResult(VisualResult rs, boolean pie) {
        VisualResult result = null;
        if (rs != null) {
            int rowCount = rs.getRowCount();
            int colCount = rs.getColumnCount();
            // Searching the value column where to extract figures of the series => simply choosing the first column where type is numeric
            int valueCol = colCount - 1; // in case it's not found for any reason, we take the last column by default
            for (int col = 0; col < colCount; col++) {
                VisualColumn column = rs.getColumns()[col];
                Type type = column.getType();
                boolean isNumber = Types.isNumberType(type);
                // If not a number, it may be a formatted number (ex: Price), so checking if the source is an expression column with a numeric type
                if (!isNumber && !Types.isArrayType(type) /*to ignore columns such as family, site, item*/ && column.getSource() instanceof EntityColumn) {
                    type = ((EntityColumn) column.getSource()).getExpression().getType();
                    isNumber = Types.isNumberType(type);
                }
                if (isNumber) {
                    valueCol = col;
                    break;
                }
            }
            VisualResultBuilder rsb = new VisualResultBuilder(rowCount, new VisualColumnBuilder(null, PrimType.STRING).setRole(pie ? "series" : null).build(), VisualColumn.create(null, PrimType.INTEGER));
            for (int row = 0; row < rowCount; row++) {
                // Generating the series name by concatenating text of all columns preceding the value column
                StringBuilder sb = new StringBuilder();
                for (int col = 0; col < valueCol; col++)
                    appendTextOnly(rs.getValue(row, col), sb);
                rsb.setValue(row, 0, sb.toString());
                // Generating the series value
                Object value = rs.getValue(row, valueCol);
                if (value != null && !(value instanceof Number)) // Ex: formatted price
                    value = Numbers.doubleValue(value); // => extracting a double value
                rsb.setValue(row, 1, value);
            }
            result = rsb.build();
        }
        return result;
    }

    private void updateSelectedGroupCondition() {
        E group = getSelectedGroup();
        DqlStatement selectedGroupFilter = null;
        DqlStatement groupFilter = getGroupDqlStatement();
        if (group != null && groupFilter != null) {
            StringBuilder sb = new StringBuilder();
            ThreadLocalReferenceResolver.executeCodeInvolvingReferenceResolver(() -> {
                ExpressionArray<E> groupByExpressionArray = group.getDomainClass().parseExpressionArray(groupFilter.getGroupBy().getDql());
                ExpressionArray<E> columnsExpressionArray = null;
                for (Expression<E> columnExpression : groupByExpressionArray.getExpressions()) {
                    if (columnExpression instanceof Call) {
                        String definition = columnExpression.toString();
                        if (columnsExpressionArray == null)
                            columnsExpressionArray = group.getDomainClass().parseExpressionArray(groupFilter.getColumns());
                        columnExpression = Arrays.stream(columnsExpressionArray.getExpressions()).filter(e -> e.toString().contains(definition)).findFirst().orElse(columnExpression);
                    }
                    if (isAggregateExpression(columnExpression))
                        continue;
                    Object value = group.evaluate(columnExpression);
                    if (value instanceof EntityId)
                        value = ((EntityId) value).getPrimaryKey();
                    value = ConstantSqlCompiler.toSqlConstant(value);
                    if (columnExpression instanceof As)
                        columnExpression = ((As<E>) columnExpression).getOperand();
                    if (sb.length() > 0)
                        sb.append(" and ");
                    sb.append(columnExpression).append('=').append(value);
                }
            }, referenceResolver);
            selectedGroupFilter = DqlStatement.where(sb);
        }
        selectedGroupConditionDqlStatementProperty().set(selectedGroupFilter);
    }

    private boolean isAggregateExpression(Expression<?> expression) {
        if (expression instanceof As)
            expression = ((As<?>) expression).getOperand();
        if (expression instanceof Call)
            switch (((Call<?>) expression).getFunctionName()) {
                case "count":
                case "sum":
                    return true;
        }
        return false;
    }

    private static void appendTextOnly(Object value, StringBuilder sb) {
        if (value == null)
            return;
        if (value instanceof Object[]) {
            for (Object v : (Object[]) value)
                appendTextOnly(v, sb);
        } else {
            String text = value.toString();
            if (text != null && !text.isEmpty() && !text.startsWith("images/")) {
                if (sb.length() > 0)
                    sb.append(' ');
                sb.append(text);
            }
        }
    }
}
