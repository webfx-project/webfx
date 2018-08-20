package naga.framework.ui.graphic.controls.sheet;

import javafx.scene.layout.Pane;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.entity.EntityStore;
import naga.framework.ui.graphic.controls.button.ButtonFactoryMixin;
import naga.fxdata.cell.renderer.ValueRenderingContext;
import naga.util.function.Callable;

/**
 * @author Bruno Salmon
 */
public final class EntityRenderingContext extends ValueRenderingContext {

    private final DomainClass entityClass;
    private final Callable<EntityStore> entityStoreGetter;
    private final Callable<Pane> parentGetter;
    private final ButtonFactoryMixin buttonFactory;

    public EntityRenderingContext(boolean readOnly, Object labelKey, Object placeholderKey, DomainClass entityClass, Callable<EntityStore> entityStoreGetter, Callable<Pane> parentGetter, ButtonFactoryMixin buttonFactory) {
        super(readOnly, labelKey, placeholderKey);
        this.entityClass = entityClass;
        this.entityStoreGetter = entityStoreGetter;
        this.parentGetter = parentGetter;
        this.buttonFactory = buttonFactory;
    }

    public DomainClass getEntityClass() {
        return entityClass;
    }

    public Callable<EntityStore> getEntityStoreGetter() {
        return entityStoreGetter;
    }

    public Callable<Pane> getParentGetter() {
        return parentGetter;
    }

    public EntityStore getEntityStore() {
        return entityStoreGetter.call();
    }

    public ButtonFactoryMixin getButtonFactory() {
        return buttonFactory;
    }

}
