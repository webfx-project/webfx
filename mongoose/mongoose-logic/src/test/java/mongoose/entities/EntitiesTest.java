package mongoose.entities;

import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.UpdateStore;
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
        d.setPersonFirstName("test");
        d.setPersonLastName("TEST");
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
