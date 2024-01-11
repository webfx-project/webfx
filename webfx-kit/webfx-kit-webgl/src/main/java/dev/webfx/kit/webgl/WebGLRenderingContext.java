package dev.webfx.kit.webgl;

import dev.webfx.platform.typedarray.TypedArray;
import javafx.scene.image.Image;

/**
 * @author Bruno Salmon
 */
public interface WebGLRenderingContext {

    int ACTIVE_ATTRIBUTES = 35721;
    int ACTIVE_TEXTURE = 34016;
    int ACTIVE_UNIFORMS = 35718;
    int ALIASED_LINE_WIDTH_RANGE = 33902;
    int ALIASED_POINT_SIZE_RANGE = 33901;
    int ALPHA = 6406;
    int ALPHA_BITS = 3413;
    int ALWAYS = 519;
    int ARRAY_BUFFER = 34962;
    int ARRAY_BUFFER_BINDING = 34964;
    int ATTACHED_SHADERS = 35717;
    int BACK = 1029;
    int BLEND = 3042;
    int BLEND_COLOR = 32773;
    int BLEND_DST_ALPHA = 32970;
    int BLEND_DST_RGB = 32968;
    int BLEND_EQUATION = 32777;
    int BLEND_EQUATION_ALPHA = 34877;
    int BLEND_EQUATION_RGB = 32777;
    int BLEND_SRC_ALPHA = 32971;
    int BLEND_SRC_RGB = 32969;
    int BLUE_BITS = 3412;
    int BOOL = 35670;
    int BOOL_VEC2 = 35671;
    int BOOL_VEC3 = 35672;
    int BOOL_VEC4 = 35673;
    int BROWSER_DEFAULT_WEBGL = 37444;
    int BUFFER_SIZE = 34660;
    int BUFFER_USAGE = 34661;
    int BYTE = 5120;
    int CCW = 2305;
    int CLAMP_TO_EDGE = 33071;
    int COLOR_ATTACHMENT0 = 36064;
    int COLOR_BUFFER_BIT = 16384;
    int COLOR_CLEAR_VALUE = 3106;
    int COLOR_WRITEMASK = 3107;
    int COMPILE_STATUS = 35713;
    int COMPRESSED_TEXTURE_FORMATS = 34467;
    int CONSTANT_ALPHA = 32771;
    int CONSTANT_COLOR = 32769;
    int CONTEXT_LOST_WEBGL = 37442;
    int CULL_FACE = 2884;
    int CULL_FACE_MODE = 2885;
    int CURRENT_PROGRAM = 35725;
    int CURRENT_VERTEX_ATTRIB = 34342;
    int CW = 2304;
    int DECR = 7683;
    int DECR_WRAP = 34056;
    int DELETE_STATUS = 35712;
    int DEPTH_ATTACHMENT = 36096;
    int DEPTH_BITS = 3414;
    int DEPTH_BUFFER_BIT = 256;
    int DEPTH_CLEAR_VALUE = 2931;
    int DEPTH_COMPONENT = 6402;
    int DEPTH_COMPONENT16 = 33189;
    int DEPTH_FUNC = 2932;
    int DEPTH_RANGE = 2928;
    int DEPTH_STENCIL = 34041;
    int DEPTH_STENCIL_ATTACHMENT = 33306;
    int DEPTH_TEST = 2929;
    int DEPTH_WRITEMASK = 2930;
    int DITHER = 3024;
    int DONT_CARE = 4352;
    int DST_ALPHA = 772;
    int DST_COLOR = 774;
    int DYNAMIC_DRAW = 35048;
    int ELEMENT_ARRAY_BUFFER = 34963;
    int ELEMENT_ARRAY_BUFFER_BINDING = 34965;
    int EQUAL = 514;
    int FASTEST = 4353;
    int FLOAT = 5126;
    int FLOAT_MAT2 = 35674;
    int FLOAT_MAT3 = 35675;
    int FLOAT_MAT4 = 35676;
    int FLOAT_VEC2 = 35664;
    int FLOAT_VEC3 = 35665;
    int FLOAT_VEC4 = 35666;
    int FRAGMENT_SHADER = 35632;
    int FRAMEBUFFER = 36160;
    int FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 36049;
    int FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 36048;
    int FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 36051;
    int FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 36050;
    int FRAMEBUFFER_BINDING = 36006;
    int FRAMEBUFFER_COMPLETE = 36053;
    int FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 36054;
    int FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 36057;
    int FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 36055;
    int FRAMEBUFFER_UNSUPPORTED = 36061;
    int FRONT = 1028;
    int FRONT_AND_BACK = 1032;
    int FRONT_FACE = 2886;
    int FUNC_ADD = 32774;
    int FUNC_REVERSE_SUBTRACT = 32779;
    int FUNC_SUBTRACT = 32778;
    int GENERATE_MIPMAP_HINT = 33170;
    int GEQUAL = 518;
    int GREATER = 516;
    int GREEN_BITS = 3411;
    int HIGH_FLOAT = 36338;
    int HIGH_INT = 36341;
    int IMPLEMENTATION_COLOR_READ_FORMAT = 35739;
    int IMPLEMENTATION_COLOR_READ_TYPE = 35738;
    int INCR = 7682;
    int INCR_WRAP = 34055;
    int INT = 5124;
    int INT_VEC2 = 35667;
    int INT_VEC3 = 35668;
    int INT_VEC4 = 35669;
    int INVALID_ENUM = 1280;
    int INVALID_FRAMEBUFFER_OPERATION = 1286;
    int INVALID_OPERATION = 1282;
    int INVALID_VALUE = 1281;
    int INVERT = 5386;
    int KEEP = 7680;
    int LEQUAL = 515;
    int LESS = 513;
    int LINEAR = 9729;
    int LINEAR_MIPMAP_LINEAR = 9987;
    int LINEAR_MIPMAP_NEAREST = 9985;
    int LINES = 1;
    int LINE_LOOP = 2;
    int LINE_STRIP = 3;
    int LINE_WIDTH = 2849;
    int LINK_STATUS = 35714;
    int LOW_FLOAT = 36336;
    int LOW_INT = 36339;
    int LUMINANCE = 6409;
    int LUMINANCE_ALPHA = 6410;
    int MAX_COMBINED_TEXTURE_IMAGE_UNITS = 35661;
    int MAX_CUBE_MAP_TEXTURE_SIZE = 34076;
    int MAX_FRAGMENT_UNIFORM_VECTORS = 36349;
    int MAX_RENDERBUFFER_SIZE = 34024;
    int MAX_TEXTURE_IMAGE_UNITS = 34930;
    int MAX_TEXTURE_SIZE = 3379;
    int MAX_VARYING_VECTORS = 36348;
    int MAX_VERTEX_ATTRIBS = 34921;
    int MAX_VERTEX_TEXTURE_IMAGE_UNITS = 35660;
    int MAX_VERTEX_UNIFORM_VECTORS = 36347;
    int MAX_VIEWPORT_DIMS = 3386;
    int MEDIUM_FLOAT = 36337;
    int MEDIUM_INT = 36340;
    int MIRRORED_REPEAT = 33648;
    int NEAREST = 9728;
    int NEAREST_MIPMAP_LINEAR = 9986;
    int NEAREST_MIPMAP_NEAREST = 9984;
    int NEVER = 512;
    int NICEST = 4354;
    int NONE = 0;
    int NOTEQUAL = 517;
    int NO_ERROR = 0;
    int ONE = 1;
    int ONE_MINUS_CONSTANT_ALPHA = 32772;
    int ONE_MINUS_CONSTANT_COLOR = 32770;
    int ONE_MINUS_DST_ALPHA = 773;
    int ONE_MINUS_DST_COLOR = 775;
    int ONE_MINUS_SRC_ALPHA = 771;
    int ONE_MINUS_SRC_COLOR = 769;
    int OUT_OF_MEMORY = 1285;
    int PACK_ALIGNMENT = 3333;
    int POINTS = 0;
    int POLYGON_OFFSET_FACTOR = 32824;
    int POLYGON_OFFSET_FILL = 32823;
    int POLYGON_OFFSET_UNITS = 10752;
    int RED_BITS = 3410;
    int RENDERBUFFER = 36161;
    int RENDERBUFFER_ALPHA_SIZE = 36179;
    int RENDERBUFFER_BINDING = 36007;
    int RENDERBUFFER_BLUE_SIZE = 36178;
    int RENDERBUFFER_DEPTH_SIZE = 36180;
    int RENDERBUFFER_GREEN_SIZE = 36177;
    int RENDERBUFFER_HEIGHT = 36163;
    int RENDERBUFFER_INTERNAL_FORMAT = 36164;
    int RENDERBUFFER_RED_SIZE = 36176;
    int RENDERBUFFER_STENCIL_SIZE = 36181;
    int RENDERBUFFER_WIDTH = 36162;
    int RENDERER = 7937;
    int REPEAT = 10497;
    int REPLACE = 7681;
    int RGB = 6407;
    int RGB5_A1 = 32855;
    int RGB565 = 36194;
    int RGBA = 6408;
    int RGBA4 = 32854;
    int SAMPLER_2D = 35678;
    int SAMPLER_CUBE = 35680;
    int SAMPLES = 32937;
    int SAMPLE_ALPHA_TO_COVERAGE = 32926;
    int SAMPLE_BUFFERS = 32936;
    int SAMPLE_COVERAGE = 32928;
    int SAMPLE_COVERAGE_INVERT = 32939;
    int SAMPLE_COVERAGE_VALUE = 32938;
    int SCISSOR_BOX = 3088;
    int SCISSOR_TEST = 3089;
    int SHADER_TYPE = 35663;
    int SHADING_LANGUAGE_VERSION = 35724;
    int SHORT = 5122;
    int SRC_ALPHA = 770;
    int SRC_ALPHA_SATURATE = 776;
    int SRC_COLOR = 768;
    int STATIC_DRAW = 35044;
    int STENCIL_ATTACHMENT = 36128;
    int STENCIL_BACK_FAIL = 34817;
    int STENCIL_BACK_FUNC = 34816;
    int STENCIL_BACK_PASS_DEPTH_FAIL = 34818;
    int STENCIL_BACK_PASS_DEPTH_PASS = 34819;
    int STENCIL_BACK_REF = 36003;
    int STENCIL_BACK_VALUE_MASK = 36004;
    int STENCIL_BACK_WRITEMASK = 36005;
    int STENCIL_BITS = 3415;
    int STENCIL_BUFFER_BIT = 1024;
    int STENCIL_CLEAR_VALUE = 2961;
    int STENCIL_FAIL = 2964;
    int STENCIL_FUNC = 2962;
    int STENCIL_INDEX8 = 36168;
    int STENCIL_PASS_DEPTH_FAIL = 2965;
    int STENCIL_PASS_DEPTH_PASS = 2966;
    int STENCIL_REF = 2967;
    int STENCIL_TEST = 2960;
    int STENCIL_VALUE_MASK = 2963;
    int STENCIL_WRITEMASK = 2968;
    int STREAM_DRAW = 35040;
    int SUBPIXEL_BITS = 3408;
    int TEXTURE = 5890;
    int TEXTURE0 = 33984;
    int TEXTURE1 = 33985;
    int TEXTURE2 = 33986;
    int TEXTURE3 = 33987;
    int TEXTURE4 = 33988;
    int TEXTURE5 = 33989;
    int TEXTURE6 = 33990;
    int TEXTURE7 = 33991;
    int TEXTURE8 = 33992;
    int TEXTURE9 = 33993;
    int TEXTURE10 = 33994;
    int TEXTURE11 = 33995;
    int TEXTURE12 = 33996;
    int TEXTURE13 = 33997;
    int TEXTURE14 = 33998;
    int TEXTURE15 = 33999;
    int TEXTURE16 = 34000;
    int TEXTURE17 = 34001;
    int TEXTURE18 = 34002;
    int TEXTURE19 = 34003;
    int TEXTURE20 = 34004;
    int TEXTURE21 = 34005;
    int TEXTURE22 = 34006;
    int TEXTURE23 = 34007;
    int TEXTURE24 = 34008;
    int TEXTURE25 = 34009;
    int TEXTURE26 = 34010;
    int TEXTURE27 = 34011;
    int TEXTURE28 = 34012;
    int TEXTURE29 = 34013;
    int TEXTURE30 = 34014;
    int TEXTURE31 = 34015;
    int TEXTURE_2D = 3553;
    int TEXTURE_BINDING_2D = 32873;
    int TEXTURE_BINDING_CUBE_MAP = 34068;
    int TEXTURE_CUBE_MAP = 34067;
    int TEXTURE_CUBE_MAP_NEGATIVE_X = 34070;
    int TEXTURE_CUBE_MAP_NEGATIVE_Y = 34072;
    int TEXTURE_CUBE_MAP_NEGATIVE_Z = 34074;
    int TEXTURE_CUBE_MAP_POSITIVE_X = 34069;
    int TEXTURE_CUBE_MAP_POSITIVE_Y = 34071;
    int TEXTURE_CUBE_MAP_POSITIVE_Z = 34073;
    int TEXTURE_MAG_FILTER = 10240;
    int TEXTURE_MIN_FILTER = 10241;
    int TEXTURE_WRAP_S = 10242;
    int TEXTURE_WRAP_T = 10243;
    int TRIANGLES = 4;
    int TRIANGLE_FAN = 6;
    int TRIANGLE_STRIP = 5;
    int UNPACK_ALIGNMENT = 3317;
    int UNPACK_COLORSPACE_CONVERSION_WEBGL = 37443;
    int UNPACK_FLIP_Y_WEBGL = 37440;
    int UNPACK_PREMULTIPLY_ALPHA_WEBGL = 37441;
    int UNSIGNED_BYTE = 5121;
    int UNSIGNED_INT = 5125;
    int UNSIGNED_SHORT = 5123;
    int UNSIGNED_SHORT_4_4_4_4 = 32819;
    int UNSIGNED_SHORT_5_5_5_1 = 32820;
    int UNSIGNED_SHORT_5_6_5 = 33635;
    int VALIDATE_STATUS = 35715;
    int VENDOR = 7936;
    int VERSION = 7938;
    int VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 34975;
    int VERTEX_ATTRIB_ARRAY_ENABLED = 34338;
    int VERTEX_ATTRIB_ARRAY_NORMALIZED = 34922;
    int VERTEX_ATTRIB_ARRAY_POINTER = 34373;
    int VERTEX_ATTRIB_ARRAY_SIZE = 34339;
    int VERTEX_ATTRIB_ARRAY_STRIDE = 34340;
    int VERTEX_ATTRIB_ARRAY_TYPE = 34341;
    int VERTEX_SHADER = 35633;
    int VIEWPORT = 2978;
    int ZERO = 0;

