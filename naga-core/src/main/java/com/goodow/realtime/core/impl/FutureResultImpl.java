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
package com.goodow.realtime.core.impl;

import com.goodow.realtime.core.AsyncResult;
import com.goodow.realtime.core.Future;
import com.goodow.realtime.core.Handler;

/*
 * @author 田传武 (aka larrytin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/core/impl/FutureResultImpl.java">Original Goodow class</a>
 */
public class FutureResultImpl<T> implements Future<T> {
  private boolean failed;
  private boolean succeeded;
  private Handler<AsyncResult<T>> handler;
  private T result;
  private Throwable throwable;

  /**
   * Create a AsyncResult that hasn't completed yet
   */
  public FutureResultImpl() {
  }

  /**
   * Create a AsyncResult that has already succeeded
   * 
   * @param result The result
   */
  public FutureResultImpl(T result) {
    setResult(result);
  }

  /**
   * Create a AsyncResult that has already completed
   * 
   * @param t The Throwable or null if succeeded
   */
  public FutureResultImpl(Throwable t) {
    if (t == null) {
      setResult(null);
    } else {
      setFailure(t);
    }
  }

  /**
   * An exception describing failure. This will be null if the operation succeeded.
   */
  @Override
  public Throwable cause() {
    return throwable;
  }

  /**
   * Has it completed?
   */
  @Override
  public boolean complete() {
    return failed || succeeded;
  }

  /**
   * Did it fail?
   */
  @Override
  public boolean failed() {
    return failed;
  }

  /**
   * The result of the operation. This will be null if the operation failed.
   */
  @Override
  public T result() {
    return result;
  }

  /**
   * Set the failure. Any handler will be called, if there is one
   */
  @Override
  public FutureResultImpl<T> setFailure(Throwable throwable) {
    this.throwable = throwable;
    failed = true;
    checkCallHandler();
    return this;
  }

  /**
   * Set a handler for the result. It will get called when it's complete
   */
  @Override
  public FutureResultImpl<T> setHandler(Handler<AsyncResult<T>> handler) {
    this.handler = handler;
    checkCallHandler();
    return this;
  }

  /**
   * Set the result. Any handler will be called, if there is one
   */
  @Override
  public FutureResultImpl<T> setResult(T result) {
    this.result = result;
    succeeded = true;
    checkCallHandler();
    return this;
  }

  /**
   * Did it succeeed?
   */
  public boolean succeeded() {
    return succeeded;
  }

  private void checkCallHandler() {
    if (handler != null && complete()) {
      handler.handle(this);
    }
  }
}