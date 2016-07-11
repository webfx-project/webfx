package naga.platform.bus;

/**
 * @author Bruno Salmon
 */
public interface BusFactory<O extends BusOptions> {

    Bus createBus(O options);

}
