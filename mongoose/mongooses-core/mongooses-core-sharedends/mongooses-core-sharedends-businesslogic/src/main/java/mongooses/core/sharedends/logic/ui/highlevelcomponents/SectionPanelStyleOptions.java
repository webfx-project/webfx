package mongooses.core.sharedends.logic.ui.highlevelcomponents;

/**
 * @author Bruno Salmon
 */
public class SectionPanelStyleOptions {

    private final boolean padding;

    public SectionPanelStyleOptions(boolean padding) {
        this.padding = padding;
    }

    public boolean hasPadding() {
        return padding;
    }
}
