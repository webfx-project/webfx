/**
 * @author Bruno Salmon
 */
module webfx.platform.jre {

    requires webfx.platform;
    requires webfx.platforms.java;

    provides webfx.providers.platform.abstr.java.JavaPlatform with webfx.providers.platform.client.jre.JrePlatform;
}