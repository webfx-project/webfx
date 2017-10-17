package naga.framework.activity.presentation.logic.impl;

import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class PresentationLogicActivityImpl<PM>
        extends PresentationLogicActivityBase<PresentationLogicActivityContextFinal<PM>, PM> {

    public PresentationLogicActivityImpl() {
    }

    public PresentationLogicActivityImpl(Factory<PM> presentationModelFactory) {
        super(presentationModelFactory);
    }
}
