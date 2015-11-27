package hellonaga.jre;

import hellonaga.HelloNagaLogic;
import naga.spi.plat.jre.JrePlatform;

/**
 * @author Bruno Salmon
 */
public class HelloNagaJreApplication {

    static {
        JrePlatform.register();
    }

    public static void main(String[] args) {
        new HelloNagaLogic().run();
    }
}
