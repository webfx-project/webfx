package naga.core.composite.codec;

import naga.core.composite.*;
import naga.core.composite.buscall.BusCallService;
import naga.core.spi.sql.SqlArgument;
import naga.core.spi.sql.SqlReadResult;
import naga.core.util.Numbers;
import naga.core.util.async.Batch;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/*
 * @author Bruno Salmon
 */

public class CompositeCodecManager {

    private static final String CODEC_ID_KEY = "$codec";

    private static final Map<Class, CompositeCodec> encoders = new HashMap<>();
    private static final Map<String, CompositeCodec> decoders = new HashMap<>();

    public static  <J> void registerCompositeCodec(Class<? extends J> javaClass, CompositeCodec<J> codec) {
        encoders.put(javaClass, codec);
        decoders.put(codec.getCodecId(), codec);
    }

    /* Not supported in J2ME CLDC
    public static void useSameCompositeCodecAs(Class javaClass1, Class javaClass2) {
        registerCompositeCodec(javaClass1, getCompositeEncoder(javaClass2));
    } */

    public static <J> CompositeCodec<J> getCompositeEncoder(Class<J> javaClass) {
        return encoders.get(javaClass);
        /* getSuperclass() is not supported in J2ME CLDC
        for (Class c = javaClass; c != null; c = c.getSuperclass()) {
            CompositeCodec<J> codec = encoders.get(c);
            if (codec != null)
                return codec;
        }
        return null;
        */
    }

    public static CompositeCodec getCompositeDecoder(String codecId) {
        return decoders.get(codecId);
    }

    public static Object encodeToComposite(Object object) {
        if (object == null || object instanceof String || Numbers.isNumber(object))
            return object;
        return encodeToCompositeObject(object);
    }

    public static CompositeObject encodeToCompositeObject(Object object) {
        if (object == null)
            return null;
        if (object instanceof CompositeObject)
            return (CompositeObject) object;
        return encodeJavaObjectToCompositeObject(object, Composites.createObject());
    }

    public static WritableCompositeObject encodeJavaObjectToCompositeObject(Object javaObject, WritableCompositeObject co) {
        CompositeCodec encoder = getCompositeEncoder(javaObject.getClass());
        if (encoder == null)
            throw new IllegalArgumentException("No CompositeCodec for type: " + javaObject.getClass());
        co.set(CODEC_ID_KEY, encoder.getCodecId());
        encoder.encodeToComposite(javaObject, co);
        return co;
    }

    public static <J> J decodeFromCompositeObject(CompositeObject co) {
        if (co == null)
            return null;
        String codecId = co.getString(CODEC_ID_KEY);
        CompositeCodec<J> decoder = getCompositeDecoder(codecId);
        if (decoder == null)
            throw new IllegalArgumentException("No CompositeCodec for id: '" + codecId + "'");
        return decoder.decodeFromComposite(co);
    }

    public static <J> J decodeFromComposite(Object object) {
        if (object instanceof CompositeObject)
            return decodeFromCompositeObject((CompositeObject) object);
        return (J) object;
    }

    public static CompositeArray encodePrimitiveArrayToCompositeArray(Object[] primArray) {
        if (primArray == null)
            return null;
        WritableCompositeArray ca = Composites.createArray();
        for (Object value : primArray)
            ca.push(value);
        return ca;
    }

    public static Object[] decodePrimitiveArrayFromCompositeArray(CompositeArray ca) {
        if (ca == null)
            return null;
        int n = ca.size();
        Object[] array = new Object[n];
        for (int i = 0; i < n; i++)
            array[i] = ca.getElement(i);
        return array;
    }

    public static CompositeArray encodeToCompositeArray(Object[] array) {
        if (array == null)
            return null;
        WritableCompositeArray ca = Composites.createArray();
        for (Object object : array)
            ca.push(encodeToCompositeObject(object));
        return ca;
    }

    public static <A> A[] decodeFromCompositeArray(CompositeArray ca, Class<A> expectedClass) {
        if (ca == null)
            return null;
        int n = ca.size();
        A[] array = (A[]) Array.newInstance(expectedClass, n);
        for (int i = 0; i < n; i++)
            array[i] = decodeFromComposite(ca.getObject(i));
        return array;
    }

    /****************************************************
     *          CompositeCodec registration             *
     * *************************************************/

    // Batch Composite Serialization support

    private static String BATCH_CODEC_ID = "Batch";
    private static String BATCH_ARRAY_KEY = "array";

    static void registerBatchCompositeCodec() {
        new AbstractCompositeCodec<Batch>(Batch.class, BATCH_CODEC_ID) {

            @Override
            public void encodeToComposite(Batch batch, WritableCompositeObject co) {
                co.set(BATCH_ARRAY_KEY, encodeToCompositeArray(batch.getArray()));
            }

            @Override
            public Batch decodeFromComposite(CompositeObject co) {
                return new Batch<>(decodeFromCompositeArray(co.getArray(BATCH_ARRAY_KEY), SqlReadResult.class /* Temporary hardcoded TODO: guess the result class from the codecID */));
            }
        };
    }

    static {
        // Registering all required composite codecs (especially for network bus calls)
        BusCallService.registerCompositeCodecs();
        SqlArgument.registerCompositeCodec();
        SqlReadResult.registerCompositeCodec();
        registerBatchCompositeCodec();
    }
}
