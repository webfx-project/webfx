package hellonaga.teavm;

import hellonaga.HelloNagaLogic;
import naga.core.spi.plat.teavm.TeaVmPlatform;

/**
 * @author Bruno Salmon
 */
public class HelloNagaTeaVmApplication {

    static {
        TeaVmPlatform.register();
    }

    public static void main(String[] args) {
        new HelloNagaLogic(message -> System.out.println(message)).run();
    }
}
