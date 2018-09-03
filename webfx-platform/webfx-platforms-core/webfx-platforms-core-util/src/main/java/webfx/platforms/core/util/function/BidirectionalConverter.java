package webfx.platforms.core.util.function;

/**
 * @author Bruno Salmon
 */
public interface BidirectionalConverter<A, B> extends Converter<A, B> {

    A convertFrom(B b);

}
