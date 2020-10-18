package webfx.tools.buildtool;

/**
 * @author Bruno Salmon
 */
public enum Platform {

    /**************************************
     * Supported target platforms for now *
     **************************************/
      JRE       // Java Runtime Environment -> for desktop applications or servers
    , GWT       // Google Web Toolkit (Java to Javascript transpiler) -> for browser applications


    /****************************************
     * Partially supported target platforms *
     ****************************************
     , TEAVM     // Java to JavaScript or WebAssembly transpiler -> for browser applications
     /**************************************/
    , TEAVM

    /*****************************************************
     * Possible supported target platforms in the future *
     *****************************************************
    , ANDROID   // -> for android applications
    , J2CL      // Java to Closure transpiler (will replace GWT) -> for browser applications
    , J2OBJC    // Java to Objective-C transpiler -> for iOS applications
    , BYTECODER // Java to WebAssembly transpiler -> for browser applications
    /****************************************************/
}