    void activeTexture(int texture);
    void attachShader(WebGLProgram program, WebGLShader shader);
/*
    void bindAttribLocation(WebGLProgram program, int index, String name);
*/
    void bindBuffer(int target, WebGLBuffer buffer);
/*
    void bindFramebuffer(int target, WebGLFramebuffer buffer);

    void bindRenderbuffer(int target, WebGLRenderbuffer buffer);
*/
    void bindTexture(int target, WebGLTexture texture);
/*
    void blendColor(double red, double green, double blue, double alpha);

    void blendEquation(int mode);

    void blendEquationSeparate(int modeRGB, int modeAlpha);

    void blendFunc(int sfactor, int dfactor);

    void blendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha);
*/
    void bufferData(int target, TypedArray data, int usage); /*{
        bufferData(
                target, Js.<WebGLRenderingContext.BufferDataDataUnionType>uncheckedCast(data), usage);
    }*/
/*
    default void bufferData(int target, ArrayBufferView data, int usage) {
        bufferData(
                target, Js.<WebGLRenderingContext.BufferDataDataUnionType>uncheckedCast(data), usage);
    }

    void bufferData(
            int target, WebGLRenderingContext.BufferDataDataUnionType data, int usage);

    default void bufferData(int target, double data, int usage) {
        bufferData(
                target, Js.<WebGLRenderingContext.BufferDataDataUnionType>uncheckedCast(data), usage);
    }

    default void bufferSubData(int target, double offset, ArrayBuffer data) {
        bufferSubData(
                target, offset, Js.<WebGLRenderingContext.BufferSubDataDataUnionType>uncheckedCast(data));
    }

    default void bufferSubData(int target, double offset, ArrayBufferView data) {
        bufferSubData(
                target, offset, Js.<WebGLRenderingContext.BufferSubDataDataUnionType>uncheckedCast(data));
    }

    void bufferSubData(
            int target, double offset, WebGLRenderingContext.BufferSubDataDataUnionType data);

    int checkFramebufferStatus(int target);
*/
    void clear(int mask);

