package mongooses.core.activities.sharedends.generic;

import webfx.framework.activity.impl.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityImpl;
import webfx.framework.expression.sqlcompiler.sql.SqlCompiled;
import webfx.platforms.core.services.json.Json;
import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.query.QueryArgument;
import webfx.platforms.core.services.query.QueryService;
import webfx.platforms.core.util.function.Factory;

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
