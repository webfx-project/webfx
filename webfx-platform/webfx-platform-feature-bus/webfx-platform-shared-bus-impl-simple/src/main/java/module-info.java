// Generated by WebFx

module webfx.platform.shared.bus.impl.simple {

    // Direct dependencies modules
    requires webfx.platform.shared.bus;

    // Exported packages
    exports dev.webfx.platform.shared.services.bus.spi.impl.simple;

    // Provided services
    provides dev.webfx.platform.shared.services.bus.spi.BusServiceProvider with dev.webfx.platform.shared.services.bus.spi.impl.simple.SimpleBusServiceProvider;

}