    void clearColor(double red, double green, double blue, double alpha);
    void clearDepth(double depth);
    /*

        void clearStencil(int s);

        void colorMask(boolean red, boolean green, boolean blue, boolean alpha);
    */
    void compileShader(WebGLShader shader);
/*
    void compressedTexImage2D(
            int target,
            int level,
            int internalformat,
            int width,
            int height,
            int border,
            ArrayBufferView data);

    void compressedTexSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int width,
            int height,
            int format,
            ArrayBufferView data);

    void copyTexImage2D(
            int target, int level, int format, int x, int y, int width, int height, int border);

    void copyTexSubImage2D(
            int target, int level, int xoffset, int yoffset, int x, int y, int width, int height);
*/
    WebGLBuffer createBuffer();
/*
    WebGLFramebuffer createFramebuffer();
*/
    WebGLProgram createProgram();
/*
    WebGLRenderbuffer createRenderbuffer();
*/
    WebGLShader createShader(int type);

    WebGLTexture createTexture();
/*
    void cullFace(int mode);

    void deleteBuffer(WebGLBuffer buffer);

    void deleteFramebuffer(WebGLFramebuffer buffer);

    void deleteProgram(WebGLProgram program);

    void deleteRenderbuffer(WebGLRenderbuffer buffer);
*/
    void deleteShader(WebGLShader shader);
/*
    void deleteTexture(WebGLTexture texture);
*/
    void depthFunc(int func);
/*
    void depthMask(boolean flag);

    void depthRange(double nearVal, double farVal);

    void detachShader(WebGLProgram program, WebGLShader shader);

    void disable(int flags);

    void disableVertexAttribArray(int index);
*/
    void drawArrays(int mode, int first, int count);
    void drawElements(int mode, int count, int type, double offset);
    void enable(int cap);
    void enableVertexAttribArray(int index);
    /*

        Object finish();

        Object flush();

        void framebufferRenderbuffer(
                int target, int attachment, int renderbuffertarget, WebGLRenderbuffer renderbuffer);

        void framebufferTexture2D(
                int target, int attachment, int textarget, WebGLTexture texture, int level);

        void frontFace(int mode);
*/
        void generateMipmap(int target);
/*
        WebGLActiveInfo getActiveAttrib(WebGLProgram program, int index);

        WebGLActiveInfo getActiveUniform(WebGLProgram program, int index);

        JsArray<WebGLShader> getAttachedShaders(WebGLProgram program);
    */
    int getAttribLocation(WebGLProgram program, String name);
/*
    Object getBufferParameter(int target, int pname);

    WebGLContextAttributes getContextAttributes();

    int getError();

    JsObject getExtension(String name);

    Object getFramebufferAttachmentParameter(int target, int attachment, int pname);

    Object getParameter(int pname);
*/
    String getProgramInfoLog(WebGLProgram program);

