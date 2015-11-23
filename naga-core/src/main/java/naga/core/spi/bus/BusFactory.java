package naga.core.spi.bus;

/**
 * @author Bruno Salmon
 */
public interface BusFactory {

    Bus createBus(BusOptions options);

}
