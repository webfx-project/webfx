package naga.core.orm.domainmodelloader;

import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.domainmodel.Label;
import naga.core.orm.domainmodelloader.domainmodelbuilder.DomainClassBuilder;
import naga.core.orm.domainmodelloader.domainmodelbuilder.DomainFieldBuilder;
import naga.core.orm.domainmodelloader.domainmodelbuilder.DomainFieldsGroupBuilder;
import naga.core.orm.domainmodelloader.domainmodelbuilder.DomainModelBuilder;
import naga.core.queryservice.QueryArgument;
import naga.core.queryservice.QueryResultSet;
import naga.core.spi.platform.Platform;
import naga.core.type.DerivedType;
import naga.core.type.PrimType;
import naga.core.type.Type;
import naga.core.util.Numbers;
import naga.core.util.async.Batch;
import naga.core.util.async.Future;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class DomainModelLoader {
    private final Object id;
    private final DomainModelBuilder dmb;
    private final Object dataSourceId = 0;
    private final Map<Object /*classId*/, DomainClassBuilder> classes = new HashMap<>();
    private final Map<Object /* id */, Type> typeMap = new HashMap<>();
    private final Map<Object /* id */, Label> labelMap = new HashMap<>();

    public DomainModelLoader(Object id) {
        dmb = new DomainModelBuilder(id);
        this.id = id;
    }

    public Future<DomainModel> loadDomainModel() {
        Future<DomainModel> future = Future.future();
        Platform.query().readBatch(generateDomainModelQueryBatch()).setHandler(asyncResult -> {
            if (asyncResult.failed())
                future.fail(asyncResult.cause());
            else
                future.complete(generateDomainModel(asyncResult.result()));
        });
        return future;
    }

    public Batch<QueryArgument> generateDomainModelQueryBatch() {
        return toQueryBatch(
                // 1) Labels loading
                "select id,text,icon from label where data_model_version_id=? or data_model_version_id is null",
                // 2) Types loading
                "select id,name,super_type_id,cell_factory_name,ui_format,sql_format from type where data_model_version_id=?",
                // 3) Classes loading
                "select id,name,sql_table_name,foreign_fields,fxml_form,search_condition,label_id from class where data_model_version_id=?",
                // 4) Style classes loading
                "select c.id,f.name,s.name,s.condition from data_view s join data_view f on f.id=s.parent_id join class c on c.id=f.scope_class_id where c.data_model_version_id=? and active and is_style and not is_folder and s.scope_activity_id is null order by c.id,f.ord,s.ord desc",
                // 5) Fields loading
                "select id,name,class_id,type_id,label_id,pref_width,expression,applicable_condition,persistent,sql_column_name,foreign_class_id,foreign_alias,foreign_condition,foreign_order_by,foreign_combo_fields,foreign_table_fields from field f join class c on f.class_id=c.id where c.data_model_version_id=?",
                // 6) Fields group loading
                "select name,class_id,fields from fields_group fg join class c on fg.class_id=c.id where c.data_model_version_id=?"
        );
    }

    private Batch<QueryArgument> toQueryBatch(String... queryStrings) {
        QueryArgument[] args = new QueryArgument[queryStrings.length];
        for (int i = 0; i < queryStrings.length; i++)
            args[i] = toQueryArgument(queryStrings[i]);
        return new Batch<>(args);
    }

    private QueryArgument toQueryArgument(String queryString) {
        return new QueryArgument(queryString, new Object[]{id}, dataSourceId);
    }

    public DomainModel generateDomainModel(Batch<QueryResultSet> batchResult) {
        QueryResultSet[] resultSets = batchResult.getArray();

        // 1) Building labels
        QueryResultSet rs = resultSets[0];
        for (int row = 0; row < rs.getRowCount(); row++)
            labelMap.put(rs.getValue(row, "id"), new Label(rs.getValue(row, "text"), rs.getValue(row, "icon")));

        // 2) Building types
        rs = resultSets[1];
        for (int row = 0; row < rs.getRowCount(); row++) {
            Object typeId = rs.getValue(row, "id");
            Type superType = getTypeFromId(rs.getValue(row, "super_type_id"));
            //TextFieldFormat uiFormat = TextFieldFormat.parseDefinition(rs.getString("ui_format"));
            //TextFieldFormat sqlFormat = TextFieldFormat.parseDefinition(rs.getString("sql_format"));
            //typeMap.put(typeId, new Type(typeId, rs.getString("name"), superType, null, rs.getString("cell_factory_name"), null, null, uiFormat, sqlFormat));
            typeMap.put(typeId, new DerivedType(rs.getValue(row, "name"), superType, false));
        }

        // 3) Building classes
        rs = resultSets[2];
        for (int row = 0; row < rs.getRowCount(); row++) {
            Object classId = rs.getValue(row, "id");
            final DomainClassBuilder classBuilder = dmb.newClassBuilder(rs.getValue(row, "name"), true);
            classBuilder.id = classId;
            classBuilder.sqlTableName = rs.getValue(row, "sql_table_name");
            classBuilder.foreignFieldsDefinition = rs.getValue(row, "foreign_fields");
            classBuilder.fxmlForm = rs.getValue(row, "fxml_form");
            classBuilder.searchCondition = rs.getValue(row, "search_condition");
            //classBuilder.css = rs.getString("css");
            classBuilder.label = labelMap.get(rs.getValue(row, "label_id"));
            classes.put(classId, classBuilder);
        }

        // 4) Style classes loading
        rs = resultSets[3];
        StringBuilder allDefinitions = null;
        String currentDefinition = null;
        String folderName = null;
        Object lastClassId = null;
        for (int row = 0; row < rs.getRowCount(); row++) {
            Object classId = rs.getValue(row, 0);
            String fName = rs.getValue(row, 1);
            String styleName = rs.getValue(row, 2);
            String condition = rs.getValue(row, 3);
            if (lastClassId == null || !lastClassId.equals(classId)) { // Going to next class
                if (lastClassId != null)
                    recordStyleClassesExpressionArrayDefinition(classes.get(lastClassId), allDefinitions, currentDefinition);
                allDefinitions = null;
                currentDefinition = null;
                folderName = null;
                lastClassId = classId;
            }
            if (folderName != null && !folderName.equals(fName)) { // Going to next folder
                allDefinitions = appendDefinition(currentDefinition, allDefinitions);
                currentDefinition = null;
            }
            folderName = fName;
            if (condition == null) // should arrive in first position due to order by desc ordering (the null condition is the last one)
                currentDefinition = "'" + styleName + "'";
            else {
                /*Object activityId = rs.getValue(row, 4);
                if (activityId != null) // if scope_activity is set, adding this criteria in the condition:
                    e = new And(e, new Equals(new Parameter("selectedActivity", null), Constant.newConstant(activityId)));*/
                currentDefinition = '(' + condition + ") ? '" + styleName + "' : " + currentDefinition;
            }
        }
        if (currentDefinition != null)
            recordStyleClassesExpressionArrayDefinition(classes.get(lastClassId), allDefinitions, currentDefinition);

        // 5) Building fields
        rs = resultSets[4];
        for (int row = 0; row < rs.getRowCount(); row++) {
            Object typeId = rs.getValue(row, "type_id");
            Type type = getTypeFromId(typeId);
            DomainClassBuilder classBuilder = classes.get(rs.getValue(row, "class_id"));
            DomainFieldBuilder fieldBuilder = classBuilder.newFieldBuilder(rs.getValue(row, "name"), type, true);
            //CoreSystem.log("Building field " + classBuilder.name + '.' + fieldBuilder.name);
            fieldBuilder.modelId = rs.getValue(row, "id"); // should be model_id (doesn't exist yet)
            fieldBuilder.label = labelMap.get(rs.getValue(row, "label_id"));
            fieldBuilder.prefWidth = rs.getInt(row, "pref_width", 0);
            fieldBuilder.expressionDefinition = rs.getValue(row, "expression");
            fieldBuilder.applicableConditionDefinition = rs.getValue(row, "applicable_condition");
            fieldBuilder.persistent = rs.getBoolean(row, "persistent", false);
            fieldBuilder.foreignAlias = rs.getValue(row, "foreign_alias");
            fieldBuilder.foreignCondition = rs.getValue(row, "foreign_condition");
            fieldBuilder.foreignOrderBy = rs.getValue(row, "foreign_order_by");
            fieldBuilder.foreignComboFields = rs.getValue(row, "foreign_combo_fields");
            fieldBuilder.foreignTableFields = rs.getValue(row, "foreign_table_fields");
            /* TODO : thinking about foreignKey management
            if (fieldBuilder.type != null && fieldBuilder.type.getBaseType() == BaseType.FOREIGN_KEY && rs.getObject("foreign_class_id") != null)
                fieldBuilder.type = new Type(classes.get(rs.getValue("foreign_class_id")).getObjClass()); */
            DomainClassBuilder foreignClassBuilder = classes.get(rs.getValue(row, "foreign_class_id"));
            if (foreignClassBuilder != null)
                fieldBuilder.foreignClass = foreignClassBuilder.getDomainClass();
            fieldBuilder.sqlColumnName = rs.getValue(row, "sql_column_name");
        }

        // 6) Building fields groups
        rs = resultSets[5];
        for (int row = 0; row < rs.getRowCount(); row++) {
            DomainClassBuilder classBuilder = classes.get(rs.getValue(row, "class_id"));
            DomainFieldsGroupBuilder groupBuilder = classBuilder.newFieldsGroupBuilder(rs.getValue(row, "name"), true);
            groupBuilder.fieldsDefinition = rs.getValue(row, "fields");
        }
        Platform.log("Domain model loaded: " + resultSets[2].getRowCount() + " classes, " + resultSets[3].getRowCount() + " fields, " + resultSets[4].getRowCount() + " fields groups and " + resultSets[0].getRowCount() + " labels");
        // Building and returning final domain model
        return dmb.build();
    }

    private Type getTypeFromId(Object typeId) {
        Type type = typeMap.get(typeId);
        if (type == null)
            type = getPrimTypeFromId(typeId);
        return type;
    }

    private PrimType getPrimTypeFromId(Object id) {
        if (id == null)
            return null;
        switch (Numbers.intValue(id)) { // Keeping compatibility with KBS2.0 types
            case 0: return PrimType.INTEGER;
            case 1: return PrimType.LONG;
            case 2: return PrimType.FLOAT;
            case 3: return PrimType.DOUBLE;
            case 4: return PrimType.BOOLEAN;
            case 5: return PrimType.STRING;
            case 6: return PrimType.DATE;
            case 7: return PrimType.LONG; // FOREIGN_KEY
            default: throw new IllegalArgumentException(); // is there anything else?
        }
    }

    private static StringBuilder appendDefinition(String definition, StringBuilder allDefinitions) {
        if (definition != null) {
            if (allDefinitions == null)
                allDefinitions = new StringBuilder(definition);
            else
                allDefinitions.append(',').append(definition);
        }
        return allDefinitions;
    }

    private static void recordStyleClassesExpressionArrayDefinition(DomainClassBuilder classBuilder, StringBuilder allDefinitions, String lastDefinition) {
        String finalExpressionDefinition;
        if (allDefinitions == null)
            finalExpressionDefinition = lastDefinition;
        else
            finalExpressionDefinition = appendDefinition(lastDefinition, allDefinitions).toString();
        classBuilder.styleClassesExpressionArrayDefinition = finalExpressionDefinition;
    }
}
