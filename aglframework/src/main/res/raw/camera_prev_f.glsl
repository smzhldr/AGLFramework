#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES inputImageTexture;
varying vec2 textureCoordinate;
void main() {
    gl_FragColor=texture2D(inputImageTexture, textureCoordinate);
}