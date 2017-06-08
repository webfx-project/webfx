package emul.javafx.scene.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.util.StringConverter;

/**
 * @author Bruno Salmon
 */
public class ChoiceBox<T> extends TextField {

    /**
     * Creates a {@code TextField} with empty text content.
     */
    public ChoiceBox() {
        this("");
    }

    /**
     * Creates a {@code TextField} with initial text content.
     *
     * @param text A string for text content.
     */
    public ChoiceBox(String text) {
        //super(new TextFieldContent());
        getStyleClass().add("text-field");
        //setAccessibleRole(AccessibleRole.TEXT_FIELD);
        setText(text);
        selectionModel = new ChoiceBoxSelectionModel<>(this);
    }

    private final ObservableList<T> items = FXCollections.observableArrayList();
    public final ObservableList<T> getItems() { return items; }

    private SingleSelectionModel<T> selectionModel;
    public SingleSelectionModel<T> getSelectionModel() {
        return selectionModel;
    }

    public final void setConverter(StringConverter<T> value) { }

    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    // package for testing
    static class ChoiceBoxSelectionModel<T> extends SingleSelectionModel<T> {
        private final ChoiceBox<T> choiceBox;

        public ChoiceBoxSelectionModel(final ChoiceBox<T> cb) {
            if (cb == null) {
                throw new NullPointerException("ChoiceBox can not be null");
            }
            this.choiceBox = cb;

            /*
             * The following two listeners are used in conjunction with
             * SelectionModel.select(T obj) to allow for a developer to select
             * an item that is not actually in the data model. When this occurs,
             * we actively try to find an index that matches this object, going
             * so far as to actually watch for all changes to the items list,
             * rechecking each time.
             */

            // watching for changes to the items list content
            final ListChangeListener<T> itemsContentObserver = c -> {
                if (choiceBox.getItems() == null || choiceBox.getItems().isEmpty()) {
                    setSelectedIndex(-1);
                } else if (getSelectedIndex() == -1 && getSelectedItem() != null) {
                    int newIndex = choiceBox.getItems().indexOf(getSelectedItem());
                    if (newIndex != -1) {
                        setSelectedIndex(newIndex);
                    }
                }
            };
            if (this.choiceBox.getItems() != null) {
                this.choiceBox.getItems().addListener(itemsContentObserver);
            }

            // watching for changes to the items list
            ChangeListener<javafx.collections.ObservableList<T>> itemsObserver = (valueModel, oldList, newList) -> {
                if (oldList != null) {
                    oldList.removeListener(itemsContentObserver);
                }
                if (newList != null) {
                    newList.addListener(itemsContentObserver);
                }
                setSelectedIndex(-1);
                if (getSelectedItem() != null) {
                    int newIndex = choiceBox.getItems().indexOf(getSelectedItem());
                    if (newIndex != -1) {
                        setSelectedIndex(newIndex);
                    }
                }
            };
            //this.choiceBox.itemsProperty().addListener(itemsObserver);
        }

        // API Implementation
        @Override protected T getModelItem(int index) {
            final javafx.collections.ObservableList<T> items = choiceBox.getItems();
            if (items == null) return null;
            if (index < 0 || index >= items.size()) return null;
            return items.get(index);
        }

        @Override protected int getItemCount() {
            final javafx.collections.ObservableList<T> items = choiceBox.getItems();
            return items == null ? 0 : items.size();
        }

        /**
         * Selects the given row. Since the SingleSelectionModel can only support having
         * a single row selected at a time, this also causes any previously selected
         * row to be unselected.
         * This method is overridden here so that we can move past a Separator
         * in a ChoiceBox and select the next valid menuitem.
         */
/*
        @Override public void select(int index) {
            // this does not sound right, we should let the superclass handle it.
            final T value = getModelItem(index);
            if (value instanceof Separator) {
                select(++index);
            } else {
                super.select(index);
            }

            if (choiceBox.isShowing()) {
                choiceBox.hide();
            }
        }
*/
    }

}
