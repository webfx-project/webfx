package naga.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import naga.core.Naga;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author Bruno Salmon
 */

public class GwtApplication implements EntryPoint {

    public void onModuleLoad() {
        Logger logger = Logger.getLogger("NagaLogger");
        logger.log(Level.INFO, new Naga().getMessage("appName"));

        /*JsonFactory js = new GWTJsonFactory();
        JsonObject p = js.parseJsonObject("{\"firstName\": \"Bruno\", \"lastName\": \"Salmon\", \"age\": 43}");
        p.setValue("fullName", p.getString("firstName") + " " + p.getString("lastName"));
        p.setInt("age", p.getInt("age") + 1);
        logger.log(Level.INFO, p.toString());*/
    }
}
