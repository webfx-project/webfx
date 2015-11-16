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
package com.goodow.realtime.channel.mqtt.packet;

/**
 * An on-the-wire representation of an MQTT message.
 *
 * @author 田传武 (aka larrytin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/mqtt/packet/MqttPacket.java">Original Goodow class</a>
 */
public abstract class MqttPacket {
  public static final byte CONNECT = 1;
  public static final byte CONNACK = 2;
  public static final byte PUBLISH = 3;
  public static final byte PUBACK = 4;
  public static final byte PUBREC = 5;
  public static final byte PUBREL = 6;
  public static final byte PUBCOMP = 7;
  public static final byte SUBSCRIBE = 8;
  public static final byte SUBACK = 9;
  public static final byte UNSUBSCRIBE = 10;
  public static final byte UNSUBACK = 11;
  public static final byte PINGREQ = 12;
  public static final byte PINGRESP = 13;
  public static final byte DISCONNECT = 14;

  private boolean dup;
  private byte qos;
  private boolean retain;

}
