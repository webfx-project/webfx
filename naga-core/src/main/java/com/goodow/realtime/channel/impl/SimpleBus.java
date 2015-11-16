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
package com.goodow.realtime.channel.impl;

import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.BusHook;
import com.goodow.realtime.channel.Message;
import com.goodow.realtime.channel.State;
import com.goodow.realtime.channel.util.IdGenerator;
import com.goodow.realtime.core.Handler;
import com.goodow.realtime.core.Platform;
import com.goodow.realtime.core.Registration;
import com.goodow.realtime.json.Json;
import com.goodow.realtime.json.JsonArray;
import com.goodow.realtime.json.JsonObject;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author 田传武 (aka larrytin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/impl/SimpleBus.java">Original Goodow class</a>
 */
@SuppressWarnings("rawtypes")
public class SimpleBus implements Bus {
  private static final Logger log = Logger.getLogger(SimpleBus.class.getName());

  static void checkNotNull(String paramName, Object param) {
    if (param == null) {
      throw new IllegalArgumentException("Parameter " + paramName + " must be specified");
    }
  }

  private JsonObject handlerMap; // LinkedHashMap<String, LinkedHashSet<Handler<Message>>>
  final JsonObject replyHandlers; // LinkedHashMap<String, Handler<Message>>
  BusHook hook;
  final IdGenerator idGenerator;

  public SimpleBus() {
    handlerMap = Json.createObject();
    replyHandlers = Json.createObject();
    idGenerator = new IdGenerator();
  }

  @Override
  public void close() {
    if (hook == null || hook.handlePreClose()) {
      doClose();
    }
  }

  @Override
  public State getReadyState() {
    return handlerMap == null ? State.CLOSED : State.OPEN;
  }

  @Override
  public String getSessionId() {
    return "@";
  }

  @Override
  public Bus publish(String topic, Object msg) {
    internalHandleSendOrPub(false, false, topic, msg, null);
    return this;
  }

  @Override
  public Bus publishLocal(String topic, Object msg) {
    internalHandleSendOrPub(true, false, topic, msg, null);
    return this;
  }

  @Override
  public Registration subscribe(final String topic,
                                final Handler<? extends Message> handler) {
    return subscribeImpl(false, topic, handler);
  }

  @Override
  public Registration subscribeLocal(final String topic,
                                     final Handler<? extends Message> handler) {
    return subscribeImpl(true, topic, handler);
  }

  @Override
  public <T> Bus send(String topic, Object msg, Handler<Message<T>> replyHandler) {
    internalHandleSendOrPub(false, true, topic, msg, replyHandler);
    return this;
  }

  @Override
  public <T> Bus sendLocal(String topic, Object msg, Handler<Message<T>> replyHandler) {
    internalHandleSendOrPub(true, true, topic, msg, replyHandler);
    return this;
  }

  @Override
  public Bus setHook(BusHook hook) {
    this.hook = hook;
    return this;
  }

  protected void doClose() {
    publishLocal(ON_CLOSE, null);
    clearHandlers();
    if (hook != null) {
      hook.handlePostClose();
    }
  }

  protected boolean doSubscribe(boolean local, String topic,
                                Handler<? extends Message> handler) {
    checkNotNull("topic", topic);
    checkNotNull("handler", handler);
    JsonArray handlers = handlerMap.getArray(topic);
    if (handlers == null) {
      handlerMap.set(topic, Json.createArray().push(handler));
      return true;
    }
    if (handlers.indexOf(handler) == -1) {
      handlers.push(handler);
      return true;
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  protected <T> void doSendOrPub(boolean local, boolean send, String topic, Object msg,
      Handler<Message<T>> replyHandler) {
    checkNotNull("topic", topic);
    String replyTopic = null;
    if (replyHandler != null) {
      replyTopic = makeUUID();
      replyHandlers.set(replyTopic, replyHandler);
    }
    MessageImpl message = new MessageImpl(local, send, this, topic, replyTopic, msg);
    if (!internalHandleReceiveMessage(message) && replyTopic != null) {
      replyHandlers.remove(replyTopic);
    }
  }

  protected boolean doUnsubscribe(boolean local, String topic,
                                  Handler<? extends Message> handler) {
    checkNotNull("topic", topic);
    checkNotNull("handler", handler);
    JsonArray handlers = handlerMap.getArray(topic);
    if (handlers == null) {
      return false;
    }
    boolean removed = handlers.removeValue(handler);
    if (handlers.length() == 0) {
      handlerMap.remove(topic);
    }
    return removed;
  }

  void clearHandlers() {
    replyHandlers.clear();
    handlerMap.clear();
    handlerMap = null;
  }

  boolean internalHandleReceiveMessage(Message message) {
    if (message.isLocal() || hook == null || hook.handleReceiveMessage(message)) {
      doReceiveMessage(message);
      return true;
    }
    return false;
  }

  <T> void internalHandleSendOrPub(boolean local, boolean send, String topic, Object msg,
      Handler<Message<T>> replyHandler) {
    if (local || hook == null || hook.handleSendOrPub(send, topic, msg, replyHandler)) {
      doSendOrPub(local, send, topic, msg, replyHandler);
    }
  }

  String makeUUID() {
    return idGenerator.next(36);
  }

  private void doReceiveMessage(final Message message) {
    final String topic = message.topic();
    JsonArray handlers = handlerMap.getArray(topic);
    if (handlers != null) {
      // We make a copy since the handler might get unregistered from within the handler itself,
      // which would screw up our iteration
      final JsonArray copy = Json.createArray();
      handlers.forEach(new JsonArray.ListIterator<Object>() {
        @Override
        public void call(int index, Object value) {
          copy.push(value);
        }
      });
      copy.forEach(new JsonArray.ListIterator<Object>() {
        @Override
        public void call(int index, Object value) {
          scheduleHandle(topic, value, message);
        }
      });
    } else {
      // Might be a reply message
      Object handler = replyHandlers.get(topic);
      if (handler != null) {
        replyHandlers.remove(topic);
        scheduleHandle(topic, handler, message);
      }
    }
  }

  private void handle(String topic, Object handler, Object message) {
    try {
      Platform.scheduler().handle(handler, message);
    } catch (Throwable e) {
      log.log(Level.WARNING, "Failed to handle on topic: " + topic, e);
      publishLocal(ON_ERROR, Json.createObject().set("topic", topic)
          .set("message", message).set("cause", e));
    }
  }

  private void scheduleHandle(final String topic, final Object handler, final Message message) {
    if (message.isLocal()) {
      handle(topic, handler, message);
    } else {
      Platform.scheduler().scheduleDeferred(new Handler<Void>() {
        @Override
        public void handle(Void ignore) {
          SimpleBus.this.handle(topic, handler, message);
        }
      });
    }
  }

  private Registration subscribeImpl(final boolean local, final String topic,
                                     final Handler<? extends Message> handler) {
    doSubscribe(local, topic, handler);
    return new Registration() {
      @Override
      public void unregister() {
        doUnsubscribe(local, topic, handler);
      }
    };
  }
}