package mongooses.core.entities;

import mongooses.core.domainmodel.loader.DomainModelSnapshotLoader;
import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.framework.orm.entity.UpdateStore;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Bruno Salmon
 */
public class EntitiesTest {

    private DataSourceModel dataSourceModel;

    @Before
    public void initialize() {
        dataSourceModel = DomainModelSnapshotLoader.getDataSourceModel();
        dataSourceModel.getDomainModel();
    }

    @Test
    public void test() throws Throwable {
        UpdateStore store = UpdateStore.create(dataSourceModel);
        Document d = store.insertEntity(Document.class);
        d.setEvent(116);
        d.setFirstName("test");
        d.setLastName("TEST");
/*
        CountDownLatch latch = new CountDownLatch(1);
        Unit<Throwable> error = new Unit<>();

        store.executeUpdate().setHandler(asyncResult -> {
            if (asyncResult.failed())
                error.set(asyncResult.cause());
            latch.countDown();
        });

        latch.await(10, TimeUnit.SECONDS);
        if (error.get() != null)
            throw error.get();
        System.out.println("Update was successful :-)");
*/
    }

}
