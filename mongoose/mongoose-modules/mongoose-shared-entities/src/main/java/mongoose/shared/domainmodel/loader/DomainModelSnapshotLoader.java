package mongoose.shared.domainmodel.loader;

import mongoose.shared.domainmodel.formatters.DateFormatter;
import mongoose.shared.domainmodel.formatters.DateTimeFormatter;
import mongoose.shared.domainmodel.formatters.PriceFormatter;
import mongoose.shared.domainmodel.functions.AbcNames;
import mongoose.shared.domainmodel.functions.DateIntervalFormat;
import mongoose.shared.entities.*;
import mongoose.shared.entities.impl.*;
import webfx.framework.shared.expression.terms.function.DomainClassType;
import webfx.framework.shared.expression.terms.function.Function;
import webfx.framework.shared.expression.terms.function.InlineFunction;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.orm.domainmodel.loader.DomainModelLoader;
import webfx.fxkit.extra.type.PrimType;
import webfx.fxkit.extra.type.Type;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonElement;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.services.resource.ResourceService;
import webfx.platform.shared.services.serial.SerialCodecManager;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;

import static webfx.framework.shared.orm.entity.EntityFactoryRegistry.registerEntityFactory;
import static webfx.framework.client.ui.util.formatter.FormatterRegistry.registerFormatter;

/**
 * @author Bruno Salmon
 */
public class DomainModelSnapshotLoader {

    private static DomainModel domainModel;
    private final static DataSourceModel dataSourceModel = new DataSourceModel() {
        @Override
        public Object getId() {
            return 1;
        }

        @Override
        public DomainModel getDomainModel() {
            return getOrLoadDomainModel();
        }
    };

    public static synchronized DomainModel getOrLoadDomainModel() {
        if (domainModel == null)
            domainModel = loadDomainModelFromSnapshot();
        return domainModel;
    }

    public static DomainModel loadDomainModelFromSnapshot() {
        try {
            // Registering formats
            registerFormatter("price", PriceFormatter.INSTANCE);
            registerFormatter("date", DateFormatter.SINGLETON);
            registerFormatter("dateTime", DateTimeFormatter.SINGLETON);
            // Registering entity java classes
            registerEntityFactory(Attendance.class, "Attendance", AttendanceImpl::new);
            registerEntityFactory(Cart.class, "Cart", CartImpl::new);
            registerEntityFactory(Country.class, "Country", CountryImpl::new);
            registerEntityFactory(DateInfo.class, "DateInfo", DateInfoImpl::new);
            registerEntityFactory(Document.class, "Document", DocumentImpl::new);
            registerEntityFactory(DocumentLine.class, "DocumentLine", DocumentLineImpl::new);
            registerEntityFactory(Event.class, "Event", EventImpl::new);
            registerEntityFactory(GatewayParameter.class, "GatewayParameter", GatewayParameterImpl::new);
            registerEntityFactory(History.class, "History", HistoryImpl::new);
            registerEntityFactory(Image.class, "Image", ImageImpl::new);
            registerEntityFactory(Item.class, "Item", ItemImpl::new);
            registerEntityFactory(ItemFamily.class, "ItemFamily", ItemFamilyImpl::new);
            registerEntityFactory(Label.class, "Label", LabelImpl::new);
            registerEntityFactory(Mail.class, "Mail", MailImpl::new);
            registerEntityFactory(Method.class, "Method", MethodImpl::new);
            registerEntityFactory(Option.class, "Option", OptionImpl::new);
            registerEntityFactory(Organization.class, "Organization", OrganizationImpl::new);
            registerEntityFactory(OrganizationType.class, "OrganizationType", OrganizationTypeImpl::new);
            registerEntityFactory(Person.class, "Person", PersonImpl::new);
            registerEntityFactory(Rate.class, "Rate", RateImpl::new);
            registerEntityFactory(Site.class, "Site", SiteImpl::new);
            registerEntityFactory(Teacher.class, "Teacher", TeacherImpl::new);
            registerEntityFactory(SystemMetricsEntity.class, "Metrics", SystemMetricsEntityImpl::new);
            registerEntityFactory(MoneyTransfer.class, "MoneyTransfer", MoneyTransferImpl::new);
            /* Commented to lighten the application as it is not used
            registerEntityFactory(LtTestSetEntity.class, "LtTestSet", LtTestSetEntityImpl::new);
            registerEntityFactory(LtTestEventEntity.class, "LtTestEvent", LtTestEventEntityImpl::new);
            */
            // Loading the model from the resource snapshot
            Future<String> text = ResourceService.getText("mongoose/shared/domainmodel/DomainModelSnapshot.json");
            String jsonString = text.result(); // LZString.decompressFromBase64(text.result());
            JsonElement json = Json.parseObject(jsonString);
            Batch<QueryResult> snapshotBatch = SerialCodecManager.decodeFromJson(json);
            DomainModel domainModel = new DomainModelLoader(1).generateDomainModel(snapshotBatch);
            // Registering functions
            new AbcNames().register();
            new AbcNames("alphaSearch").register();
            new DateIntervalFormat().register();
            new Function("interpret_brackets", PrimType.STRING).register();
            new Function("compute_dates").register();
            new InlineFunction("searchMatchesDocument", "d", new Type[]{new DomainClassType("Document")}, "d..ref=?searchInteger or d..person_abcNames like ?abcSearchLike or d..person_email like ?searchEmailLike", domainModel.getClass("Document"), domainModel.getParserDomainModelReader()).register();
            new InlineFunction("searchMatchesPerson", "p", new Type[]{new DomainClassType("Person")}, "abcNames(p..firstName + ' ' + p..lastName) like ?abcSearchLike or p..email like ?searchEmailLike", "Person", domainModel.getParserDomainModelReader()).register();
            return domainModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }
}
