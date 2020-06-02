precision mediump float;
uniform sampler2D inputImageTexture;
uniform float level;
varying vec2 textureCoordinate;

void modifyColor(vec4 color){
    color.r=max(min(color.r, 1.0), 0.0);
    color.g=max(min(color.g, 1.0), 0.0);
    color.b=max(min(color.b, 1.0), 0.0);
    color.a=max(min(color.a, 1.0), 0.0);
}

void main() {
    vec4 nColor = texture2D(inputImageTexture, textureCoordinate);
    vec4 deltaColor = nColor+vec4(level * 0.15, level * 0.25, level * 0.25, 0.0);
    modifyColor(deltaColor);
    gl_FragColor = deltaColor;
}