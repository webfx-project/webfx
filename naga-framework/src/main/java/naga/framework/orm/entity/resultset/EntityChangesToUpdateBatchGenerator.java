package naga.framework.orm.entity.resultset;

import naga.commons.util.async.Batch;
import naga.framework.expression.Expression;
import naga.framework.expression.lci.CompilerDomainModelReader;
import naga.framework.expression.lci.ParserDomainModelReader;
import naga.framework.expression.sqlcompiler.ExpressionSqlCompiler;
import naga.framework.expression.sqlcompiler.sql.DbmsSqlSyntaxOptions;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.expression.terms.*;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.orm.entity.EntityId;
import naga.platform.services.update.UpdateArgument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class EntityChangesToUpdateBatchGenerator {

    private EntityChangesToUpdateBatchGenerator() {
    }

    public static Batch<UpdateArgument> generateUpdateBatch(EntityChanges changes, DataSourceModel dataSourceModel) {
        return generateUpdateBatch(changes, dataSourceModel.getId(), DbmsSqlSyntaxOptions.POSTGRES_SYNTAX, dataSourceModel.getDomainModel());
    }

    public static Batch<UpdateArgument> generateUpdateBatch(EntityChanges changes, Object dataSourceId, DbmsSqlSyntaxOptions dbmsSyntax, DomainModel domainModel) {
        return generateUpdateBatch(changes, dataSourceId, dbmsSyntax, domainModel.getParserDomainModelReader(), domainModel.getCompilerDomainModelReader());
    }

    public static Batch<UpdateArgument> generateUpdateBatch(EntityChanges changes, Object dataSourceId, DbmsSqlSyntaxOptions dbmsSyntax, ParserDomainModelReader parserModelReader, CompilerDomainModelReader compilerModelReader) {
        return new BatchGenerator(changes, dataSourceId, dbmsSyntax, compilerModelReader).generate();
    }

    private static class BatchGenerator {

        final static Expression WHERE_ID_EQUALS_PARAM = new Equals(IdExpression.singleton, Parameter.UNNAMED_PARAMETER);

        private final EntityChanges changes;
        private final Object dataSourceId;
        private final DbmsSqlSyntaxOptions dbmsSyntax;
        private final CompilerDomainModelReader compilerModelReader;
        private final List<UpdateArgument> updateArguments = new ArrayList<>();

        public BatchGenerator(EntityChanges changes, Object dataSourceId, DbmsSqlSyntaxOptions dbmsSyntax, CompilerDomainModelReader compilerModelReader) {
            this.changes = changes;
            this.dataSourceId = dataSourceId;
            this.dbmsSyntax = dbmsSyntax;
            this.compilerModelReader = compilerModelReader;
        }

        Batch<UpdateArgument> generate() {
            // First generating delete statements
            generateDeletes();
            // Then insert and update statements
            generateInsertUpdates();
            // Returning the batch
            return new Batch<>(updateArguments.toArray(new UpdateArgument[updateArguments.size()]));
        }

        void generateDeletes() {
            Collection<EntityId> deletedEntities = changes.getDeletedEntityIds();
            if (deletedEntities != null && !deletedEntities.isEmpty()) {
                List<EntityId> deletedList = new ArrayList<>(deletedEntities);
                // Sorting according to classes references
                Collections.sort(deletedList, (id1, id2) -> id1.getDomainClass().getName().compareTo(id2.getDomainClass().getName()));
                for (EntityId deletedId : deletedList) // java 8 forEach doesn't compile with GWT
                    generateDelete(deletedId);
            }
        }

        void generateDelete(EntityId id) {
            Delete delete = new Delete(id.getDomainClass(), null, WHERE_ID_EQUALS_PARAM);
            Object[] parameterValues = {id.getPrimaryKey()};
            addToBatch(compileDelete(delete, parameterValues), parameterValues);
        }

        SqlCompiled compileDelete(Delete delete, Object[] parameterValues) {
            return ExpressionSqlCompiler.compileDelete(delete, parameterValues, dbmsSyntax, compilerModelReader);
        }

        void generateInsertUpdates() {
            EntityResultSet rs = changes.getInsertedUpdatedEntityResultSet();
            if (rs != null) {
                for (EntityId id : rs.getEntityIds()) {
                    List<Equals> assignments = new ArrayList<>();
                    for (Object fieldId : rs.getFieldIds(id))
                        if (fieldId != null) {
                            Object fieldValue = rs.getFieldValue(id, fieldId);
                            if (fieldValue instanceof EntityId)
                                fieldValue = ((EntityId) fieldValue).getPrimaryKey();
                            assignments.add(new Equals(id.getDomainClass().getField(fieldId), Constant.newConstant(fieldValue)));
                        }
                    ExpressionArray setClause = new ExpressionArray(assignments);
                    if (id.isNew()) { // insert statement
                        Insert insert = new Insert(id.getDomainClass(), setClause);
                        addToBatch(compileInsert(insert, null), null);
                    } else { // update statement
                        Update update = new Update(id.getDomainClass(), setClause, WHERE_ID_EQUALS_PARAM);
                        Object[] parameterValues = {id.getPrimaryKey()};
                        addToBatch(compileUpdate(update, parameterValues), parameterValues);
                    }
                }
                // TODO: sort sql statements
            }
        }

        SqlCompiled compileInsert(Insert insert, Object[] parameterValues) {
            return ExpressionSqlCompiler.compileInsert(insert, parameterValues, dbmsSyntax, compilerModelReader);
        }

        SqlCompiled compileUpdate(Update update, Object[] parameterValues) {
            return ExpressionSqlCompiler.compileUpdate(update, parameterValues, dbmsSyntax, compilerModelReader);
        }

        void addToBatch(SqlCompiled sqlcompiled, Object[] parameterValues) {
            updateArguments.add(new UpdateArgument(sqlcompiled.getSql(), parameterValues, false, dataSourceId));
        }
    }
}
