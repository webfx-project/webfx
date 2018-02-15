package naga.platform.services.auth.spi.authz;

/**
 * @author Bruno Salmon
 */
public interface Authorization<A> {

    boolean authorizes(A authority);

    Class<A> authorityClass(); // used for registration when coming from parsing

}
