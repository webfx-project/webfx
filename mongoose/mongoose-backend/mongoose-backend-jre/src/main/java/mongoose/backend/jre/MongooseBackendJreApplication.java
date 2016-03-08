package mongoose.backend.jre;

import mongoose.logic.MongooseLogic;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendJreApplication {

    /* No need for JrePlatform.register(); as the platform will be found by the ServiceLoader */

    public static void main(String[] args) {
        MongooseLogic.runBackendApplication();
    }
}
