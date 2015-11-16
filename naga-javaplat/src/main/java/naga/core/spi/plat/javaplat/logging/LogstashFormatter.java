/*
 * Note: this code is a fork of Goodow realtime-android project https://github.com/goodow/realtime-android
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
package naga.core.spi.plat.javaplat.logging;

import naga.core.spi.json.Json;
import naga.core.spi.json.JsonObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Java Util Logging formatter to encode logging events as json events which can consumed by
 * logstash
 */
public class LogstashFormatter extends Formatter {
    private static String LINE_SEPARATOR = System.getProperty("line.separator");

    @Override
    public String format(LogRecord record) {
        JsonObject obj = Json.createObject();
        obj.set("thread", Thread.currentThread().getName());
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss,SSS");
        // Minimize memory allocations here.
        date.setTime(record.getMillis());
        obj.set("@timestamp", dateFormat.format(date));
        obj.set("priority", record.getLevel().toString());
        obj.set("logger_name", record.getLoggerName());

        obj.set("message", record.getMessage());
        encodeFields(record, obj);

        return obj.toJsonString() + LINE_SEPARATOR;
    }

    private JsonObject encodeFields(final LogRecord record, JsonObject obj) {
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                obj.set("stack_trace", sw.toString());
            } catch (Exception ex) {
            }
        }
        return obj;
    }
}