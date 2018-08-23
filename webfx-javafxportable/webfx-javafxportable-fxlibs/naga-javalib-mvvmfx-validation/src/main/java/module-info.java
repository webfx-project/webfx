/**
 * @author Bruno Salmon
 */
module naga.javalib.mvvmfx.validation {
    requires static javafx.controls;
    requires naga.javalib.controlsfx.validation;

    exports de.saxsys.mvvmfx.utils.validation;
    exports de.saxsys.mvvmfx.utils.validation.visualization;
}