    Object getProgramParameter(WebGLProgram program, int pname);
/*
    Object getRenderbufferParameter(int target, int pname);

*/
    String getShaderInfoLog(WebGLShader shader);
    Object getShaderParameter(WebGLShader shader, int pname);
/*
    WebGLShaderPrecisionFormat getShaderPrecisionFormat(
            int shadertype, int precisiontype);

    String getShaderSource(WebGLShader shader);

    JsArray<String> getSupportedExtensions();

    Object getTexParameter(int target, int pname);

    Object getUniform(WebGLProgram program, WebGLUniformLocation location);
*/
    WebGLUniformLocation getUniformLocation(WebGLProgram program, String name);
/*
    Object getVertexAttrib(int index, int pname);

    double getVertexAttribOffset(int index, int pname);

    void hint(int target, int mode);

    boolean isBuffer(WebGLObject buffer);

    boolean isContextLost();

    boolean isEnabled(int cap);

    boolean isFramebuffer(WebGLObject framebuffer);

    boolean isProgram(WebGLObject program);

    boolean isRenderbuffer(WebGLObject renderbuffer);

    boolean isShader(WebGLObject shader);

    boolean isTexture(WebGLObject texture);

    void lineWidth(double width);
*/
    void linkProgram(WebGLProgram program);
/*
    void pixelStorei(int pname, WebGLRenderingContext.PixelStoreiParamUnionType param);
*/
    void pixelStorei(int pname, boolean param);/* {
        pixelStorei(pname, Js.<WebGLRenderingContext.PixelStoreiParamUnionType>uncheckedCast(param));
    }*/
/*
    default void pixelStorei(int pname, int param) {
        pixelStorei(pname, Js.<WebGLRenderingContext.PixelStoreiParamUnionType>uncheckedCast(param));
    }

    void polygonOffset(double factor, double units);

    void readPixels(
            int x, int y, int width, int height, int format, int type, ArrayBufferView pixels);

    void renderbufferStorage(int target, int internalformat, int width, int height);

    void sampleCoverage(double coverage, boolean invert);

    void scissor(int x, int y, int width, int height);
*/
    void shaderSource(WebGLShader shader, String source);
/*
    void stencilFunc(int func, int ref, int mask);

    void stencilFuncSeparate(int face, int func, int ref, int mask);

    void stencilMask(int mask);

    void stencilMaskSeparate(int face, int mask);

    void stencilOp(int fail, int zfail, int zpass);

    void stencilOpSeparate(int face, int fail, int zfail, int zpass);

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            HTMLCanvasElement img,
            int format0,
            int type0,
            ArrayBufferView pixels) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0,
                pixels);
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            HTMLCanvasElement img,
            int format0,
            int type0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0);
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            HTMLCanvasElement img,
            int format0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0);
    }
*/
    void texImage2D(
            int target, int level, int internalformat, int format, int type, Image img); /* {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img));
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            HTMLImageElement img,
            int format0,
            int type0,
            ArrayBufferView pixels) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0,
                pixels);
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            HTMLImageElement img,
            int format0,
            int type0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0);
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            HTMLImageElement img,
            int format0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0);
    }

    default void texImage2D(
            int target, int level, int internalformat, int format, int type, HTMLImageElement img) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img));
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            HTMLVideoElement img,
            int format0,
            int type0,
            ArrayBufferView pixels) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0,
                pixels);
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            HTMLVideoElement img,
            int format0,
            int type0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0);
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            HTMLVideoElement img,
            int format0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0);
    }
*/
    void texImage2D(
            int target, int level, int internalformat, int format, int type, javafx.scene.media.MediaView img);/* {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img));
    }*/
/*
    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            ImageBitmap img,
            int format0,
            int type0,
            ArrayBufferView pixels) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0,
                pixels);
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            ImageBitmap img,
            int format0,
            int type0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0);
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            ImageBitmap img,
            int format0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0);
    }

    default void texImage2D(
            int target, int level, int internalformat, int format, int type, ImageBitmap img) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img));
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            ImageData img,
            int format0,
            int type0,
            ArrayBufferView pixels) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0,
                pixels);
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            ImageData img,
            int format0,
            int type0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0);
    }

    default void texImage2D(
            int target, int level, int internalformat, int format, int type, ImageData img, int format0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0);
    }

    default void texImage2D(
            int target, int level, int internalformat, int format, int type, ImageData img) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img));
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            OffscreenCanvas img,
            int format0,
            int type0,
            ArrayBufferView pixels) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0,
                pixels);
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            OffscreenCanvas img,
            int format0,
            int type0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0);
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            OffscreenCanvas img,
            int format0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0);
    }

    default void texImage2D(
            int target, int level, int internalformat, int format, int type, OffscreenCanvas img) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img));
    }

    void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            WebGLRenderingContext.TexImage2DImgUnionType img,
            int format0,
            int type0,
            ArrayBufferView pixels);

    void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            WebGLRenderingContext.TexImage2DImgUnionType img,
            int format0,
            int type0);

    void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            WebGLRenderingContext.TexImage2DImgUnionType img,
            int format0);

    void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            WebGLRenderingContext.TexImage2DImgUnionType img);
*/
    void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            int img,
            int format0,
            int type0,
            TypedArray pixels);/* {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0,
                pixels);
    }

    default void texImage2D(
            int target,
            int level,
            int internalformat,
            int format,
            int type,
            int img,
            int format0,
            int type0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0,
                type0);
    }

    default void texImage2D(
            int target, int level, int internalformat, int format, int type, int img, int format0) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img),
                format0);
    }

    default void texImage2D(
            int target, int level, int internalformat, int format, int type, int img) {
        texImage2D(
                target,
                level,
                internalformat,
                format,
                type,
                Js.<WebGLRenderingContext.TexImage2DImgUnionType>uncheckedCast(img));
    }

    void texParameterf(int target, int pname, double param);
*/
    void texParameteri(int target, int pname, int param);
