package io.fxgame.game2048;

import java.util.Properties;

/**
 * @author Jose Pereda
 */
public class RecordManager {

    public final String propertiesFilename;
    private final Properties props = new Properties();

    public RecordManager(int grid_size) {
        this.propertiesFilename = "game2048_" + grid_size + "_record.properties";
    }

    public void saveRecord(Integer score) {
        int oldRecord = restoreRecord();
        props.setProperty("record", Integer.toString(Math.max(oldRecord, score)));
        var fileName = propertiesFilename;
        UserSettings.LOCAL.store(props, fileName);
    }

    public int restoreRecord() {
        UserSettings.LOCAL.restore(props, propertiesFilename);

        String score = props.getProperty("record");
        if (score != null) {
            return Integer.parseInt(score);
        }
        return 0;
    }

}
