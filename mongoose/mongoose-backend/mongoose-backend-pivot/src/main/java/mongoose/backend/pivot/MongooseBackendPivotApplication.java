package mongoose.backend.pivot;

import mongoose.logic.MongooseLogic;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendPivotApplication {

    static {
        MongooseLogic.setUpWebSocketConnection();
    }

    public static void main(String[] args) {
        MongooseLogic.runBackendApplication();
    }
}
