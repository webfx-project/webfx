package webfx.kit.mapper.peers.javafxgraphics.gwt.util;

import elemental2.dom.DataTransfer;

public final class DragboardDataTransferHolder {

    private static DataTransfer dragboardDataTransfer;

    public static void setDragboardDataTransfer(DataTransfer dragboardDataTransfer) {
        DragboardDataTransferHolder.dragboardDataTransfer = dragboardDataTransfer;
    }

    public static DataTransfer getDragboardDataTransfer() {
        return dragboardDataTransfer;
    }
}
