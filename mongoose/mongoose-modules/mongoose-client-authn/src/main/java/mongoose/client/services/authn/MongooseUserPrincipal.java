package mongoose.client.services.authn;

/**
 * @author Bruno Salmon
 */
public final class MongooseUserPrincipal {

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

    // Static methods helpers

    public static Object getUserPersonId(Object principal) {
        return principal instanceof MongooseUserPrincipal ? ((MongooseUserPrincipal) principal).getUserPersonId() : null;
    }

    public static Object getUserAccountId(Object principal) {
        return principal instanceof MongooseUserPrincipal ? ((MongooseUserPrincipal) principal).getUserAccountId() : null;
    }

}
