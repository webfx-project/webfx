package naga.framework.orm.domainmodel.builder;

import naga.type.Type;
import naga.framework.expression.sqlcompiler.ExpressionSqlCompiler;
import naga.framework.expression.terms.IdExpression;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.domainmodel.DomainField;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.orm.domainmodel.FieldsGroup;
import naga.fxdata.displaydata.Label;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class DomainClassBuilder {
    public DomainModelBuilder domainModelBuilder;
    public DomainModel domainModel;
    public Object id;
    public Object modelId;
    public String name;
    public String sqlTableName;
    public String idColumnName = "id";
    public Label label;
    public String foreignFieldsDefinition;
    public String fxmlForm;
    public String searchCondition;
    public String css;
    public Map<String, DomainFieldBuilder> fieldMap = new HashMap<>();
    public Map<String, DomainFieldsGroupBuilder> fieldsGroupMap = new HashMap<>();
    public String styleClassesExpressionArrayDefinition;

    public DomainClass domainClass;
    private boolean built;
    private Map<Object, DomainField> logicalMap = new HashMap<>();
    private Map<String, DomainField> sqlMap = new HashMap<>();
    private Map<Object, FieldsGroup> groupMap = new HashMap<>();

    public DomainClassBuilder(String name) {
        this.name = name;
    }

    public void registerField(DomainFieldBuilder field) {
        fieldMap.put(field.name, field);
    }

    public void registerFieldsGroup(DomainFieldsGroupBuilder fieldsGroup) {
        fieldsGroupMap.put(fieldsGroup.name, fieldsGroup);
    }

    public DomainFieldBuilder newFieldBuilder(String name, Type type, boolean register) {
        DomainFieldBuilder fieldBuilder = new DomainFieldBuilder(name);
        fieldBuilder.classBuilder = this;
        fieldBuilder.type = type;
        if (register)
            registerField(fieldBuilder);
        return fieldBuilder;
    }

    public DomainFieldsGroupBuilder newFieldsGroupBuilder(String name, boolean register) {
        DomainFieldsGroupBuilder groupBuilder = new DomainFieldsGroupBuilder(name);
        groupBuilder.classBuilder = this;
        if (register)
            registerFieldsGroup(groupBuilder);
        return groupBuilder;
    }

    public DomainClass getDomainClass() {
        if (domainClass == null) {
            if (domainModel == null)
                domainModel = domainModelBuilder.dataModel;
            if (modelId == null)
                modelId = name;
            domainClass = domainModel.getClass(modelId);
            built = domainClass != null;
            if (domainClass == null) {
                if (sqlTableName == null)
                    sqlTableName = ExpressionSqlCompiler.toSqlString(name);
                if (label == null)
                    label = new Label(name);
                domainClass = new DomainClass(domainModel, id, modelId, name, sqlTableName, idColumnName, label, foreignFieldsDefinition, searchCondition, css, styleClassesExpressionArrayDefinition, logicalMap, sqlMap, groupMap, fxmlForm);
            }
        }
        return domainClass;
    }

    public DomainClass build() {
        getDomainClass();
        if (!built) {
            for (DomainFieldBuilder fieldBuilder : fieldMap.values())
                buildField(fieldBuilder);
            if (domainClass.getIdField() == null) {
                DomainFieldBuilder idFieldBuilder = new DomainFieldBuilder("id");
                idFieldBuilder.expression = IdExpression.singleton;
                idFieldBuilder.sqlColumnName = idColumnName;
                buildField(idFieldBuilder);
            }
            for (DomainFieldsGroupBuilder groupBuilder : fieldsGroupMap.values())
                buildFieldsGroup(groupBuilder);
            built = true;
        }
        return domainClass;
    }

    private void buildField(DomainFieldBuilder fieldBuilder) {
        fieldBuilder.domainClass = domainClass;
        DomainField field = fieldBuilder.build();
        logicalMap.put(field.getName(), field);
        if (field.getSqlColumnName() != null)
            sqlMap.put(field.getSqlColumnName(), field);
    }

    private void buildFieldsGroup(DomainFieldsGroupBuilder groupBuilder) {
        groupBuilder.domainClass = domainClass;
        FieldsGroup group = groupBuilder.build();
        groupMap.put(group.getName(), group);
    }

}
