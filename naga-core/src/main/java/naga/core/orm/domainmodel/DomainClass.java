package naga.core.orm.domainmodel;

import naga.core.orm.domainmodel.lciimpl.ParserDomainModelReaderImpl;
import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.*;
import naga.core.orm.expressionparser.ExpressionParser;
import naga.core.util.Booleans;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class DomainClass  {
    private final DomainModel dataModel;
    private final Object id;
    private final Object modelId;
    private final String name;
    private final String sqlTableName;
    private final String idColumnName;
    private final Label label;
    private final String foreignFieldsDefinition;
    private Expression foreignFields;
    private final String css;
    private ExpressionArray rowStyles;
    private final String fxmlForm;
    private final String searchCondition;
    private final Map<Object /* modelId or name */, DomainField> fieldMap;
    private final Map<String /* sql column */, DomainField> sqlMap;
    private final Map<Object /* modelId or name */, FieldsGroup> fieldsGroupMap;

    private Delete deleteWhereId;

    public DomainClass(DomainModel dataModel, Object id, Object modelId, String name, String sqlTableName, String idColumnName, Label label, String foreignFieldsDefinition, String searchCondition, String css, String rowStyleDefinition, Map<Object, DomainField> fieldMap, Map<String, DomainField> sqlMap, Map<Object, FieldsGroup> fieldsGroupMap, String fxmlForm) {
        this(dataModel, id, modelId, name, sqlTableName, idColumnName, label, foreignFieldsDefinition, searchCondition, css, fieldMap, sqlMap, fieldsGroupMap, fxmlForm);
    }

    public DomainClass(DomainModel dataModel, Object id, Object modelId, String name, String sqlTableName, String idColumnName, Label label, String foreignFieldsDefinition, String searchCondition, String css, Map<Object, DomainField> fieldMap, Map<String, DomainField> sqlMap, Map<Object, FieldsGroup> fieldsGroupMap, String fxmlForm) {
        this.dataModel = dataModel;
        this.id = id;
        this.modelId = modelId;
        this.name = name;
        this.sqlTableName = sqlTableName;
        this.idColumnName = idColumnName;
        this.label = label;
        this.foreignFieldsDefinition = foreignFieldsDefinition;
        this.searchCondition = searchCondition;
        this.css = css;
        this.fieldMap = fieldMap;
        this.sqlMap = sqlMap;
        this.fieldsGroupMap = fieldsGroupMap;
        this.fxmlForm = fxmlForm;
    }

    public DomainModel getDataModel() {
        return dataModel;
    }

    public Object getId() {
        return id;
    }

    public Object getModelId() {
        return modelId;
    }

    public String getName() {
        return name;
    }

    public String getSqlTableName() {
        return sqlTableName;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public Label getLabel() {
        return label;
    }

    public Expression getForeignFields() {
        if (foreignFields == null && foreignFieldsDefinition != null)
            foreignFields = parseExpression(foreignFieldsDefinition);
        return foreignFields;
    }

    public ExpressionArray getRowStyles() {
        return rowStyles;
    }

    public String getFxmlForm() {
        return fxmlForm;
    }

    public String getSearchCondition() {
        return searchCondition;
    }

    public String getCss() {
        return css;
    }

    public DomainField getField(Object id) {
        return fieldMap.get(id);
    }

    public DomainField getIdField() {
        return sqlMap.get(idColumnName);
    }

    public DomainField getFieldFromSqlColumn(String sqlColumnName) {
        return sqlMap.get(sqlColumnName);
    }

    public FieldsGroup getFieldsGroup(Object id) {
        return fieldsGroupMap.get(id);
    }

    public StringBuilder toString(StringBuilder sb, Object parameter) {
        return sb.append(Booleans.isTrue(parameter) ? modelId : name);
    }

    @Override
    public String toString() {
        return name;
    }

    public Expression parseExpression(String definition) {
        return definition == null ? null : ExpressionParser.parseExpression(definition, this, new ParserDomainModelReaderImpl(dataModel), false);
    }

    public ExpressionArray parseExpressionArray(String definition) {
        return definition == null ? null : (ExpressionArray) ExpressionParser.parseExpression(definition, this, new ParserDomainModelReaderImpl(dataModel), true);
    }

    public final static Expression WHERE_ID_EQUALS_PARAM = new Equals(IDExpression.singleton, Parameter.UNNAMED_PARAMETER);

    public Delete getDeleteWhereId() {
        if (deleteWhereId == null)
            deleteWhereId = new Delete(this, null, WHERE_ID_EQUALS_PARAM);
        return deleteWhereId;
    }
}
