#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES uTexture;
varying vec2 aCoordinate;
void main() {
    gl_FragColor=texture2D(uTexture, aCoordinate);
}