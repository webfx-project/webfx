package naga.framework.ui.controls;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import naga.commons.util.Arrays;
import naga.commons.util.Strings;
import naga.commons.util.collection.Collections;
import naga.framework.activity.view.ViewActivityContextMixin;
import naga.framework.expression.Expression;
import naga.framework.expression.terms.ExpressionArray;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityStore;
import naga.framework.ui.filter.ExpressionColumn;
import naga.framework.ui.filter.ReactiveExpressionFilter;
import naga.framework.ui.filter.StringFilter;
import naga.framework.ui.filter.StringFilterBuilder;
import naga.framework.ui.i18n.I18n;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.fxdata.cell.renderer.ValueRenderer;
import naga.fxdata.cell.renderer.ValueRendererFactory;
import naga.fxdata.control.DataGrid;
import naga.fxdata.control.ToolkitDataGrid;
import naga.fxdata.displaydata.DisplayResultSet;

import static naga.framework.ui.controls.LayoutUtil.createHGrowable;
import static naga.framework.ui.controls.LayoutUtil.setPrefSizeToInfinite;

/**
 * @author Bruno Salmon
 */
public class EntityButtonSelector {

    private Object jsonOrClass;
    private final ViewActivityContextMixin viewActivityContextMixin;
    private final Pane parent;

    private final DataSourceModel dataSourceModel;

    private Button entityButton;
    private Expression renderingExpression;
    private ValueRenderer entityRenderer;
    private final Property<Entity> entityProperty = new SimpleObjectProperty<>();

    private boolean modelDialog = false; // flag specifying if the choice is displayed in a modal dialog or a drop down menu
    private EntityStore loadingStore;
    private BorderPane entityDialogPane;
    private TextField searchBox;
    private Button okButton, cancelButton;
    private DialogCallback entityDialogCallback;
    private ReactiveExpressionFilter entityDialogFilter;

    public EntityButtonSelector(Object jsonOrClass, ViewActivityContextMixin viewActivityContextMixin, Pane parent, DataSourceModel dataSourceModel) {
        this.viewActivityContextMixin = viewActivityContextMixin;
        this.parent = parent;
        this.dataSourceModel = dataSourceModel;
        setJsonOrClass(jsonOrClass);
        Properties.runOnPropertiesChange(p -> updateEntityButton(), entityProperty);
    }

    public void setModelDialog(boolean modelDialog) {
        this.modelDialog = modelDialog;
    }

    public void setJsonOrClass(Object jsonOrClass) {
        this.jsonOrClass = jsonOrClass;
        renderingExpression = null;
        if (jsonOrClass != null) {
            StringFilter stringFilter = new StringFilterBuilder(jsonOrClass).build();
            DomainClass entityClass = dataSourceModel.getDomainModel().getClass(stringFilter.getDomainClassId());
            if (stringFilter.getColumns() != null) {
                ExpressionColumn[] expressionColumns = ExpressionColumn.fromJsonArray(stringFilter.getColumns());
                renderingExpression = new ExpressionArray(Arrays.map(expressionColumns, expressionColumn -> expressionColumn.parseExpressionDefinitionIfNecessary(dataSourceModel.getDomainModel(), stringFilter.getDomainClassId()).getExpression(), Expression[]::new));
            } else if (stringFilter.getFields() != null)
                renderingExpression = entityClass.parseExpression(stringFilter.getFields());
            else
                renderingExpression = entityClass.getForeignFields();
        }
        entityRenderer = renderingExpression == null ? null : ValueRendererFactory.getDefault().createCellRenderer(renderingExpression.getType());
        entityDialogPane = null;
    }

    public void setEditable(boolean editable) {
        getEntityButton().setDisable(!editable);
    }

    public void setLoadingStore(EntityStore loadingStore) {
        this.loadingStore = loadingStore;
    }

    public Property<Entity> entityProperty() {
        return entityProperty;
    }

    public Entity getEntity() {
        return entityProperty.getValue();
    }

    public void setEntity(Entity entity) {
        entityProperty.setValue(entity);
    }

    public Button getEntityButton() {
        if (entityButton == null)
            setEntityButton(ButtonUtil.newDrowDownButton());
        return entityButton;
    }

    public void setEntityButton(Button entityButton) {
        this.entityButton = entityButton;
        entityButton.setOnAction(e -> showEntityDialog());
    }

    private void updateEntityButton() {
        Toolkit.get().scheduler().runInUiThread(() -> {
            Entity entity = getEntity();
            Object renderedValue = entity == null ? null : entity.evaluate(renderingExpression);
            Node renderedNode = entityRenderer.renderCellValue(renderedValue);
            getEntityButton().setGraphic(renderedNode);
        });
    }

