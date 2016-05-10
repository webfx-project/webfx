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
import naga.core.composite.CompositeArray;
import naga.core.composite.WritableCompositeObject;

import java.util.Collection;

/**
 * Client-side implementation of JsonObject interface.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 * 
 * <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonObject.java">Original Goodow class</a>
 */
final class GwtJsonObject extends GwtJsonElement implements WritableCompositeObject {

    protected GwtJsonObject() { // no public constructor, instances are always obtained from a cast
    }

    @Override
    public native GwtJsonValue getNativeElement(String key) /*-{
        return this[key];
    }-*/;

    @Override
    public native boolean has(String key) /*-{
        return key in this;
    }-*/;

    @Override
    public Collection<String> keys() {
        return null; // TODO
        /*JsArrayString str = keys0();
        return reinterpretCast(str);*/
    }

    @Override
    public native <T> T remove(String key) /*-{
        toRtn = this[key];
        delete this[key];
        return toRtn;
    }-*/;

    @Override
    public native void setNativeElement(String key, Object element) /*-{
        this[key] = value;
    }-*/;

    @Override
    public native void set(String key, double number) /*-{
        this[key] = number;
    }-*/;

    @Override
    public void set(String key, Object element) {
        if (element instanceof Boolean)
            set(key, ((Boolean) element).booleanValue());
        else if (element instanceof Number)
            set(key, ((Number) element).doubleValue());
        else
            setObject(key, element);
    }

    // TODO: We still have problem with "__proto__"
    private native GwtJsonObject setObject(String key, Object element) /*-{
        this[key] = element;
        return this;
    }-*/;

    private native GwtJsonObject setString(String key, String element) /*-{
        this[key] = element;
        return this;
    }-*/;


    /**
     * Returns the size of the map (the number of keys).
     * <p>
     * <p>NB: This method is currently O(N) because it iterates over all keys.
     *
     * @return the size of the map.
     */
    @Override
    public final native int size() /*-{
        size = 0;
        for (key in this)
          if (Object.prototype.hasOwnProperty.call(this, key))
            size++;
        return size;
    }-*/;


    private native JsArrayString keys0() /*-{
        var keys = [];
        for(var key in this)
          if (Object.prototype.hasOwnProperty.call(this, key) && key != '$H')
            keys.push(key);
        return keys;
    }-*/;

    private native CompositeArray reinterpretCast(JsArrayString arrayString) /*-{
        return arrayString;
    }-*/;
}