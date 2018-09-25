package webfx.platforms.java.services.storage;

import webfx.platforms.core.services.storage.spi.LocalStorageProvider;

import java.io.*;

/**
 * @author Bruno Salmon
 */
public final class JavaLocalStorageProviderImpl extends JavaStorageProviderImpl implements LocalStorageProvider {

    private final File locateStorageFile = new File(getWorkingDirectory(), "storage/localStorage.properties");

    public JavaLocalStorageProviderImpl() {
        loadProperties();
        Runtime.getRuntime().addShutdownHook(new Thread(this::storeProperties));
    }

    private void loadProperties() {
        try (InputStream is = new FileInputStream(locateStorageFile)) {
            properties.load(is);
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeProperties() {
        try {
            locateStorageFile.getParentFile().mkdirs();
            locateStorageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (OutputStream os = new FileOutputStream(locateStorageFile)) {
            properties.store(os, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File getWorkingDirectory() {
        String jarPath = JavaLocalStorageProviderImpl.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (jarPath.endsWith(".jar"))
            return new File(jarPath.replaceAll("%20", " ")).getParentFile();
        return new File(jarPath);
    }
}
