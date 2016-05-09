package naga.core.buscall;

import naga.core.composite.CompositeObject;
import naga.core.composite.WritableCompositeObject;
import naga.core.codec.AbstractCompositeCodec;
import naga.core.codec.CompositeCodecManager;


/*
 * @author Bruno Salmon
 */
class BusCallArgument {

    private static int SEQ = 0;

    private final String targetAddress;
    private final Object targetArgument;
    private final int callNumber;

    private Object compositeEncodedTargetArgument; // can be a CompositeObject or simply a scalar

    public BusCallArgument(String targetAddress, Object targetArgument) {
        this(targetAddress, targetArgument, ++SEQ);
    }

    private BusCallArgument(String targetAddress, Object targetArgument, int callNumber) {
        this.targetAddress = targetAddress;
        this.targetArgument = targetArgument;
        this.callNumber = callNumber;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public Object getTargetArgument() {
        return targetArgument;
    }

    public int getCallNumber() {
        return callNumber;
    }

    Object getCompositeEncodedTargetArgument() {
        if (compositeEncodedTargetArgument == null && targetArgument != null)
            compositeEncodedTargetArgument = CompositeCodecManager.encodeToComposite(targetArgument);
        return compositeEncodedTargetArgument;
    }

    /****************************************************
     *                 Composite Codec                  *
     * *************************************************/

    private static String CODEC_ID = "call";
    private static String CALL_NUMBER_KEY = "seq";
    private static String TARGET_ADDRESS_KEY = "addr";
    private static String TARGET_ARGUMENT_KEY = "arg";

    public static void registerCompositeCodec() {
        new AbstractCompositeCodec<BusCallArgument>(BusCallArgument.class, CODEC_ID) {

            @Override
            public void encodeToComposite(BusCallArgument call, WritableCompositeObject co) {
                co.set(CALL_NUMBER_KEY, call.callNumber);
                co.set(TARGET_ADDRESS_KEY, call.getTargetAddress());
                co.set(TARGET_ARGUMENT_KEY, call.getCompositeEncodedTargetArgument());
            }

            @Override
            public BusCallArgument decodeFromComposite(CompositeObject co) {
                return new BusCallArgument(
                        co.getString(TARGET_ADDRESS_KEY),
                        CompositeCodecManager.decodeFromComposite(co.get(TARGET_ARGUMENT_KEY)),
                        (int) co.getDouble(CALL_NUMBER_KEY)
                );
            }
        };
    }

}
