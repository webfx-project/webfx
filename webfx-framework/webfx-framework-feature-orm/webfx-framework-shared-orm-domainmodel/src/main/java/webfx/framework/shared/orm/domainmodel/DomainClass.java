package webfx.framework.shared.orm.domainmodel;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.parser.ExpressionParser;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;
import webfx.extras.label.HasLabel;
import webfx.extras.label.Label;
import webfx.platform.shared.util.Booleans;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class DomainClass implements HasLabel, HasDomainModel {
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

    boolean isAggregate;

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

    public boolean isAggregate() {
        return isAggregate;
    }

    public void setAggregate(boolean aggregate) {
        isAggregate = aggregate;
    }

    @Override
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

    public <E> Expression<E> getForeignFields() {
        if (foreignFields == null && foreignFieldsDefinition != null)
            foreignFields = parseExpression(foreignFieldsDefinition);
        return foreignFields;
    }

    public <E> ExpressionArray<E> getStyleClassesExpressionArray() {
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

    public <E> Expression<E> parseExpression(String definition) {
        return definition == null ? null : ExpressionParser.parseExpression(definition, this, domainModel.getParserDomainModelReader(), false);
    }

    public <E> ExpressionArray<E> parseExpressionArray(String definition) {
        return definition == null ? null : (ExpressionArray) ExpressionParser.parseExpression(definition, this, domainModel.getParserDomainModelReader(), true);
    }
}
