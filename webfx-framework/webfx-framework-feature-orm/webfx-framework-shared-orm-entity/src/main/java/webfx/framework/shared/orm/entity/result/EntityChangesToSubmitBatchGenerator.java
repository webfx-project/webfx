package webfx.framework.shared.orm.entity.result;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainField;
import webfx.framework.shared.orm.dql.sqlcompiler.ExpressionSqlCompiler;
import webfx.framework.shared.orm.dql.sqlcompiler.lci.CompilerDomainModelReader;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.SqlCompiled;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.dbms.DbmsSqlSyntax;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.parser.lci.ParserDomainModelReader;
import webfx.framework.shared.orm.expression.terms.*;
import webfx.platform.shared.datascope.DataScope;
import webfx.platform.shared.services.submit.GeneratedKeyBatchIndex;
import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.services.submit.SubmitResult;
import webfx.platform.shared.util.Arrays;
import webfx.platform.shared.util.async.Batch;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public final class EntityChangesToSubmitBatchGenerator {

    private EntityChangesToSubmitBatchGenerator() {
    }

    public static BatchGenerator createSubmitBatchGenerator(EntityChanges changes, DataSourceModel dataSourceModel, DataScope dataScope, SubmitArgument... initialUpdates) {
        return createSubmitBatchGenerator(changes, dataSourceModel.getDataSourceId(), dataScope, dataSourceModel.getDbmsSqlSyntax(), dataSourceModel.getDomainModel().getParserDomainModelReader(), dataSourceModel.getCompilerDomainModelReader(), initialUpdates);
    }

    public static BatchGenerator createSubmitBatchGenerator(EntityChanges changes, Object dataSourceId, DataScope dataScope, DbmsSqlSyntax dbmsSyntax, ParserDomainModelReader parserModelReader, CompilerDomainModelReader compilerModelReader, SubmitArgument... initialUpdates) {
        return new BatchGenerator(changes, dataSourceId, dataScope, dbmsSyntax, compilerModelReader, initialUpdates);
    }

    public static Batch<SubmitArgument> generateSubmitBatch(EntityChanges changes, DataSourceModel dataSourceModel, DataScope dataScope, SubmitArgument... initialUpdates) {
        return createSubmitBatchGenerator(changes, dataSourceModel, dataScope, initialUpdates).generate();
    }

    public static Batch<SubmitArgument> generateSubmitBatch(EntityChanges changes, Object dataSourceId, DataScope dataScope, DbmsSqlSyntax dbmsSyntax, ParserDomainModelReader parserModelReader, CompilerDomainModelReader compilerModelReader, SubmitArgument... initialUpdates) {
        return createSubmitBatchGenerator(changes, dataSourceId, dataScope, dbmsSyntax, parserModelReader, compilerModelReader, initialUpdates).generate();
    }

    public static final class BatchGenerator {

        final static Expression<?> WHERE_ID_EQUALS_PARAM = new Equals<>(IdExpression.singleton, Parameter.UNNAMED_PARAMETER);

        private final EntityChanges changes;
        private final Object dataSourceId;
        private final DataScope dataScope;
        private final DbmsSqlSyntax dbmsSyntax;
        private final CompilerDomainModelReader compilerModelReader;
        private final List<SubmitArgument> submitArguments;
        private final Map<EntityId, Integer> newEntityIdInitialInsertBatchIndexes = new IdentityHashMap<>();
        private List<Integer> newEntityFinalInsertBatchIndexes;

        BatchGenerator(EntityChanges changes, Object dataSourceId, DataScope dataScope, DbmsSqlSyntax dbmsSyntax, CompilerDomainModelReader compilerModelReader, SubmitArgument... initialUpdates) {
            submitArguments = initialUpdates == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(initialUpdates));
            this.changes = changes;
            this.dataSourceId = dataSourceId;
            this.dataScope = dataScope;
            this.dbmsSyntax = dbmsSyntax;
            this.compilerModelReader = compilerModelReader;
        }

        public Batch<SubmitArgument> generate() {
            // First generating delete statements
            generateDeletes();
            // Then insert and update statements. Statements parameters values may temporary contains EntityId objects,
            // which will be replaced on next step while sorting statements.
            generateInsertUpdates();
            // Finally sorting the statements so that any statement (insert or update) that is referring to a new entity
            // will be executed after that entity has been inserted into the database. For such statements, the parameter
            // value referring to the new entity is replaced with a GeneratedKeyBatchIndex object that contains the index
            // of the insert statement in the batch. The SubmitService must replace that value with the generated key
            // returned by that insert statement (which will be already executed at this stage thanks to the sort).
            // This sorting method also replaces all other (not new) EntityId with their primary key in the parameter
            // values so that they can be used as is without any transformation by the SubmitService.
            sortStatementsByCreationOrder();
            // Returning the batch
            return new Batch<>(submitArguments.toArray(new SubmitArgument[0]));
        }

        public void applyGeneratedKeys(Batch<SubmitResult> ar, EntityStore store) {
            for (Map.Entry<EntityId, Integer> entry : newEntityIdInitialInsertBatchIndexes.entrySet())
                store.applyEntityIdRefactor(entry.getKey(), EntityId.create(entry.getKey().getDomainClass(), ar.getArray()[newEntityFinalInsertBatchIndexes.get(entry.getValue())].getGeneratedKeys()[0]));
        }

        void sortStatementsByCreationOrder() {
            int size = submitArguments.size();
            int sorted = 0;
            newEntityFinalInsertBatchIndexes = new ArrayList<>(size);
            List<SubmitArgument> sortedList = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                newEntityFinalInsertBatchIndexes.add(null);
                sortedList.add(null);
            }
            while (sorted < size) {
                boolean someResolved = false;
                loop:
                for (int batchIndex = 0; batchIndex < size; batchIndex++) {
                    SubmitArgument arg = submitArguments.get(batchIndex);
                    if (arg != null) {
                        Object[] parameters = arg.getParameters();
                        for (int parameterIndex = 0, length = Arrays.length(parameters); parameterIndex < length; parameterIndex++) {
                            Object value = parameters[parameterIndex];
                            if (value instanceof EntityId) {
                                EntityId entityId = (EntityId) value;
                                if (!entityId.isNew())
                                    parameters[parameterIndex] = entityId.getPrimaryKey();
                                else {
                                    Integer initialIndex = newEntityIdInitialInsertBatchIndexes.get(entityId);
                                    Integer finalIndex = newEntityFinalInsertBatchIndexes.get(initialIndex);
                                    if (finalIndex == null)
                                        continue loop;
                                    parameters[parameterIndex] = new GeneratedKeyBatchIndex(finalIndex);
                                }
                            }
                        }
                        newEntityFinalInsertBatchIndexes.set(batchIndex, sorted);
                        sortedList.set(sorted++, arg);
                        submitArguments.set(batchIndex, null);
                        someResolved = true;
                    }
                }
                if (!someResolved)
                    throw new IllegalStateException("Cyclic references detected");
            }
            for (int i = 0; i < size; i++)
                submitArguments.set(i, sortedList.get(i));
        }


        void generateDeletes() {
            Collection<EntityId> deletedEntities = changes.getDeletedEntityIds();
            if (deletedEntities != null && !deletedEntities.isEmpty()) {
                List<EntityId> deletedList = new ArrayList<>(deletedEntities);
                // Sorting according to classes references
                // Doesn't work on Android: Collections.sort(deletedList, Comparator.comparing(id -> id.getDomainClass().getName()));
                deletedList.sort(comparing(id -> id.getDomainClass().getName()));
                for (EntityId deletedId : deletedList) // java 8 forEach doesn't compile with GWT
                    generateDelete(deletedId);
            }
        }

        private static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor) {
            //Objects.requireNonNull(keyExtractor);
            return (Comparator<T> & Serializable)
                    (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
        }

        void generateDelete(EntityId id) {
            Delete<?> delete = new Delete<>(id.getDomainClass(), null, WHERE_ID_EQUALS_PARAM);
            addToBatch(delete, id.getPrimaryKey());
        }

        void generateInsertUpdates() {
            EntityResult rs = changes.getInsertedUpdatedEntityResult();
            if (rs != null) {
                for (EntityId id : rs.getEntityIds()) {
                    List<Equals<?>> assignments = new ArrayList<>();
                    List<Object> values = new ArrayList<>();
                    for (Object fieldId : rs.getFieldIds(id))
                        if (fieldId != null) {
                            DomainField field = id.getDomainClass().getField(fieldId);
                            assignments.add(new Equals<>(field, Parameter.UNNAMED_PARAMETER));
                            values.add(rs.getFieldValue(id, fieldId));
                        }
                    if (assignments.isEmpty() && !id.isNew())
                        continue;
                    ExpressionArray<?> setClause = new ExpressionArray(assignments);
                    if (id.isNew()) { // insert statement
                        newEntityIdInitialInsertBatchIndexes.put(id, submitArguments.size());
                        Insert<?> insert = new Insert(id.getDomainClass(), setClause);
                        addToBatch(insert, values.isEmpty() ? null : values.toArray());
                    } else { // update statement
                        Update<?> update = new Update(id.getDomainClass(), setClause, WHERE_ID_EQUALS_PARAM);
                        values.add(id.getPrimaryKey());
                        addToBatch(update, values.toArray());
                    }
                }
                // TODO: sort sql statements
            }
        }

        private static final boolean USE_DQL_LANGUAGE = true;

        void addToBatch(DqlStatement<?> dqlStatement, Object... parameterValues) {
            if (USE_DQL_LANGUAGE)
                addToBatch("DQL", dqlStatement.toString(), parameterValues);
            else
                addToBatch(ExpressionSqlCompiler.compileStatement(dqlStatement, dbmsSyntax, compilerModelReader), parameterValues);
        }

        void addToBatch(SqlCompiled sqlcompiled, Object... parameters) {
            addToBatch(null, sqlcompiled.getSql(), parameters);
        }

        void addToBatch(String submitLang, String submitString, Object... parameters) {
            submitArguments.add(SubmitArgument.builder()
                    .setDataSourceId(dataSourceId)
                    .setDataScope(dataScope)
                    .setLanguage(submitLang)
                    .setStatement(submitString)
                    .setParameters(parameters)
                    .build());
        }
    }
}
