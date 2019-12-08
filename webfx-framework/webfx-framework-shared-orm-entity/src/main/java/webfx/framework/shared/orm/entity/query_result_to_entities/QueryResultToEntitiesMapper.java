package webfx.framework.shared.orm.entity.query_result_to_entities;

import webfx.extras.type.PrimType;
import webfx.framework.shared.orm.domainmodel.DomainField;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.dql.sqlcompiler.mapping.QueryColumnToEntityFieldMapping;
import webfx.framework.shared.orm.dql.sqlcompiler.mapping.QueryRowToEntityMapping;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.util.Dates;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Bruno Salmon
 */
public final class QueryResultToEntitiesMapper {

    public static <E extends Entity> EntityList<E> mapQueryResultToEntities(QueryResult rs, QueryRowToEntityMapping rowMapping, EntityStore store, Object listId) {
        // Creating an empty entity list in the store
        EntityList<E> entities = store.getOrCreateEntityList(listId);
        entities.clear();
        // Now iterating along the query result to create one entity per record
        for (int rowIndex = 0, rowCount = rs.getRowCount(); rowIndex < rowCount; rowIndex++) {
            // Retrieving the primary key of this record
            Object primaryKey = rs.getValue(rowIndex, rowMapping.getPrimaryKeyColumnIndex());
            // Creating the entity (empty for now)
            E entity = store.getOrCreateEntity(rowMapping.getDomainClassId(), primaryKey);
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
                // Some conversion to do if it is a domain field
                if (fieldId instanceof DomainField) {
                    DomainField domainField = (DomainField) fieldId;
                    // First the id the actually the domain field id
                    fieldId = domainField.getId();
                    // And second, converting the dates possibly returned as String by the QueryService into LocalDate or LocalDateTime objects
                    if (value != null && domainField.getType() == PrimType.DATE && value instanceof String) {
                        LocalDateTime localDateTime = Dates.toLocalDateTime((String) value);
                        if (localDateTime != null)
                            value = localDateTime;
                        else {
                            LocalDate localDate = Dates.toLocalDate((String) value);
                            if (localDate != null)
                                value = localDate;
                        }
                    }
                }
                targetEntity.setFieldValue(fieldId, value);
                //System.out.println(targetEntity.getId().toString() + '.' + columnMapping.getDomainFieldId() + " = " + value);
            }
            // And finally adding this entity to the list
            entities.add(entity);
        }
        //Logger.log("Ok : " + entities);
        return entities;
    }

}
