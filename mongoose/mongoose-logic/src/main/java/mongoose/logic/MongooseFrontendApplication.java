package mongoose.logic;

import naga.core.routing.Router;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendApplication extends MongooseApplication {

    @Override
    protected Router setUpRouter() {
        return super.setUpRouter().defaultPath("/cart/a58faba5-5b0b-4573-b547-361e10c788dc");
    }

}
