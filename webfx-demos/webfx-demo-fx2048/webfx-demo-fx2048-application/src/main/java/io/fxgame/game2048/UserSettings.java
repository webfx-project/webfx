package io.fxgame.game2048;

import webfx.platform.client.services.storage.LocalStorage;

import java.util.Properties;

/**
 * UserSettings
 * 
 * @author Bruno Borges
 */
public enum UserSettings {

    LOCAL;

    public final static int MARGIN = 36;
    //private File userGameFolder;

/*
    private UserSettings() {
        var userHome = System.getProperty("user.home");
        var gamePath = Path.of(userHome, ".fx2048");
        gamePath.toFile().mkdir();
        userGameFolder = gamePath.toFile();

        try {
            var isWindows = System.getProperty("os.arch").toUpperCase().contains("WINDOWS");
            if (isWindows) {
                Files.setAttribute(gamePath, "dos:hidden", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

    public void store(Properties data, String fileName) {
        data.forEach((key, value) -> LocalStorage.setItem(key.toString(), value.toString()));

        /*
        try {
            data.store(new FileWriter(new File(userGameFolder, fileName)), fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

    public void restore(Properties props, String fileName) {
        LocalStorage.getKeys().forEachRemaining(key -> props.put(key, LocalStorage.getItem(key)));
        /*
        try (Reader reader = new FileReader(fileName)) {
            props.load(reader);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
*/
    }

	public int getGridSize() {
        // @TODO save settings for grid size
		return GridOperator.DEFAULT_GRID_SIZE;
	}

}
