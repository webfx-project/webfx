package naga.core.spi.toolkit.android.controls;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.android.AndroidToolkit;
import naga.core.spi.toolkit.android.node.AndroidNode;
import naga.core.spi.toolkit.controls.SearchBox;
import naga.core.util.Objects;

/**
 * @author Bruno Salmon
 */
public class AndroidSearchBox extends AndroidNode<EditText> implements SearchBox<EditText> {

    public AndroidSearchBox() {
        this(AndroidToolkit.currentActivity);
    }

    public AndroidSearchBox(Context context) {
        this(new EditText(context));
    }

    public AndroidSearchBox(EditText search) {
        super(search);
        textProperty.addListener((observable1, oldValue, newValue) -> { if (!Objects.areEquals(newValue, search.getText().toString())) search.setText(newValue); } );
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                textProperty.setValue(search.getText().toString());
            }
        });
        placeholderProperty.addListener((observable, oldValue, newValue) -> node.setHint(newValue));
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

    private final Property<String> placeholderProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> placeholderProperty() {
        return placeholderProperty;
    }
}