/*
    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            HTMLCanvasElement data,
            int type0,
            ArrayBufferView pixels) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0,
                pixels);
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            HTMLCanvasElement data,
            int type0) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0);
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            HTMLCanvasElement data) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data));
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            HTMLImageElement data,
            int type0,
            ArrayBufferView pixels) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0,
                pixels);
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            HTMLImageElement data,
            int type0) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0);
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            HTMLImageElement data) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data));
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            HTMLVideoElement data,
            int type0,
            ArrayBufferView pixels) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0,
                pixels);
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            HTMLVideoElement data,
            int type0) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0);
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            HTMLVideoElement data) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data));
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            ImageBitmap data,
            int type0,
            ArrayBufferView pixels) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0,
                pixels);
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            ImageBitmap data,
            int type0) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0);
    }

    default void texSubImage2D(
            int target, int level, int xoffset, int yoffset, int format, int type, ImageBitmap data) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data));
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            ImageData data,
            int type0,
            ArrayBufferView pixels) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0,
                pixels);
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            ImageData data,
            int type0) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0);
    }

    default void texSubImage2D(
            int target, int level, int xoffset, int yoffset, int format, int type, ImageData data) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data));
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            OffscreenCanvas data,
            int type0,
            ArrayBufferView pixels) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0,
                pixels);
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            OffscreenCanvas data,
            int type0) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0);
    }

    default void texSubImage2D(
            int target, int level, int xoffset, int yoffset, int format, int type, OffscreenCanvas data) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data));
    }

    void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            WebGLRenderingContext.TexSubImage2DDataUnionType data,
            int type0,
            ArrayBufferView pixels);

    void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            WebGLRenderingContext.TexSubImage2DDataUnionType data,
            int type0);

    void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            WebGLRenderingContext.TexSubImage2DDataUnionType data);

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            double data,
            int type0,
            ArrayBufferView pixels) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0,
                pixels);
    }

    default void texSubImage2D(
            int target,
            int level,
            int xoffset,
            int yoffset,
            int format,
            int type,
            double data,
            int type0) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data),
                type0);
    }

    default void texSubImage2D(
            int target, int level, int xoffset, int yoffset, int format, int type, double data) {
        texSubImage2D(
                target,
                level,
                xoffset,
                yoffset,
                format,
                type,
                Js.<WebGLRenderingContext.TexSubImage2DDataUnionType>uncheckedCast(data));
    }

    void uniform1f(WebGLUniformLocation location, double value);

    default void uniform1fv(WebGLUniformLocation location, Float32Array value) {
        uniform1fv(location, Js.<WebGLRenderingContext.Uniform1fvValueUnionType>uncheckedCast(value));
    }

    default void uniform1fv(WebGLUniformLocation location, JsArray<Double> value) {
        uniform1fv(location, Js.<WebGLRenderingContext.Uniform1fvValueUnionType>uncheckedCast(value));
    }

    void uniform1fv(
            WebGLUniformLocation location, WebGLRenderingContext.Uniform1fvValueUnionType value);

    default void uniform1fv(WebGLUniformLocation location, double[] value) {
        uniform1fv(location, Js.<JsArray<Double>>uncheckedCast(value));
    }

    void uniform1i(
            WebGLUniformLocation location, WebGLRenderingContext.Uniform1iValueUnionType value);

    default void uniform1i(WebGLUniformLocation location, boolean value) {
        uniform1i(location, Js.<WebGLRenderingContext.Uniform1iValueUnionType>uncheckedCast(value));
    }
*/
    void uniform1i(WebGLUniformLocation location, int value);/* {
        uniform1i(location, Js.<WebGLRenderingContext.Uniform1iValueUnionType>uncheckedCast(value));
    }*/
