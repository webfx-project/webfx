package webfx.framework.client.ui.controls.entity.sheet;

import javafx.scene.Node;
import webfx.framework.client.ui.controls.entity.selector.EntityButtonSelector;
import webfx.framework.client.orm.reactive.mapping.entities_to_grid.EntityColumn;
import webfx.framework.shared.orm.entity.Entities;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.extras.cell.renderer.ValueRenderer;
import webfx.extras.cell.renderer.ValueRenderingContext;
import webfx.platform.shared.services.json.Json;

/**
 * @author Bruno Salmon
 */
final class EntityRenderer implements ValueRenderer {

    final static EntityRenderer SINGLETON = new EntityRenderer();

    @Override
    public Node renderValue(Object value, ValueRenderingContext context) {
        // Expecting an EntityId for the value
        EntityId entityId = (EntityId) value;
        // Expecting an EntityRenderingContext for the context
        EntityRenderingContext erc = (EntityRenderingContext) context;
        // Retrieving the entity store and the domain class id
        EntityStore store = erc.getEntityStore();
        Object domainClassId = erc.getEntityClass().getModelId();
        // Defining the json or class object to be passed to the entity button selector
        EntityColumn foreignFieldColumn = erc.getForeignFieldColumn();
        Object jsonOrClass = foreignFieldColumn == null ? domainClassId // just the class id if there is no foreign field column defined
            : Json.createObject() // Json object otherwise (most of the case) with both "class" and "columns" set
                .set("class", domainClassId)
                .set("alias", foreignFieldColumn.getForeignAlias())
                // We prefix the columns definition with "expr:=" to ensure that the parsing - done by EntityColumns.fromJsonArrayOrExpressionsDefinition() - will work when foreign fields is an expression array (ex: "[icon,name]"), because in that case the string is an expression definition and not a json array despite of the brackets (the correct json array string would be ['icon','name'] instead). So the prefix will remove that possible confusion.
                .set("columns", "expr:=" + foreignFieldColumn.getForeignColumns())
                .set("where", foreignFieldColumn.getForeignWhere())
                .set("orderBy", foreignFieldColumn.getForeignOrderBy())
                ;
        // Creating the entity button selector and setting the initial entity
        EntityButtonSelector<Entity> selector = new EntityButtonSelector<>(jsonOrClass, erc.getButtonFactory(), erc.getParentGetter(), store.getDataSourceModel());
        if (foreignFieldColumn != null) {
            String searchCondition = foreignFieldColumn.getForeignSearchCondition();
            if (searchCondition != null)
                selector.setSearchCondition(searchCondition);
        }
        selector.setReadOnly(context.isReadOnly());
        // Also setting the edited value property in the rendering context to be the id of the entity selected in the button selector
        context.bindConvertedEditedValuePropertyTo(selector.selectedItemProperty(), store.getEntity(entityId), Entities::getId, store::getEntity);
        return selector.getButton();
    }
}
