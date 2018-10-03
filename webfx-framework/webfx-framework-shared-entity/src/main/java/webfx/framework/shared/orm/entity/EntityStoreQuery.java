package webfx.framework.shared.orm.entity;

/**
 * @author Bruno Salmon
 */
public final class EntityStoreQuery {

    final String select;
    final Object[] parameters;
    final Object listId;

    public EntityStoreQuery(String select) {
        this(select, select);
    }

    public EntityStoreQuery(String select, Object listId) {
        this(select, null, listId);
    }

    public EntityStoreQuery(String select, Object[] parameters) {
        this(select, parameters, select);
    }

    public EntityStoreQuery(String select, Object[] parameters, Object listId) {
        this.select = select;
        this.parameters = parameters;
        this.listId = listId;
    }
}
