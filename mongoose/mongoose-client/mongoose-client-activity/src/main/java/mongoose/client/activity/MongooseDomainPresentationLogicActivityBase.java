package mongoose.client.activity;

import webfx.framework.client.activity.impl.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityImpl;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryService;
import webfx.platform.shared.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class MongooseDomainPresentationLogicActivityBase<PM>
    extends DomainPresentationLogicActivityImpl<PM>  {

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
        QueryService.executeQuery(QueryArgument.builder()
                .setLanguage("DQL")
                .setStatement("select parameters from ActivityState where id=?")
                .setParameters(activityStateId)
                .setDataSourceId(getDataSourceId())
                .build()).setHandler(ar -> {
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
