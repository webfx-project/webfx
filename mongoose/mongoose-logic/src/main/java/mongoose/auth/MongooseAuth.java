package mongoose.auth;

import naga.commons.util.async.Future;
import naga.platform.services.auth.SuperUser;
import naga.platform.services.auth.spi.User;
import naga.platform.services.auth.spi.AuthService;

/**
 * @author Bruno Salmon
 */
public class MongooseAuth implements AuthService {

    private final static MongooseAuth SINGLETON = new MongooseAuth();

    public static MongooseAuth get() {
        return SINGLETON;
    }

    @Override
    public Future<User> authenticate(Object authInfo) {
        return Future.succeededFuture(new SuperUser());
    }
}
