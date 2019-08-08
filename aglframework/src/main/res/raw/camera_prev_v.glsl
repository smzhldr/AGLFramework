attribute vec4 vPosition;
uniform mat4 u_Matrix;
attribute vec2 atextureCoordinate;
varying vec2 aCoordinate;

void main() {
    gl_Position = u_Matrix * vPosition;
    aCoordinate = atextureCoordinate;
}