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
package com.goodow.realtime.channel;

/*
 * @author 田传武 (aka larrytin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/State.java">Original Goodow class</a>
 */
public enum ReplyFailure {
  TIMEOUT, NO_HANDLERS, RECIPIENT_FAILURE;

  public static ReplyFailure fromInt(int i) {
    switch (i) {
      case 0:
        return TIMEOUT;
      case 1:
        return NO_HANDLERS;
      case 2:
        return RECIPIENT_FAILURE;
      default:
        throw new IllegalStateException("Invalid index " + i);
    }
  }

  public int toInt() {
    switch (this) {
      case TIMEOUT:
        return 0;
      case NO_HANDLERS:
        return 1;
      case RECIPIENT_FAILURE:
        return 2;
      default:
        throw new IllegalStateException("How did we get here?");
    }
  }
}