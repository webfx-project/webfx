package naga.commons.bus.spi;

/**
 * @author Bruno Salmon
 */
public interface BusFactory<O extends BusOptions> {

    Bus createBus(O options);

}
