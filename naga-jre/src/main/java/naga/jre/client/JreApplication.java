package naga.jre.client;

import naga.core.spi.plat.Platform;
import naga.core.spi.plat.javaplat.JavaPlatform;

/*
 * @author Bruno Salmon
 */

public class JreApplication {

    public static void main(String[] args) {
        JavaPlatform.register();

        Platform.createBus().send("version", null, event -> System.out.println(event.body()));
    }
}
