package naga.core.orm.entity;

import naga.core.util.collection.Collections;

import java.util.ArrayList;

/**
 * @author Bruno Salmon
 */
public class EntityList extends ArrayList<Entity> {

    private final Object listId;
    private final EntityStore store;

    public EntityList(Object listId, EntityStore store) {
        this.listId = listId;
        this.store = store;
    }

    public Object getListId() {
        return listId;
    }

    public EntityStore getStore() {
        return store;
    }

    @Override
    public String toString() {
        return Collections.toString(iterator());
    }
}
