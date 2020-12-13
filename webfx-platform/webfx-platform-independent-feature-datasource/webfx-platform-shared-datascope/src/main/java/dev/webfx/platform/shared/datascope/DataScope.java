package dev.webfx.platform.shared.datascope;

import java.util.ArrayList;

/**
 * @author Bruno Salmon
 */
public interface DataScope {

    boolean intersects(DataScope otherScope);

    static DataScope concat(DataScope... dataScopes) {
        if (dataScopes == null)
            return null;
        switch (dataScopes.length) {
            case 0: return null;
            case 1: return dataScopes[0];
            case 2: if (dataScopes[0] == null) return dataScopes[1];
            default:
                ArrayList<KeyDataScope> keyDataScopes = MultiKeyDataScope.collectKeyDataScopes(new ArrayList<>(), dataScopes);
                if (keyDataScopes.isEmpty())
                    return null;
                if (keyDataScopes.size() == 1)
                    return keyDataScopes.get(0);
                return new MultiKeyDataScopeImpl(keyDataScopes.toArray(new KeyDataScope[0]));
        }
    }

}
