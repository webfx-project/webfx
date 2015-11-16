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
package com.goodow.realtime.channel.impl;

import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.BusHook;
import com.goodow.realtime.channel.State;
import com.goodow.realtime.channel.util.FuzzingBackOffGenerator;
import com.goodow.realtime.core.Handler;
import com.goodow.realtime.core.Platform;
import com.goodow.realtime.json.Json;
import com.goodow.realtime.json.JsonArray;
import com.goodow.realtime.json.JsonObject;
import com.google.gwt.core.client.js.JsExport;
import com.google.gwt.core.client.js.JsNamespace;

/*
 * @author 田传武 (aka larrytin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/impl/ReconnectBus.java">Original Goodow class</a>
 */
@JsNamespace("$wnd.realtime.channel")
@JsExport
public class ReconnectBus extends WebSocketBus {
  public static final String AUTO_RECONNECT = "reconnect";
  private final FuzzingBackOffGenerator backOffGenerator;
  private BusHook hook;
  private boolean reconnect;
  private final JsonArray queuedMessages = Json.createArray(); // ArrayList<JsonObject>()
  private final JsonObject options;

  @JsExport
  public ReconnectBus(String serverUri, JsonObject options) {
    super(serverUri, options);
    this.options = options;
    reconnect =
        options == null || !options.has(AUTO_RECONNECT) ? true : options.getBoolean(AUTO_RECONNECT);
    backOffGenerator = new FuzzingBackOffGenerator(1 * 1000, 30 * 60 * 1000, 0.5);

    super.setHook(new BusHookProxy() {
      @Override
      public void handleOpened() {
        backOffGenerator.reset();

        handlerCount.keys().forEach(new JsonArray.ListIterator<String>() {
          @Override
          public void call(int index, String topic) {
            assert handlerCount.getNumber(topic) > 0 : "Handlers registried on " + topic
                + " shouldn't be empty";
            sendUnsubscribe(topic);
            sendSubscribe(topic);
          }
        });

        if (queuedMessages.length() > 0) {
          JsonArray copy = queuedMessages.copy();
          queuedMessages.clear();
          // Drain any messages that came in while the channel was not open.
          copy.forEach(new JsonArray.ListIterator<JsonObject>() {
            @Override
            public void call(int index, JsonObject msg) {
              send(msg);
            }
          });
        }
        super.handleOpened();
      }

      @Override
      public void handlePostClose() {
        if (reconnect) {
          Platform.scheduler().scheduleDelay(backOffGenerator.next().targetDelay,
              new Handler<Void>() {
                @Override
                public void handle(Void event) {
                  if (reconnect) {
                    reconnect();
                  }
                }
              });
        }
        super.handlePostClose();
      }

      @Override
      protected BusHook delegate() {
        return hook;
      }
    });
  }

  public void reconnect() {
    if (getReadyState() == State.OPEN || getReadyState() == State.CONNECTING) {
      return;
    }
    if (webSocket != null) {
      webSocket.close();
    }

    connect(serverUri, options);
  }

  @Override
  public Bus setHook(BusHook hook) {
    this.hook = hook;
    return this;
  }

  @Override
  protected void doClose() {
    reconnect = false;
    backOffGenerator.reset();
    queuedMessages.clear();
    super.doClose();
  }

  @Override
  protected void send(JsonObject msg) {
    if (getReadyState() == State.OPEN) {
      super.send(msg);
      return;
    }
    if (reconnect) {
      reconnect();
    }
    String type = msg.getString(WebSocketBus.TYPE);
    if ("ping".equals(type) || "register".equals(type)) {
      return;
    }
    queuedMessages.push(msg);
  }
}
