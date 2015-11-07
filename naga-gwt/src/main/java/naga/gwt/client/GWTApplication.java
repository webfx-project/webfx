package naga.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import naga.core.Naga;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author Bruno Salmon
 */

public class GWTApplication implements EntryPoint {

    public void onModuleLoad() {
        Logger logger = Logger.getLogger("NagaLogger");
        logger.log(Level.INFO, new Naga().getMessage("appName"));
    }
}
