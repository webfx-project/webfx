/*
 * Note: this code is a fork of Goodow realtime-channel project https://github.com/goodow/realtime-channel
 */

/*
 * Copyright 2014 Goodow.com
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

/**
 * Simple fibonacci back off with fuzzing. This is important for reconnection so that everyone
 * doesn't retry at the same time.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the webfx project
 *         <p>
 *         <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/util/FuzzingBackOffGenerator.java">Original Goodow class</a>
 */
final class FuzzingBackOffGenerator {

    static class BackOffParameters {
        final int targetDelay;
        final int minimumDelay;

        private BackOffParameters(int targetDelay, int minimumDelay) {
            this.targetDelay = targetDelay;
            this.minimumDelay = minimumDelay;
        }
    }

    /**
     * Randomization factor. Must be between 0 and 1.
     */
    private final double randomizationFactor;
    /**
     * The first time we back off.
     */
    private final int initialBackOff;
    /**
     * The max back off value, it'll be fuzzed.
     */
    private final int maxBackOff;
    /**
     * The next time we've backed off.
     */
    private int nextBackOffTime;
    /**
     * The current back off time.
     */
    private int backOffTime;

    /**
     * @param initialBackOff      Initial value to back off. This class does not interpret the meaning of
     *                            this value. must be > 0
     * @param maxBackOff          Max value to back off
     * @param randomizationFactor between 0 and 1 to control the range of randomness.
     */
    FuzzingBackOffGenerator(int initialBackOff, int maxBackOff, double randomizationFactor) {
        if (randomizationFactor < 0 || randomizationFactor > 1)
            throw new IllegalArgumentException("randomizationFactor must be between 0 and 1. actual " + randomizationFactor);

        if (initialBackOff <= 0)
            throw new IllegalArgumentException("initialBackOff must be between 0 and 1. actual " + initialBackOff);

        this.randomizationFactor = randomizationFactor;
        this.initialBackOff = initialBackOff;
        this.maxBackOff = maxBackOff;
        this.nextBackOffTime = initialBackOff;
        this.backOffTime = 0;
    }

    /**
     * Gets the next back off time. Until maxBackOff is reached.
     */
    public BackOffParameters next() {
        int ret = Math.min(nextBackOffTime, maxBackOff);
        nextBackOffTime += backOffTime;
        if (nextBackOffTime <= 0)
            nextBackOffTime = Integer.MAX_VALUE;
        backOffTime = ret;

        int randomizeTime = (int) (backOffTime * (Math.random() *  randomizationFactor));
        int minAllowedTime = (int) Math.round(randomizeTime - backOffTime * randomizationFactor);

        return new BackOffParameters(randomizeTime, minAllowedTime);
    }

    /**
     * Resets the back off.
     */
    public void reset() {
        nextBackOffTime = initialBackOff;
        backOffTime = 0;
    }
}