package naga.toolkit.fxdata.cell.rowstyle;

/**
 * @author Bruno Salmon
 */
public interface RowAdapter {

    int getRowIndex();

    void addStyleClass(String styleClass);

    void removeStyleClass(String styleClass);

}
