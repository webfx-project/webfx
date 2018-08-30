/**
 * @author Bruno Salmon
 */
module webfx.platform.jre {

    requires naga.platform;
    requires webfx.platforms.java;

    provides naga.providers.platform.abstr.java.JavaPlatform with naga.providers.platform.client.jre.JrePlatform;
}