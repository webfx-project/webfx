/**
 * @author Bruno Salmon
 */
module webfx.fxkits.core {
    requires webfx.scheduler;
    requires webfx.util;
    requires webfx.type;

    requires static javafx.controls;

    exports webfx.fx.properties;
    exports webfx.fx.properties.conversion;
    exports webfx.fx.properties.markers;
    exports webfx.fx.scene;
    exports webfx.fx.spi;
    exports webfx.fx.spi.peer;
    exports webfx.fx.spi.peer.base;
    exports webfx.fx.util;
    exports webfx.fxdata.cell.collator;
    exports webfx.fxdata.cell.renderer;
    exports webfx.fxdata.cell.rowstyle;
    exports webfx.fxdata.chart;
    exports webfx.fxdata.control;
    exports webfx.fxdata.displaydata;
    exports webfx.fxdata.spi.peer.base;

}