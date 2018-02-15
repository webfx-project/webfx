package naga.platform.services.auth.spi.authz;

import naga.util.collection.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class AuthorizationRegistry implements Authorization {

    private final Map<Class, Collection<Authorization>> registeredAuthorizations = new HashMap<>();
    private AuthorizationParser authorizationParser;
    private AuthorityParser authorityParser;

    public void setAuthorizationParser(AuthorizationParser authorizationParser) {
        this.authorizationParser = authorizationParser;
    }

    public void addAuthorizationParser(AuthorizationParser authorizationParser) {
        if (this.authorizationParser == null)
            this.authorizationParser = authorizationParser;
        else {
            AuthorizationParserRegistry registry;
            if (this.authorizationParser instanceof AuthorizationParserRegistry)
                registry = (AuthorizationParserRegistry) this.authorizationParser;
            else {
                registry = new AuthorizationParserRegistry();
                registry.registerParser(this.authorizationParser);
            }
            registry.registerParser(authorizationParser);
        }
    }

    public void setAuthorityParser(AuthorityParser authorityParser) {
        this.authorityParser = authorityParser;
    }

    public void addAuthorityParser(AuthorityParser authorityParser) {
        if (this.authorityParser == null)
            this.authorityParser = authorityParser;
        else {
            AuthorityParserRegistry registry;
            if (this.authorityParser instanceof AuthorizationParserRegistry)
                registry = (AuthorityParserRegistry) this.authorityParser;
            else {
                registry = new AuthorityParserRegistry();
                registry.registerParser(this.authorityParser);
            }
            registry.registerParser(authorityParser);
        }
    }

    public <A> void registerAuthorization(Class<A> authorityClass, Authorization<A> authorization) {
        Collection<Authorization> authorizations = registeredAuthorizations.get(authorityClass);
        if (authorizations == null)
            registeredAuthorizations.put(authorityClass, authorizations = new ArrayList<>());
        authorizations.add(authorization);
    }

    public <A> void registerAuthorization(Authorization authorization) {
        registerAuthorization(authorization.authorityClass(), authorization);
    }

    public void registerAuthorization(String authorization) {
        registerAuthorization(authorizationParser.parseAuthorization(authorization));
    }

    @Override
    public Class authorityClass() {
        return Object.class;
    }

    @Override
    public boolean authorizes(Object authority) {
        Object parsedAuthority = authority instanceof String && authorityParser != null ? authorityParser.parseAuthority((String) authority) : authority;
        return Collections.hasAtLeastOneMatching(
                registeredAuthorizations.get(authority.getClass()),
                authorization -> authorization.authorizes(parsedAuthority)
        );
    }
}
