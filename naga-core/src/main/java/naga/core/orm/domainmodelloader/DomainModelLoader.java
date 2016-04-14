package naga.core.orm.domainmodelloader;

import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.domainmodel.Label;
import naga.core.orm.domainmodelloader.domainmodelbuilder.DomainClassBuilder;
import naga.core.orm.domainmodelloader.domainmodelbuilder.DomainFieldBuilder;
import naga.core.orm.domainmodelloader.domainmodelbuilder.DomainFieldsGroupBuilder;
import naga.core.orm.domainmodelloader.domainmodelbuilder.DomainModelBuilder;
import naga.core.spi.platform.Platform;
import naga.core.spi.sql.SqlArgument;
import naga.core.spi.sql.SqlReadResult;
import naga.core.type.DerivedType;
import naga.core.type.PrimType;
import naga.core.type.Type;
import naga.core.util.async.Batch;
import naga.core.util.Numbers;
import naga.core.util.async.Future;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class DomainModelLoader {
    protected final Object id;
    protected final DomainModelBuilder dmb;
    protected final Object dataSourceId = 0;
    protected final Map<Object /*classId*/, DomainClassBuilder> classes = new HashMap<>();
    protected final Map<Object /* id */, Type> typeMap = new HashMap<>();
    protected final Map<Object /* id */, Label> labelMap = new HashMap<>();

    public DomainModelLoader(Object id) {
        dmb = new DomainModelBuilder(id);
        this.id = id;
    }

    public Future<DomainModel> loadDomainModel() {
        Future<DomainModel> future = Future.future();
        Platform.sql().readBatch(generateDomainModelSqlBatch()).setHandler(asyncResult -> {
            if (asyncResult.failed())
                future.fail(asyncResult.cause());
            else
                future.complete(generateDomainModel(asyncResult.result()));
        });
        return future;
    }

    public Batch<SqlArgument> generateDomainModelSqlBatch() {
        return toSqlBatch(
                // 1) Labels loading
                "select id,text,icon from label where data_model_version_id=? or data_model_version_id is null",
                // 2) Types loading
                "select id,name,super_type_id,cell_factory_name,ui_format,sql_format from type where data_model_version_id=?",
                // 3) Classes loading
                "select id,name,sql_table_name,foreign_fields,fxml_form,search_condition,label_id from class where data_model_version_id=?",
                // 4) Fields loading
                "select id,name,class_id,type_id,label_id,pref_width,expression,applicable_condition,persistent,sql_column_name,foreign_class_id,foreign_alias,foreign_condition,foreign_order_by,foreign_combo_fields,foreign_table_fields from field f join class c on f.class_id=c.id where c.data_model_version_id=?",
                // 5) Fields group loading
                "select name,class_id,fields from fields_group fg join class c on fg.class_id=c.id where c.data_model_version_id=?");
    }

    private Batch<SqlArgument> toSqlBatch(String... sqls) {
        SqlArgument[] args = new SqlArgument[sqls.length];
        for (int i = 0; i < sqls.length; i++)
            args[i] = toSqlArgument(sqls[i]);
        return new Batch<>(args);
    }

    private SqlArgument toSqlArgument(String sql) {
        return new SqlArgument(sql, new Object[]{id}, dataSourceId);
    }

    public DomainModel generateDomainModel(Batch<SqlReadResult> batchResult) {
        SqlReadResult[] results = batchResult.getArray();
        // 1) Building labels
        SqlReadResult qr = results[0];
        for (int row = 0; row < qr.getRowCount(); row++)
            labelMap.put(qr.getValue(row, "id"), new Label(qr.getValue(row, "text"), qr.getValue(row, "icon")));
        // 2) Building types
        qr = results[1];
        for (int row = 0; row < qr.getRowCount(); row++) {
            Object typeId = qr.getValue(row, "id");
            Type superType = getType(qr.getValue(row, "super_type_id"));
            //TextFieldFormat uiFormat = TextFieldFormat.parseDefinition(qr.getString("ui_format"));
            //TextFieldFormat sqlFormat = TextFieldFormat.parseDefinition(qr.getString("sql_format"));
            //typeMap.put(typeId, new Type(typeId, qr.getString("name"), superType, null, qr.getString("cell_factory_name"), null, null, uiFormat, sqlFormat));
            typeMap.put(typeId, new DerivedType(qr.getValue(row, "name"), superType, false));
        }
        // 3) Building classes
        qr = results[2];
        for (int row = 0; row < qr.getRowCount(); row++) {
            Object classId = qr.getValue(row, "id");
            final DomainClassBuilder classBuilder = dmb.newClassBuilder(qr.getValue(row, "name"), true);
            classBuilder.id = classId;
            classBuilder.sqlTableName = qr.getValue(row, "sql_table_name");
            classBuilder.foreignFieldsDefinition = qr.getValue(row, "foreign_fields");
            classBuilder.fxmlForm = qr.getValue(row, "fxml_form");
            classBuilder.searchCondition = qr.getValue(row, "search_condition");
            //classBuilder.css = qr.getString("css");
            classBuilder.label = labelMap.get(qr.getValue(row, "label_id"));
            classes.put(classId, classBuilder);
        }
        // 4) Building fields
        qr = results[3];
        for (int row = 0; row < qr.getRowCount(); row++) {
            Object typeId = qr.getValue(row, "type_id");
            Type type = getType(typeId);
            DomainClassBuilder classBuilder = classes.get(qr.getValue(row, "class_id"));
            DomainFieldBuilder fieldBuilder = classBuilder.newFieldBuilder(qr.getValue(row, "name"), type, true);
            //CoreSystem.log("Building field " + classBuilder.name + '.' + fieldBuilder.name);
            fieldBuilder.modelId = qr.getValue(row, "id"); // should be model_id (doesn't exist yet)
            fieldBuilder.label = labelMap.get(qr.getValue(row, "label_id"));
            fieldBuilder.prefWidth = qr.getInt(row, "pref_width", 0);
            fieldBuilder.expressionDefinition = qr.getValue(row, "expression");
            fieldBuilder.applicableConditionDefinition = qr.getValue(row, "applicable_condition");
            fieldBuilder.persistent = qr.getBoolean(row, "persistent", false);
            fieldBuilder.foreignAlias = qr.getValue(row, "foreign_alias");
            fieldBuilder.foreignCondition = qr.getValue(row, "foreign_condition");
            fieldBuilder.foreignOrderBy = qr.getValue(row, "foreign_order_by");
            fieldBuilder.foreignComboFields = qr.getValue(row, "foreign_combo_fields");
            fieldBuilder.foreignTableFields = qr.getValue(row, "foreign_table_fields");
            /* TODO : thinking about foreignKey management
            if (fieldBuilder.type != null && fieldBuilder.type.getBaseType() == BaseType.FOREIGN_KEY && qr.getObject("foreign_class_id") != null)
                fieldBuilder.type = new Type(classes.get(qr.getValue("foreign_class_id")).getObjClass()); */
            DomainClassBuilder foreignClassBuilder = classes.get(qr.getValue(row, "foreign_class_id"));
            if (foreignClassBuilder != null)
                fieldBuilder.foreignClass = foreignClassBuilder.getDomainClass();
            fieldBuilder.sqlColumnName = qr.getValue(row, "sql_column_name");
        }
        // 5) Building fields groups
        qr = results[4];
        for (int row = 0; row < qr.getRowCount(); row++) {
            DomainClassBuilder classBuilder = classes.get(qr.getValue(row, "class_id"));
            DomainFieldsGroupBuilder groupBuilder = classBuilder.newFieldsGroupBuilder(qr.getValue(row, "name"), true);
            groupBuilder.fieldsDefinition = qr.getValue(row, "fields");
        }
        Platform.log("Domain model loaded: " + results[2].getRowCount() + " classes, " + results[3].getRowCount() + " fields, " + results[4].getRowCount() + " fields groups and " + results[0].getRowCount() + " labels");
        // Building and returning final domain model
        return dmb.build();
    }

    private Type getType(Object typeId) {
        Type type = typeMap.get(typeId);
        if (type == null)
            type = getPrimType(typeId);
        return type;
    }

    private PrimType getPrimType(Object id) {
        if (id == null)
            return null;
        switch (Numbers.intValue(id)) { // Keeping compatibility with KBS2.0 types
            case 0: return PrimType.BOOLEAN;
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
}
