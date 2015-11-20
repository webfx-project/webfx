/*
 * Note: this code is a fork of Goodow realtime-channel project https://github.com/goodow/realtime-channel
 */

/*
 * Copyright 2013 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package naga.core.spi.plat;

/**
 * The main Platform interface. The static methods in this class provide access to the various
 * available subsystems.
 * <p>
 * <p>
 * You must register a {@link Platform} before calling any of these methods. For example,
 * <code>JavaPlatform.register();</code>.
 * </p>
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/core/Platform.java">Original Goodow class</a>
 */
public class Platform {

    public enum Type {
        JAVA, HTML, ANDROID, IOS, FLASH, STUB, VERTX
    }

    private static PlatformFactory FACTORY;

    public static Diff diff() {
        return get().diff();
    }

    public static Net net() {
        return get().net();
    }

    public static Scheduler scheduler() {
        return get().scheduler();
    }

    /**
     * Configures the current {@link Platform}. Do not call this directly unless you're implementing a
     * new platform.
     */
    public static void setFactory(PlatformFactory factory) {
        FACTORY = factory;
    }

    public static Platform.Type type() {
        return get().type();
    }

    private static PlatformFactory get() {
        assert FACTORY != null :
                "You must register a platform first by invoke {Java|Android}Platform.register()";
        return FACTORY;
    }

    // Non-instantiable
    protected Platform() {
    }
}
