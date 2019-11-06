/*
 * Copyright (c) 2010, 2015, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.sun.javafx.scene.control.behavior;

import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.input.KeyCode.SPACE;

public class TitledPaneBehavior extends BehaviorBase<TitledPane> {

    private TitledPane titledPane;

    public TitledPaneBehavior(TitledPane pane) {
        super(pane, TITLEDPANE_BINDINGS);
        this.titledPane = pane;
    }

    /***************************************************************************
     *                                                                         *
     * Key event handling                                                      *
     *                                                                         *
     **************************************************************************/

    private static final String PRESS_ACTION = "Press";

    protected static final List<KeyBinding> TITLEDPANE_BINDINGS = new ArrayList<KeyBinding>();
    static {
        // ENTER should not be a key binding for TitledPane, as this is the
        // key reserved for the default button. See RT-40166 for more detail.
        // TITLEDPANE_BINDINGS.add(new KeyBinding(ENTER, PRESS_ACTION));
        TITLEDPANE_BINDINGS.add(new KeyBinding(SPACE, PRESS_ACTION));
    }

    @Override protected void callAction(String name) {
        switch (name) {
            case PRESS_ACTION:
                if (titledPane.isCollapsible() && titledPane.isFocused()) {
                    titledPane.setExpanded(!titledPane.isExpanded());
                    titledPane.requestFocus();
                }
                break;
            default:
                super.callAction(name);
        }
    }

    /***************************************************************************
     *                                                                         *
     * Mouse event handling                                                    *
     *                                                                         *
     **************************************************************************/

    @Override public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        TitledPane tp = getControl();
        tp.requestFocus();
    }

    /**************************************************************************
     *                         State and Functions                            *
     *************************************************************************/

    public void expand() {
        titledPane.setExpanded(true);
    }

    public void collapse() {
        titledPane.setExpanded(false);
    }

    public void toggle() {
        titledPane.setExpanded(!titledPane.isExpanded());
    }

}

