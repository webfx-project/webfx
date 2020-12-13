package dev.webfx.platform.shared.util.function;

/**
 * @author Bruno Salmon
 */

public interface Converter<A, B> {

    B convert(A a);

}
