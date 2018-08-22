/**
 * @author Bruno Salmon
 */
module webfx.platformport.jre {

    requires naga.platform;
    requires webfx.platformports.java;

    provides naga.providers.platform.abstr.java.JavaPlatform with naga.providers.platform.client.jre.JrePlatform;
}