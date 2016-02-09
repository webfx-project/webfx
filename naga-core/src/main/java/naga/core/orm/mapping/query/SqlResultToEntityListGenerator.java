package naga.core.orm.mapping.query;

import naga.core.orm.entity.*;
import naga.core.spi.sql.SqlReadResult;

/**
 * @author Bruno Salmon
 */
public class SqlResultToEntityListGenerator {

    public static EntityList createEntityList(SqlReadResult entityQueryResult, SqlRowToEntityMapping rowMapping, EntityStore store, Object listId) {
        // Creating an empty entity list in the store
        EntityList entityList = store.getOrCreateEntityList(listId);
        entityList.clear();
        // Now iterating along the query result to create one entity per record
        int rowCount = entityQueryResult.getRowCount();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            // Retrieving the primary key of this record
            Object primaryKey = entityQueryResult.getValue(rowIndex, rowMapping.getPrimaryKeyColumnIndex());
            // Creating the entity (empty for now)
            Entity entity = store.getOrCreateEntity(rowMapping.getDomainClassId(), primaryKey);
            // Now populating the entity values by iterating along the other column indexes (though column mappings)
            for (SqlColumnToEntityFieldMapping columnMapping : rowMapping.getColumnMappings()) {
                // The target entity (to affect the column value to) is normally the current entity
                Entity targetEntity = entity;
                // However if this column index is associated with a join, it actually refers to a foreign entity, so let's check this
                SqlColumnToEntityFieldMapping joinMapping = columnMapping.getJoinMapping();
                if (joinMapping != null) { // Yes it is a join
                    // So let's first get the row id of the database foreign record
                    Object foreignKey = entityQueryResult.getValue(rowIndex, joinMapping.getQueryColumnIndex());
                    // If it is null, there is nothing to do (finally no target entity)
                    if (foreignKey == null)
                        continue;
                    // And creating the foreign entity (or getting the same instance if already created)
                    targetEntity = store.getOrCreateEntity(joinMapping.getForeignModelClassId(), foreignKey); // And finally using is as the target entity
                }
                // Now that we have the target entity, getting the value for the column index
                Object value = entityQueryResult.getValue(rowIndex, columnMapping.getQueryColumnIndex());
                // If this is a foreign key (when foreignClassId is filled), we transform the value into a link to the foreign entity
                if (columnMapping.getForeignModelClassId() != null)
                    value = store.getOrCreateEntity(columnMapping.getForeignModelClassId(), value);
                // Now everything is ready to set the field on the target entity
                targetEntity.setFieldValue(columnMapping.getModelFieldId(), value);
                //System.out.println(targetEntity.getId().toString() + '.' + columnMapping.getModelFieldId() + " = " + value);
            }
            // And finally adding this entity to the list
            entityList.add(entity);
        }
        return entityList;
    }

}
