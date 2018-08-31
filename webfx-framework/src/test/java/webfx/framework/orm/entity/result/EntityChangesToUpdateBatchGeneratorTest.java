package webfx.framework.orm.entity.result;

import webfx.framework.expression.lci.mocks.CompilerDomainModelReaderMock;
import webfx.framework.expression.lci.mocks.ParserDomainModelReaderMock;
import webfx.framework.expression.sqlcompiler.sql.dbms.PostgresSyntax;
import webfx.util.Arrays;
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
