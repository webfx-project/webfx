package webfx.framework.shared.router.session;

import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface SessionStore {

    Session createSession();

    /**
     * Get the session with the specified ID
     *
     * @param id  the unique ID of the session
     * @return a future holding the session, or a failure
     */
    Future<Session> get(String id);

    /**
     * Delete the session with the specified ID
     *
     * @param id  the unique ID of the session
     * @return  a future holding true/false, or a failure
     */
    Future<Boolean> delete(String id);

    /**
     * Add a session with the specified ID
     *
     * @param session  the session
     * @return  a future holding true/false, or a failure
     */
    Future<Boolean> put(Session session);

    /**
     * Remove all sessions from the store
     *
     * @return  a future holding true/false, or a failure
     */
    Future<Boolean> clear();

}
