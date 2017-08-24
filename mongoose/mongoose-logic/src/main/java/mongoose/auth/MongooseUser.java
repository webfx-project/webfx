package mongoose.auth;

import naga.platform.services.auth.SuperUser;
import naga.platform.services.auth.spi.User;

/**
 * @author Bruno Salmon
 */
public class MongooseUser extends SuperUser implements User {

    private final Object userAccountPrimaryKey;

    public MongooseUser(Object userAccountPrimaryKey) {
        this.userAccountPrimaryKey = userAccountPrimaryKey;
    }

    public Object getUserAccountPrimaryKey() {
        return userAccountPrimaryKey;
    }
}
