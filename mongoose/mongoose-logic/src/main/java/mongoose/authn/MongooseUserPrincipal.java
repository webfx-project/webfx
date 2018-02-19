package mongoose.authn;

/**
 * @author Bruno Salmon
 */
public class MongooseUserPrincipal {

    private final Object userAccountId;

    public MongooseUserPrincipal(Object userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Object getUserAccountId() {
        return userAccountId;
    }

}
