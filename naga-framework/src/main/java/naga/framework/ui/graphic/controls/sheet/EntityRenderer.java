package naga.framework.ui.graphic.controls.sheet;

import javafx.scene.Node;
import naga.framework.orm.entity.Entities;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.ui.graphic.controls.button.EntityButtonSelector;
import naga.fx.properties.Properties;
import naga.fxdata.cell.renderer.ValueRenderer;
import naga.fxdata.cell.renderer.ValueRenderingContext;
import naga.platform.services.json.Json;

/**
 * @author Bruno Salmon
 */
public final class EntityRenderer implements ValueRenderer {

    public final static EntityRenderer SINGLETON = new EntityRenderer();

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
        Object jsonOrClass = erc.getForeignFieldColumn() == null ? domainClassId // just the class id if there is no foreign field column defined
            : Json.createObject() // Json object otherwise (most of the case) with both "class" and "columns" set
                .set("class", domainClassId)
                // We prefix the columns definition with "expr:=" to prevent ExpressionColumns.fromJsonArrayOrExpressionsDefinition() to be confused when foreign fields is an expression array (ex: "[icon,name]"), it must not be considered as a json array (the correct definition for a json array would be "['icon','name'] instead)
                .set("columns", "expr:=" + erc.getForeignFieldColumn().getForeignFields());
        // Creating the entity button selector and setting the initial entity
        EntityButtonSelector<Entity> selector = new EntityButtonSelector<>(jsonOrClass, erc.getButtonFactory(), erc.getParentGetter(), store.getDataSourceModel());
        Entity entity = store.getEntity(entityId);
        selector.setSelectedItem(entity);
        // Also setting the edited value property in the rendering context to be the id of the entity selected in the button selector
        context.setEditedValueProperty(Properties.compute(selector.selectedItemProperty(), Entities::getId));
        return selector.getButton();
    }
}
