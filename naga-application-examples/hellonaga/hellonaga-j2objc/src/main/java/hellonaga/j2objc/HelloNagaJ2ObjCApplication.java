package hellonaga.j2objc;

import hellonaga.logic.HelloNagaLogic;
import naga.core.spi.platform.client.j2objc.J2ObjCPlatform;

/**
 * @author Bruno Salmon
 */
public class HelloNagaJ2ObjCApplication {

    static {
        J2ObjCPlatform.register();
    }

    public static void main(String[] args) {
        HelloNagaLogic.runClient();
    }
}
