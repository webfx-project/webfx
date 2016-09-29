package mongoose.domainmodel.loader;

import mongoose.domainmodel.format.DateFormatter;
import mongoose.domainmodel.format.PriceFormatter;
import mongoose.entities.*;
import mongoose.entities.impl.*;
import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.orm.domainmodel.loader.DomainModelLoader;
import naga.platform.compression.string.LZString;
import naga.platform.json.Json;
import naga.platform.json.codec.JsonCodecManager;
import naga.platform.json.spi.JsonElement;
import naga.platform.services.query.QueryResultSet;
import naga.platform.spi.Platform;

import static naga.framework.orm.entity.EntityFactoryRegistry.registerEntityFactory;
import static naga.framework.ui.format.FormatterRegistry.registerFormatter;

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
            registerFormatter("price", PriceFormatter.SINGLETON);
            registerFormatter("date", DateFormatter.SINGLETON);
            // Registering entity java classes
            registerEntityFactory(DateInfo.class, "DateInfo", DateInfoImpl::new);
            registerEntityFactory(Document.class, "Document", DocumentImpl::new);
            registerEntityFactory(Event.class, "Event", EventImpl::new);
            registerEntityFactory(Image.class, "Image", ImageImpl::new);
            registerEntityFactory(Item.class, "Item", ItemImpl::new);
            registerEntityFactory(ItemFamily.class, "ItemFamily", ItemFamilyImpl::new);
            registerEntityFactory(Label.class, "Label", LabelImpl::new);
            registerEntityFactory(Option.class, "Option", OptionImpl::new);
            registerEntityFactory(Organization.class, "Organization", OrganizationImpl::new);
            registerEntityFactory(OrganizationType.class, "OrganizationType", OrganizationTypeImpl::new);
            registerEntityFactory(Rate.class, "Rate", RateImpl::new);
            registerEntityFactory(Site.class, "Site", SiteImpl::new);
            registerEntityFactory(Teacher.class, "Teacher", TeacherImpl::new);
            registerEntityFactory(MetricsEntity.class, "Metrics", MetricsEntityImpl::new);
            registerEntityFactory(LtTestSetEntity.class, "LtTestSet", LtTestSetEntityImpl::new);
            registerEntityFactory(LtTestEventEntity.class, "LtTestEvent", LtTestEventEntityImpl::new);
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
