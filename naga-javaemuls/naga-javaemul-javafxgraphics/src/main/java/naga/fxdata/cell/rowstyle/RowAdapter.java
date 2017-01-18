package naga.fxdata.cell.rowstyle;

import emul.javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public interface RowAdapter {

    int getRowIndex();

    void addStyleClass(String styleClass);

    void removeStyleClass(String styleClass);

    void applyBackground(Paint fill);

}
