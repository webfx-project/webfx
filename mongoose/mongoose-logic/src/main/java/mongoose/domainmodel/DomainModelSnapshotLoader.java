package mongoose.domainmodel;

import naga.core.composite.CompositeElement;
import naga.core.composite.Composites;
import naga.core.composite.codec.CompositeCodecManager;
import naga.core.orm.domainmodel.DataSourceModel;
import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.domainmodelloader.DomainModelLoader;
import naga.core.spi.platform.client.ClientPlatform;
import naga.core.spi.sql.SqlReadResult;
import naga.core.util.async.Batch;
import naga.core.util.async.Future;
import naga.core.util.compression.string.LZString;

/**
 * @author Bruno Salmon
 */
public class DomainModelSnapshotLoader {

    private static DomainModel domainModel;
    private final static DataSourceModel dataSourceModel = new DataSourceModel() {
        @Override
        public Object getId() {
            return 3;
        }

        @Override
        public DomainModel getDomainModel() {
            return getOrLoadDomainModel();
        }
    };

    public static DomainModel getOrLoadDomainModel() {
        if (domainModel == null)
            domainModel = loadDomainModelFromSnapshot();
        return domainModel;
    }

    public static DomainModel loadDomainModelFromSnapshot() {
        try {
            Future<String> text = ClientPlatform.res().getText("mongoose/domainmodel/DomainModelSnapshot.lzb64json");
            String jsonString = LZString.decompressFromBase64(text.result());
            CompositeElement co = Composites.parseObject(jsonString);
            Batch<SqlReadResult> snapshotBatch = CompositeCodecManager.decodeFromComposite(co);
            return new DomainModelLoader(1).generateDomainModel(snapshotBatch);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }
}
