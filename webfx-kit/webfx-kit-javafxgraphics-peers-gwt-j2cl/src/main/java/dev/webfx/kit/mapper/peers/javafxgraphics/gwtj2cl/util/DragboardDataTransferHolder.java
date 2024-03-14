package dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util;

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
