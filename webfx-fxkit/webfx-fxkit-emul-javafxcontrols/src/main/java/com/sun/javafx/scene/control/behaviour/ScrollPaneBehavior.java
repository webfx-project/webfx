package com.sun.javafx.scene.control.behaviour;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

/**
 * Behavior for ScrollPane.
 *
 * TODO: the function variables are a poor way to couple to the rest of
 * the system. This technique avoids a direct dependency on the skin class.
 * However, this should really be coupled through the control itself instead
 * of directly to the skin.
 */
public class ScrollPaneBehavior extends BehaviorBase<ScrollPane> {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    public ScrollPaneBehavior(ScrollPane scrollPane) {
        super(scrollPane, /*SCROLL_PANE_BINDINGS*/ null);
    }

    /***************************************************************************
     *                                                                         *
     * Functions                                                               *
     *                                                                         *
     **************************************************************************/

/*
    public void horizontalUnitIncrement() {
        ((ScrollPaneSkin)getControl().getSkin()).hsbIncrement();
    }
    public void horizontalUnitDecrement() {
        ((ScrollPaneSkin)getControl().getSkin()).hsbDecrement();
    }
    public void verticalUnitIncrement() {
        ((ScrollPaneSkin)getControl().getSkin()).vsbIncrement();
    }
    void verticalUnitDecrement() {
        ((ScrollPaneSkin)getControl().getSkin()).vsbDecrement();
    }
    void horizontalPageIncrement() {
        ((ScrollPaneSkin)getControl().getSkin()).hsbPageIncrement();
    }
    void horizontalPageDecrement() {
        ((ScrollPaneSkin)getControl().getSkin()).hsbPageDecrement();
    }
    void verticalPageIncrement() {
        ((ScrollPaneSkin)getControl().getSkin()).vsbPageIncrement();
    }
    void verticalPageDecrement() {
        ((ScrollPaneSkin)getControl().getSkin()).vsbPageDecrement();
    }
    void verticalHome() {
        ScrollPane sp = getControl();
        sp.setHvalue(sp.getHmin());
        sp.setVvalue(sp.getVmin());
    }
    void verticalEnd() {
        ScrollPane sp = getControl();
        sp.setHvalue(sp.getHmax());
        sp.setVvalue(sp.getVmax());
    }


    public void contentDragged(double deltaX, double deltaY) {
        // negative when dragged to the right/bottom
        ScrollPane scroll = getControl();
        if (!scroll.isPannable()) return;
        if (deltaX < 0 && scroll.getHvalue() != 0 || deltaX > 0 && scroll.getHvalue() != scroll.getHmax()) {
            scroll.setHvalue(scroll.getHvalue() + deltaX);
        }
        if (deltaY < 0 && scroll.getVvalue() != 0 || deltaY > 0 && scroll.getVvalue() != scroll.getVmax()) {
            scroll.setVvalue(scroll.getVvalue() + deltaY);
        }
    }
*/

    /***************************************************************************
     *                                                                         *
     * Key event handling                                                      *
     *                                                                         *
     **************************************************************************/

/*
    static final String TRAVERSE_DEBUG = "TraverseDebug";
    static final String HORIZONTAL_UNITDECREMENT = "HorizontalUnitDecrement";
    static final String HORIZONTAL_UNITINCREMENT = "HorizontalUnitIncrement";
    static final String VERTICAL_UNITDECREMENT = "VerticalUnitDecrement";
    static final String VERTICAL_UNITINCREMENT = "VerticalUnitIncrement";
    static final String VERTICAL_PAGEDECREMENT = "VerticalPageDecrement";
    static final String VERTICAL_PAGEINCREMENT = "VerticalPageIncrement";
    static final String VERTICAL_HOME = "VerticalHome";
    static final String VERTICAL_END = "VerticalEnd";
*/

    /**
     * We manually handle focus traversal keys due to the ScrollPane binding
     * the left/right/up/down keys specially.
     */
/*
    protected static final List<KeyBinding> SCROLL_PANE_BINDINGS = new ArrayList<>();
    static {
        // TODO XXX DEBUGGING ONLY
        SCROLL_PANE_BINDINGS.add(new KeyBinding(F4, TRAVERSE_DEBUG).alt().ctrl().shift());

        SCROLL_PANE_BINDINGS.add(new KeyBinding(LEFT, HORIZONTAL_UNITDECREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(RIGHT, HORIZONTAL_UNITINCREMENT));

        SCROLL_PANE_BINDINGS.add(new KeyBinding(UP, VERTICAL_UNITDECREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(DOWN, VERTICAL_UNITINCREMENT));

        SCROLL_PANE_BINDINGS.add(new KeyBinding(PAGE_UP, VERTICAL_PAGEDECREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(PAGE_DOWN, VERTICAL_PAGEINCREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(SPACE, VERTICAL_PAGEINCREMENT));

        SCROLL_PANE_BINDINGS.add(new KeyBinding(HOME, VERTICAL_HOME));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(END, VERTICAL_END));
    }

    protected */
/*final*//*
 String matchActionForEvent(KeyEvent e) {
        //TODO - untested code doesn't seem to get triggered (key eaten?)
        String action = super.matchActionForEvent(e);
        if (action != null) {
            if (e.getCode() == LEFT) {
                if (getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    action = "HorizontalUnitIncrement";
                }
            } else if (e.getCode() == RIGHT) {
                if (getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    action = "HorizontalUnitDecrement";
                }
            }
        }
        return action;
    }

    @Override protected void callAction(String name) {
        switch (name) {
        case HORIZONTAL_UNITDECREMENT:
            horizontalUnitDecrement();
            break;
        case HORIZONTAL_UNITINCREMENT:
            horizontalUnitIncrement();
            break;
        case VERTICAL_UNITDECREMENT:
            verticalUnitDecrement();
            break;
        case VERTICAL_UNITINCREMENT:
            verticalUnitIncrement();
            break;
        case VERTICAL_PAGEDECREMENT:
            verticalPageDecrement();
            break;
        case VERTICAL_PAGEINCREMENT:
            verticalPageIncrement();
            break;
        case VERTICAL_HOME:
            verticalHome();
            break;
        case VERTICAL_END:
            verticalEnd();
            break;
        default :
         super.callAction(name);
            break;
        }
    }
*/

    /***************************************************************************
     *                                                                         *
     * Mouse event handling                                                    *
     *                                                                         *
     **************************************************************************/

    public void mouseClicked() {
        getControl().requestFocus();
    }

    @Override public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        getControl().requestFocus();
    }
}
