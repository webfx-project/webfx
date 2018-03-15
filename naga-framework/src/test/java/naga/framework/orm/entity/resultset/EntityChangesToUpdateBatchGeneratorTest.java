package naga.framework.orm.entity.resultset;

import naga.framework.expression.lci.mocks.CompilerDomainModelReaderMock;
import naga.framework.expression.lci.mocks.ParserDomainModelReaderMock;
import naga.framework.expression.sqlcompiler.sql.dbms.PostgresSyntax;
import naga.util.Arrays;
import org.junit.Test;

/**
 * @author Bruno Salmon
 */
public class EntityChangesToUpdateBatchGeneratorTest {

    @Test
    public void test() throws Exception {
        System.out.println(Arrays.toStringWithLineFeeds(
        EntityChangesToUpdateBatchGenerator.generateUpdateBatch(EntityChangesBuilder.create()
/*
                .addFieldChange(EntityId.create("Document", -5), "cancelled", true)
                .addDeletedEntityId(EntityId.create("Document", 12))
                .addDeletedEntityId(EntityId.create("Document", 13))
*/
                .build(), 3, PostgresSyntax.get(), new ParserDomainModelReaderMock(), new CompilerDomainModelReaderMock())
                .getArray()));
    }
}
