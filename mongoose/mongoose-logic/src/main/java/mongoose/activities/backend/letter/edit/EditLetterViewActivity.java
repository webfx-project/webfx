package mongoose.activities.backend.letter.edit;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import naga.commons.util.Objects;
import naga.commons.util.tuples.Pair;
import naga.framework.activity.view.impl.ViewActivityImpl;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.UpdateStore;
import naga.framework.orm.mapping.QueryResultSetToEntityListGenerator;
import naga.framework.ui.controls.LayoutUtil;
import naga.fx.properties.Properties;
import naga.fx.util.ImageStore;
import naga.fxdata.control.HtmlTextEditor;
import naga.platform.services.query.QueryArgument;
import naga.platform.spi.Platform;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class EditLetterViewActivity extends ViewActivityImpl {

    private final static String[] languages = {"en", "de", "es", "fr", "pt"};
    private final Property<Object> routeLetterIdProperty = new SimpleObjectProperty<>();

    private final Map<Object /*letterId*/, UpdateStore> letterStores = new HashMap<>();
    private final Map<Pair<Object /*letterId**/, Object /*language*/>, LangLetter> langLetters = new HashMap<>();

    @Override
    public void onStart() {
        super.onStart();
        toggleGroup = new ToggleGroup();
        Properties.runOnPropertiesChange(p -> {
            if (isActive())
                onLetterChanged();
        }, routeLetterIdProperty, activeProperty(), toggleGroup.selectedToggleProperty());
    }

    @Override
    protected void fetchRouteParameters() {
        routeLetterIdProperty.setValue(getParameter("letterId"));
    }

    private BorderPane borderPane;
    private Map<Object, ToggleButton> languageButtons = new HashMap<>();
    private ToggleGroup toggleGroup;
    @Override
    public Node buildUi() {
        borderPane = new BorderPane();
        for (String language : languages) {
            ToggleButton languageButton = new ToggleButton(null, ImageStore.createImageView("images/32/system/lang_" + language + ".png"));
            languageButton.setUserData(language);
            languageButton.setMinWidth(50d);
            languageButtons.put(language, languageButton);
        }
        toggleGroup.getToggles().setAll(languageButtons.values());
        toggleGroup.selectToggle(languageButtons.get(getI18n().getLanguage()));
        LangLetter langLetter = getLangLetter(false);
        if (langLetter != null)
            langLetter.displayEditor();
        return borderPane;
    }

    private void onLetterChanged() {
        LangLetter langLetter = getLangLetter(true);
        langLetter.displayEditor();
        Object letterId = routeLetterIdProperty.getValue();
        if (!letterStores.containsKey(letterId)) {
            SqlCompiled sqlCompiled = getDataSourceModel().getDomainModel().compileSelect("select de,en,es,fr,pt,subject_de,subject_en,subject_es,subject_fr,subject_pt from Letter where id=?");
            // Then we ask the query service to execute the sql query
            Platform.getQueryService().executeQuery(new QueryArgument(sqlCompiled.getSql(), new Object[]{letterId}, getDataSourceModel().getId())).setHandler(ar -> {
                if (ar.succeeded()) {
                    UpdateStore store = UpdateStore.create(getDataSourceModel());
                    Entity letterEntity = QueryResultSetToEntityListGenerator.createEntityList(ar.result(), sqlCompiled.getQueryMapping(), store, "letter").get(0);
                    store.markChangesAsCommitted();
                    letterStores.put(letterId, store);
                    langLetter.setLetterEntity(letterEntity);
                }
            });
        } else
            langLetter.setLetterEntity(letterStores.get(letterId).getEntityList("letter").get(0));
    }

    private LangLetter getLangLetter(boolean createIfNotExists) {
        Toggle selectedLanguageButton = toggleGroup.getSelectedToggle();
        Object language = selectedLanguageButton != null ? selectedLanguageButton.getUserData() : getI18n().getLanguage();
        Pair<Object, Object> pair = new Pair<>(routeLetterIdProperty.getValue(), language);
        LangLetter langLetter = langLetters.get(pair);
        if (langLetter == null && createIfNotExists)
            langLetters.put(pair, langLetter = new LangLetter(pair.get2()));
        return langLetter;
    }

    private class LangLetter {
        private final TextField subjectTextField = new TextField();
        private final HtmlTextEditor editor = new HtmlTextEditor();
        private final Button saveButton = getI18n().translateText(new Button(), "Save");
        private final Button revertButton = getI18n().translateText(new Button(), "Revert");
        private final Object subjectField;
        private final Object bodyField;
        private UpdateStore letterStore;
        private Entity letterEntity;

        LangLetter(Object lang) {
            subjectField = "subject_" + lang;
            bodyField = lang;
            Properties.runOnPropertiesChange(p -> syncEntityFromUi(), subjectTextField.textProperty(), editor.textProperty());
            revertButton.setOnAction(e -> revert());
            saveButton.setOnAction(e -> save());
        }

        void syncEntityFromUi() {
            if (letterEntity != null) {
                String uiLetterSubject = subjectTextField.getText();
                String entityLetterSubject = letterEntity.getStringFieldValue(subjectField);
                if (!Objects.areEquals(uiLetterSubject, entityLetterSubject))
                    letterEntity.setFieldValue(subjectField, uiLetterSubject);
                String uiLetterBody = editor.getText();
                String entityLetterBody = letterEntity.getStringFieldValue(bodyField);
                if (uiLetterBody != null && !Objects.areEquals(uiLetterBody, entityLetterBody))
                    letterEntity.setFieldValue(bodyField, uiLetterBody);
                updateButtonsDisable();
            }
        }

        void syncUiFromEntity() {
            if (letterEntity != null) {
                String uiLetterSubject = subjectTextField.getText();
                String entityLetterSubject = letterEntity.getStringFieldValue(subjectField);
                if (!Objects.areEquals(uiLetterSubject, entityLetterSubject))
                    subjectTextField.setText(entityLetterSubject);
                String uiLetterBody = editor.getText();
                String entityLetterBody = letterEntity.getStringFieldValue(bodyField);
                if (!Objects.areEquals(uiLetterBody, entityLetterBody))
                    editor.setText(entityLetterBody);
                updateButtonsDisable();
            }
        }

        void setLetterEntity(Entity letterEntity) {
            if (this.letterEntity != letterEntity) {
                this.letterEntity = letterEntity;
                letterStore = (UpdateStore) letterEntity.getStore();
                syncUiFromEntity();
            }
        }

        void revert() {
            letterStore.cancelChanges();
            syncUiFromEntity();
        }

        void save() {
            letterStore.executeUpdate().setHandler(ar -> {
                if (ar.succeeded()) {
                    updateButtonsDisable();
                }
            });
        }

        void updateButtonsDisable() {
            boolean disable = !letterStore.hasChanges();
            saveButton.setDisable(disable);
            revertButton.setDisable(disable);
        }

        void displayEditor() {
            if (borderPane != null && borderPane.getCenter() != editor) {
                syncUiFromEntity();
                borderPane.setTop(subjectTextField);
                borderPane.setCenter(editor);
                BorderPane buttonsBar = new BorderPane();
                HBox hBox = new HBox();
                for (Object language : languages)
                    hBox.getChildren().add(languageButtons.get(language));
                buttonsBar.setLeft(hBox);
                buttonsBar.setCenter(new HBox(LayoutUtil.createHGrowable(), saveButton, revertButton, LayoutUtil.createHGrowable()));
                borderPane.setBottom(buttonsBar);
                // The following code is just a temporary workaround to make CKEditor work in html platform (to be removed once fixed)
                if (letterEntity != null) {
                    editor.resize(1, 1);
                    editor.requestLayout();
                }
            }
        }
    }
}
