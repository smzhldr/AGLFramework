varying highp vec2 textureCoordinate;

uniform sampler2D inputImageTexture;
uniform sampler2D lookupTexture; // lookup texture

//uniform highp float lookupDimension; // 64 or 16
uniform lowp float intensity;

void main()
{
    float lookupDimension = 64.0;
    highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);

    highp float blueColor = textureColor.b * (lookupDimension - 1.0);
    highp float lookupDimensionSqrt = sqrt(lookupDimension);

    highp vec2 quad1;
    quad1.y = floor(floor(blueColor) / lookupDimensionSqrt);
    quad1.x = floor(blueColor) - (quad1.y * lookupDimensionSqrt);

    highp vec2 quad2;
    quad2.y = floor(ceil(blueColor) / lookupDimensionSqrt);
    quad2.x = ceil(blueColor) - (quad2.y * lookupDimensionSqrt);

    highp vec2 texPos1;
    texPos1.x = (quad1.x / lookupDimensionSqrt) + 0.5/lookupDimensionSqrt/lookupDimension + ((1.0/lookupDimensionSqrt - 1.0/lookupDimensionSqrt/lookupDimension) * textureColor.r);
    texPos1.y = (quad1.y / lookupDimensionSqrt) + 0.5/lookupDimensionSqrt/lookupDimension + ((1.0/lookupDimensionSqrt - 1.0/lookupDimensionSqrt/lookupDimension) * textureColor.g);

    highp vec2 texPos2;
    texPos2.x = (quad2.x / lookupDimensionSqrt) + 0.5/lookupDimensionSqrt/lookupDimension + ((1.0/lookupDimensionSqrt - 1.0/lookupDimensionSqrt/lookupDimension) * textureColor.r);
    texPos2.y = (quad2.y / lookupDimensionSqrt) + 0.5/lookupDimensionSqrt/lookupDimension + ((1.0/lookupDimensionSqrt - 1.0/lookupDimensionSqrt/lookupDimension) * textureColor.g);

    lowp vec4 newColor1 = texture2D(lookupTexture, texPos1);
    lowp vec4 newColor2 = texture2D(lookupTexture, texPos2);

    lowp vec4 newColor = vec4(mix(newColor1.rgb, newColor2.rgb, fract(blueColor)), textureColor.w);
    gl_FragColor = mix(textureColor, newColor, intensity);
}
