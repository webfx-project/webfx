/**
 * @author Bruno Salmon
 */
module naga.platform.jre {

    requires naga.platform;
    requires naga.abstractplatform.java;

    provides naga.providers.platform.abstr.java.JavaPlatform with naga.providers.platform.client.jre.JrePlatform;
}