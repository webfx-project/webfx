package webfx.framework.shared.orm.dql.sqlcompiler.mapping;

import webfx.platform.shared.util.Arrays;

/**
 * Mapping between the query row and an entity
 *
 * @author Bruno Salmon
 */
public final class QueryRowToEntityMapping {

    private final int primaryKeyColumnIndex; // the column index that contains the primary key
    private final Object domainClassId; // the domain class id for the entity
    private final QueryColumnToEntityFieldMapping[] columnMappings; // column mappings (other than for the component id)

    public QueryRowToEntityMapping(int primaryKeyColumnIndex, Object domainClassId, QueryColumnToEntityFieldMapping[] columnMappings) {
        this.primaryKeyColumnIndex = primaryKeyColumnIndex;
        this.domainClassId = domainClassId;
        this.columnMappings = columnMappings;
    }

    public int getPrimaryKeyColumnIndex() {
        return primaryKeyColumnIndex;
    }

    public Object getDomainClassId() {
        return domainClassId;
    }

    public QueryColumnToEntityFieldMapping[] getColumnMappings() {
        return columnMappings;
    }

    @Override
    public String toString() {
        return "{DomainClass: " + domainClassId + ", primaryKeyColumnIndex: " + primaryKeyColumnIndex + ", columnMappings: " + Arrays.toStringWithLineFeeds(columnMappings) + "}";
    }
}
