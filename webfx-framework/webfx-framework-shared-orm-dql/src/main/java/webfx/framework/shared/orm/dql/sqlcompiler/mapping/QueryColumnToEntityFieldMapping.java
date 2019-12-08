package webfx.framework.shared.orm.dql.sqlcompiler.mapping;


/**
 * Mapping between a query column (ex: a sql column) and an entity field. There are 2 cases.
 * (ex: select p.id, p.first_name, p.last_name, c.id, c.name from person p join country c on c.id=p.country_id)
 * 1) either the column refers to a value of the primary entity (ex: column p.last_name => p = primary Person entity)
 * 2) or it refers to a value of a foreign entity because of a join (ex: column c.name => c = foreign Country entity)
 *
 *
 * @author Bruno Salmon
 */
public final class QueryColumnToEntityFieldMapping {

    /**
     * The query result column index (first = 0) that we are talking about. The column contains value that needs to be
     * mapped into an entity field.
     */
    private final int columnIndex;
    /**
     * The domain field (id) that needs to receive the column value in the target entity (which can be a primary or a foreign entity).
     * Ex: "lastName" for last_name column (entity field names conventionally have logical names with no underscore as
     * opposed to sql names).
     */
    private final Object domainFieldId;

    // The following fields are filled only in case 2

    /**
     * Contains the domain class (id) of the foreign entity when the column contains a foreign key (ie a primary key to
     * a foreign entity) or null if the column value is not a foreign key.
     * Ex: it contains "Country" for column c.id (because c.id is a primary key to country table = Country entity).
     * Note: in this case, domainFieldId can be "country" and it will point to that foreign Country entity.
     */
    private final Object foreignClassId;
    /**
     * Contains the id column mapping of the foreign entity when the column value needs to be mapped into a field of a
     * foreign entity (because of a join), or null if the target entity is simply the primary entity (no join).
     * Ex: for column c.name it contains the c.id column mapping
     * Note: if foreignIdColumnMapping is not null, foreignIdColumnMapping.getForeignClassId() returns the foreign
     * entity class (ex: "Country").
     */
    private final QueryColumnToEntityFieldMapping foreignIdColumnMapping;

    // Constructor for when the column is a simple value that needs to be mapped into a simple field of the primary entity
    public QueryColumnToEntityFieldMapping(int columnIndex, Object domainFieldId) {
        this(columnIndex, domainFieldId, null, null);
    }

    // Constructor for when the column is a foreign key that needs to me mapped into a foreign field of the primary entity (it will point to the foreign entity).
    public QueryColumnToEntityFieldMapping(int columnIndex, Object foreignFieldId, Object foreignClassId) {
        this(columnIndex, foreignFieldId, foreignClassId, null);
    }

    // Constructor for when the column is a simple value that needs to me mapped into a simple field of a foreign entity.
    public QueryColumnToEntityFieldMapping(int columnIndex, QueryColumnToEntityFieldMapping foreignIdColumnMapping, Object foreignEntityFieldId) {
        this(columnIndex, foreignEntityFieldId, null, foreignIdColumnMapping);
    }

    // Constructor for when the column is a foreign key that needs to me mapped into a foreign field of a foreign entity (it will point to that "second" foreign entity).
    public QueryColumnToEntityFieldMapping(int columnIndex, QueryColumnToEntityFieldMapping foreignIdColumnMapping, Object foreignFieldId, Object foreignClassId) {
        this(columnIndex, foreignFieldId, foreignClassId, foreignIdColumnMapping);
    }

    // Constructor that can cover all cases
    public QueryColumnToEntityFieldMapping(int columnIndex, Object domainFieldId, Object foreignClassId, QueryColumnToEntityFieldMapping foreignIdColumnMapping) {
        this.columnIndex = columnIndex;
        this.domainFieldId = domainFieldId;
        this.foreignClassId = foreignClassId;
        this.foreignIdColumnMapping = foreignIdColumnMapping;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public Object getDomainFieldId() {
        return domainFieldId;
    }

    public Object getForeignClassId() {
        return foreignClassId;
    }

    public QueryColumnToEntityFieldMapping getForeignIdColumnMapping() {
        return foreignIdColumnMapping;
    }

    @Override
    public String toString() {
        return "{columnIndex: " + columnIndex + ", domainField: " + domainFieldId + (foreignClassId == null ? "" : " -> " + foreignClassId) + (foreignIdColumnMapping == null ? "" : " of " + foreignIdColumnMapping.foreignClassId + " whose primary key is on column " + foreignIdColumnMapping.columnIndex) + "}";
    }
}
