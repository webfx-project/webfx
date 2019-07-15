package webfx.framework.client.ui.controls.sheet;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import webfx.framework.client.ui.filter.ExpressionColumn;
import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.util.formatter.Formatter;
import webfx.framework.shared.util.formatter.Parser;
import webfx.fxkit.extra.cell.renderer.ValueRenderer;
import webfx.fxkit.extra.cell.renderer.ValueRenderingContext;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.extra.controls.displaydata.datagrid.SkinnedDataGrid;
import webfx.fxkit.extra.displaydata.*;
import webfx.fxkit.extra.displaydata.impl.DisplayColumnImpl;
import webfx.fxkit.extra.type.Types;
import webfx.fxkit.extra.util.ImageStore;
import webfx.platform.client.services.uischeduler.AnimationFramePass;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.util.Arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public final class EntityPropertiesSheet<E extends Entity> extends EntityUpdateDialog<E> {

    private final static DisplayColumn LABEL_COLUMN = DisplayColumn.create((value, context) -> new Label(((webfx.fxkit.extra.label.Label) value).getText(), ImageStore.createImageView(((webfx.fxkit.extra.label.Label) value).getIconPath())));
    private final static DisplayColumn VALUE_COLUMN = new DisplayColumnImpl(null, null, null, null, DisplayStyle.CENTER_STYLE, (value, context) -> (Node) value, null, null);

    private final ExpressionColumn[] expressionColumns;
    private final ValueRenderingContext[] valueRenderingContexts;
    private final Node[] renderingNodes;
    private ExpressionColumn[] applicableExpressionColumns;
    private boolean tableLayout = true;

    private EntityPropertiesSheet(E entity, String expressionColumns) {
        this(ExpressionColumn.fromJsonArrayOrExpressionsDefinition(expressionColumns, entity.getDomainClass()));
        setEntity(entity);
    }

    private EntityPropertiesSheet(ExpressionColumn[] expressionColumns) {
        this.expressionColumns = expressionColumns;
        valueRenderingContexts = Arrays.map(expressionColumns, this::createValueRenderingContext, ValueRenderingContext[]::new);
        renderingNodes = new Node[expressionColumns.length];
        // Temporary code
        //TextRenderer.SINGLETON.setTextFieldFactory(this::newMaterialTextField);
    }

    @Override
    Expression expressionToLoad() {
        return ExpressionColumn.toDisplayExpressionArray(expressionColumns);
    }

    private ValueRenderingContext createValueRenderingContext(ExpressionColumn expressionColumn) {
        String labelKey = expressionColumn.getDisplayColumn().getLabel().getCode();
        DomainClass foreignClass = expressionColumn.getForeignClass();
        ValueRenderingContext context;
        // Returning a standard ValueRenderingContext if the expression expresses just a value and not a foreign entity
        if (foreignClass == null)
            context = new ValueRenderingContext(expressionColumn.isReadOnly(), labelKey, null, Types.isNumberType(expressionColumn.getExpression().getType()) ? DisplayStyle.RIGHT_STYLE.getTextAlign() : null);
        // Returning a EntityRenderingContext otherwise (in case of a foreign entity) which will be used by the EntityRenderer
        else
            context = new EntityRenderingContext(expressionColumn.isReadOnly(), labelKey, null, expressionColumn, () -> entity.getStore(), () -> dialogParent, this);
        context.getEditedValueProperty().addListener((observable, oldValue, newValue) -> applyUiChangeOnEntity(expressionColumn, context));
        return context;
    }

    @Override
    public void setEntity(E entity) {
        super.setEntity(entity);
        for (ExpressionColumn expressionColumn : expressionColumns)
            expressionColumn.parseExpressionDefinitionIfNecessary(entity.getDomainClass());
    }

    @Override
    Node buildNode() {
        if (!tableLayout)
            return new VBox(10);
        DataGrid dataGrid = new SkinnedDataGrid();
        dataGrid.setHeaderVisible(false);
        dataGrid.setFullHeight(true);
        dataGrid.setSelectionMode(SelectionMode.DISABLED);
        return dataGrid;
    }

    @Override
    void syncUiFromModel() {
        initDisplay();
        for (int i = 0, n = applicableExpressionColumns.length; i < n; i++) {
            ExpressionColumn expressionColumn = applicableExpressionColumns[i];
            Object modelValue = updateEntity.evaluate(castExpression(expressionColumn.getExpression()));
            Formatter displayFormatter = expressionColumn.getDisplayFormatter();
            if (displayFormatter != null)
                modelValue = displayFormatter.format(modelValue);
            int j = getApplicableValueRenderingContextIndex(expressionColumn);
            ValueRenderingContext context = valueRenderingContexts[j];
            if (context.isReadOnly() || renderingNodes[j] == null) {
                // Using a standard value renderer in case of a value, or the EntityRenderer in case of a foreign entity
                ValueRenderer valueRenderer = expressionColumn.getForeignClass() == null ? expressionColumn.getDisplayColumn().getValueRenderer() : EntityRenderer.SINGLETON;
                renderingNodes[j] = valueRenderer.renderValue(modelValue, context);
            }
            addExpressionRow(i, expressionColumn, renderingNodes[j]);
        }
        applyDisplay();
    }

    private boolean isColumnApplicable(ExpressionColumn expressionColumn) {
        Expression<E> applicableCondition = castExpression(expressionColumn.getApplicableCondition());
        return applicableCondition == null || Boolean.TRUE.equals(updateEntity.evaluate(applicableCondition));
    }

    private Expression<E> castExpression(Expression expression) {
        return (Expression<E>) expression;
    }

    private int getApplicableValueRenderingContextIndex(ExpressionColumn applicableExpressionColumns) {
        return Arrays.indexOf(expressionColumns, applicableExpressionColumns);
    }

    private DisplayResultBuilder rsb;
    private List<Node> children;

    private void initDisplay() {
        applicableExpressionColumns = java.util.Arrays.stream(expressionColumns).filter(this::isColumnApplicable).toArray(ExpressionColumn[]::new);
        if (tableLayout)
            rsb = new DisplayResultBuilder(applicableExpressionColumns.length, LABEL_COLUMN, VALUE_COLUMN);
        else
            children = new ArrayList<>();
    }

    private void addExpressionRow(int row, ExpressionColumn expressionColumn, Node renderedValueNode) {
        if (tableLayout) {
            rsb.setValue(row, 0, expressionColumn.getDisplayColumn().getLabel());
            rsb.setValue(row, 1, renderedValueNode);
        } else
            children.add(renderedValueNode);
    }

    private void applyDisplay() {
        if (tableLayout) {
            DisplayResult rs = rsb.build();
            DataGrid dataGrid = (DataGrid) node;
            DisplayResult oldRs = dataGrid.getDisplayResult();
            int rowCount = rs.getRowCount();
            if (oldRs == null || oldRs.getRowCount() != rowCount)
                dataGrid.setDisplayResult(rs);
            else
                for (int i = 0; i < rowCount; i++) {
                    if (rs.getValue(i, 1) != oldRs.getValue(i, 1)) {
                        dataGrid.setDisplayResult(rs);
                        break;
                    }
                }
            // The following code is a workaround for the web version which doesn't compute correctly on first show the widths of the drop down buttons (if any present in the value column)
            if (oldRs == null) // => indicates first show
                UiScheduler.scheduleInFutureAnimationFrame(2, () -> { // 2 frames is enough to make it work
                    dataGrid.setDisplayResult(null); // Resetting to null and then reestablishing the result set forces
                    dataGrid.setDisplayResult(rs);   // the data grid to recompute all widths (correctly now)
                }, AnimationFramePass.UI_UPDATE_PASS);
        } else
            ((Pane) node).getChildren().setAll(children);
    }

    private void applyUiChangeOnEntity(ExpressionColumn expressionColumn, ValueRenderingContext valueRenderingContext) {
        Object value = valueRenderingContext.getEditedValue();
        Expression<E> expression = castExpression(expressionColumn.getExpression());
        // Checking if it is a formatted value
        Formatter displayFormatter = expressionColumn.getDisplayFormatter();
        if (displayFormatter != null) {
            // Parsing the value if applicable
            if (displayFormatter instanceof Parser)
                value = ((Parser) displayFormatter).parse(value);
            // Ignoring the new value if it renders the same formatted value as before (this is mainly to prevent
            // update a LocalDateTime if the formatter doesn't display the milliseconds)
            Object previousModelValue = entity.evaluate(expression);
            if (Objects.equals(displayFormatter.format(value), displayFormatter.format(previousModelValue)))
                value = previousModelValue;
        }
        updateEntity.setExpressionValue(expression, value);
        updateOkButton();
        syncUiFromModel();
    }

    public static <E extends Entity> void editEntity(E entity, String expressionColumns, Pane parent) {
        if (entity != null)
            new EntityPropertiesSheet<>(entity, expressionColumns).showAsDialog(parent);
    }
}
