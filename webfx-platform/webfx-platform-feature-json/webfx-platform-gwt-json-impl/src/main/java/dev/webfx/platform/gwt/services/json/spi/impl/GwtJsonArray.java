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

import dev.webfx.platform.shared.services.json.WritableJsonArray;

/**
 * Client-side implementation of JsonArray.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the webfx project
 *
 * <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonArray.java">Original Goodow class</a>
 */
public final class GwtJsonArray extends GwtJsonElement implements WritableJsonArray {

    // GWT: Constructors must be 'protected' in subclasses of JavaScriptObject
    protected GwtJsonArray() {} // instances are actually always obtained from a javascript array cast

    static GwtJsonArray create() {
        return createArray().cast();
    }

    @Override
    public native int indexOfNativeElement(Object element) /*-{
        return this.indexOf(element);
    }-*/;

    @Override
    public native GwtJsonElement getNativeElement(int index) /*-{
        return this[index];
      }-*/;

    @Override
    public native GwtJsonArray setNativeElement(int index, Object element) /*-{
        this[index] = element;
        return this;
    }-*/;

    @Override
    public native GwtJsonArray pushNativeElement(Object element) /*-{
        this[this.length] = element;
        return this;
    }-*/;

    @Override
    public native <T> T getElement(int index) /*-{
        return this[index];
    }-*/;

    @Override
    public GwtJsonObject getObject(int index) {
        return getNativeElement(index).cast();
    }

    @Override
    public GwtJsonArray getArray(int index) {
        return getNativeElement(index).cast();
    }

    @Override
    public native Boolean getBoolean(int index) /*-{
        return this[index];
    }-*/;

    @Override
    public native Double getDouble(int index) /*-{
        return this[index];
    }-*/;

    @Override
    public native String getString(int index) /*-{
        return this[index];
    }-*/;

    @Override
    public native GwtJsonArray push(boolean value) /*-{
        this[this.length] = value;
        return this;
      }-*/;

    @Override
    public native GwtJsonArray push(double value) /*-{
        this[this.length] = value;
        return this;
      }-*/;

    @Override
    public native GwtJsonArray push(String value) /*-{
        this[this.length] = value;
        return this;
     }-*/;

    @Override
    public native <T> T remove(int index) /*-{
        return this.splice(index, 1)[0];
     }-*/;
}