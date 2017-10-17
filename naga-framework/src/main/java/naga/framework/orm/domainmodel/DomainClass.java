package naga.framework.orm.domainmodel;

import naga.util.Booleans;
import naga.framework.expression.Expression;
import naga.framework.expression.parser.ExpressionParser;
import naga.framework.expression.terms.*;
import naga.fxdata.displaydata.HasLabel;
import naga.fxdata.displaydata.Label;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class DomainClass implements HasLabel {
    private final DomainModel domainModel;
    private final Object id;
    private final Object modelId;
    private final String name;
    private final String sqlTableName;
    private final String idColumnName;
    private final Label label;
    private final String foreignFieldsDefinition;
    private Expression foreignFields;
    private final String css;
    private final String styleClassesExpressionArrayDefinition;
    private ExpressionArray styleClassesExpressionArray;
    private final String fxmlForm;
    private final String searchCondition;
    private final Map<Object /* modelId or name */, DomainField> fieldMap;
    private final Map<String /* sql column */, DomainField> sqlMap;
    private final Map<Object /* modelId or name */, FieldsGroup> fieldsGroupMap;

    public DomainClass(DomainModel domainModel, Object id, Object modelId, String name, String sqlTableName, String idColumnName, Label label, String foreignFieldsDefinition, String searchCondition, String css, String styleClassesExpressionArrayDefinition, Map<Object, DomainField> fieldMap, Map<String, DomainField> sqlMap, Map<Object, FieldsGroup> fieldsGroupMap, String fxmlForm) {
        this.domainModel = domainModel;
        this.id = id;
        this.modelId = modelId;
        this.name = name;
        this.sqlTableName = sqlTableName;
        this.idColumnName = idColumnName;
        this.label = label;
        this.foreignFieldsDefinition = foreignFieldsDefinition;
        this.searchCondition = searchCondition;
        this.css = css;
        this.styleClassesExpressionArrayDefinition = styleClassesExpressionArrayDefinition;
        this.fieldMap = fieldMap;
        this.sqlMap = sqlMap;
        this.fieldsGroupMap = fieldsGroupMap;
        this.fxmlForm = fxmlForm;
    }

    public DomainModel getDomainModel() {
        return domainModel;
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

    public ExpressionArray getStyleClassesExpressionArray() {
        if (styleClassesExpressionArray == null && styleClassesExpressionArrayDefinition != null)
            styleClassesExpressionArray = parseExpressionArray(styleClassesExpressionArrayDefinition);
        return styleClassesExpressionArray;
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

    public DomainClass getForeignClass(Object foreignFieldId) {
        return (DomainClass) domainModel.getParserDomainModelReader().getSymbolForeignDomainClass(this, getField(foreignFieldId));
    }

    @Override
    public String toString() {
        return name;
    }

    public Expression parseExpression(String definition) {
        return definition == null ? null : ExpressionParser.parseExpression(definition, this, domainModel.getParserDomainModelReader(), false);
    }

    public ExpressionArray parseExpressionArray(String definition) {
        return definition == null ? null : (ExpressionArray) ExpressionParser.parseExpression(definition, this, domainModel.getParserDomainModelReader(), true);
    }
}
