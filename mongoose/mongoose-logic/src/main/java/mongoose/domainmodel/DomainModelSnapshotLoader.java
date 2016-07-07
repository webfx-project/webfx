package mongoose.domainmodel;

import naga.commons.json.spi.JsonElement;
import naga.commons.json.Json;
import naga.commons.json.codec.JsonCodecManager;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.orm.domainmodel.loader.DomainModelLoader;
import naga.platform.spi.Platform;
import naga.commons.services.query.QueryResultSet;
import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;
import naga.commons.util.compression.string.LZString;

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
            Future<String> text = Platform.getResourceService().getText("mongoose/domainmodel/DomainModelSnapshot.lzb64json");
            String jsonString = LZString.decompressFromBase64(text.result());
            JsonElement json = Json.parseObject(jsonString);
            Batch<QueryResultSet> snapshotBatch = JsonCodecManager.decodeFromJson(json);
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
