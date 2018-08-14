package mongoose.services.authn;

/**
 * @author Bruno Salmon
 */
public class MongooseUserPrincipal {

    private final Object userPersonId;
    private final Object userAccountId;

    public MongooseUserPrincipal(Object userPersonId, Object userAccountId) {
        this.userPersonId = userPersonId;
        this.userAccountId = userAccountId;
    }

    public Object getUserPersonId() {
        return userPersonId;
    }

    public Object getUserAccountId() {
        return userAccountId;
    }

}
