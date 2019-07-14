package com.aglframework.smzh.filter;

import android.opengl.GLES20;

import com.aglframework.smzh.AGLFilter;
import com.aglframework.smzh.OpenGlUtils;

public class WhiteFilter extends AGLFilter {

    private static final String WIHITE_FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "uniform sampler2D inputImageTexture;\n" +
                    "uniform float level;\n" +
                    "varying vec2 textureCoordinate;\n" +
                    "\n " +
                    "void modifyColor(vec4 color){\n" +
                    "color.r=max(min(color.r,1.0),0.0);\n" +
                    "color.g=max(min(color.g,1.0),0.0);\n" +
                    "color.b=max(min(color.b,1.0),0.0);\n" +
                    "color.a=max(min(color.a,1.0),0.0);\n" +
                    " }\n" +
                    "\n " +
                    "void main() {\n" +
                    "vec4 nColor = texture2D(inputImageTexture,textureCoordinate);\n" +
                    "vec4 deltaColor = nColor+vec4(vec3(level * 0.25),0.0);\n" +
                    "modifyColor(deltaColor);\n" +
                    "gl_FragColor = deltaColor;\n" +
                    "}";


    private int glUniformLevel;

    private float level;

    @Override
    protected void onInit() {
        programId = OpenGlUtils.loadProgram(NO_FILTER_VERTEX_SHADER, WIHITE_FRAGMENT_SHADER);
        glAttrPosition = GLES20.glGetAttribLocation(programId, "position");
        glAttrTextureCoordinate = GLES20.glGetAttribLocation(programId, "inputTextureCoordinate");
        glUniformTexture = GLES20.glGetUniformLocation(programId, "inputImageTexture");
        glUniformLevel = GLES20.glGetUniformLocation(programId, "level");
    }

    @Override
    protected void onDrawArraysPre(Frame frame) {
        GLES20.glUniform1f(glUniformLevel, level);
    }

    public void setWhiteLevel(float level) {
        this.level = level;
    }
}
