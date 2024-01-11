package dev.webfx.kit.webgl.spi.impl.gwt;

import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlGraphicsContext;
import dev.webfx.kit.mapper.peers.javafxmedia.spi.gwt.GwtMediaPlayerPeer;
import dev.webfx.kit.webgl.*;
import dev.webfx.platform.typedarray.TypedArray;
import dev.webfx.platform.typedarray.spi.impl.gwt.GwtTypedArray;
import elemental2.dom.HTMLImageElement;
import elemental2.dom.HTMLVideoElement;
import javafx.scene.image.Image;

/**
 * @author Bruno Salmon
 */

public class GwtWebGLRenderingContext implements WebGLRenderingContext {

    private final elemental2.webgl.WebGLRenderingContext gl;

    public GwtWebGLRenderingContext(elemental2.webgl.WebGLRenderingContext gl) {
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
        return new GwtWebGLShader(gl.createShader(type));
    }

    @Override
    public void shaderSource(WebGLShader shader, String source) {
        gl.shaderSource(((GwtWebGLShader) shader).jsWebGLShader, source);
    }

    @Override
    public void compileShader(WebGLShader shader) {
        gl.compileShader(((GwtWebGLShader) shader).jsWebGLShader);
    }

    @Override
    public Object getShaderParameter(WebGLShader shader, int pname) {
        return gl.getShaderParameter(((GwtWebGLShader) shader).jsWebGLShader, pname);
    }

    @Override
    public void deleteShader(WebGLShader shader) {
        gl.deleteShader(((GwtWebGLShader) shader).jsWebGLShader);
    }

    @Override
    public String getShaderInfoLog(WebGLShader shader) {
        return gl.getShaderInfoLog(((GwtWebGLShader) shader).jsWebGLShader);
    }

    @Override
    public WebGLProgram createProgram() {
        return new GwtWebGLProgram(gl.createProgram());
    }

    @Override
    public void attachShader(WebGLProgram program, WebGLShader shader) {
        gl.attachShader(((GwtWebGLProgram) program).jsWebGLProgram, ((GwtWebGLShader) shader).jsWebGLShader);
    }

    @Override
    public void linkProgram(WebGLProgram program) {
        gl.linkProgram(((GwtWebGLProgram) program).jsWebGLProgram);
    }

    @Override
    public Object getProgramParameter(WebGLProgram program, int pname) {
        return gl.getProgramParameter(((GwtWebGLProgram) program).jsWebGLProgram, pname);
    }

    @Override
    public String getProgramInfoLog(WebGLProgram program) {
        return gl.getProgramInfoLog(((GwtWebGLProgram) program).jsWebGLProgram);
    }

    @Override
    public WebGLBuffer createBuffer() {
        return new GwtWebGLBuffer(gl.createBuffer());
    }

    @Override
    public void bindBuffer(int target, WebGLBuffer buffer) {
        gl.bindBuffer(target, ((GwtWebGLBuffer) buffer).jsWebGLBuffer);
    }

    @Override
    public void bufferData(int target, TypedArray data, int usage) {
        GwtTypedArray gwtArrayBuffer = (GwtTypedArray) data;
        if (gwtArrayBuffer.jsArrayBuffer != null)
            gl.bufferData(target, gwtArrayBuffer.jsArrayBuffer, usage);
        else
            gl.bufferData(target, gwtArrayBuffer.jsArrayBufferView, usage);
    }

    @Override
    public int getAttribLocation(WebGLProgram program, String name) {
        return gl.getAttribLocation(((GwtWebGLProgram) program).jsWebGLProgram, name);
    }

    @Override
    public WebGLUniformLocation getUniformLocation(WebGLProgram program, String name) {
        return new GwtWebGLUniformLocation(gl.getUniformLocation(((GwtWebGLProgram) program).jsWebGLProgram, name));
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
        gl.useProgram(((GwtWebGLProgram) program).jsWebGLProgram);
    }

    @Override
    public void drawArrays(int mode, int first, int count) {
        gl.drawArrays(mode, first, count);
    }

    @Override
    public void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose, double[] data) {
        gl.uniformMatrix4fv(((GwtWebGLUniformLocation) location).jsWebGLUniformLocation, transpose, data);
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
        return new GwtWebGLTexture(gl.createTexture());
    }

    @Override
    public void bindTexture(int target, WebGLTexture texture) {
        gl.bindTexture(target, ((GwtWebGLTexture) texture).jsWebGLTexture);
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
        GwtTypedArray gwtArrayBuffer = (GwtTypedArray) pixels;
        gl.texImage2D(target, level, internalformat, format, type, img, format0, type0, gwtArrayBuffer.jsArrayBufferView);
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
        gl.uniform1i(((GwtWebGLUniformLocation) location).jsWebGLUniformLocation, value);
    }

    @Override
    public void texImage2D(int target, int level, int internalformat, int format, int type, javafx.scene.media.MediaView img) {
        HTMLVideoElement video = (HTMLVideoElement) ((GwtMediaPlayerPeer) img.getMediaPlayer().getPeer()).getMediaElement();
        gl.texImage2D(target, level, internalformat, format, type, video);
    }
}
