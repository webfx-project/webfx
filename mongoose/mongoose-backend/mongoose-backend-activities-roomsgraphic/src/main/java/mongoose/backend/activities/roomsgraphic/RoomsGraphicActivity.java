package mongoose.backend.activities.roomsgraphic;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import mongoose.backend.controls.masterslave.MasterSlaveView;
import mongoose.backend.operations.entities.resourceconfiguration.*;
import mongoose.client.activity.eventdependent.EventDependentPresentationModel;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.presentationmodel.HasSelectedDocumentProperty;
import mongoose.shared.entities.Document;
import mongoose.shared.entities.DocumentLine;
import mongoose.shared.entities.Site;
import webfx.extras.flexbox.FlexBox;
import webfx.extras.imagestore.ImageStore;
import webfx.extras.visual.controls.grid.SkinnedVisualGrid;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.framework.client.orm.reactive.mapping.dql_to_entities.ReactiveEntitiesMapper;
import webfx.framework.client.orm.reactive.mapping.entities_to_objects.IndividualEntityToObjectMapper;
import webfx.framework.client.orm.reactive.mapping.entities_to_objects.ReactiveObjectsMapper;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import webfx.framework.client.ui.action.ActionGroup;
import webfx.framework.client.ui.action.operation.OperationActionFactoryMixin;
import webfx.framework.client.ui.util.layout.LayoutUtil;
import webfx.framework.shared.orm.entity.Entity;
import webfx.kit.util.properties.Properties;
import webfx.platform.shared.datascope.aggregate.AggregateScope;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.serial.SerialCodecManager;

import java.util.List;
import java.util.Objects;

import static webfx.framework.shared.orm.dql.DqlStatement.where;

