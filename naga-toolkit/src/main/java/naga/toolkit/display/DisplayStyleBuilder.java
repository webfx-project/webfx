package naga.toolkit.display;

import naga.toolkit.display.impl.DisplayStyleImpl;

/**
 * @author Bruno Salmon
 */
public class DisplayStyleBuilder {

    private Double prefWidth;
    private String textAlign;

    private DisplayStyleBuilder() {
    }

    public DisplayStyleBuilder setPrefWidth(Double prefWidth) {
        this.prefWidth = prefWidth;
        return this;
    }

    public DisplayStyleBuilder setTextAlign(String textAlign) {
        this.textAlign = textAlign;
        return this;
    }

    public DisplayStyle build() {
        return new DisplayStyleImpl(prefWidth, textAlign);
    }

    public static DisplayStyleBuilder create() {
        return new DisplayStyleBuilder();
    }

}
