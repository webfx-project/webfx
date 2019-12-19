package webfx.framework.client.ui.controls.entity.sheet;

import javafx.scene.layout.Pane;
import webfx.framework.client.orm.reactive.mapping.entities_to_grid.EntityColumn;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.client.ui.controls.button.ButtonFactoryMixin;
import webfx.extras.cell.renderer.ValueRenderingContext;
import webfx.platform.shared.util.function.Callable;

/**
 * @author Bruno Salmon
 */
final class EntityRenderingContext extends ValueRenderingContext {

    private final EntityColumn foreignFieldColumn;
    private final DomainClass entityClass;
    private final Callable<EntityStore> entityStoreGetter;
    private final Callable<Pane> parentGetter;
    private final ButtonFactoryMixin buttonFactory;

    EntityRenderingContext(boolean readOnly, Object labelKey, Object placeholderKey, EntityColumn foreignFieldColumn, Callable<EntityStore> entityStoreGetter, Callable<Pane> parentGetter, ButtonFactoryMixin buttonFactory) {
        this(readOnly, labelKey, placeholderKey, foreignFieldColumn, foreignFieldColumn.getForeignClass(), entityStoreGetter, parentGetter, buttonFactory);
    }

    EntityRenderingContext(boolean readOnly, Object labelKey, Object placeholderKey, DomainClass entityClass, Callable<EntityStore> entityStoreGetter, Callable<Pane> parentGetter, ButtonFactoryMixin buttonFactory) {
        this(readOnly, labelKey, placeholderKey, null, entityClass, entityStoreGetter, parentGetter, buttonFactory);
    }

    private EntityRenderingContext(boolean readOnly, Object labelKey, Object placeholderKey, EntityColumn foreignFieldColumn, DomainClass entityClass, Callable<EntityStore> entityStoreGetter, Callable<Pane> parentGetter, ButtonFactoryMixin buttonFactory) {
        super(readOnly, labelKey, placeholderKey, null);
        this.foreignFieldColumn = foreignFieldColumn;
        this.entityClass = entityClass;
        this.entityStoreGetter = entityStoreGetter;
        this.parentGetter = parentGetter;
        this.buttonFactory = buttonFactory;
    }

    EntityColumn getForeignFieldColumn() {
        return foreignFieldColumn;
    }

    DomainClass getEntityClass() {
        return entityClass;
    }

    Callable<EntityStore> getEntityStoreGetter() {
        return entityStoreGetter;
    }

    Callable<Pane> getParentGetter() {
        return parentGetter;
    }

    EntityStore getEntityStore() {
        return entityStoreGetter.call();
    }

    ButtonFactoryMixin getButtonFactory() {
        return buttonFactory;
    }

}
