package naga.core.buscall;

import naga.core.jsoncodec.JsonCodecManager;
import naga.core.jsoncodec.SerializableAsyncResult;
import naga.core.spi.bus.Message;
import naga.core.spi.platform.Platform;
import naga.core.util.async.AsyncFunction;
import naga.core.util.async.Future;
import naga.core.util.function.BiConsumer;
import naga.core.util.function.Callable;
import naga.core.util.function.Function;
import naga.core.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public class BusCallService {

    private final static String ENTRY_CALL_SERVICE_ADDRESS = "call";

    public static <T> PendingBusCall<T> call(String address, Object javaArgument) {
        // Creating a PendingBusCall that will be immediately returned to the caller
        PendingBusCall<T> pendingBusCall = new PendingBusCall<>();
        // Making the actual call by sending the (wrapped) java argument over the event bus and providing a java reply handler
        BusCallService.<BusCallResult<T>> // specifying BusCallResult<T> parameterized type for the expected as java class result
            sendJavaObjectAndWaitJavaReply( // helper method that does the job to send the (wrapped) java argument
                ENTRY_CALL_SERVICE_ADDRESS, // the addressee is the counterpart BusCallService (assuming it is listening entry calls)
                new BusCallArgument(address, javaArgument), // the java argument is wrapped into a BusCallArgument (as expected by the counterpart BusCallService)
                pendingBusCall::onBusCallResult // it just forwards the target result to the caller using the future
            );
        // We return the future immediately while we are waiting for the call result
        return pendingBusCall; // The caller can set a handler to it that will be called back later on call result reception
    }

    public static void listenEntryCalls() {
        // Registering a java reply handler that expects BusCallArgument objects
        BusCallService.<BusCallArgument, Object> // specifying BusCallArgument parameterized type for the expected as java class reply
                registerJavaHandler( // helper method that does the job to register the java reply handler
                    ENTRY_CALL_SERVICE_ADDRESS, // the address that receives the BusCallArgument objects
                    (busCallArgument, callerMessage) -> // great, a BusCallArgument has been received
                        // Forwarding the target argument to the target address (kind of local call) and waiting for the result
                        sendJavaObjectAndWaitJsonReply(busCallArgument.getTargetAddress(), busCallArgument.getJsonEncodedTargetArgument(), targetJsonReplyMessage -> {
                            // Wrapping the result into a BusCallResult and sending it back to the initial BusCallService counterpart
                            sendJavaReply(new BusCallResult(busCallArgument.getCallNumber(), targetJsonReplyMessage.body()), callerMessage);
                        }),
                    false); // not local so it is public and visible for the whole event bus (including clients)
    }


    /*********************************************************************************
     * Private implementing methods of the "java layer" on top of the json event bus *
     ********************************************************************************/

    /**
     * Method to send a java object over the event bus. The java object is first serialized into json format assuming
     * there is a json codec registered for that java class. The reply handler will be called back on reply reception.
     *
     * @param <J> The java class expected by the java reply handler
     */
    private static <J> void sendJavaObjectAndWaitJavaReply(String address, Object javaObject, Handler<J> javaReplyHandler) {
        // Transforming the java reply handler into a json reply handler that takes care of the json  reply
        Handler<Message<Object>> jsonReplyMessageHandler = javaHandlerToJsonMessageHandler(javaReplyHandler);
        // Delegating the rest of the job to sendJavaObjectAndWaitJsonReply()
        sendJavaObjectAndWaitJsonReply(address, javaObject,jsonReplyMessageHandler);
    }

    /**
     * Method to send a java object over the event bus. The java object is first serialized into json format assuming
     * there is a json codec registered for that java class. The reply handler will be called back on reply reception.
     */
    private static <T> void sendJavaObjectAndWaitJsonReply(String address, Object javaObject, Handler<Message<T>> jsonReplyMessageHandler) {
        // Serializing the java object into json format (a json object most of the time but may also be a simple string or number)
        Object jsonObject = JsonCodecManager.encodeToJson(javaObject);
        // Sending that json object over the json event bus
        Platform.bus().send(address, jsonObject, jsonReplyMessageHandler);
    }

    /**
     * Method to send a java reply over the event bus. Basically the same as the previous method but using the
     * Message.reply() method instead and no reply is expected.
     */
    private static <T> void sendJavaReply(Object javaReply, Message<T> callerMessage) {
        // Serializing the java reply into json format (a json object most of the time but may also be a simple string or number)
        Object jsonReply = JsonCodecManager.encodeToJson(javaReply);
        // Sending that json reply to the caller over the json event bus
        callerMessage.reply(jsonReply);
    }

    /**
     * Method to extract a java object from a json message (its body is supposed to be in json format).
     * The message json body is deserialized into a java object (assuming there is a json deserializer registered for that java class)
     *
     * @param <J> expected java class
     */
    private static <J> J jsonMessageToJavaObject(Message message) {
        // Getting the message body in json format
        Object jsonBody = message.body();
        // Converting it into a java object through json deserialization
        return JsonCodecManager.decodeFromJson(jsonBody);
    }

    /**
     * Method to convert a java handler Handler<J> into a json message handler Handler<Message<T>>. The resulted json
     * message handler will just call the java handler after having deserialized the json message into a java object
     *
     * @param <J> expected java class as input for the java handler
     */
    private static <J, T> Handler<Message<T>> javaHandlerToJsonMessageHandler(Handler<J> javaHandler) {
        return BusCallService.<J, T>javaHandlerToJsonMessageHandler((javaObject, ignoredJsonMessage) ->  javaHandler.handle(javaObject));
    }

    /**
     * Exactly the same but accepting a BiConsumer for the java handler and pass the json message to it (in addition to
     * the java object). In this way the java handler can send a reply to the caller.
     *
     * @param <J> expected java class as input for the java handler
     */
    private static <J, T> Handler<Message<T>> javaHandlerToJsonMessageHandler(BiConsumer<J, Message<T>> javaHandler) {
        return jsonMessage -> {
            try {
                // Getting the java object from the json message
                J javaObject = jsonMessageToJavaObject(jsonMessage); // this implicit cast may throw a ClassCastException
                // and calling the java handler with that java object
                javaHandler.accept(javaObject, jsonMessage);
            } catch (Throwable throwable) {
                throwable.printStackTrace(); // what to do?
            }
        };
    }

    /**
     * Method to register a java handler (a handler expecting java objects and not a json objects). So json objects sent
     * to this address will automatically be deserialized into the java class expected by the java handler (assuming all
     * necessary json codecs are registered to make this possible).
     *
     * @param <J> expected java class as input for the java handler
     */
    private static <J, T> void registerJavaHandler(String address, BiConsumer<J, Message<T>> javaReplyHandler, boolean local) {
        registerJsonMessageHandler(address, javaHandlerToJsonMessageHandler(javaReplyHandler), local);
    }

    /**
     * Method to register a json message handler (just delegates this to the event bus).
     */
    private static <T> void registerJsonMessageHandler(String address, Handler<Message<T>> jsonMessageHandler, boolean local) {
        if (local)
            Platform.bus().subscribeLocal(address, jsonMessageHandler);
        else
            Platform.bus().subscribe(address, jsonMessageHandler);
    }

    /***********************************************************************************************
     * Public helper methods to register java handlers and functions working with the "java layer" *
     **********************************************************************************************/

    /**
     * Method to register a java asynchronous function (which returns a Future) as a java service so it can be called
     * through the BusCallService.
     *
     * @param <A> java class of the input argument of the asynchronous function
     * @param <R> java class of the output result of the asynchronous function
     */
    public static <A, R> void registerJavaService(String address, AsyncFunction<A, R> javaAsyncFunction) {
        BusCallService.<A, R>registerJavaHandler(address, (javaArgument , callerMessage) -> {
            // Calling the java function each time a java object is received
            Future<R> future = javaAsyncFunction.apply(javaArgument); // the asynchronous java function returns a future
            // Setting a handler on this future for when the java result will be ready
            future.setHandler(javaAsyncResult -> // the java result of the asynchronous function is now ready
                    // Replying to the caller by sending this java async result to it
                    sendJavaReply(
                            // And making sure that it is serializable using SerializableAsyncResult (but assuming that javaAsyncResult.result() is serializable)
                            SerializableAsyncResult.getSerializableAsyncResult(javaAsyncResult),
                            callerMessage
                    )
            );
        }, true); // locally on the bus so it is private and visible only on this machine (not visible for clients)
    }

    /**
     * Method to register a java synchronous function as a java service so it can be called through the BusCallService.
     *
     * @param <A> java class of the input argument of the synchronous function
     * @param <R> java class of the output result of the synchronous function
     */
    public static <A, R> void registerJavaService(String address, Function<A, R> javaFunction) {
        BusCallService.<A, R>registerJavaHandler(address, (javaArgument , callerMessage) ->
                sendJavaReply(javaFunction.apply(javaArgument), callerMessage)
            , true); // locally on the bus so it is private and visible only on this machine (not visible for clients)
        }

    /**
     * Method to register a java callable (synchronous function with no input argument) as a java service so it can be called through the BusCallService.
     *
     * @param <R> java class of the output result of the callable
     */
    public static <R> void registerJavaService(String address, Callable<R> callable) {
        BusCallService.<Object, R>registerJavaHandler(address, (ignoredJavaArgument , callerMessage) ->
                        sendJavaReply(callable.call(), callerMessage)
                , true); // locally on the bus so it is private and visible only on this machine (not visible for clients)
    }


    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    public static void registerJsonCodecs() {
        BusCallArgument.registerJsonCodec();
        BusCallResult.registerJsonCodec();
        SerializableAsyncResult.registerJsonCodec();
    }
}
