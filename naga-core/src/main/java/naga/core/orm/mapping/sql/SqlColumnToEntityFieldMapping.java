package naga.core.orm.mapping.sql;


/**
 * Mapping between the sql column and the entity field. There are 2 cases.
 * (ex: select p.id, p.first_name, p.last_name, c.id, c.name from person p join country c on c.id=p.country_id)
 * 1) either the column refers to a value of the record itself (ex: column 3 = p.last_name)
 * 2) or it refers to a value of a foreign record because of a join (ex: column 5 = c.name)
 *
 *
 * @author Bruno Salmon
 */
public class SqlColumnToEntityFieldMapping {

    private final int queryColumnIndex; // The column index of the query result that we need to process (we focused on 3 for case 1 and 5 for case 2 in our example)
    private final Object modelFieldId; // The field id of the target entity - which depends on the case (ex: last_name in case 1, name in case 2)
    // The following fields are filled only in case 2
    private final Object foreignModelClassId; // The class id of the foreign entity (ex: Country)
    private final SqlColumnToEntityFieldMapping joinMapping; // The join mapping object - so containing the foreign entity column index (ex: 4)

    // Constructor for case 1
    public SqlColumnToEntityFieldMapping(int queryColumnIndex, Object modelFieldId) {
        this(queryColumnIndex, modelFieldId, null, null);
    }

    // Constructor for case 2
    public SqlColumnToEntityFieldMapping(int queryColumnIndex, Object modelFieldId, Object foreignModelClassId, SqlColumnToEntityFieldMapping joinMapping) {
        this.modelFieldId = modelFieldId;
        this.queryColumnIndex = queryColumnIndex;
        this.foreignModelClassId = foreignModelClassId;
        this.joinMapping = joinMapping;
    }

    public int getQueryColumnIndex() {
        return queryColumnIndex;
    }

    public Object getForeignModelClassId() {
        return foreignModelClassId;
    }

    public SqlColumnToEntityFieldMapping getJoinMapping() {
        return joinMapping;
    }

    public Object getModelFieldId() {
        return modelFieldId;
    }
}
