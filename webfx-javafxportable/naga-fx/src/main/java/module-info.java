/**
 * @author Bruno Salmon
 */
module naga.fx {
    requires naga.scheduler;
    requires naga.util;
    requires naga.type;

    requires static javafx.controls;

    exports naga.fx.properties;
    exports naga.fx.properties.conversion;
    exports naga.fx.properties.markers;
    exports naga.fx.scene;
    exports naga.fx.spi;
    exports naga.fx.spi.peer;
    exports naga.fx.spi.peer.base;
    exports naga.fx.util;
    exports naga.fxdata.cell.collator;
    exports naga.fxdata.cell.renderer;
    exports naga.fxdata.cell.rowstyle;
    exports naga.fxdata.chart;
    exports naga.fxdata.control;
    exports naga.fxdata.displaydata;
    exports naga.fxdata.spi.peer.base;

}