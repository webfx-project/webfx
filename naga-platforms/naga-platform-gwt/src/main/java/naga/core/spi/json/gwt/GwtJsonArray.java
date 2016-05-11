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
package naga.core.spi.json.gwt;

import naga.core.json.WritableJsonArray;

/**
 * Client-side implementation of JsonArray.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonArray.java">Original Goodow class</a>
 */
final class GwtJsonArray extends GwtJsonElement implements WritableJsonArray {

    protected GwtJsonArray() { // no public constructor, instances are always obtained from a cast
    }

    @Override
    public native int indexOfNativeElement(Object element) /*-{
        return this.indexOf(value);
    }-*/;

    @Override
    public native GwtJsonValue getNativeElement(int index) /*-{
        return this[index];
      }-*/;

    @Override
    public native void setNativeElement(int index, Object value) /*-{
        this[index] = value;
    }-*/;

    @Override
    public native void pushNativeElement(Object element) /*-{
        this[this.length] = value;
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
    public native boolean getBoolean(int index) /*-{
        return this[index];
    }-*/;

    @Override
    public native double getDouble(int index) /*-{
        return this[index];
    }-*/;

    @Override
    public native String getString(int index) /*-{
        return this[index];
    }-*/;

    @Override
    public native void push(boolean bool_) /*-{
        this[this.length] = bool_;
      }-*/;

    @Override
    public native void push(double number) /*-{
        this[this.length] = number;
      }-*/;

    @Override
    public native void push(Object element) /*-{
        this[this.length] = element;
      }-*/;

    @Override
    public native <T> T remove(int index) /*-{
        return this.splice(index, 1)[0];
      }-*/;
}