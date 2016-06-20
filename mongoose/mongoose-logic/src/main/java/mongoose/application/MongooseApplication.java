package mongoose.application;

import mongoose.activities.cart.CartActivity;
import mongoose.activities.container.ContainerActivity;
import mongoose.activities.organizations.OrganizationsActivity;
import naga.core.activity.Activity;
import naga.core.activity.ActivityContext;
import naga.core.activity.ActivityRouter;

/**
 * @author Bruno Salmon
 */
abstract class MongooseApplication implements Activity {

    protected ActivityRouter activityRouter;

    @Override
    public void onCreate(ActivityContext context) {
        activityRouter = ActivityRouter.create(context)
                .routeAndMountSubRouter("/", ContainerActivity::new, ActivityRouter.createSubRouter(context)
                    .route("/organizations", OrganizationsActivity::new)
                    .route("/cart/:cartUuid", CartActivity::new)
                );
    }

    @Override
    public void onStart() {
        activityRouter.start();
    }

}
