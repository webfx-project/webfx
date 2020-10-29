/*
 * Copyright (c) 2020 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.spacefx;

import webfx.platform.client.services.storage.LocalStorage;
import webfx.platform.shared.util.uuid.Uuid;

import java.util.HashMap;
import java.util.Map;


public enum PropertyManager {
    INSTANCE;

    private final Map<String, String> properties;


    // ******************** Constructors **************************************
    PropertyManager() {
        properties = new HashMap<>();
        // Load properties
        /*final String propFilePath = new StringBuilder(System.getProperty("user.home")).append(File.separator).append(PROPERTIES_FILE_NAME).toString();
        try (FileInputStream propFile = new FileInputStream(propFilePath)) {
            properties.load(propFile);
        } catch (IOException ex) {
            System.out.println("Error reading properties file. " + ex);
        }*/
        LocalStorage.getKeys().forEachRemaining(key -> properties.put(key, LocalStorage.getItem(key)));

        // If properties empty, fill with default values
        if (properties.isEmpty()) { createProperties(properties); }
    }


    // ******************** Methods *******************************************
    //public Properties getProperties() { return properties; }

    public Object get(final String KEY) { return properties.getOrDefault(KEY, ""); }
    public void set(final String KEY, final String VALUE) {
        properties.put(KEY, VALUE);
        storeProperties();
    }

    public String getString(final String key) { return properties.getOrDefault(key, "").toString(); }

    public double getDouble(final String key) { return Double.parseDouble(properties.getOrDefault(key, "0").toString()); }

    public float getFloat(final String key) { return Float.parseFloat(properties.getOrDefault(key, "0").toString()); }

    public int getInt(final String key) { return Integer.parseInt(properties.getOrDefault(key, "0").toString()); }

    public long getLong(final String key) { return Long.parseLong(properties.getOrDefault(key, "0").toString()); }


    // ******************** Properties ****************************************
    private void createProperties(final Map<String, String> properties) {
        //final String propFilePath = new StringBuilder(System.getProperty("user.home")).append(File.separator).append(PROPERTIES_FILE_NAME).toString();
        //try (OutputStream output = new FileOutputStream(propFilePath)) {
            properties.put("hallOfFame1", generateUuid() + ",AA,0");
            properties.put("hallOfFame2", generateUuid() + ",BB,0");
            properties.put("hallOfFame3", generateUuid() + ",CC,0");
            storeProperties();
            //properties.store(output, null);
        /*} catch (IOException ex) {
            System.out.println("Error creating properties file. " + ex);
        }*/
    }

    private String generateUuid() {
        return Uuid.randomUuid();
    }

    public void storeProperties() {
        properties.forEach(LocalStorage::setItem);
/*
        try {
            final String propFilePath = new StringBuilder(System.getProperty("user.home")).append(File.separator).append(PROPERTIES_FILE_NAME).toString();
            properties.store(new FileOutputStream(propFilePath), "");
        } catch (Exception ex) {
            System.out.println("Error writing properties file. " + ex);
        }
*/
    }
}
