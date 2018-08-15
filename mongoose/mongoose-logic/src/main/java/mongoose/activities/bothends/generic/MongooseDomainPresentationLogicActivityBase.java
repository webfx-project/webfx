package mongoose.activities.bothends.generic;

import naga.framework.activity.base.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityImpl;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.platform.services.json.Json;
import naga.platform.services.json.JsonObject;
import naga.platform.services.json.WritableJsonObject;
import naga.platform.services.log.Logger;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryService;
import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class MongooseDomainPresentationLogicActivityBase<PM>
    extends DomainPresentationLogicActivityImpl<PM>  {

    public MongooseDomainPresentationLogicActivityBase() {
    }

    public MongooseDomainPresentationLogicActivityBase(Factory<PM> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected void updateModelFromContextParameters() {
        super.updateModelFromContextParameters();
        Object activityStateId = getParameter("activityStateId");
        if (activityStateId != null)
            loadActivityState(activityStateId);
    }

    private void loadActivityState(Object activityStateId) {
        // Loading the parameters from the requested activity state
        Object[] parameter = {activityStateId};
        SqlCompiled sqlCompiled = getDomainModel().compileSelect("select parameters from ActivityState where id=?", parameter);
        QueryService.executeQuery(new QueryArgument(sqlCompiled.getSql(), parameter, getDataSourceId())).setHandler(ar -> {
            if (ar.failed())
                Logger.log(ar.cause());
            else {
                // Parsing the read parameters (json string expected) into a Json object
                JsonObject stateParameters = Json.parseObject(ar.result().getValue(0, 1));
                // Merging these parameters into the context
                WritableJsonObject contextParams = (WritableJsonObject) getParams(); // not beautiful...
                Json.mergeInto(stateParameters, contextParams);
                // Updating the presentation model from the new context parameters
                updatePresentationModelFromContextParameters(getPresentationModel());
            }
        });
    }

}
