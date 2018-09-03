package mongooses.core.activities.backend.letter;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import mongooses.core.activities.backend.util.MultiLanguageEditor;
import webfx.framework.activity.base.elementals.view.impl.ViewDomainActivityBase;
import webfx.fxkits.core.properties.Properties;

/**
 * @author Bruno Salmon
 */
final class LetterActivity extends ViewDomainActivityBase {

    private final Property<Object> routeLetterIdProperty = new SimpleObjectProperty<>();

    @Override
    public void onStart() {
        super.onStart();
        Properties.runOnPropertiesChange(() -> {
            if (isActive())
                onLetterChanged();
        }, routeLetterIdProperty, activeProperty());
    }

    @Override
    protected void updateModelFromContextParameters() {
        routeLetterIdProperty.setValue(getParameter("letterId"));
    }

    private MultiLanguageEditor multiLanguageEditor;
    @Override
    public Node buildUi() {
        multiLanguageEditor = new MultiLanguageEditor(this, routeLetterIdProperty::getValue, getDataSourceModel(), lang -> lang, lang -> "subject_" + lang, "Letter");
        return multiLanguageEditor.getUiNode();
    }

    private void onLetterChanged() {
        if (multiLanguageEditor != null)
            multiLanguageEditor.onEntityChanged();
    }
}
