package dev.webfx.kit.webgl.spi.impl.gwtj2cl;

import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.HtmlGraphicsContext;
import dev.webfx.kit.mapper.peers.javafxmedia.spi.gwtj2cl.GwtJ2clMediaPlayerPeer;
import dev.webfx.kit.webgl.*;
import dev.webfx.platform.typedarray.TypedArray;
import dev.webfx.platform.typedarray.spi.impl.gwtj2cl.GwtJ2clTypedArray;
import elemental2.dom.HTMLImageElement;
import elemental2.dom.HTMLVideoElement;
import javafx.scene.image.Image;

/**
 * @author Bruno Salmon
 */

public class GwtJ2clWebGLRenderingContext implements WebGLRenderingContext {

    private final elemental2.webgl.WebGLRenderingContext gl;

    public GwtJ2clWebGLRenderingContext(elemental2.webgl.WebGLRenderingContext gl) {
        this.gl = gl;
    }

    @Override
    public void clear(int mask) {
        gl.clear(mask);
    }

    @Override
    public void clearColor(double red, double green, double blue, double alpha) {
        gl.clearColor(red, green, blue, alpha);
    }

    @Override
    public WebGLShader createShader(int type) {
        return new GwtJ2clWebGLShader(gl.createShader(type));
    }

    @Override
    public void shaderSource(WebGLShader shader, String source) {
        gl.shaderSource(((GwtJ2clWebGLShader) shader).jsWebGLShader, source);
    }

    @Override
    public void compileShader(WebGLShader shader) {
        gl.compileShader(((GwtJ2clWebGLShader) shader).jsWebGLShader);
    }

    @Override
    public Object getShaderParameter(WebGLShader shader, int pname) {
        return gl.getShaderParameter(((GwtJ2clWebGLShader) shader).jsWebGLShader, pname);
    }

    @Override
    public void deleteShader(WebGLShader shader) {
        gl.deleteShader(((GwtJ2clWebGLShader) shader).jsWebGLShader);
    }

    @Override
    public String getShaderInfoLog(WebGLShader shader) {
        return gl.getShaderInfoLog(((GwtJ2clWebGLShader) shader).jsWebGLShader);
    }

    @Override
    public WebGLProgram createProgram() {
        return new GwtJ2clWebGLProgram(gl.createProgram());
    }

    @Override
    public void attachShader(WebGLProgram program, WebGLShader shader) {
        gl.attachShader(((GwtJ2clWebGLProgram) program).jsWebGLProgram, ((GwtJ2clWebGLShader) shader).jsWebGLShader);
    }

    @Override
    public void linkProgram(WebGLProgram program) {
        gl.linkProgram(((GwtJ2clWebGLProgram) program).jsWebGLProgram);
    }

    @Override
    public Object getProgramParameter(WebGLProgram program, int pname) {
        return gl.getProgramParameter(((GwtJ2clWebGLProgram) program).jsWebGLProgram, pname);
    }

    @Override
    public String getProgramInfoLog(WebGLProgram program) {
        return gl.getProgramInfoLog(((GwtJ2clWebGLProgram) program).jsWebGLProgram);
    }

    @Override
    public WebGLBuffer createBuffer() {
        return new GwtJ2clWebGLBuffer(gl.createBuffer());
    }

    @Override
    public void bindBuffer(int target, WebGLBuffer buffer) {
        gl.bindBuffer(target, ((GwtJ2clWebGLBuffer) buffer).jsWebGLBuffer);
    }

    @Override
    public void bufferData(int target, TypedArray data, int usage) {
        GwtJ2clTypedArray gwtArrayBuffer = (GwtJ2clTypedArray) data;
        if (gwtArrayBuffer.jsArrayBuffer != null)
            gl.bufferData(target, gwtArrayBuffer.jsArrayBuffer, usage);
        else
            gl.bufferData(target, gwtArrayBuffer.jsArrayBufferView, usage);
    }

    @Override
    public int getAttribLocation(WebGLProgram program, String name) {
        return gl.getAttribLocation(((GwtJ2clWebGLProgram) program).jsWebGLProgram, name);
    }

    @Override
    public WebGLUniformLocation getUniformLocation(WebGLProgram program, String name) {
        return new GwtJ2clWebGLUniformLocation(gl.getUniformLocation(((GwtJ2clWebGLProgram) program).jsWebGLProgram, name));
    }

    @Override
    public void clearDepth(double depth) {
        gl.clearDepth(depth);
    }

