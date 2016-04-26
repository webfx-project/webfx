package mongoose.logic;

import naga.core.ngui.lifecycle.ApplicationController;
import naga.core.routing.Router;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplication extends MongooseApplication {

    @Override
    protected Router setUpRouter() {
        return super.setUpRouter().defaultPath("/organizations");
    }

    public static void main(String[] args) {
        ApplicationController.startApplication(new MongooseBackendApplication(), args);
    }

}
