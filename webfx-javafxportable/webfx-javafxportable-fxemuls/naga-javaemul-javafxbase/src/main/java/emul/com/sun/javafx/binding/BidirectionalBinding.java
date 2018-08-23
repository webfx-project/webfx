/*
 * Copyright (c) 2011, 2016, Oracle and/or its affiliates. All rights reserved.
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

package emul.com.sun.javafx.binding;

import emul.javafx.beans.WeakListener;
import emul.javafx.beans.property.Property;
import emul.javafx.beans.value.ChangeListener;
import emul.javafx.beans.value.ObservableValue;
import emul.javafx.util.StringConverter;

import java.lang.ref.WeakReference;



public abstract class BidirectionalBinding<T> implements ChangeListener<T>, WeakListener {

    private static void checkParameters(Object property1, Object property2) {
        if ((property1 == null) || (property2 == null)) {
            throw new NullPointerException("Both properties must be specified.");
        }
        if (property1 == property2) {
            throw new IllegalArgumentException("Cannot bind property to itself");
        }
    }

    public static <T> BidirectionalBinding bind(Property<T> property1, Property<T> property2) {
        checkParameters(property1, property2);
        final BidirectionalBinding binding =
                /* removed */ new TypedGenericBidirectionalBinding<T>(property1, property2);
        property1.setValue(property2.getValue());
        property1.addListener(binding);
        property2.addListener(binding);
        return binding;
    }

    // removed

    public static <T> Object bind(Property<String> stringProperty, Property<T> otherProperty, StringConverter<T> converter) {
        checkParameters(stringProperty, otherProperty);
        if (converter == null) {
            throw new NullPointerException("Converter cannot be null");
        }
        final StringConversionBidirectionalBinding<T> binding = new StringConverterBidirectionalBinding<T>(stringProperty, otherProperty, converter);
        stringProperty.setValue(converter.toString(otherProperty.getValue()));
        stringProperty.addListener(binding);
        otherProperty.addListener(binding);
        return binding;
    }

    public static <T> void unbind(Property<T> property1, Property<T> property2) {
        checkParameters(property1, property2);
        final BidirectionalBinding binding = new UntypedGenericBidirectionalBinding(property1, property2);
        property1.removeListener(binding);
        property2.removeListener(binding);
    }

    public static void unbind(Object property1, Object property2) {
        checkParameters(property1, property2);
        final BidirectionalBinding binding = new UntypedGenericBidirectionalBinding(property1, property2);
        if (property1 instanceof ObservableValue) {
            ((ObservableValue) property1).removeListener(binding);
        }
        if (property2 instanceof ObservableValue) {
            ((ObservableValue) property2).removeListener(binding);
        }
    }

    // removed

    private final int cachedHashCode;

    private BidirectionalBinding(Object property1, Object property2) {
        cachedHashCode = property1.hashCode() * property2.hashCode();
    }

    protected abstract Object getProperty1();

    protected abstract Object getProperty2();

    @Override
    public int hashCode() {
        return cachedHashCode;
    }

    @Override
    public boolean wasGarbageCollected() {
        return (getProperty1() == null) || (getProperty2() == null);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        final Object propertyA1 = getProperty1();
        final Object propertyA2 = getProperty2();
        if ((propertyA1 == null) || (propertyA2 == null)) {
            return false;
        }

        if (obj instanceof BidirectionalBinding) {
            final BidirectionalBinding otherBinding = (BidirectionalBinding) obj;
            final Object propertyB1 = otherBinding.getProperty1();
            final Object propertyB2 = otherBinding.getProperty2();
            if ((propertyB1 == null) || (propertyB2 == null)) {
                return false;
            }

            if (propertyA1 == propertyB1 && propertyA2 == propertyB2) {
                return true;
            }
            if (propertyA1 == propertyB2 && propertyA2 == propertyB1) {
                return true;
            }
        }
        return false;
    }

    // removed

    private static class TypedGenericBidirectionalBinding<T> extends BidirectionalBinding<T> {
        private final WeakReference<Property<T>> propertyRef1;
        private final WeakReference<Property<T>> propertyRef2;
        private boolean updating = false;

        private TypedGenericBidirectionalBinding(Property<T> property1, Property<T> property2) {
            super(property1, property2);
            propertyRef1 = new WeakReference<Property<T>>(property1);
            propertyRef2 = new WeakReference<Property<T>>(property2);
        }

        @Override
        protected Property<T> getProperty1() {
            return propertyRef1.get();
        }

        @Override
        protected Property<T> getProperty2() {
            return propertyRef2.get();
        }

        @Override
        public void changed(ObservableValue<? extends T> sourceProperty, T oldValue, T newValue) {
            if (!updating) {
                final Property<T> property1 = propertyRef1.get();
                final Property<T> property2 = propertyRef2.get();
                if ((property1 == null) || (property2 == null)) {
                    if (property1 != null) {
                        property1.removeListener(this);
                    }
                    if (property2 != null) {
                        property2.removeListener(this);
                    }
                } else {
                    try {
                        updating = true;
                        if (property1 == sourceProperty) {
                            property2.setValue(newValue);
                        } else {
                            property1.setValue(newValue);
                        }
                    } catch (RuntimeException e) {
                        try {
                            if (property1 == sourceProperty) {
                                property1.setValue(oldValue);
                            } else {
                                property2.setValue(oldValue);
                            }
                        } catch (Exception e2) {
                            e2.addSuppressed(e);
                            unbind(property1, property2);
                            throw new RuntimeException(
                                "Bidirectional binding failed together with an attempt"
                                        + " to restore the source property to the previous value."
                                        + " Removing the bidirectional binding from properties " +
                                        property1 + " and " + property2, e2);
                        }
                        throw new RuntimeException(
                                "Bidirectional binding failed, setting to the previous value", e);
                    } finally {
                        updating = false;
                    }
                }
            }
        }
    }

    private static class TypedNumberBidirectionalBinding<T extends Number> extends BidirectionalBinding<Number> {
        private final WeakReference<Property<T>> propertyRef1;
        private final WeakReference<Property<Number>> propertyRef2;
        private boolean updating = false;

        private TypedNumberBidirectionalBinding(Property<T> property1, Property<Number> property2) {
            super(property1, property2);
            propertyRef1 = new WeakReference<Property<T>>(property1);
            propertyRef2 = new WeakReference<Property<Number>>(property2);
        }

        @Override
        protected Property<T> getProperty1() {
            return propertyRef1.get();
        }

        @Override
        protected Property<Number> getProperty2() {
            return propertyRef2.get();
        }

        @Override
        public void changed(ObservableValue<? extends Number> sourceProperty, Number oldValue, Number newValue) {
            if (!updating) {
                final Property<T> property1 = propertyRef1.get();
                final Property<Number> property2 = propertyRef2.get();
                if ((property1 == null) || (property2 == null)) {
                    if (property1 != null) {
                        property1.removeListener(this);
                    }
                    if (property2 != null) {
                        property2.removeListener(this);
                    }
                } else {
                    try {
                        updating = true;
                        if (property1 == sourceProperty) {
                            property2.setValue(newValue);
                        } else {
                            property1.setValue((T)newValue);
                        }
                    } catch (RuntimeException e) {
                        try {
                            if (property1 == sourceProperty) {
                                property1.setValue((T)oldValue);
                            } else {
                                property2.setValue(oldValue);
                            }
                        } catch (Exception e2) {
                            e2.addSuppressed(e);
                            unbind(property1, property2);
                            throw new RuntimeException(
                                "Bidirectional binding failed together with an attempt"
                                        + " to restore the source property to the previous value."
                                        + " Removing the bidirectional binding from properties " +
                                        property1 + " and " + property2, e2);
                        }
                        throw new RuntimeException(
                                        "Bidirectional binding failed, setting to the previous value", e);
                    } finally {
                        updating = false;
                    }
                }
            }
        }
    }

    private static class UntypedGenericBidirectionalBinding extends BidirectionalBinding<Object> {

        private final Object property1;
        private final Object property2;

        public UntypedGenericBidirectionalBinding(Object property1, Object property2) {
            super(property1, property2);
            this.property1 = property1;
            this.property2 = property2;
        }

        @Override
        protected Object getProperty1() {
            return property1;
        }

        @Override
        protected Object getProperty2() {
            return property2;
        }

        @Override
        public void changed(ObservableValue<? extends Object> sourceProperty, Object oldValue, Object newValue) {
            throw new RuntimeException("Should not reach here");
        }
    }

    public abstract static class StringConversionBidirectionalBinding<T> extends BidirectionalBinding<Object> {

        private final WeakReference<Property<String>> stringPropertyRef;
        private final WeakReference<Property<T>> otherPropertyRef;
        private boolean updating;

        public StringConversionBidirectionalBinding(Property<String> stringProperty, Property<T> otherProperty) {
            super(stringProperty, otherProperty);
            stringPropertyRef = new WeakReference<Property<String>>(stringProperty);
            otherPropertyRef = new WeakReference<Property<T>>(otherProperty);
        }

        protected abstract String toString(T value);

        protected abstract T fromString(String value) throws java.text.ParseException;

        @Override
        protected Object getProperty1() {
            return stringPropertyRef.get();
        }

        @Override
        protected Object getProperty2() {
            return otherPropertyRef.get();
        }

        @Override
        public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
            if (!updating) {
                final Property<String> property1 = stringPropertyRef.get();
                final Property<T> property2 = otherPropertyRef.get();
                if ((property1 == null) || (property2 == null)) {
                    if (property1 != null) {
                        property1.removeListener(this);
                    }
                    if (property2 != null) {
                        property2.removeListener(this);
                    }
                } else {
                    try {
                        updating = true;
                        if (property1 == observable) {
                            try {
                                property2.setValue(fromString(property1.getValue()));
                            } catch (Exception e) {
                                //Logging.getLogger().warning("Exception while parsing String in bidirectional binding", e);
                                property2.setValue(null);
                            }
                        } else {
                            try {
                                property1.setValue(toString(property2.getValue()));
                            } catch (Exception e) {
                                //Logging.getLogger().warning("Exception while converting Object to String in bidirectional binding", e);
                                property1.setValue("");
                            }
                        }
                    } finally {
                        updating = false;
                    }
                }
            }
        }
    }

    // removed

    private static class StringConverterBidirectionalBinding<T> extends StringConversionBidirectionalBinding<T> {

        private final StringConverter<T> converter;

        public StringConverterBidirectionalBinding(Property<String> stringProperty, Property<T> otherProperty, StringConverter<T> converter) {
            super(stringProperty, otherProperty);
            this.converter = converter;
        }

        @Override
        protected String toString(T value) {
            return converter.toString(value);
        }

        @Override
        protected T fromString(String value) throws java.text.ParseException {
            return converter.fromString(value);
        }
    }
}