final class RoomsGraphicActivity extends EventDependentViewDomainActivity implements
        HasSelectedDocumentProperty,
        OperationActionFactoryMixin {

    private final static String ITEM_FAMILY_CODE = "acco";

    private final ObjectProperty<Document> selectedDocumentProperty = new SimpleObjectProperty<>();

    @Override
    public ObjectProperty<Document> selectedDocumentProperty() {
        return selectedDocumentProperty;
    }

    private Pane container; // Keeping a reference to the container to act as parent for dialog windows
    private VisualGrid peopleGridFocus;
    private ReactiveObjectsMapper<Site, Tab> sitesToTabsMapper;

    @Override
    public Node buildUi() {
        TabPane sitesTabPane = new TabPane();
        EventDependentPresentationModel pm = getPresentationModel();
        // Mapping the sites of the current events to tabs
        sitesToTabsMapper = ReactiveObjectsMapper.<Site, Tab>createPushReactiveChain(this)
                .always("{class: 'Site', alias: 's', fields: 'icon,name', where: `exists(select ResourceConfiguration where resource.site=s and item.family.code='acco')`, orderBy: 'ord,id'}")
                // Applying the event condition
                .ifNotNullOtherwiseEmpty(pm.eventIdProperty(), eventId -> where("event=?", eventId))
                .setIndividualEntityToObjectMapperFactory(IndividualSiteToTabMapper::new)
                .storeMappedObjectsInto(sitesTabPane.getTabs())
                .start();
        MasterSlaveView masterSlaveView = new MasterSlaveView(sitesTabPane, MasterSlaveView.createAndBindSlaveViewIfApplicable(this, this, () -> container).buildUi());
        masterSlaveView.slaveVisibleProperty().bind(Properties.compute(selectedDocumentProperty(), Objects::nonNull));
        return container = masterSlaveView.buildUi();
    }


    class IndividualSiteToTabMapper implements IndividualEntityToObjectMapper<Site, Tab> {

        final ObjectProperty<Site> siteProperty = new SimpleObjectProperty<>();
        final TabPane siteItemsTabPane = new TabPane();
        final Tab siteTab = new Tab(null, siteItemsTabPane);
        final ReactiveObjectsMapper<Entity, Tab> siteItemsToTabsMapper;

        IndividualSiteToTabMapper(Site site) {
            siteTab.setClosable(false);
            onEntityChangedOrReplaced(site);
            // Mapping the site items to tabs. Each site item will be represented by a ResourceConfiguration group
            // (of this site and one of its possible items)
            siteItemsToTabsMapper = ReactiveObjectsMapper.<Entity, Tab>createPushReactiveChain(RoomsGraphicActivity.this)
                    .always("{class: 'ResourceConfiguration', fields: 'item.icon,item.name,resource.site', groupBy: 'item', orderBy: 'item.ord,item.id'}")
                    .ifNotNullOtherwiseEmpty(siteProperty, s -> where("resource.site=? and item.family.code=?", s, ITEM_FAMILY_CODE))
                    .setActiveParent(sitesToTabsMapper)
                    .bindActivePropertyTo(siteTab.selectedProperty())
                    .setIndividualEntityToObjectMapperFactory(IndividualSiteItemToTabMapper::new)
                    .storeMappedObjectsInto(siteItemsTabPane.getTabs())
                    .start();
        }

        @Override
        public Tab getMappedObject() {
            return siteTab;
        }

        @Override
        public void onEntityChangedOrReplaced(Site site) {
            siteProperty.set(site);
            siteTab.setText(site.getName());
            siteTab.setGraphic(ImageStore.createImageView(site.getIcon()));
        }

        @Override
        public void onEntityRemoved(Site site) {
            siteItemsToTabsMapper.stop();
        }


        class IndividualSiteItemToTabMapper implements IndividualEntityToObjectMapper<Entity, Tab> {
            // The site item is represented by a ResourceConfiguration group (having this site and item)
            final ObjectProperty<Entity> siteItemProperty = new SimpleObjectProperty<>();
            final Pane boxesContainer = new FlexBox(10, 10);
            final Tab siteItemTab = new Tab(null, LayoutUtil.createVerticalScrollPane(boxesContainer));
            final ReactiveObjectsMapper<Entity, Node> siteItemResourceConfigurationsToBoxesMapper;

            IndividualSiteItemToTabMapper(Entity siteItem) { // actually a ResourceConfiguration group
                boxesContainer.setPadding(new Insets(10));
                siteItemTab.setClosable(false);
                onEntityChangedOrReplaced(siteItem);
                // Mapping the site item resource (configurations) to boxes. Each ResourceConfiguration is an individual
                // member of the site item ResourceConfiguration group (so all having the same site and item).
                siteItemResourceConfigurationsToBoxesMapper = ReactiveObjectsMapper.<Entity, Node>create(ReactiveEntitiesMapper.createPushReactiveChain(RoomsGraphicActivity.this))
                        .setActiveParent(siteItemsToTabsMapper)
                        .always("{class: 'ResourceConfiguration', fields: 'name,online,max,comment', orderBy: 'name'}")
                        .ifNotNullOtherwiseEmpty(siteItemProperty, rc -> where("resource.site=? and item=?", rc.evaluate("resource.site"), rc.evaluate("item")))
                        .bindActivePropertyTo(siteItemTab.selectedProperty())
                        .setIndividualEntityToObjectMapperFactory(IndividualSiteItemResourceConfigurationToBoxMapper::new)
                        .storeMappedObjectsInto(boxesContainer.getChildren())
                        .start();
            }

            @Override
            public Tab getMappedObject() {
                return siteItemTab;
            }

            @Override
            public void onEntityChangedOrReplaced(Entity siteItem) { // actually a ResourceConfiguration group
                siteItemProperty.set(siteItem);
                siteItemTab.setText((String) siteItem.evaluate("item.name"));
                siteItemTab.setGraphic(ImageStore.createImageView((String) siteItem.evaluate("item.icon")));
            }

            @Override
            public void onEntityRemoved(Entity siteItem) {
                siteItemResourceConfigurationsToBoxesMapper.stop();
            }


            class IndividualSiteItemResourceConfigurationToBoxMapper implements IndividualEntityToObjectMapper<Entity, Node> {

                private final ObjectProperty<Entity> siteItemResourceConfigurationProperty = new SimpleObjectProperty<>();
                private final Label label = new Label();
                private final VisualGrid peopleGrid = new SkinnedVisualGrid();
                private final VBox box = new VBox(LayoutUtil.setMaxWidthToInfinite(label), peopleGrid);
                private final ReactiveVisualMapper<DocumentLine> peopleVisualMapper;
                private boolean dragBackgroundVisible;

                IndividualSiteItemResourceConfigurationToBoxMapper(Entity siteItemResourceConfiguration) {
                    label.setTextFill(Color.WHITE);
                    label.setAlignment(Pos.CENTER);
                    peopleGrid.setHeaderVisible(false);
                    peopleGrid.setFullHeight(true);
                    VBox.setMargin(label, new Insets(2));
                    VBox.setMargin(peopleGrid, new Insets(0, 5, 5, 5));
                    VBox.setVgrow(peopleGrid, Priority.ALWAYS);
                    box.setMinWidth(150);
                    box.setEffect(BOX_SHADOW_EFFECT);
                    onEntityChangedOrReplaced(siteItemResourceConfiguration);
                    // Visualizing people booked for that site item resource configuration into a visual grid (inside the box)
                    // Each person (row in the grid) is represented by a DocumentLine allocated to that site item resource configuration
                    peopleVisualMapper = ReactiveVisualMapper.<DocumentLine>createPushReactiveChain(RoomsGraphicActivity.this)
                            .setActiveParent(siteItemResourceConfigurationsToBoxesMapper)
                            .always("{class: 'DocumentLine', columns: 'document.<ident>', where: `!cancelled`, orderBy: 'id'}")
                            .ifNotNullOtherwiseEmpty(siteItemResourceConfigurationProperty, rc -> where("resourceConfiguration=?", rc))
                            // Setting the aggregate scope TODO Can this be automatically set by the Dql query push interceptor (or QueryInfo.getQueryScope())?
                            .setAggregateScope(siteItemResourceConfigurationProperty, rc -> AggregateScope.builder().addAggregate("ResourceConfiguration", rc.getPrimaryKey()).build())
                            .unbindActiveProperty() // always active (under parent), no need for additional (and costly) active calls
                            .visualizeResultInto(peopleGrid)
                            .applyDomainModelRowStyle()
                            .setSelectedEntityHandler(dl -> {
                                if (dl != null) {
                                    setSelectedDocument(dl.getDocument());
                                    if (peopleGrid != peopleGridFocus && peopleGridFocus != null) {
                                        VisualGrid previousFocus = peopleGridFocus;
                                        peopleGridFocus = null;
                                        previousFocus.setVisualSelection(null);
                                    }
                                    peopleGridFocus = peopleGrid;
                                } else if (peopleGrid == peopleGridFocus)
                                    setSelectedDocument(null);
                            })
                            .start();
                    setUpContextMenu(box, this::createContextMenuActionGroup);
                    setUpDragAndDropSupport();
                }

                @Override
                public Node getMappedObject() {
                    return box;
                }

                @Override
                public void onEntityChangedOrReplaced(Entity siteItemResourceConfiguration) {
                    siteItemResourceConfigurationProperty.set(siteItemResourceConfiguration);
                    label.setText((String) siteItemResourceConfiguration.evaluate("name"));
                    Boolean online = siteItemResourceConfiguration.getBooleanFieldValue("online");
                    box.setBackground(dragBackgroundVisible ? DRAG_BOX_BACKGROUND : online ? ONLINE_BOX_BACKGROUND : OFFLINE_BOX_BACKGROUND);
                    box.setBorder(BOX_BORDER);
                    peopleGrid.setBackground(online ? ONLINE_PEOPLE_BACKGROUND : OFFLINE_PEOPLE_BACKGROUND);
                }

                @Override
                public void onEntityRemoved(Entity entity) {
                    peopleVisualMapper.stop();
                }

                Entity getSiteItemResourceConfiguration() {
                    return siteItemResourceConfigurationProperty.get();
                }

                void showDragBackground(boolean show) {
                    dragBackgroundVisible = show;
                    onEntityChangedOrReplaced(getSiteItemResourceConfiguration());
                }

                ActionGroup createContextMenuActionGroup() {
                    return newActionGroup(
                            newOperationAction(() -> new EditResourceConfigurationPropertiesRequest(getSiteItemResourceConfiguration(), container)),
                            newOperationAction(() -> new ToggleResourceConfigurationOnlineOfflineRequest(getSiteItemResourceConfiguration())),
                            newOperationAction(() -> new ChangeResourceConfigurationItemRequest(getSiteItemResourceConfiguration(), container, "acco", siteProperty.get().getId())),
                            newOperationAction(() -> new DeleteResourceRequest(getSiteItemResourceConfiguration(), container))
                    );
                }

                /******************** Drag & Drop support ********************/

                private void setUpDragAndDropSupport() {
                    peopleGrid.setOnDragDetected(e -> {
                        Dragboard db = peopleGrid.startDragAndDrop(TransferMode.MOVE);
                        ClipboardContent content = new ClipboardContent();
                        content.put(dndDataFormat, exportDragSelectionForDragboard());
                        db.setContent(content);
                    });
                    // Showing drag background only when entered and accepted
                    box.setOnDragEntered(e -> showDragBackground(acceptsDrag(e)));
                    box.setOnDragOver(e -> {
                        if (acceptsDrag(e))
                            e.acceptTransferModes(TransferMode.MOVE);
                    });
                    box.setOnDragDropped(e -> {
                        Dragboard db = e.getDragboard();
                        if (db.hasContent(dndDataFormat)) {
                            Object[] keys = importDragSelectionFromDragboard(db.getContent(dndDataFormat));
                            executeOperation(new MoveToResourceConfigurationRequest(getSiteItemResourceConfiguration(), keys));
                        }
                    });
                    box.setOnDragExited(e -> showDragBackground(false));
                }

                private boolean acceptsDrag(DragEvent e) {
                    return e.getGestureSource() != peopleGrid && e.getDragboard().hasContent(dndDataFormat);
                }

                private String exportDragSelectionForDragboard() {
                    // Taking the selected people as drag selection (if set)
                    List<DocumentLine> documentLines = peopleVisualMapper.getSelectedEntities();
                    if (documentLines == null) // Otherwise (if nobody is selected),
                        documentLines = peopleVisualMapper.getCurrentEntities(); // taking all people
                    // Exporting the document lines primary keys as a json array
                    return SerialCodecManager.encodePrimitiveArrayToJsonArray(documentLines.stream().map(Entity::getPrimaryKey).toArray()).toJsonString();
                }

                private Object[] importDragSelectionFromDragboard(Object dragContent) {
                    // Parsing the expected json array and decoding it as a java array containing all primary keys
                    return SerialCodecManager.decodePrimitiveArrayFromJsonArray(Json.parseArray(dragContent.toString()));
                }
            }
        }
    }

    // Graphical settings for the resource boxes
    private final static CornerRadii BOX_RADII = new CornerRadii(8);
    private final static Color ONLINE_BOX_COLOR = Color.color(0, 0.8, 0);
    private final static Color OFFLINE_BOX_COLOR = Color.ORANGE;
    private final static Color DRAG_BOX_COLOR = Color.BLUEVIOLET;
    private final static Border BOX_BORDER = new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, BOX_RADII, null));
    private final static Effect BOX_SHADOW_EFFECT = new DropShadow(5, 7, 7, Color.GRAY);
    private final static Background ONLINE_BOX_BACKGROUND = new Background(new BackgroundFill(ONLINE_BOX_COLOR, BOX_RADII, null));
    private final static Background ONLINE_PEOPLE_BACKGROUND = new Background(new BackgroundFill(ONLINE_BOX_COLOR.brighter(), null, null));
    private final static Background OFFLINE_BOX_BACKGROUND = new Background(new BackgroundFill(OFFLINE_BOX_COLOR, BOX_RADII, null));
    private final static Background OFFLINE_PEOPLE_BACKGROUND = new Background(new BackgroundFill(OFFLINE_BOX_COLOR.brighter(), null, null));
    private final static Background DRAG_BOX_BACKGROUND = new Background(new BackgroundFill(DRAG_BOX_COLOR, BOX_RADII, null));

    private static final DataFormat dndDataFormat = DataFormat.PLAIN_TEXT; // Using standard plain text format to ensure drag & drop works between applications
}
