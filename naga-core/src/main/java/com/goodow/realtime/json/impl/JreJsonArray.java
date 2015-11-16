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
package com.goodow.realtime.json.impl;

import com.goodow.realtime.json.JsonArray;
import com.goodow.realtime.json.JsonType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Server-side implementation of JsonArray.
 *
 * @author 田传武 (aka larrytin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/blob/master/src/main/java/com/goodow/json/impl/JreJsonArray.java">Original Goodow class</a>
 */
public class JreJsonArray extends JreJsonElement implements JsonArray {
  private static final long serialVersionUID = -4799870976276999803L;

  @SuppressWarnings("unchecked")
  static List<Object> convertList(List<?> list) {
    List<Object> arr = new ArrayList<Object>(list.size());
    for (Object obj : list) {
      if (obj instanceof Map) {
        arr.add(JreJsonObject.convertMap((Map<String, Object>) obj));
      } else if (obj instanceof List) {
        arr.add(convertList((List<?>) obj));
      } else {
        arr.add(obj);
      }
    }
    return arr;
  }

  protected List<Object> list;

  public JreJsonArray() {
    this.list = new ArrayList<Object>();
  }

  public JreJsonArray(List<Object> array) {
    this.list = array;
  }
  /*
  public JreJsonArray(String jsonString) {
    list = JacksonUtil.decodeValue(jsonString, List.class);
  }
  */
  @SuppressWarnings("unchecked")
  @Override
  public JsonArray clear() {
    if (needsCopy) {
      list = new ArrayList<Object>();
      needsCopy = false;
    } else {
      list.clear();
    }
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public JsonArray copy() {
    JreJsonArray copy = new JreJsonArray(list);
    // We actually do the copy lazily if the object is subsequently mutated
    copy.needsCopy = true;
    needsCopy = true;
    return copy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    JreJsonArray that = (JreJsonArray) o;

    if (this.list.size() != that.list.size()) {
      return false;
    }

    java.util.Iterator<?> iter = that.list.iterator();
    for (Object entry : this.list) {
      Object other = iter.next();
      if (entry == null) {
        if (other != null) {
          return false;
        }
      } else if (!entry.equals(other)) {
        return false;
      }
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> void forEach(ListIterator<T> handler) {
    int i = 0;
    for (Object value : list) {
      handler.call(i++, (T) get(value));
    }
  }

  @Override
  public <T> T get(int index) {
    return get(list.get(index));
  }

  @Override
  public JreJsonArray getArray(int index) {
    @SuppressWarnings("unchecked")
    List<Object> l = (List<Object>) list.get(index);
    return l == null ? null : new JreJsonArray(l);
  }

  @Override
  public boolean getBoolean(int index) {
    return (Boolean) list.get(index);
  }

  @Override
  public double getNumber(int index) {
    return ((Number) list.get(index)).doubleValue();
  }

  @Override
  public JreJsonObject getObject(int index) {
    @SuppressWarnings("unchecked")
    Map<String, Object> m = (Map<String, Object>) list.get(index);
    return m == null ? null : new JreJsonObject(m);
  }

  @Override
  public String getString(int index) {
    return (String) list.get(index);
  }

  @Override
  public JsonType getType(int index) {
    return super.getType(list.get(index));
  }

  @Override
  public int indexOf(Object value) {
    return list.indexOf(value);
  }

  @Override
  public JsonArray insert(int index, Object value) {
    checkCopy();
    if (value instanceof JreJsonObject) {
      value = ((JreJsonObject) value).map;
    } else if (value instanceof JreJsonArray) {
      value = ((JreJsonArray) value).list;
    }
    list.add(index, value);
    return this;
  }

  @Override
  public int length() {
    return list.size();
  }

  @Override
  public JsonArray push(boolean bool_) {
    checkCopy();
    list.add(bool_);
    return this;
  }

  @Override
  public JsonArray push(double number) {
    checkCopy();
    list.add(number);
    return this;
  }

  @Override
  public JsonArray push(Object value) {
    checkCopy();
    if (value instanceof JreJsonObject) {
      value = ((JreJsonObject) value).map;
    } else if (value instanceof JreJsonArray) {
      value = ((JreJsonArray) value).list;
    }
    list.add(value);
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T remove(int index) {
    checkCopy();
    return (T) list.remove(index);
  }

  @Override
  public boolean removeValue(Object value) {
    return list.remove(value);
  }

  @Override
  public String toJsonString() {
    return JacksonUtil.encode(list);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> toNative() {
    return (List<T>) list;
  }

  @Override
  public String toString() {
    return toJsonString();
  }

  private void checkCopy() {
    if (needsCopy) {
      // deep copy the list lazily if the object is mutated
      list = convertList(list);
      needsCopy = false;
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private <T> T get(Object value) {
    if (value instanceof Map) {
      value = new JreJsonObject((Map) value);
    } else if (value instanceof List) {
      value = new JreJsonArray((List) value);
    }
    return (T) value;
  }
}