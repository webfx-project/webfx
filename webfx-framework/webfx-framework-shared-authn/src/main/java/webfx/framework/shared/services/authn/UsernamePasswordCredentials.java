package webfx.framework.shared.services.authn;

/**
 * @author Bruno Salmon
 */
public final class UsernamePasswordCredentials {
    private final String username;
    private final String password;

    public UsernamePasswordCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
