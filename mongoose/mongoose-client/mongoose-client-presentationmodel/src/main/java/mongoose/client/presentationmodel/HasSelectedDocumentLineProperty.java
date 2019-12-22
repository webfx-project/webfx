package mongoose.client.presentationmodel;

import javafx.beans.property.ObjectProperty;
import mongoose.shared.entities.DocumentLine;

public interface HasSelectedDocumentLineProperty {

    ObjectProperty<DocumentLine> selectedDocumentLineProperty();

    default DocumentLine getSelectedDocumentLine() { return selectedDocumentLineProperty().getValue(); }

    default void setSelectedDocumentLine(DocumentLine value) { selectedDocumentLineProperty().setValue(value); }

}
