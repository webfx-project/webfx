package naga.platform.services.auth.spi.authn;

/**
 * @author Bruno Salmon
 */
public class UsernamePasswordToken {
    private final String username;
    private final String password;

    public UsernamePasswordToken(String username, String password) {
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
