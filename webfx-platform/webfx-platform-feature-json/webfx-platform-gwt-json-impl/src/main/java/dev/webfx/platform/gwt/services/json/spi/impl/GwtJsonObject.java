/*
 * Note: this code is a fork of Goodow realtime-json project https://github.com/goodow/realtime-json
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
package dev.webfx.platform.gwt.services.json.spi.impl;

import com.google.gwt.core.client.JsArrayString;
import dev.webfx.platform.shared.services.json.WritableJsonObject;

/**
 * Client-side implementation of JsonObject interface.
 *
 * @author Bruno Salmon
 */
public final class GwtJsonObject extends GwtJsonElement implements WritableJsonObject {

    // GWT: Constructors must be 'protected' in subclasses of JavaScriptObject
    protected GwtJsonObject() {} // instances are actually always obtained from a javascript object cast

    public static GwtJsonObject create() {
        return createObject().cast();
    }

    @Override
    public native boolean has(String key) /*-{
        return key in this;
    }-*/;


    @Override
    public native GwtJsonElement getNativeElement(String key) /*-{
        return this[key];
    }-*/;

    @Override
    public native GwtJsonObject setNativeElement(String key, Object element) /*-{
        this[key] = element;
        return this;
    }-*/;

    @Override
    public GwtJsonArray keys() {
        return nativeToJavaJsonArray(nativeKeys());
    }

    private native JsArrayString nativeKeys() /*-{
        var keys = [];
        for(var key in this)
          if (Object.prototype.hasOwnProperty.call(this, key) && key != '$H')
            keys.push(key);
        return keys;
    }-*/;

    @Override
    public native <T> T remove(String key) /*-{
        var old = this[key];
        delete this[key];
        return old;
    }-*/;
}