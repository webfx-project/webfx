package webfx.demo.enzoclocks.settings;

import eu.hansolo.enzo.clock.Clock;
import eu.hansolo.enzo.clock.ClockBuilder;
import eu.hansolo.enzo.flippanel.FlipPanel;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import webfx.extras.type.PrimType;
import webfx.extras.visual.SelectionMode;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualResultBuilder;
import webfx.extras.visual.VisualStyle;
import webfx.extras.visual.controls.grid.SkinnedVisualGrid;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.kit.util.properties.Properties;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Bruno Salmon
 */
public class ClockSetting {

    private final static List<String> AVAILABLE_ZONE_IDS = ZoneId.getAvailableZoneIds().stream().filter(z -> !zoneShortName(z).equals(zoneShortName(z).toUpperCase())).sorted().collect(Collectors.toList());
    private List<String> filteredZoneIds;

    private ZoneId zoneId;
    private String text;
    private Clock.Design design;
    private boolean discreteSecond;
    private final Clock clock;
    private Parent container;
    private Runnable onRemoveRequested;

    public ClockSetting(String zoneName, String text, Clock.Design design) {
        this(ZoneId.of(zoneName), text, design);
    }

    public ClockSetting(ZoneId zoneId, String text, Clock.Design design) {
        this.zoneId = zoneId;
        this.text = text;
        this.design = design;
        discreteSecond = true;
        clock = ClockBuilder.create()
                .design(design)
                .text(text != null ? text : zoneShortName(zoneId.getId()))
                .autoNightMode(true)
                .discreteSecond(discreteSecond)
                .time(LocalTime.now(zoneId))
                .build();
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
        setText(text);
    }

