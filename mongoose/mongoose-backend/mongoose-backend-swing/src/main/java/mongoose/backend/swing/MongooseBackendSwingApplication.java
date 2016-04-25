package mongoose.backend.swing;

import mongoose.logic.MongooseLogic;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendSwingApplication {

    static {
        MongooseLogic.setUpWebSocketConnection();
    }

    public static void main(String[] args) {
        MongooseLogic.runBackendApplication();
    }
}
