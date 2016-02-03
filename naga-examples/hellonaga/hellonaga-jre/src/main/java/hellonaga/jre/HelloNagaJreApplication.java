package hellonaga.jre;

import hellonaga.logic.HelloNagaLogic;

/**
 * @author Bruno Salmon
 */
public class HelloNagaJreApplication {

    /* No need for JrePlatform.register(); as the platform will be found by the ServiceLoader */

    public static void main(String[] args) {
        HelloNagaLogic.runClient();
    }
}