    @Override
    public void enable(int cap) {
        gl.enable(cap);
    }

    @Override
    public void depthFunc(int func) {
        gl.depthFunc(func);
    }

    @Override
    public void useProgram(WebGLProgram program) {
        gl.useProgram(((GwtJ2clWebGLProgram) program).jsWebGLProgram);
    }

    @Override
    public void drawArrays(int mode, int first, int count) {
        gl.drawArrays(mode, first, count);
    }

    @Override
    public void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose, double[] data) {
        gl.uniformMatrix4fv(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, transpose, data);
    }

    @Override
    public void vertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, double offset) {
        gl.vertexAttribPointer(indx, size, type, normalized, stride, offset);
    }

    @Override
    public void enableVertexAttribArray(int index) {
        gl.enableVertexAttribArray(index);
    }

    @Override
    public void drawElements(int mode, int count, int type, double offset) {
        gl.drawElements(mode, count, type, offset);
    }

    @Override
    public WebGLTexture createTexture() {
        return new GwtJ2clWebGLTexture(gl.createTexture());
    }

    @Override
    public void bindTexture(int target, WebGLTexture texture) {
        gl.bindTexture(target, ((GwtJ2clWebGLTexture) texture).jsWebGLTexture);
    }

    @Override
    public void generateMipmap(int target) {
        gl.generateMipmap(target);
    }

    @Override
    public void texImage2D(int target, int level, int internalformat, int format, int type, Image img) {
        HTMLImageElement htmlImageElement = HtmlGraphicsContext.getHTMLImageElement(img);
        gl.texImage2D(target, level, internalformat, format, type, htmlImageElement);
    }

    @Override
    public void texImage2D(int target, int level, int internalformat, int format, int type, int img, int format0, int type0, TypedArray pixels) {
        GwtJ2clTypedArray gwtArrayBuffer = (GwtJ2clTypedArray) pixels;
        gl.texImage2D(target, level, internalformat, format, type, img, format0, type0, gwtArrayBuffer == null ? null : gwtArrayBuffer.jsArrayBufferView);
    }

    @Override
    public void texParameteri(int target, int pname, int param) {
        gl.texParameteri(target, pname, param);
    }

    @Override
    public void pixelStorei(int pname, boolean param) {
        gl.pixelStorei(pname, param);
    }

    @Override
    public void activeTexture(int texture) {
        gl.activeTexture(texture);
    }

    @Override
    public void uniform1i(WebGLUniformLocation location, int value) {
        gl.uniform1i(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, value);
    }

    @Override
    public void texImage2D(int target, int level, int internalformat, int format, int type, javafx.scene.media.MediaView img) {
        HTMLVideoElement video = (HTMLVideoElement) ((GwtJ2clMediaPlayerPeer) img.getMediaPlayer().getPeer()).getMediaElement();
        gl.texImage2D(target, level, internalformat, format, type, video);
    }

    @Override
    public void bindAttribLocation(WebGLProgram program, int index, String name) {
        gl.bindAttribLocation(((GwtJ2clWebGLProgram) program).jsWebGLProgram, index, name);
    }

    @Override
    public void blendColor(double red, double green, double blue, double alpha) {
        gl.blendColor(red, green, blue, alpha);
    }

    @Override
    public void blendEquation(int mode) {
        gl.blendEquation(mode);
    }

    @Override
    public void blendEquationSeparate(int modeRGB, int modeAlpha) {
        gl.blendEquationSeparate(modeRGB, modeAlpha);
    }

    @Override
    public void blendFunc(int sfactor, int dfactor) {
        gl.blendFunc(sfactor, dfactor);
    }

    @Override
    public void blendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
        gl.blendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
    }

    @Override
    public void clearStencil(int s) {
        gl.clearStencil(s);
    }

    @Override
    public void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        gl.colorMask(red, green, blue, alpha);
    }

    @Override
    public void cullFace(int mode) {
        gl.cullFace(mode);
    }

    @Override
    public void deleteBuffer(WebGLBuffer buffer) {
        gl.deleteBuffer(((GwtJ2clWebGLBuffer) buffer).jsWebGLBuffer);
    }

    @Override
    public void deleteProgram(WebGLProgram program) {
        gl.deleteProgram(((GwtJ2clWebGLProgram) program).jsWebGLProgram);
    }

    @Override
    public void deleteTexture(WebGLTexture texture) {
        gl.deleteTexture(((GwtJ2clWebGLTexture) texture).jsWebGLTexture);
    }

    @Override
    public void depthMask(boolean flag) {
        gl.depthMask(flag);
    }

    @Override
    public void depthRange(double nearVal, double farVal) {
        gl.depthRange(nearVal, farVal);
    }

    @Override
    public void detachShader(WebGLProgram program, WebGLShader shader) {
        gl.detachShader(((GwtJ2clWebGLProgram) program).jsWebGLProgram, ((GwtJ2clWebGLShader) shader).jsWebGLShader);
    }

    @Override
    public void disable(int flags) {
        gl.disable(flags);
    }

    @Override
    public void disableVertexAttribArray(int index) {
        gl.disableVertexAttribArray(index);
    }

    @Override
    public Object finish() {
        return gl.finish();
    }

    @Override
    public Object flush() {
        return gl.flush();
    }

    @Override
    public void framebufferTexture2D(int target, int attachment, int textarget, WebGLTexture texture, int level) {
        gl.framebufferTexture2D(target, attachment, textarget, ((GwtJ2clWebGLTexture) texture).jsWebGLTexture, level);
    }

    @Override
    public void frontFace(int mode) {
        gl.frontFace(mode);
    }

    @Override
    public Object getBufferParameter(int target, int pname) {
        return gl.getBufferParameter(target, pname);
    }

    @Override
    public int getError() {
        return gl.getError();
    }

    @Override
    public Object getFramebufferAttachmentParameter(int target, int attachment, int pname) {
        return gl.getFramebufferAttachmentParameter(target, attachment, pname);
    }

    @Override
    public Object getParameter(int pname) {
        return gl.getParameter(pname);
    }

    @Override
    public Object getRenderbufferParameter(int target, int pname) {
        return gl.getRenderbufferParameter(target, pname);
    }

    @Override
    public String getShaderSource(WebGLShader shader) {
        return gl.getShaderSource(((GwtJ2clWebGLShader) shader).jsWebGLShader);
    }

    @Override
    public Object getTexParameter(int target, int pname) {
        return gl.getTexParameter(target, pname);
    }

    @Override
    public Object getUniform(WebGLProgram program, WebGLUniformLocation location) {
        return gl.getUniform(((GwtJ2clWebGLProgram) program).jsWebGLProgram, ((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation);
    }

    @Override
    public Object getVertexAttrib(int index, int pname) {
        return gl.getVertexAttrib(index, pname);
    }

    @Override
    public double getVertexAttribOffset(int index, int pname) {
        return gl.getVertexAttribOffset(index, pname);
    }

    @Override
    public void hint(int target, int mode) {
        gl.hint(target, mode);
    }

    @Override
    public boolean isContextLost() {
        return gl.isContextLost();
    }

    @Override
    public boolean isEnabled(int cap) {
        return gl.isEnabled(cap);
    }

    @Override
    public void lineWidth(double width) {
        gl.lineWidth(width);
    }

    @Override
    public void pixelStorei(int pname, int param) {
        gl.pixelStorei(pname, param);
    }

    @Override
    public void polygonOffset(double factor, double units) {
        gl.polygonOffset(factor, units);
    }

    @Override
    public void renderbufferStorage(int target, int internalformat, int width, int height) {
        gl.renderbufferStorage(target, internalformat, width, height);
    }

    @Override
    public void sampleCoverage(double coverage, boolean invert) {
        gl.sampleCoverage(coverage, invert);
    }

    @Override
    public void scissor(int x, int y, int width, int height) {
        gl.scissor(x, y, width, height);
    }

    @Override
    public void stencilFunc(int func, int ref, int mask) {
        gl.stencilFunc(func, ref, mask);
    }

    @Override
    public void stencilFuncSeparate(int face, int func, int ref, int mask) {
        gl.stencilFuncSeparate(face, func, ref, mask);
    }

    @Override
    public void stencilMask(int mask) {
        gl.stencilMask(mask);
    }

    @Override
    public void stencilMaskSeparate(int face, int mask) {
        gl.stencilMaskSeparate(face, mask);
    }

    @Override
    public void stencilOp(int fail, int zfail, int zpass) {
        gl.stencilOp(fail, zfail, zpass);
    }

    @Override
    public void stencilOpSeparate(int face, int fail, int zfail, int zpass) {
        gl.stencilOpSeparate(face, fail, zfail, zpass);
    }

    @Override
    public void texParameterf(int target, int pname, double param) {
        gl.texParameterf(target, pname, param);
    }

    @Override
    public void uniform1f(WebGLUniformLocation location, double value) {
        gl.uniform1f(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, value);
    }

    @Override
    public void uniform1fv(WebGLUniformLocation location, double[] value) {
        gl.uniform1fv(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, value);
    }

    @Override
    public void uniform1iv(WebGLUniformLocation location, Object[] value) {
        gl.uniform1iv(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, value);
    }

    @Override
    public void uniform2fv(WebGLUniformLocation location, double[] value) {
        gl.uniform2fv(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, value);
    }

    @Override
    public void uniform2iv(WebGLUniformLocation location, Object[] value) {
        gl.uniform2iv(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, value);
    }

    @Override
    public void uniform3f(WebGLUniformLocation location, double value1, double value2, double value3) {
        gl.uniform3f(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, value1, value2, value3);
    }

    @Override
    public void uniform3fv(WebGLUniformLocation location, double[] value) {
        gl.uniform3fv(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, value);
    }

    @Override
    public void uniform3iv(WebGLUniformLocation location, Object[] value) {
        gl.uniform3iv(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, value);
    }

    @Override
    public void uniform4f(WebGLUniformLocation location, double value1, double value2, double value3, double value4) {
        gl.uniform4f(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, value1, value2, value3, value4);
    }

    @Override
    public void uniform4fv(WebGLUniformLocation location, double[] value) {
        gl.uniform4fv(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, value);
    }

    @Override
    public void uniformMatrix2fv(WebGLUniformLocation location, boolean transpose, double[] data) {
        gl.uniformMatrix2fv(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, transpose, data);
    }

    @Override
    public void uniformMatrix3fv(WebGLUniformLocation location, boolean transpose, double[] data) {
        gl.uniformMatrix3fv(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, transpose, data);
    }

    @Override
    public void validateProgram(WebGLProgram program) {
        gl.validateProgram(((GwtJ2clWebGLProgram) program).jsWebGLProgram);
    }

    @Override
    public void vertexAttrib1f(int indx, double x) {
        gl.vertexAttrib1f(indx, x);
    }

    @Override
    public void vertexAttrib1fv(int indx, double[] values) {
        gl.vertexAttrib1fv(indx, values);
    }

    @Override
    public void vertexAttrib2f(int indx, double x, double y) {
        gl.vertexAttrib2f(indx, x, y);
    }

    @Override
    public void vertexAttrib2fv(int indx, double[] values) {
        gl.vertexAttrib2fv(indx, values);
    }

    @Override
    public void vertexAttrib3f(int indx, double x, double y, double z) {
        gl.vertexAttrib3f(indx, x, y, z);
    }

    @Override
    public void vertexAttrib4f(int indx, double x, double y, double z, double w) {
        gl.vertexAttrib4f(indx, x, y, z, w);
    }

    @Override
    public void viewport(int x, int y, int width, int height) {
        gl.viewport(x, y, width, height);
    }

    @Override
    public Object getExtension(String name) {
        return gl.getExtension(name);
    }

    @Override
    public void bindFramebuffer(int target, WebGLFramebuffer buffer) {
        gl.bindFramebuffer(target, buffer == null ? null : ((GwtJ2clWebGLFramebuffer) buffer).jsWebGLFramebuffer);
    }

    @Override
    public int checkFramebufferStatus(int target) {
        return gl.checkFramebufferStatus(target);
    }

    @Override
    public WebGLFramebuffer createFramebuffer() {
        return new GwtJ2clWebGLFramebuffer(gl.createFramebuffer());
    }

    @Override
    public WebGLActiveInfo getActiveAttrib(WebGLProgram program, int index) {
        return new GwtJ2clWebGLActiveInfo(gl.getActiveAttrib(((GwtJ2clWebGLProgram) program).jsWebGLProgram, index));
    }

    @Override
    public WebGLActiveInfo getActiveUniform(WebGLProgram program, int index) {
        return new GwtJ2clWebGLActiveInfo(gl.getActiveUniform(((GwtJ2clWebGLProgram) program).jsWebGLProgram, index));
    }

    @Override
    public void uniform2f(WebGLUniformLocation location, double value1, double value2) {
        gl.uniform2f(((GwtJ2clWebGLUniformLocation) location).jsWebGLUniformLocation, value1, value2);
    }

    @Override
    public int getDrawingBufferWidth() {
        return gl.drawingBufferWidth;
    }

    @Override
    public int getDrawingBufferHeight() {
        return gl.drawingBufferHeight;
    }
}
