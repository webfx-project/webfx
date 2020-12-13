/*
 * Note: this code is a fork of Goodow realtime-channel project https://github.com/goodow/realtime-channel
 */

/*
 * Copyright 2012 Goodow.com
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
package dev.webfx.platform.client.services.websocketbus;

import java.util.Random;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the webfx project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/util/IdGenerator.java">Original Goodow class</a>
 */
final class IdGenerator {

    /**
     * valid characters.
     */
    static final char[] WEB64_ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();
    static final char[] NUMBERS = "0123456789".toCharArray();

    private final Random random;

    IdGenerator() {
        this(new Random());
    }

    IdGenerator(Random random) {
        this.random = random;
    }

    /**
     * Returns a string with {@code length} random characters.
     */
    public String next(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(WEB64_ALPHABET[random.nextInt(64)]);
        }
        return result.toString();
    }

    public String nextNumbers(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(NUMBERS[random.nextInt(10)]);
        }
        return result.toString();
    }
}