import webfx.platform.jre.JrePlatform;
import webfx.platforms.java.JavaPlatform;

/**
 * @author Bruno Salmon
 */
module webfx.platform.jre {

    requires webfx.platform;
    requires webfx.platforms.java;

    provides JavaPlatform with JrePlatform;
}