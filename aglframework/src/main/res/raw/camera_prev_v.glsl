attribute vec4 position;
uniform mat4 u_Matrix;
attribute vec2 inputTextureCoordinate;
varying vec2 textureCoordinate;

void main() {
    gl_Position = u_Matrix * position;
    textureCoordinate = inputTextureCoordinate;
}