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

/**
 * @author Bruno Salmon
 */
public class EntityRenderer implements ValueRenderer {

    public final static EntityRenderer SINGLETON = new EntityRenderer();

    @Override
    public Node renderValue(Object value, ValueRenderingContext context) {
        EntityId entityId = (EntityId) value;
        EntityRenderingContext erc = (EntityRenderingContext) context;
        EntityStore store = erc.getEntityStore();
        EntityButtonSelector<Entity> selector = new EntityButtonSelector<>(erc.getEntityClass().getModelId(), erc.getButtonFactory(), erc.getParentGetter(), store.getDataSourceModel());
        //selector.setShowMode(ButtonSelector.ShowMode.MODAL_DIALOG);
        Entity entity = store.getEntity(entityId);
        selector.setSelectedItem(entity);
        context.setEditedValueProperty(Properties.compute(selector.selectedItemProperty(), Entities::getId));
        return selector.getButton();
    }
}
