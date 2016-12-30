package naga.fxdata.cell.rowstyle;

import naga.fx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public interface RowAdapter {

    int getRowIndex();

    void addStyleClass(String styleClass);

    void removeStyleClass(String styleClass);

    void applyBackground(Paint fill);

}
