package naga.framework.ui.presentation;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.commons.util.Strings;
import naga.commons.util.function.Factory;
import naga.framework.activity.client.HasMountNodeProperty;
import naga.framework.activity.client.UiDomainActivityContext;
import naga.framework.activity.client.UiDomainActivityContextDirectAccess;
import naga.framework.ui.filter.ReactiveExpressionFilter;
import naga.platform.activity.Activity;
import naga.platform.json.Json;
import naga.toolkit.properties.markers.HasImageProperty;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class PresentationActivity<VM extends ViewModel, PM extends PresentationModel> implements Activity<UiDomainActivityContext>, UiDomainActivityContextDirectAccess {

    private Factory<PM> presentationModelFactory;
    private PM presentationModel;
    private VM viewModel;
    private UiDomainActivityContext activityContext;

    private boolean viewBoundWithPresentationModel;
    private boolean presentationModelBoundWithLogic;

    private final Property<Boolean> activeProperty = new SimpleObjectProperty<>(false); // Should be stored in UiContext?

    public boolean isActive() {
        return activeProperty.getValue();
    }

    private void setActive(boolean active) {
        activeProperty.setValue(active);
    }

    private static final Map<Class, ViewBuilder> viewBuilders = new HashMap<>();

    public static <VM extends ViewModel> void registerViewBuilder(Class<? extends PresentationActivity<VM, ? extends PresentationModel>> presentationActivityClass, ViewBuilder<VM> viewBuilder) {
        viewBuilders.put(presentationActivityClass, viewBuilder);
    }

    protected PresentationActivity() {
    }

    protected PresentationActivity(Factory<PM> presentationModelFactory) {
        setPresentationModelFactory(presentationModelFactory);
    }

    protected void setPresentationModelFactory(Factory<PM> presentationModelFactory) {
        this.presentationModelFactory = presentationModelFactory;
    }

    public UiDomainActivityContext getActivityContext() {
        return activityContext;
    }

    @Override
    public void onCreate(UiDomainActivityContext context) {
        this.activityContext = context;
    }

    @Override
    public void onStart() {
        if (presentationModel == null)
            presentationModel = presentationModelFactory != null ? presentationModelFactory.create() : buildPresentationModel();
        initializePresentationModel(presentationModel);
        if (!presentationModelBoundWithLogic) {
            bindPresentationModelWithLogic(presentationModel);
            presentationModelBoundWithLogic = true;
        }
        setActive(true);
    }

    @Override
    public void onResume() {
        initializePresentationModel(presentationModel); // Doing it again, in case the params have changed on a later resume
        setActive(true);
        Toolkit toolkit = Toolkit.get();
        if (viewModel == null) {
            //Platform.log("Building UI model on resuming " + this.getClass());
            ViewBuilder<VM> viewBuilder = viewBuilders.get(getClass());
            viewModel = viewBuilder != null ? viewBuilder.buildView(toolkit) : buildView(toolkit);
        }
        if (!viewBoundWithPresentationModel) {
            // Binding the mount node property so that child sub routed pages are displayed
            if (viewModel instanceof HasMountNodeProperty)
                ((HasMountNodeProperty) viewModel).mountNodeProperty().bind(mountNodeProperty());
            //Platform.log("Binding UI model with presentation model");
            bindViewModelWithPresentationModel(viewModel, presentationModel);
            viewBoundWithPresentationModel = true;
        }
        toolkit.scheduler().runInUiThread(() -> activityContext.setNode(viewModel.getContentNode()));
    }

    @Override
    public void onPause() {
        setActive(false);
    }

    @Override
    public void onDestroy() {
        viewModel = null;
        viewBoundWithPresentationModel = false;
    }

    protected PM buildPresentationModel() { throw new RuntimeException();}

    protected void initializePresentationModel(PM pm) {}

    protected abstract void bindPresentationModelWithLogic(PM pm);

    protected abstract VM buildView(Toolkit toolkit);

    protected abstract void bindViewModelWithPresentationModel(VM vm, PM pm);

    /** Helpers **/

    protected Image createImage(String urlOrJson) { // TODO: move into Toolkit when Json will be move into naga-commons
        return Toolkit.get().createImage(Strings.startsWith(urlOrJson, "{") ? Json.parseObject(urlOrJson) : urlOrJson);
    }

    protected <T extends HasImageProperty> T setImage(T hasImageProperty, String urlOrJson) {
        hasImageProperty.setImage(createImage(urlOrJson));
        return hasImageProperty;
    }

    protected ReactiveExpressionFilter createReactiveExpressionFilter() {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter());
    }

    protected ReactiveExpressionFilter createReactiveExpressionFilter(Object jsonOrClass) {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter(jsonOrClass));
    }

    private ReactiveExpressionFilter initializeReactiveExpressionFilter(ReactiveExpressionFilter reactiveExpressionFilter) {
        reactiveExpressionFilter.activePropertyProperty().bind(activeProperty);
        return reactiveExpressionFilter
                .setDataSourceModel(activityContext.getDataSourceModel())
                .setI18n(getI18n())
                ;
    }
}