/*
    default void uniform1iv(WebGLUniformLocation location, Int32Array value) {
        uniform1iv(location, Js.<WebGLRenderingContext.Uniform1ivValueUnionType>uncheckedCast(value));
    }

    default void uniform1iv(WebGLUniformLocation location, JsArray<Object> value) {
        uniform1iv(location, Js.<WebGLRenderingContext.Uniform1ivValueUnionType>uncheckedCast(value));
    }

    default void uniform1iv(WebGLUniformLocation location, Object[] value) {
        uniform1iv(location, Js.<JsArray<Object>>uncheckedCast(value));
    }

    void uniform1iv(
            WebGLUniformLocation location, WebGLRenderingContext.Uniform1ivValueUnionType value);

    void uniform2f(WebGLUniformLocation location, double value1, double value2);

    default void uniform2fv(WebGLUniformLocation location, Float32Array value) {
        uniform2fv(location, Js.<WebGLRenderingContext.Uniform2fvValueUnionType>uncheckedCast(value));
    }

    default void uniform2fv(WebGLUniformLocation location, JsArray<Double> value) {
        uniform2fv(location, Js.<WebGLRenderingContext.Uniform2fvValueUnionType>uncheckedCast(value));
    }

    void uniform2fv(
            WebGLUniformLocation location, WebGLRenderingContext.Uniform2fvValueUnionType value);

    default void uniform2fv(WebGLUniformLocation location, double[] value) {
        uniform2fv(location, Js.<JsArray<Double>>uncheckedCast(value));
    }

    void uniform2i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform2iValue1UnionType value1,
            WebGLRenderingContext.Uniform2iValue2UnionType value2);

    default void uniform2i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform2iValue1UnionType value1,
            boolean value2) {
        uniform2i(
                location, value1, Js.<WebGLRenderingContext.Uniform2iValue2UnionType>uncheckedCast(value2));
    }

    default void uniform2i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform2iValue1UnionType value1,
            int value2) {
        uniform2i(
                location, value1, Js.<WebGLRenderingContext.Uniform2iValue2UnionType>uncheckedCast(value2));
    }

    default void uniform2i(
            WebGLUniformLocation location,
            boolean value1,
            WebGLRenderingContext.Uniform2iValue2UnionType value2) {
        uniform2i(
                location, Js.<WebGLRenderingContext.Uniform2iValue1UnionType>uncheckedCast(value1), value2);
    }

    default void uniform2i(WebGLUniformLocation location, boolean value1, boolean value2) {
        uniform2i(
                location,
                Js.<WebGLRenderingContext.Uniform2iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform2iValue2UnionType>uncheckedCast(value2));
    }

    default void uniform2i(WebGLUniformLocation location, boolean value1, int value2) {
        uniform2i(
                location,
                Js.<WebGLRenderingContext.Uniform2iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform2iValue2UnionType>uncheckedCast(value2));
    }

    default void uniform2i(
            WebGLUniformLocation location,
            int value1,
            WebGLRenderingContext.Uniform2iValue2UnionType value2) {
        uniform2i(
                location, Js.<WebGLRenderingContext.Uniform2iValue1UnionType>uncheckedCast(value1), value2);
    }

    default void uniform2i(WebGLUniformLocation location, int value1, boolean value2) {
        uniform2i(
                location,
                Js.<WebGLRenderingContext.Uniform2iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform2iValue2UnionType>uncheckedCast(value2));
    }

    default void uniform2i(WebGLUniformLocation location, int value1, int value2) {
        uniform2i(
                location,
                Js.<WebGLRenderingContext.Uniform2iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform2iValue2UnionType>uncheckedCast(value2));
    }

    default void uniform2iv(WebGLUniformLocation location, Int32Array value) {
        uniform2iv(location, Js.<WebGLRenderingContext.Uniform2ivValueUnionType>uncheckedCast(value));
    }

    default void uniform2iv(WebGLUniformLocation location, JsArray<Object> value) {
        uniform2iv(location, Js.<WebGLRenderingContext.Uniform2ivValueUnionType>uncheckedCast(value));
    }

    default void uniform2iv(WebGLUniformLocation location, Object[] value) {
        uniform2iv(location, Js.<JsArray<Object>>uncheckedCast(value));
    }

    void uniform2iv(
            WebGLUniformLocation location, WebGLRenderingContext.Uniform2ivValueUnionType value);

    void uniform3f(
            WebGLUniformLocation location, double value1, double value2, double value3);

    default void uniform3fv(WebGLUniformLocation location, Float32Array value) {
        uniform3fv(location, Js.<WebGLRenderingContext.Uniform3fvValueUnionType>uncheckedCast(value));
    }

    default void uniform3fv(WebGLUniformLocation location, JsArray<Double> value) {
        uniform3fv(location, Js.<WebGLRenderingContext.Uniform3fvValueUnionType>uncheckedCast(value));
    }

    void uniform3fv(
            WebGLUniformLocation location, WebGLRenderingContext.Uniform3fvValueUnionType value);

    default void uniform3fv(WebGLUniformLocation location, double[] value) {
        uniform3fv(location, Js.<JsArray<Double>>uncheckedCast(value));
    }

    void uniform3i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform3iValue1UnionType value1,
            WebGLRenderingContext.Uniform3iValue2UnionType value2,
            WebGLRenderingContext.Uniform3iValue3UnionType value3);

    default void uniform3i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform3iValue1UnionType value1,
            WebGLRenderingContext.Uniform3iValue2UnionType value2,
            boolean value3) {
        uniform3i(
                location,
                value1,
                value2,
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform3iValue1UnionType value1,
            WebGLRenderingContext.Uniform3iValue2UnionType value2,
            int value3) {
        uniform3i(
                location,
                value1,
                value2,
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform3iValue1UnionType value1,
            boolean value2,
            WebGLRenderingContext.Uniform3iValue3UnionType value3) {
        uniform3i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                value3);
    }

    default void uniform3i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform3iValue1UnionType value1,
            boolean value2,
            boolean value3) {
        uniform3i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform3iValue1UnionType value1,
            boolean value2,
            int value3) {
        uniform3i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform3iValue1UnionType value1,
            int value2,
            WebGLRenderingContext.Uniform3iValue3UnionType value3) {
        uniform3i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                value3);
    }

    default void uniform3i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform3iValue1UnionType value1,
            int value2,
            boolean value3) {
        uniform3i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform3iValue1UnionType value1,
            int value2,
            int value3) {
        uniform3i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location,
            boolean value1,
            WebGLRenderingContext.Uniform3iValue2UnionType value2,
            WebGLRenderingContext.Uniform3iValue3UnionType value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                value2,
                value3);
    }

    default void uniform3i(
            WebGLUniformLocation location,
            boolean value1,
            WebGLRenderingContext.Uniform3iValue2UnionType value2,
            boolean value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location,
            boolean value1,
            WebGLRenderingContext.Uniform3iValue2UnionType value2,
            int value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location,
            boolean value1,
            boolean value2,
            WebGLRenderingContext.Uniform3iValue3UnionType value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                value3);
    }

    default void uniform3i(
            WebGLUniformLocation location, boolean value1, boolean value2, boolean value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location, boolean value1, boolean value2, int value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location,
            boolean value1,
            int value2,
            WebGLRenderingContext.Uniform3iValue3UnionType value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                value3);
    }

    default void uniform3i(
            WebGLUniformLocation location, boolean value1, int value2, boolean value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location, boolean value1, int value2, int value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location,
            int value1,
            WebGLRenderingContext.Uniform3iValue2UnionType value2,
            WebGLRenderingContext.Uniform3iValue3UnionType value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                value2,
                value3);
    }

    default void uniform3i(
            WebGLUniformLocation location,
            int value1,
            WebGLRenderingContext.Uniform3iValue2UnionType value2,
            boolean value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location,
            int value1,
            WebGLRenderingContext.Uniform3iValue2UnionType value2,
            int value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location,
            int value1,
            boolean value2,
            WebGLRenderingContext.Uniform3iValue3UnionType value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                value3);
    }

    default void uniform3i(
            WebGLUniformLocation location, int value1, boolean value2, boolean value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location, int value1, boolean value2, int value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(
            WebGLUniformLocation location,
            int value1,
            int value2,
            WebGLRenderingContext.Uniform3iValue3UnionType value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                value3);
    }

    default void uniform3i(
            WebGLUniformLocation location, int value1, int value2, boolean value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3i(WebGLUniformLocation location, int value1, int value2, int value3) {
        uniform3i(
                location,
                Js.<WebGLRenderingContext.Uniform3iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform3iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform3iValue3UnionType>uncheckedCast(value3));
    }

    default void uniform3iv(WebGLUniformLocation location, Int32Array value) {
        uniform3iv(location, Js.<WebGLRenderingContext.Uniform3ivValueUnionType>uncheckedCast(value));
    }

    default void uniform3iv(WebGLUniformLocation location, JsArray<Object> value) {
        uniform3iv(location, Js.<WebGLRenderingContext.Uniform3ivValueUnionType>uncheckedCast(value));
    }

    default void uniform3iv(WebGLUniformLocation location, Object[] value) {
        uniform3iv(location, Js.<JsArray<Object>>uncheckedCast(value));
    }

    void uniform3iv(
            WebGLUniformLocation location, WebGLRenderingContext.Uniform3ivValueUnionType value);

    void uniform4f(
            WebGLUniformLocation location, double value1, double value2, double value3, double value4);

    default void uniform4fv(WebGLUniformLocation location, Float32Array value) {
        uniform4fv(location, Js.<WebGLRenderingContext.Uniform4fvValueUnionType>uncheckedCast(value));
    }

    default void uniform4fv(WebGLUniformLocation location, JsArray<Double> value) {
        uniform4fv(location, Js.<WebGLRenderingContext.Uniform4fvValueUnionType>uncheckedCast(value));
    }

    void uniform4fv(
            WebGLUniformLocation location, WebGLRenderingContext.Uniform4fvValueUnionType value);

    default void uniform4fv(WebGLUniformLocation location, double[] value) {
        uniform4fv(location, Js.<JsArray<Double>>uncheckedCast(value));
    }

    void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4);

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            boolean value4) {
        uniform4i(
                location,
                value1,
                value2,
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            int value4) {
        uniform4i(
                location,
                value1,
                value2,
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            boolean value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                value1,
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            boolean value3,
            boolean value4) {
        uniform4i(
                location,
                value1,
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            boolean value3,
            int value4) {
        uniform4i(
                location,
                value1,
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            int value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                value1,
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            int value3,
            boolean value4) {
        uniform4i(
                location,
                value1,
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            int value3,
            int value4) {
        uniform4i(
                location,
                value1,
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            boolean value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            boolean value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            boolean value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            boolean value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            int value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            boolean value2,
            boolean value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            boolean value2,
            boolean value3,
            boolean value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            boolean value2,
            boolean value3,
            int value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            boolean value2,
            int value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            boolean value2,
            int value3,
            boolean value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            boolean value2,
            int value3,
            int value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            int value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            int value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            boolean value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            int value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            int value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            int value2,
            boolean value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            int value2,
            boolean value3,
            boolean value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            int value2,
            boolean value3,
            int value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            int value2,
            int value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            int value2,
            int value3,
            boolean value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            WebGLRenderingContext.Uniform4iValue1UnionType value1,
            int value2,
            int value3,
            int value4) {
        uniform4i(
                location,
                value1,
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                value3,
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            boolean value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            boolean value3,
            boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            boolean value3,
            int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            int value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            int value3,
            boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            int value3,
            int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            boolean value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            boolean value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            boolean value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            boolean value2,
            boolean value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            boolean value2,
            boolean value3,
            boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location, boolean value1, boolean value2, boolean value3, int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            boolean value2,
            int value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location, boolean value1, boolean value2, int value3, boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location, boolean value1, boolean value2, int value3, int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            int value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            int value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            int value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            int value2,
            boolean value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location, boolean value1, int value2, boolean value3, boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location, boolean value1, int value2, boolean value3, int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            boolean value1,
            int value2,
            int value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location, boolean value1, int value2, int value3, boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location, boolean value1, int value2, int value3, int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                value3,
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            boolean value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            boolean value3,
            boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            boolean value3,
            int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            int value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            int value3,
            boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            WebGLRenderingContext.Uniform4iValue2UnionType value2,
            int value3,
            int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                value2,
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            boolean value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            boolean value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            boolean value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            boolean value2,
            boolean value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location, int value1, boolean value2, boolean value3, boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location, int value1, boolean value2, boolean value3, int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            boolean value2,
            int value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location, int value1, boolean value2, int value3, boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location, int value1, boolean value2, int value3, int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            int value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            int value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            int value2,
            WebGLRenderingContext.Uniform4iValue3UnionType value3,
            int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                value3,
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            int value2,
            boolean value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location, int value1, int value2, boolean value3, boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location, int value1, int value2, boolean value3, int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location,
            int value1,
            int value2,
            int value3,
            WebGLRenderingContext.Uniform4iValue4UnionType value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                value4);
    }

    default void uniform4i(
            WebGLUniformLocation location, int value1, int value2, int value3, boolean value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4i(
            WebGLUniformLocation location, int value1, int value2, int value3, int value4) {
        uniform4i(
                location,
                Js.<WebGLRenderingContext.Uniform4iValue1UnionType>uncheckedCast(value1),
                Js.<WebGLRenderingContext.Uniform4iValue2UnionType>uncheckedCast(value2),
                Js.<WebGLRenderingContext.Uniform4iValue3UnionType>uncheckedCast(value3),
                Js.<WebGLRenderingContext.Uniform4iValue4UnionType>uncheckedCast(value4));
    }

    default void uniform4iv(WebGLUniformLocation location, Int32Array value) {
        uniform4iv(location, Js.<WebGLRenderingContext.Uniform4ivValueUnionType>uncheckedCast(value));
    }

    default void uniform4iv(WebGLUniformLocation location, JsArray<Object> value) {
        uniform4iv(location, Js.<WebGLRenderingContext.Uniform4ivValueUnionType>uncheckedCast(value));
    }

    default void uniform4iv(WebGLUniformLocation location, Object[] value) {
        uniform4iv(location, Js.<JsArray<Object>>uncheckedCast(value));
    }

    void uniform4iv(
            WebGLUniformLocation location, WebGLRenderingContext.Uniform4ivValueUnionType value);

    default void uniformMatrix2fv(
            WebGLUniformLocation location, boolean transpose, Float32Array data) {
        uniformMatrix2fv(
                location,
                transpose,
                Js.<WebGLRenderingContext.UniformMatrix2fvDataUnionType>uncheckedCast(data));
    }

    default void uniformMatrix2fv(
            WebGLUniformLocation location, boolean transpose, JsArray<Double> data) {
        uniformMatrix2fv(
                location,
                transpose,
                Js.<WebGLRenderingContext.UniformMatrix2fvDataUnionType>uncheckedCast(data));
    }

    void uniformMatrix2fv(
            WebGLUniformLocation location,
            boolean transpose,
            WebGLRenderingContext.UniformMatrix2fvDataUnionType data);

    default void uniformMatrix2fv(
            WebGLUniformLocation location, boolean transpose, double[] data) {
        uniformMatrix2fv(location, transpose, Js.<JsArray<Double>>uncheckedCast(data));
    }

    default void uniformMatrix3fv(
            WebGLUniformLocation location, boolean transpose, Float32Array data) {
        uniformMatrix3fv(
                location,
                transpose,
                Js.<WebGLRenderingContext.UniformMatrix3fvDataUnionType>uncheckedCast(data));
    }

    default void uniformMatrix3fv(
            WebGLUniformLocation location, boolean transpose, JsArray<Double> data) {
        uniformMatrix3fv(
                location,
                transpose,
                Js.<WebGLRenderingContext.UniformMatrix3fvDataUnionType>uncheckedCast(data));
    }

    void uniformMatrix3fv(
            WebGLUniformLocation location,
            boolean transpose,
            WebGLRenderingContext.UniformMatrix3fvDataUnionType data);

    default void uniformMatrix3fv(
            WebGLUniformLocation location, boolean transpose, double[] data) {
        uniformMatrix3fv(location, transpose, Js.<JsArray<Double>>uncheckedCast(data));
    }

    default void uniformMatrix4fv(
            WebGLUniformLocation location, boolean transpose, Float32Array data) {
        uniformMatrix4fv(
                location,
                transpose,
                Js.<WebGLRenderingContext.UniformMatrix4fvDataUnionType>uncheckedCast(data));
    }

    default void uniformMatrix4fv(
            WebGLUniformLocation location, boolean transpose, JsArray<Double> data) {
        uniformMatrix4fv(
                location,
                transpose,
                Js.<WebGLRenderingContext.UniformMatrix4fvDataUnionType>uncheckedCast(data));
    }

    void uniformMatrix4fv(
            WebGLUniformLocation location,
            boolean transpose,
            WebGLRenderingContext.UniformMatrix4fvDataUnionType data);
*/
    void uniformMatrix4fv(
            WebGLUniformLocation location, boolean transpose, double[] data); /* {
        uniformMatrix4fv(location, transpose, Js.<JsArray<Double>>uncheckedCast(data));
    }*/

    void useProgram(WebGLProgram program);
