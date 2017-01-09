package naga.fx.sun.tk;

/**
 * TKPulseListener - Listener for those intrested in toolkit repaint pulses
 *
 */
public interface TKPulseListener {

    /**
     * This is called by the toolkit on every repaint pulse
     */
    void pulse();
}
