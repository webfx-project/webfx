package naga.framework.orm.entity.resultset;

import naga.framework.orm.domainmodel.lciimpl.CompilerDomainModelReaderImpl;
import naga.framework.orm.entity.EntityId;
import naga.framework.expression.Expression;
import naga.framework.expression.terms.*;
import naga.framework.expression.lci.ParserDomainModelReader;
import naga.framework.expression.sqlcompiler.ExpressionSqlCompiler;
import naga.framework.expression.lci.CompilerDomainModelReader;
import naga.framework.expression.sqlcompiler.sql.DbmsSqlSyntaxOptions;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.commons.services.update.UpdateArgument;
import naga.commons.util.async.Batch;

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

    public static Batch<UpdateArgument> generateUpdateBatch(EntityChanges changes, Object dataSourceId) {
        return generateUpdateBatch(changes, dataSourceId, DbmsSqlSyntaxOptions.POSTGRES_SYNTAX, null, CompilerDomainModelReaderImpl.SINGLETON);
    }

    public static Batch<UpdateArgument> generateUpdateBatch(EntityChanges changes, Object dataSourceId, DbmsSqlSyntaxOptions dbmsSyntax, ParserDomainModelReader parserModelReader, CompilerDomainModelReader compilerModelReader) {
        return new BatchGenerator(changes, dataSourceId, dbmsSyntax, parserModelReader, compilerModelReader).generate();
    }

    private static class BatchGenerator {

        final static Expression WHERE_ID_EQUALS_PARAM = new Equals(IdExpression.singleton, Parameter.UNNAMED_PARAMETER);

        private final EntityChanges changes;
        private final Object dataSourceId;
        private final DbmsSqlSyntaxOptions dbmsSyntax;
        private final ParserDomainModelReader parserModelReader;
        private final CompilerDomainModelReader compilerModelReader;
        private final List<UpdateArgument> updateArguments = new ArrayList<>();

        public BatchGenerator(EntityChanges changes, Object dataSourceId, DbmsSqlSyntaxOptions dbmsSyntax, ParserDomainModelReader parserModelReader, CompilerDomainModelReader compilerModelReader) {
            this.changes = changes;
            this.dataSourceId = dataSourceId;
            this.dbmsSyntax = dbmsSyntax;
            this.parserModelReader = parserModelReader;
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
                Collections.sort(deletedList, (id1, id2) -> {
                    Object classId1 = id1.getDomainClassId();
                    return (classId1 instanceof Comparable) ? - ((Comparable) classId1).compareTo(id2.getDomainClassId()) : 0;
                });
                for (EntityId deletedId : deletedList) // java 8 forEach doesn't compile with GWT
                    generateDelete(deletedId);
            }
        }

        void generateDelete(EntityId id) {
            Delete delete = new Delete(id.getDomainClassId(), null, WHERE_ID_EQUALS_PARAM);
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
                        assignments.add(new Equals(parserModelReader.getDomainFieldSymbol(id.getDomainClassId(), fieldId.toString()), Constant.newConstant(rs.getFieldValue(id, fieldId))));
                    ExpressionArray setClause = new ExpressionArray(assignments);
                    if (id.isNew()) { // insert statement
                        Insert insert = new Insert(id.getDomainClassId(), setClause);
                        addToBatch(compileInsert(insert, null), null);
                    } else { // update statement
                        Update update = new Update(id.getDomainClassId(), setClause, WHERE_ID_EQUALS_PARAM);
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