    public void setText(String text) {
        String zoneShortName = zoneShortName(zoneId.getId());
        this.text = zoneShortName.equals(text) ? null : text;
        clock.setText(text != null ? text : zoneShortName);
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public Clock getClock() {
        return clock;
    }

    public Parent getContainer() {
        return container;
    }

    public void setContainer(Parent container) {
        this.container = container;
    }

    public void setOnRemoveRequested(Runnable onRemoveRequested) {
        this.onRemoveRequested = onRemoveRequested;
    }

    private Clock createDesignSelectorClock(Clock.Design d) {
        Clock designSelectorClock = new ClockSetting(zoneId, text, d).clock;
        designSelectorClock.timeProperty().bind(clock.timeProperty());
        designSelectorClock.setCursor(Cursor.HAND);
        designSelectorClock.setOnMouseClicked(e -> {
            clock.setDesign(d);
            clock.setDiscreteSecond(discreteSecond);
            Pane root = (Pane) clock.getScene().getRoot();
            root.getChildren().remove(root.getChildren().size() - 1);
/*
            if (container instanceof FlipPanel)
                ((FlipPanel) container).flipToFront();
*/
        });
        return designSelectorClock;
    }

    public Parent embedClock() {
        FlipPanel fp = new FlipPanel(Orientation.VERTICAL);
        Text[] timeZoneLetters = createLetters("Time Zone", Color.LIGHTSKYBLUE),
                captionLetters = createLetters("Caption",   Color.ORANGE),
                designLetters  = createLetters("Design",    Color.YELLOW),
                removeLetters  = createLetters("Remove",    Color.RED);
        Text okText = createText("Ok", Color.YELLOWGREEN);
        Pane back = new Pane(concat(Text[]::new, designLetters, timeZoneLetters, captionLetters, removeLetters, new Text[]{okText})) {
            @Override
            protected void layoutChildren() {
                layoutLetters(timeZoneLetters, true, true);
                layoutLetters(captionLetters, true, false);
                layoutLetters(designLetters, false, true);
                layoutLetters(removeLetters, false, false);
            }

            private void layoutLetters(Text[] letters, boolean top, boolean left) {
                double width  = getWidth();
                double height = getHeight();
                double size = Math.min(width, height);
                double r = size / 2;
                double middleAngle = Math.PI * (top ? (left ? -0.75 : -0.25) : (left ? 0.75 : 0.25));
                double length = Arrays.stream(letters).mapToDouble(l -> l.getBoundsInLocal().getWidth()).sum();
                double anglePerLength = 0.9 * Math.PI / 2 / length * (top ? 1 : -1);
                double angle = middleAngle - anglePerLength * length / 2;
                Font font = Font.font(r * 0.2);
                for (Text letter : letters) {
                    letter.setFont(font);
                    Bounds letterBounds = letter.getBoundsInLocal();
                    double lw = letterBounds.getWidth();
                    double lh = letterBounds.getHeight();
                    angle += anglePerLength * lw / 2;
                    letter.setRotate(angle / Math.PI * 180 + (top ? 90 : -90));
                    //letter.getTransforms().setAll(new Rotate(angle / Math.PI * 180 + (top ? 90 : -90), lw / 2, lh / 2));
                    layoutInArea(letter, r + 0.75 * r * Math.cos(angle) - lw / 2, r + 0.75 * r * Math.sin(angle) - lh / 2, lw, lh, 0, HPos.CENTER, VPos.CENTER);
                    angle += anglePerLength * lw / 2;
                }
                okText.setFont(font);
                Bounds okBounds = okText.getBoundsInLocal();
                double lw = okBounds.getWidth();
                double lh = okBounds.getHeight();
                layoutInArea(okText, r - lw / 2, r - lh / 2, lw, lh, 0, HPos.CENTER, VPos.CENTER);
            }
        };
        back.setOnMouseClicked(e -> {
            double xc = back.getWidth() / 2;
            double yc = back.getHeight() / 2;
            double x = e.getX() - xc;
            double y = e.getY() - yc;
            if (x * x + y * y < xc * yc / 8) { // central zone (closed to Ok)
                fp.flipToFront();
            } else if (x <= 0 && y < 0) { // left top corner => time zone
                Pane root = (Pane) fp.getScene().getRoot();
                VisualGrid grid = new SkinnedVisualGrid();
                TextField searchField = new TextField();
                searchField.setPromptText("Type here to narrow the list");
                searchField.setAlignment(Pos.CENTER);
                updateZoneGrid(grid, null);
                searchField.textProperty().addListener((observableValue, s, text) -> updateZoneGrid(grid, text));
                ResponsiveGridPane gridPane = new ResponsiveGridPane(new BorderPane(grid, searchField, null, null, null));
                BorderPane.setAlignment(grid, Pos.TOP_LEFT);
                grid.setHeaderVisible(false);
                grid.setSelectionMode(SelectionMode.SINGLE);
                grid.visualSelectionProperty().addListener((observableValue, visualSelection, t1) -> {
                    setZoneId(ZoneId.of(filteredZoneIds.get(t1.getSelectedRow())));
                    root.getChildren().remove(gridPane);
                    //fp.flipToFront();
                });
                grid.setCursor(Cursor.HAND);
                root.getChildren().add(gridPane);
                searchField.requestFocus();
            } else if (x > 0 && y < 0) { // right top corner => caption
                Pane root = (Pane) fp.getScene().getRoot();
                TextField captionField = new TextField(clock.getText());
                Pane pane = new ResponsiveGridPane(captionField);
                captionField.setFont(Font.font(48));
                captionField.setAlignment(Pos.CENTER);
                captionField.setOnAction(ea -> {
                    setText(captionField.getText());
                    root.getChildren().remove(pane);
                    //fp.flipToFront();
                });
                root.getChildren().add(pane);
                captionField.requestFocus();
                captionField.selectAll();
            } else if (x <= 0 && y > 0) { // left bottom corner => design
                Pane root = (Pane) fp.getScene().getRoot();
                ResponsiveGridPane gridPane = new ResponsiveGridPane(Arrays.stream(Clock.Design.values()).map(this::createDesignSelectorClock).toArray(Node[]::new));
                gridPane.setSquare(true);
                root.getChildren().add(gridPane);
            } else { // right bottom corner => remove
                if (onRemoveRequested != null)
                    onRemoveRequested.run();
            }
        });
        clock.setOnMouseClicked(e -> {
            if (e.isControlDown() && onRemoveRequested != null)
                onRemoveRequested.run();
            else {
                updateBack(back);
                fp.flipToBack();
            }
        });
        back.setMinSize(0, 0);
        back.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        fp.getFront().getChildren().setAll(clock);
        fp.getBack() .getChildren().setAll(back);
        fp.setCursor(Cursor.HAND);
        // Even if there is no need visually (clock and back are already round), we add a clip to prevent any click
        // confusions between clocks - because the children nodes of a clock can overlap those of another (transforms,
        // etc...). Also this makes the hand cursor visible only on the circle which is pretty good.
        Circle clip = new Circle();
        fp.setClip(clip);
        Properties.runOnPropertiesChange(() -> {
            double radius = fp.getWidth() / 2;
            clip.setRadius(radius);
            clip.setCenterX(radius);
            clip.setCenterY(radius);
/*
            if (fp.isBackVisible())
                updateBack(back);
*/
        }, fp.widthProperty(), fp.heightProperty());
        setContainer(fp);
        return fp;
    }

    private Text createText(String text, Paint fill) {
        Text t = new Text(text);
        t.setFill(fill);
        t.setMouseTransparent(true);
        t.setTextAlignment(TextAlignment.CENTER);
        return t;
    }

    private Text[] createLetters(String text,  Paint fill) {
        return text.chars().mapToObj(c -> createText("" + (char) c, fill)).toArray(Text[]::new);
    }

    private void updateBack(Pane back) {
        double width  = back.getWidth();
        double height = back.getHeight();
        double size = Math.min(width, height);
        CornerRadii radii = new CornerRadii(size / 2);
        double hInset = Math.max(0, (width  - size) / 2);
        double vInset = Math.max(0, (height - size) / 2);
        back.setBackground(new Background(new BackgroundFill(Color.grayRgb(40, 0.5), radii, new Insets(vInset, hInset, vInset, hInset))));
    }

    private void updateZoneGrid(VisualGrid grid, String searchText) {
        String lowerSearchText = searchText == null ? null : searchText.toLowerCase();
        filteredZoneIds = searchText == null || searchText.isEmpty() ? AVAILABLE_ZONE_IDS : AVAILABLE_ZONE_IDS.stream().filter(z -> z.toLowerCase().contains(lowerSearchText)).collect(Collectors.toList());
        int n = filteredZoneIds.size();
        VisualResultBuilder rsb = new VisualResultBuilder(n, VisualColumn.create("Continent", PrimType.STRING, VisualStyle.CENTER_STYLE), VisualColumn.create("City", PrimType.STRING, VisualStyle.CENTER_STYLE), VisualColumn.create("Offset", PrimType.STRING, VisualStyle.CENTER_STYLE));
        Instant now = Instant.now();
        for (int i = 0 ; i < n; i++) {
            String zoneId = filteredZoneIds.get(i);
            String city = zoneShortName(zoneId);
            rsb.setValue(i, 0, zoneId.equals(city) ? null : zoneId.substring(0, zoneId.indexOf('/')));
            rsb.setValue(i, 1, city);
            ZoneId z = ZoneId.of(zoneId);
            rsb.setValue(i, 2, z.getRules().getOffset(now).getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        }
        grid.setVisualResult(rsb.build());
    }

    public static ClockSetting createRandom(List<ClockSetting> excluded) {
        return Stream.generate(Math::random).map(r -> AVAILABLE_ZONE_IDS.get(clampRandom(r, AVAILABLE_ZONE_IDS.size()))).filter(z -> excluded.stream().noneMatch(s -> s.getZoneId().toString().equals(z))).map(z -> new ClockSetting(z, null, Clock.Design.values()[clampRandom(Math.random(), Clock.Design.values().length)])).findFirst().orElse(null);
    }

    private static String zoneShortName(String zoneName) {
        return zoneName.substring(zoneName.lastIndexOf('/') + 1).replace("_", " ");
    }

    private static int clampRandom(double random, int n) {
        return (int) Math.floor(random * n);
    }

    public static <A> A[] concat(IntFunction<A[]> arrayGenerator, A[]... as) {
        int length = 0;
        for (A[] a : as)
            length += a.length;
        A[] array = arrayGenerator.apply(length);
        length = 0;
        for (A[] a : as) {
            System.arraycopy(a, 0, array, length, a.length);
            length += a.length;
        }
        return array;
    }

}