/*
    void validateProgram(WebGLProgram program);

    void vertexAttrib1f(int indx, double x);

    default void vertexAttrib1fv(int indx, Float32Array values) {
        vertexAttrib1fv(
                indx, Js.<WebGLRenderingContext.VertexAttrib1fvValuesUnionType>uncheckedCast(values));
    }

    default void vertexAttrib1fv(int indx, JsArray<Double> values) {
        vertexAttrib1fv(
                indx, Js.<WebGLRenderingContext.VertexAttrib1fvValuesUnionType>uncheckedCast(values));
    }

    void vertexAttrib1fv(
            int indx, WebGLRenderingContext.VertexAttrib1fvValuesUnionType values);

    default void vertexAttrib1fv(int indx, double[] values) {
        vertexAttrib1fv(indx, Js.<JsArray<Double>>uncheckedCast(values));
    }

    void vertexAttrib2f(int indx, double x, double y);

    default void vertexAttrib2fv(int indx, Float32Array values) {
        vertexAttrib2fv(
                indx, Js.<WebGLRenderingContext.VertexAttrib2fvValuesUnionType>uncheckedCast(values));
    }

    default void vertexAttrib2fv(int indx, JsArray<Double> values) {
        vertexAttrib2fv(
                indx, Js.<WebGLRenderingContext.VertexAttrib2fvValuesUnionType>uncheckedCast(values));
    }

    void vertexAttrib2fv(
            int indx, WebGLRenderingContext.VertexAttrib2fvValuesUnionType values);

    default void vertexAttrib2fv(int indx, double[] values) {
        vertexAttrib2fv(indx, Js.<JsArray<Double>>uncheckedCast(values));
    }

    void vertexAttrib3f(int indx, double x, double y, double z);

    default void vertexAttrib3fv(int indx, Float32Array values) {
        vertexAttrib3fv(
                indx, Js.<WebGLRenderingContext.VertexAttrib3fvValuesUnionType>uncheckedCast(values));
    }

    default void vertexAttrib3fv(int indx, JsArray<Double> values) {
        vertexAttrib3fv(
                indx, Js.<WebGLRenderingContext.VertexAttrib3fvValuesUnionType>uncheckedCast(values));
    }

    void vertexAttrib3fv(
            int indx, WebGLRenderingContext.VertexAttrib3fvValuesUnionType values);

    default void vertexAttrib3fv(int indx, double[] values) {
        vertexAttrib3fv(indx, Js.<JsArray<Double>>uncheckedCast(values));
    }

    void vertexAttrib4f(int indx, double x, double y, double z, double w);

    default void vertexAttrib4fv(int indx, Float32Array values) {
        vertexAttrib4fv(
                indx, Js.<WebGLRenderingContext.VertexAttrib4fvValuesUnionType>uncheckedCast(values));
    }

    default void vertexAttrib4fv(int indx, JsArray<Double> values) {
        vertexAttrib4fv(
                indx, Js.<WebGLRenderingContext.VertexAttrib4fvValuesUnionType>uncheckedCast(values));
    }

    void vertexAttrib4fv(
            int indx, WebGLRenderingContext.VertexAttrib4fvValuesUnionType values);

    default void vertexAttrib4fv(int indx, double[] values) {
        vertexAttrib4fv(indx, Js.<JsArray<Double>>uncheckedCast(values));
    }
*/
    void vertexAttribPointer(
            int indx, int size, int type, boolean normalized, int stride, double offset);
/*
    void viewport(int x, int y, int width, int height);

 */
}
