package webfx.framework.shared.router.session;

/**
 * @author Bruno Salmon
 */
public interface Session {

    /**
     * @return The unique ID of the session. This is generated using a random secure UUID.
     */
    String id();

    /**
     * Put some data in a session
     *
     * @param key  the key for the data
     * @param obj  the data
     * @return a reference to this, so the API can be used fluently
     */
    Session put(String key, Object obj);

    /**
     * Get some data from the session
     *
     * @param key  the key of the data
     * @return  the data
     */
    <T> T get(String key);

    /**
     * Remove some data from the session
     *
     * @param key  the key of the data
     * @return  the data that was there or null if none there
     */
    <T> T remove(String key);

}
