package mongoose.shared.domainmodel;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.dbms.PostgresSyntax;
import webfx.platform.shared.services.datasource.DBMS;

/**
 * @author Bruno Salmon
 */
public final class MongooseDataSourceModel {

    private final static Object MONGOOSE_DATA_SOURCE_ID = "MDS";
    private final static DBMS MONGOOSE_DBMS = DBMS.POSTGRES;
    private final static DataSourceModel MONGOOSE_DATA_SOURCE_MODEL = new DataSourceModel(MONGOOSE_DATA_SOURCE_ID, PostgresSyntax.get());

    public static Object getDataSourceId() {
        return MONGOOSE_DATA_SOURCE_ID;
    }

    public static DBMS getDbms() {
        return MONGOOSE_DBMS;
    }

    public static DataSourceModel get() {
        return MONGOOSE_DATA_SOURCE_MODEL;
    }

}