    public void showEntityDialog() {
        setUpEntityDialog(true);
    }

    private DataGrid dataGrid;

    private void setUpEntityDialog(boolean show) {
        if (entityDialogPane == null) {
            if (entityRenderer == null)
                return;
            dataGrid = new ToolkitDataGrid();
            dataGrid.setHeaderVisible(false);
            BorderPane.setAlignment(dataGrid, Pos.TOP_LEFT);
            entityDialogPane = new BorderPane(dataGrid);
            if (modelDialog)
                setPrefSizeToInfinite(dataGrid);
            else
                dataGrid.setMaxHeight(200d);
            I18n i18n = viewActivityContextMixin.getI18n();
            EntityStore filterStore = loadingStore != null ? loadingStore : getEntity() != null ? getEntity().getStore() : null;
            entityDialogFilter = new ReactiveExpressionFilter(jsonOrClass).setDataSourceModel(dataSourceModel).setI18n(i18n).setStore(filterStore);
            String searchCondition = entityDialogFilter.getDomainClass().getSearchCondition();
            if (searchCondition != null) {
                searchBox = i18n.translatePromptText(new TextField(), "GenericSearchPlaceholder");
                entityDialogPane.setTop(searchBox);
                entityDialogFilter.combine(searchBox.textProperty(), s -> {
                    if (Strings.isEmpty(s))
                        return null;
                    setSearchParameters(s, entityDialogFilter.getStore());
                    return "{where: `" + searchCondition + "`}";
                });
                searchBox.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
                    if (KeyCode.ESCAPE.equals(e.getCode()) || e.getCharacter().charAt(0) == 27) {
                        entityDialogCallback.closeDialog();
                        e.consume();
                    }
                });
            }
            entityDialogFilter
                    .setExpressionColumns(ExpressionColumn.create(renderingExpression))
                    .displayResultSetInto(dataGrid.displayResultSetProperty())
                    .setDisplaySelectionProperty(dataGrid.displaySelectionProperty())
                    //.setSelectedEntityHandler(dataGrid.displaySelectionProperty(), o -> onOkEntityDialog())
                    .start();
            if (modelDialog) {
                okButton = viewActivityContextMixin.newOkButton(this::onOkEntityDialog);
                cancelButton = viewActivityContextMixin.newCancelButton(this::onCancelEntityDialog);
                HBox hBox = new HBox(20, createHGrowable(), okButton, cancelButton, createHGrowable());
                hBox.setPadding(new Insets(20, 0, 0, 0));
                entityDialogPane.setBottom(hBox);
            }
            dataGrid.setOnMouseClicked(e -> {if (e.getClickCount() == 1) onOkEntityDialog(); });
            entityDialogPane.setBorder(BorderUtil.newBorder(Color.DARKGRAY));
        }
        entityDialogFilter.setActive(true);
        if (show) {
            if (modelDialog) {
                entityDialogCallback = DialogUtil.showModalNodeInGoldLayout(entityDialogPane, parent, 0.9, 0.8);
                // Resetting default and cancel buttons (required for JavaFx if displayed a second time)
                ButtonUtil.resetDefaultButton(okButton);
                ButtonUtil.resetCancelButton(cancelButton);
            } else {
                Property<DisplayResultSet> deferred = new SimpleObjectProperty<>();
                dataGrid.displayResultSetProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> deferred.setValue(newValue)));
                entityDialogCallback = DialogUtil.showDropDownDialog(entityDialogPane, entityButton, parent, deferred);
            }
        }
        if (searchBox != null) {
            searchBox.setText(null); // Resetting the search box
            searchBox.requestFocus();
        }
    }

    protected void setSearchParameters(String search, EntityStore store) {
        store.setParameterValue("search", search);
        store.setParameterValue("lowerSearch", search.toLowerCase());
        store.setParameterValue("searchLike", "%" + search + "%");
        store.setParameterValue("lowerSearchLike", "%" + search.toLowerCase() + "%");
    }

    private void onOkEntityDialog() {
        closeEntityDialog();
        setEntity(entityDialogFilter.getSelectedEntity());
    }

    private void onCancelEntityDialog() {
        closeEntityDialog();
    }

    private void closeEntityDialog() {
        entityDialogCallback.closeDialog();
        entityDialogFilter.setActive(false);
    }

    public void autoSelectFirstEntity() {
        setUpEntityDialog(false);
        if (entityDialogFilter != null)
            entityDialogFilter.setEntitiesHandler(entityList -> setEntity(Collections.first(entityList)));
    }
    
}
