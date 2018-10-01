package webfx.platforms.core.services.bus;

/**
 * @author Bruno Salmon
 */
public interface BusFactory<O extends BusOptions> {

    Bus createBus(O options);

}
