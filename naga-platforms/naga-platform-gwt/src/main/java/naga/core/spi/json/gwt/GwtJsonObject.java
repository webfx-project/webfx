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

import com.google.gwt.core.client.JsArrayString;
import naga.core.composite.WritableCompositeObject;

/**
 * Client-side implementation of JsonObject interface.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 * 
 * <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonObject.java">Original Goodow class</a>
 */
public final class GwtJsonObject extends GwtJsonElement implements WritableCompositeObject {

    protected GwtJsonObject() { // no public constructor, instances are always obtained from a cast
    }


    @Override
    public native boolean has(String key) /*-{
        return key in this;
    }-*/;


    @Override
    public native GwtJsonValue getNativeElement(String key) /*-{
        return this[key];
    }-*/;

    @Override
    public native void setNativeElement(String key, Object element) /*-{
        this[key] = element;
    }-*/;

    @Override
    public GwtJsonArray keys() {
        return nativeToCompositeArray(keys0());
    }

    private native JsArrayString keys0() /*-{
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