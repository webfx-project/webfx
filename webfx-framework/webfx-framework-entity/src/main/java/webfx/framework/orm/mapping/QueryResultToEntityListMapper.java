package webfx.framework.orm.mapping;

import webfx.framework.expression.sqlcompiler.mapping.QueryColumnToEntityFieldMapping;
import webfx.framework.expression.sqlcompiler.mapping.QueryRowToEntityMapping;
import webfx.framework.orm.domainmodel.DomainField;
import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.EntityList;
import webfx.framework.orm.entity.EntityStore;
import webfx.platforms.core.services.query.QueryResult;

/**
 * @author Bruno Salmon
 */
public final class QueryResultToEntityListMapper {

    public static <E extends Entity> EntityList<E> createEntityList(QueryResult rs, QueryRowToEntityMapping rowMapping, EntityStore store, Object listId) {
        //Logger.log("createEntityList()");
        // Creating an empty entity list in the store
        EntityList entityList = store.getOrCreateEntityList(listId);
        entityList.clear();
        // Now iterating along the query result to create one entity per record
        for (int rowIndex = 0, rowCount = rs.getRowCount(); rowIndex < rowCount; rowIndex++) {
            // Retrieving the primary key of this record
            Object primaryKey = rs.getValue(rowIndex, rowMapping.getPrimaryKeyColumnIndex());
            // Creating the entity (empty for now)
            Entity entity = store.getOrCreateEntity(rowMapping.getDomainClassId(), primaryKey);
            // Now populating the entity values by iterating along the other column indexes (though column mappings)
            for (QueryColumnToEntityFieldMapping columnMapping : rowMapping.getColumnMappings()) {
                // The target entity (to affect the column value to) is normally the current entity
                Entity targetEntity = entity;
                // However if this column index is associated with a join, it actually refers to a foreign entity, so let's check this
                QueryColumnToEntityFieldMapping joinMapping = columnMapping.getForeignIdColumnMapping();
                if (joinMapping != null) { // Yes it is a join
                    // So let's first get the row id of the database foreign record
                    Object foreignKey = rs.getValue(rowIndex, joinMapping.getColumnIndex());
                    // If it is null, there is nothing to do (finally no target entity)
                    if (foreignKey == null)
                        continue;
                    // And creating the foreign entity (or getting the same instance if already created)
                    targetEntity = store.getOrCreateEntity(joinMapping.getForeignClassId(), foreignKey); // And finally using is as the target entity
                }
                // Now that we have the target entity, getting the value for the column index
                Object value = rs.getValue(rowIndex, columnMapping.getColumnIndex());
                // If this is a foreign key (when foreignClassId is filled), we transform the value into a link to the foreign entity
                if (value != null && columnMapping.getForeignClassId() != null)
                    value = store.getOrCreateEntity(columnMapping.getForeignClassId(), value).getId();
                // Now everything is ready to set the field on the target entity
                Object fieldId = columnMapping.getDomainFieldId();
                if (fieldId instanceof DomainField)
                    fieldId = ((DomainField) fieldId).getId();
                targetEntity.setFieldValue(fieldId, value);
                //System.out.println(targetEntity.getId().toString() + '.' + columnMapping.getDomainFieldId() + " = " + value);
            }
            // And finally adding this entity to the list
            entityList.add(entity);
        }
        //Logger.log("Ok : " + entityList);
        return entityList;
    }

}
