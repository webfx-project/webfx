package webfx.platform.shared.datascope;

import java.util.ArrayList;

/**
 * @author Bruno Salmon
 */
public interface DataScope {

    boolean intersects(DataScope dataScope);

    static DataScope concat(DataScope... dataScopes) {
        if (dataScopes == null || dataScopes.length == 0)
            return null;
        if (dataScopes.length == 1)
            return dataScopes[0];
        ArrayList<KeyDataScope> keyDataScopes = MultiKeyDataScope.collectKeyDataScopes(new ArrayList<>(), dataScopes);
        if (keyDataScopes.isEmpty())
            return null;
        if (keyDataScopes.size() == 1)
            return keyDataScopes.get(0);
        return new MultiKeyDataScope(keyDataScopes.toArray(new KeyDataScope[0]));
    }

}
