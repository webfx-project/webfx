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
package naga.core.spi.plat.javaplat;

import naga.core.spi.plat.Diff;
import naga.core.spi.json.JsonArray;

import java.util.Comparator;

//import name.fraser.neil.plaintext.diff_match_patch;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/channel/mqtt/packet/MqttPacket.java">Original Goodow class</a>
 */
public class JavaDiff implements Diff {
    //private final diff_match_patch dmp = new diff_match_patch();

    @Override
    public void diff(String before, String after, ListTarget<String> target) {
        /*
        LinkedList<diff_match_patch.Diff> diffs = dmp.diff_main(before, after);
        dmp.diff_cleanupSemantic(diffs);
        int cursor = 0;
        for (diff_match_patch.Diff diff : diffs) {
            String text = diff.text;
            int len = text.length();
            switch (diff.operation) {
                case EQUAL:
                    cursor += len;
                    break;
                case INSERT:
                    target.insert(cursor, text);
                    cursor += len;
                    break;
                case DELETE:
                    target.remove(cursor, len);
                    break;
                default:
                    throw new RuntimeException("Shouldn't reach here!");
            }
        }
        */
    }

    @Override
    public void diff(JsonArray before, JsonArray after, ListTarget<JsonArray> target,
                     Comparator<Object> comparator) {

    }
}