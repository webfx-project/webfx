package webfx.framework.client.ui.controls.entity.sheet;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import webfx.extras.cell.renderer.ValueRenderer;
import webfx.extras.cell.renderer.ValueRenderingContext;
import webfx.extras.imagestore.ImageStore;
import webfx.extras.type.Types;
import webfx.extras.visual.*;
import webfx.extras.visual.controls.grid.SkinnedVisualGrid;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.extras.visual.impl.VisualColumnImpl;
import webfx.framework.client.orm.reactive.mapping.entities_to_grid.EntityColumn;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.VisualEntityColumn;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.VisualEntityColumnFactory;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.formatter.ValueFormatter;
import webfx.framework.shared.orm.domainmodel.formatter.ValueParser;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.expression.Expression;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.util.Arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public final class EntityPropertiesSheet<E extends Entity> extends EntityUpdateDialog<E> {

    private final static VisualColumn LABEL_COLUMN = VisualColumn.create((value, context) -> new Label(((webfx.extras.label.Label) value).getText(), ImageStore.createImageView(((webfx.extras.label.Label) value).getIconPath())));
    private final static VisualColumn VALUE_COLUMN = new VisualColumnImpl(null, null, null, null, VisualStyle.CENTER_STYLE, (value, context) -> (Node) value, null, null);

    private final VisualEntityColumn<E>[] entityColumns;
    private final ValueRenderingContext[] valueRenderingContexts;
    private final Node[] renderingNodes;
    private VisualEntityColumn<E>[] applicableEntityColumns;
    private boolean tableLayout = true;

    private EntityPropertiesSheet(E entity, String expressionColumns) {
        this((VisualEntityColumn<E>[]) VisualEntityColumnFactory.get().fromJsonArrayOrExpressionsDefinition(expressionColumns, entity.getDomainClass()));
        setEntity(entity);
    }

    private EntityPropertiesSheet(VisualEntityColumn<E>[] entityColumns) {
        this.entityColumns = entityColumns;
        valueRenderingContexts = Arrays.map(entityColumns, this::createValueRenderingContext, ValueRenderingContext[]::new);
        renderingNodes = new Node[entityColumns.length];
        // Temporary code
        //TextRenderer.SINGLETON.setTextFieldFactory(this::newMaterialTextField);
    }

    @Override
    Expression<? extends Entity> expressionToLoad() {
        return VisualEntityColumnFactory.get().toDisplayExpressionArray(entityColumns);
    }

    private ValueRenderingContext createValueRenderingContext(VisualEntityColumn<E> entityColumn) {
        String labelKey = entityColumn.getVisualColumn().getLabel().getCode();
        DomainClass foreignClass = entityColumn.getForeignClass();
        ValueRenderingContext context;
        // Returning a standard ValueRenderingContext if the expression expresses just a value and not a foreign entity
        if (foreignClass == null)
            context = new ValueRenderingContext(entityColumn.isReadOnly(), labelKey, null, Types.isNumberType(entityColumn.getExpression().getType()) ? VisualStyle.RIGHT_STYLE.getTextAlign() : null);
        // Returning a EntityRenderingContext otherwise (in case of a foreign entity) which will be used by the EntityRenderer
        else
            context = new EntityRenderingContext(entityColumn.isReadOnly(), labelKey, null, entityColumn, () -> entity.getStore(), () -> dialogParent, this);
        context.getEditedValueProperty().addListener((observable, oldValue, newValue) -> applyUiChangeOnEntity(entityColumn, context));
        return context;
    }

    @Override
    public void setEntity(E entity) {
        super.setEntity(entity);
        for (EntityColumn<E> entityColumn : entityColumns)
            entityColumn.parseExpressionDefinitionIfNecessary(entity.getDomainClass());
    }

    @Override
    Node buildNode() {
        if (!tableLayout)
            return new VBox(10);
        VisualGrid visualGrid = new SkinnedVisualGrid();
        visualGrid.setHeaderVisible(false);
        visualGrid.setFullHeight(true);
        visualGrid.setSelectionMode(SelectionMode.DISABLED);
        return visualGrid;
    }

    @Override
    void syncUiFromModel() {
        initDisplay();
        for (int i = 0, n = applicableEntityColumns.length; i < n; i++) {
            VisualEntityColumn entityColumn = applicableEntityColumns[i];
            Object modelValue = updateEntity.evaluate(castExpression(entityColumn.getExpression()));
            ValueFormatter displayFormatter = entityColumn.getDisplayFormatter();
            if (displayFormatter != null)
                modelValue = displayFormatter.formatValue(modelValue);
            int j = getApplicableValueRenderingContextIndex(entityColumn);
            ValueRenderingContext context = valueRenderingContexts[j];
            if (context.isReadOnly() || renderingNodes[j] == null) {
                // Using a standard value renderer in case of a value, or the EntityRenderer in case of a foreign entity
                ValueRenderer valueRenderer = entityColumn.getForeignClass() == null ? entityColumn.getVisualColumn().getValueRenderer() : EntityRenderer.SINGLETON;
                renderingNodes[j] = valueRenderer.renderValue(modelValue, context);
            }
            addExpressionRow(i, entityColumn, renderingNodes[j]);
        }
        applyDisplay();
    }

    private boolean isColumnApplicable(EntityColumn entityColumn) {
        Expression<E> applicableCondition = castExpression(entityColumn.getApplicableCondition());
        return applicableCondition == null || Boolean.TRUE.equals(updateEntity.evaluate(applicableCondition));
    }

    private Expression<E> castExpression(Expression expression) {
        return (Expression<E>) expression;
    }

    private int getApplicableValueRenderingContextIndex(EntityColumn applicableEntityColumns) {
        return Arrays.indexOf(entityColumns, applicableEntityColumns);
    }

    private VisualResultBuilder rsb;
    private List<Node> children;

    private void initDisplay() {
        applicableEntityColumns = java.util.Arrays.stream(entityColumns).filter(this::isColumnApplicable).toArray(VisualEntityColumn[]::new);
        if (tableLayout)
            rsb = new VisualResultBuilder(applicableEntityColumns.length, LABEL_COLUMN, VALUE_COLUMN);
        else
            children = new ArrayList<>();
    }

    private void addExpressionRow(int row, VisualEntityColumn entityColumn, Node renderedValueNode) {
        if (tableLayout) {
            rsb.setValue(row, 0, entityColumn.getVisualColumn().getLabel());
            rsb.setValue(row, 1, renderedValueNode);
        } else
            children.add(renderedValueNode);
    }

    private void applyDisplay() {
        if (tableLayout) {
            VisualResult rs = rsb.build();
            VisualGrid visualGrid = (VisualGrid) node;
            VisualResult oldRs = visualGrid.getVisualResult();
            int rowCount = rs.getRowCount();
            if (oldRs == null || oldRs.getRowCount() != rowCount)
                visualGrid.setVisualResult(rs);
            else
                for (int i = 0; i < rowCount; i++) {
                    if (rs.getValue(i, 1) != oldRs.getValue(i, 1)) {
                        visualGrid.setVisualResult(rs);
                        break;
                    }
                }
            // The following code is a workaround for the web version which doesn't compute correctly on first show the widths of the drop down buttons (if any present in the value column)
            if (oldRs == null) // => indicates first show
                UiScheduler.scheduleInAnimationFrame(() -> {
                    visualGrid.setVisualResult(null); // Resetting to null and then reestablishing the result set forces
                    visualGrid.setVisualResult(rs);   // the data grid to recompute all widths (correctly now)
                }, 2); // 2 frames is enough to make it work
        } else
            ((Pane) node).getChildren().setAll(children);
    }

    private void applyUiChangeOnEntity(EntityColumn entityColumn, ValueRenderingContext valueRenderingContext) {
        Object value = valueRenderingContext.getEditedValue();
        Expression<E> expression = castExpression(entityColumn.getExpression());
        // Checking if it is a formatted value
        ValueFormatter formatter = entityColumn.getDisplayFormatter();
        if (formatter != null) {
            // Parsing the value if applicable
            if (formatter instanceof ValueParser)
                value = ((ValueParser) formatter).parseValue(value);
            // Ignoring the new value if it renders the same formatted value as before (this is mainly to prevent
            // update a LocalDateTime if the formatter doesn't display the milliseconds)
            Object previousModelValue = entity.evaluate(expression);
            if (Objects.equals(formatter.formatValue(value), formatter.formatValue(previousModelValue)))
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
