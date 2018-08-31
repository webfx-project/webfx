package webfx.framework.orm.entity;

/**
 * @author Bruno Salmon
 */
public class EntityListWrapper<E extends Entity> implements EntityListMixin<E> {
    private final EntityList<E> entityList;

    public EntityListWrapper(EntityList<E> entityList) {
        this.entityList = entityList;
    }

    @Override
    public EntityList<E> getEntityList() {
        return entityList;
    }
}
