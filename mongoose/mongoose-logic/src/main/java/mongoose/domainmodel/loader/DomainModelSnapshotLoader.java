package mongoose.domainmodel.loader;

import mongoose.domainmodel.format.DateFormatter;
import mongoose.domainmodel.format.PriceFormatter;
import mongoose.entities.*;
import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.orm.domainmodel.loader.DomainModelLoader;
import naga.framework.orm.entity.EntityFactoryRegistry;
import naga.framework.ui.format.FormatterRegistry;
import naga.platform.compression.string.LZString;
import naga.platform.json.Json;
import naga.platform.json.codec.JsonCodecManager;
import naga.platform.json.spi.JsonElement;
import naga.platform.services.query.QueryResultSet;
import naga.platform.spi.Platform;

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
            // Registering formats
            FormatterRegistry.registerFormatter("price", PriceFormatter.SINGLETON);
            FormatterRegistry.registerFormatter("date", DateFormatter.SINGLETON);
            // Registering entity java classes
            EntityFactoryRegistry.registerEntityFactory(LtTestSetEntity.class, "LtTestSet", LtTestSetEntityImpl::new);
            EntityFactoryRegistry.registerEntityFactory(LtTestEventEntity.class, "LtTestEvent", LtTestEventEntityImpl::new);
            EntityFactoryRegistry.registerEntityFactory(MetricsEntity.class, "Metrics", MetricsEntityImpl::new);
            EntityFactoryRegistry.registerEntityFactory(Document.class, "Document", DocumentImpl::new);
            EntityFactoryRegistry.registerEntityFactory(Event.class, "Event", EventImpl::new);
            // Loading the model from the resource snapshot
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
