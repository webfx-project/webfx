package dev.webfx.kit.webgl.spi.impl;

import dev.webfx.kit.webgl.*;
import dev.webfx.platform.typedarray.TypedArray;
import javafx.scene.image.Image;
import javafx.scene.media.MediaView;

/**
 * @author Bruno Salmon
 */
public class WebGLRenderingContextLogger implements WebGLRenderingContext {

    private final WebGLRenderingContext gl;

    public WebGLRenderingContextLogger(WebGLRenderingContext gl) {
        this.gl = gl;
    }

    private void log(String msg) {
        dev.webfx.platform.console.Console.log(msg);
    }


    @Override
    public void attachShader(WebGLProgram program, WebGLShader shader) {
        log("attachShader(program = " + program + ", shader = " + shader + ")");
        gl.attachShader(program, shader);
    }

    @Override
    public void bindBuffer(int target, WebGLBuffer buffer) {
        log("bindBuffer(target = " + target + ", buffer = " + buffer + ")");
        gl.bindBuffer(target, buffer);
    }

    @Override
    public void bufferData(int target, TypedArray data, int usage) {
        log("bufferData(target = " + target +", data = " + data + ", usage = " + usage + ")");
        gl.bufferData(target, data, usage);
    }

    @Override
    public void clear(int mask) {
        log("clear(mask = " + mask + ")");
        gl.clear(mask);
    }

    @Override
    public void clearColor(double red, double green, double blue, double alpha) {
        log("clearColor(red = " + red + ", green = " + green + ", blue = " + blue + ", alpha = " + alpha + ")");
        gl.clearColor(red, green, blue, alpha);
    }

    @Override
    public void clearDepth(double depth) {
        log("clearDepth(depth = " + depth + ")");
        gl.clearDepth(depth);
    }

    @Override
    public void compileShader(WebGLShader shader) {
        log("compileShader(shader = " + shader + ")");
        gl.compileShader(shader);
    }

    @Override
    public WebGLBuffer createBuffer() {
        log("createBuffer()");
        return gl.createBuffer();
    }

    @Override
    public WebGLProgram createProgram() {
        log("createProgram()");
        return gl.createProgram();
    }

    @Override
    public WebGLShader createShader(int type) {
        log("createShader(type = " + type + ")");
        return gl.createShader(type);
    }

    @Override
    public void deleteShader(WebGLShader shader) {
        log("deleteShader(shader = " + shader + ")");
        gl.deleteShader(shader);
    }

    @Override
    public void depthFunc(int func) {
        log("depthFunc(func = " + func + ")");
        gl.depthFunc(func);
    }

    @Override
    public void drawArrays(int mode, int first, int count) {
        log("drawArrays(mode = " + mode + ", first = " + first + ", count = " + count + ")");
        gl.drawArrays(mode, first, count);
    }

    @Override
    public void enable(int cap) {
        log("enable(cap = " + cap + ")");
        gl.enable(cap);
    }

    @Override
    public void enableVertexAttribArray(int index) {
        log("enableVertexAttribArray(index : " + index + ")");
        gl.enableVertexAttribArray(index);
    }

    @Override
    public int getAttribLocation(WebGLProgram program, String name) {
        log("getAttribLocation(program = " + program + ", name = " + name + ")");
        return gl.getAttribLocation(program, name);
    }

    @Override
    public String getProgramInfoLog(WebGLProgram program) {
        log("getProgramInfoLog(program = " + program + ")");
        return gl.getProgramInfoLog(program);
    }

    @Override
    public Object getProgramParameter(WebGLProgram program, int pname) {
        log("getProgramParameter(program = " + program + ", pname = " + pname + ")");
        return gl.getProgramParameter(program, pname);
    }

    @Override
    public String getShaderInfoLog(WebGLShader shader) {
        log("getShaderInfoLog(shader = " + shader + ")");
        return gl.getShaderInfoLog(shader);
    }

    @Override
    public Object getShaderParameter(WebGLShader shader, int pname) {
        log("getShaderParameter(shader = " + shader + ", pname = " + pname + ")");
        return gl.getShaderParameter(shader, pname);
    }

    @Override
    public WebGLUniformLocation getUniformLocation(WebGLProgram program, String name) {
        log("getUniformLocation(program = " + program + ", name = " + name + ")");
        return gl.getUniformLocation(program, name);
    }

    @Override
    public void linkProgram(WebGLProgram program) {
        log("linkProgram(program = " + program + ")");
        gl.linkProgram(program);
    }

    @Override
    public void shaderSource(WebGLShader shader, String source) {
        log("shaderSource(shader = " + shader + ", source = " + source + ")");
        gl.shaderSource(shader, source);
    }

    @Override
    public void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose, double[] data) {
        log("uniformMatrix4fv(location = " + location + ", transpose = " + transpose + ", data = " + data + ")");
        gl.uniformMatrix4fv(location, transpose, data);
    }

    @Override
    public void useProgram(WebGLProgram program) {
        log("useProgram(program = " + program + ")");
        gl.useProgram(program);
    }

    @Override
    public void vertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, double offset) {
        log("vertexAttribPointer(indx = " + indx + ", size = " + size + ", type = " + type + ", normalized = " + normalized + ", stride = " + stride + ", offset = " + offset + ")");
        gl.vertexAttribPointer(indx, size, type, normalized, stride, offset);
    }

    @Override
    public void drawElements(int mode, int count, int type, double offset) {
        log("drawElements()");
        gl.drawElements(mode, count, type, offset);
    }

    @Override
    public WebGLTexture createTexture() {
        log("createTexture()");
        return gl.createTexture();
    }

    @Override
    public void bindTexture(int target, WebGLTexture texture) {
        log("bindTexture()");
        gl.bindTexture(target, texture);
    }

    @Override
    public void activeTexture(int texture) {
        gl.activeTexture(texture);
    }

    @Override
    public void generateMipmap(int target) {
        gl.generateMipmap(target);
    }

    @Override
    public void pixelStorei(int pname, boolean param) {
        gl.pixelStorei(pname, param);
    }

    @Override
    public void texImage2D(int target, int level, int internalformat, int format, int type, Image img) {
        gl.texImage2D(target, level, internalformat, format, type, img);
    }

    @Override
    public void texImage2D(int target, int level, int internalformat, int format, int type, int img, int format0, int type0, TypedArray pixels) {
        gl.texImage2D(target, level, internalformat, format, type, img, format0, type0, pixels);
    }

    @Override
    public void texParameteri(int target, int pname, int param) {
        gl.texParameteri(target, pname, param);
    }

    @Override
    public void uniform1i(WebGLUniformLocation location, int value) {
        gl.uniform1i(location, value);
    }

    @Override
    public void texImage2D(int target, int level, int internalformat, int format, int type, MediaView img) {

    }
}
