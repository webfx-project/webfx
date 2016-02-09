package naga.core.orm.mapping.query;

/**
 * Mapping between the sql row and the entity
 *
 * @author Bruno Salmon
 */
public class SqlRowToEntityMapping {

    private final int primaryKeyColumnIndex; // the column index that contains the primary key
    private final Object domainClassId; // the domain class id for the entity
    private final SqlColumnToEntityFieldMapping[] columnMappings; // column mappings (other than for the component id)

    public SqlRowToEntityMapping(int primaryKeyColumnIndex, Object domainClassId, SqlColumnToEntityFieldMapping[] columnMappings) {
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

    public SqlColumnToEntityFieldMapping[] getColumnMappings() {
        return columnMappings;
    }
}
