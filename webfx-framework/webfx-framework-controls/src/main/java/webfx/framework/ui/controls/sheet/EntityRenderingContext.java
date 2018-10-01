package webfx.framework.ui.controls.sheet;

import javafx.scene.layout.Pane;
import webfx.framework.orm.domainmodel.DomainClass;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.ui.filter.ExpressionColumn;
import webfx.framework.ui.controls.button.ButtonFactoryMixin;
import webfx.fxkits.extra.cell.renderer.ValueRenderingContext;
import webfx.platform.shared.util.function.Callable;

/**
 * @author Bruno Salmon
 */
public final class EntityRenderingContext extends ValueRenderingContext {

    private final ExpressionColumn foreignFieldColumn;
    private final DomainClass entityClass;
    private final Callable<EntityStore> entityStoreGetter;
    private final Callable<Pane> parentGetter;
    private final ButtonFactoryMixin buttonFactory;

    public EntityRenderingContext(boolean readOnly, Object labelKey, Object placeholderKey, ExpressionColumn foreignFieldColumn, Callable<EntityStore> entityStoreGetter, Callable<Pane> parentGetter, ButtonFactoryMixin buttonFactory) {
        this(readOnly, labelKey, placeholderKey, foreignFieldColumn, foreignFieldColumn.getForeignClass(), entityStoreGetter, parentGetter, buttonFactory);
    }

    public EntityRenderingContext(boolean readOnly, Object labelKey, Object placeholderKey, DomainClass entityClass, Callable<EntityStore> entityStoreGetter, Callable<Pane> parentGetter, ButtonFactoryMixin buttonFactory) {
        this(readOnly, labelKey, placeholderKey, null, entityClass, entityStoreGetter, parentGetter, buttonFactory);
    }

    private EntityRenderingContext(boolean readOnly, Object labelKey, Object placeholderKey, ExpressionColumn foreignFieldColumn, DomainClass entityClass, Callable<EntityStore> entityStoreGetter, Callable<Pane> parentGetter, ButtonFactoryMixin buttonFactory) {
        super(readOnly, labelKey, placeholderKey);
        this.foreignFieldColumn = foreignFieldColumn;
        this.entityClass = entityClass;
        this.entityStoreGetter = entityStoreGetter;
        this.parentGetter = parentGetter;
        this.buttonFactory = buttonFactory;
    }

    public ExpressionColumn getForeignFieldColumn() {
        return foreignFieldColumn;
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
