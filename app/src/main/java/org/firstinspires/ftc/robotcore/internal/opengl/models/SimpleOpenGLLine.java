package org.firstinspires.ftc.robotcore.internal.opengl.models;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import org.firstinspires.ftc.robotcore.internal.opengl.shaders.ColorFragmentShader;
import org.firstinspires.ftc.robotcore.internal.opengl.shaders.SimpleColorProgram;

public class SimpleOpenGLLine {
  protected static final int coordinatesPerVertex = 3;
  
  protected static final float[] defaultCoordinates = new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F };
  
  protected float[] color;
  
  protected FloatBuffer vertexBuffer;
  
  protected final int vertexCount;
  
  protected final int vertexStride;
  
  public SimpleOpenGLLine() {
    int i = defaultCoordinates.length / 3;
    this.vertexCount = i;
    this.vertexStride = 12;
    this.color = new float[] { 0.0F, 0.0F, 0.0F, 1.0F };
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(i * 12);
    byteBuffer.order(ByteOrder.nativeOrder());
    FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
    this.vertexBuffer = floatBuffer;
    floatBuffer.put(defaultCoordinates);
    this.vertexBuffer.position(0);
  }
  
  public void draw(float[] paramArrayOffloat, SimpleColorProgram paramSimpleColorProgram) {
    paramSimpleColorProgram.useProgram();
    ColorFragmentShader colorFragmentShader = paramSimpleColorProgram.fragment;
    float[] arrayOfFloat = this.color;
    colorFragmentShader.setColor(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3]);
    int i = paramSimpleColorProgram.vertex.getPositionAttributeLocation();
    GLES20.glVertexAttribPointer(i, 3, 5126, false, 12, this.vertexBuffer);
    GLES20.glEnableVertexAttribArray(i);
    paramSimpleColorProgram.vertex.setModelViewProjectionMatrix(paramArrayOffloat);
    GLES20.glDrawArrays(1, 0, this.vertexCount);
    paramSimpleColorProgram.vertex.disableAttributes();
  }
  
  public void setColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    float[] arrayOfFloat = this.color;
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[1] = paramFloat2;
    arrayOfFloat[2] = paramFloat3;
    arrayOfFloat[3] = paramFloat4;
  }
  
  public void setVerts(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    float[] arrayOfFloat = new float[this.vertexCount * 3];
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[1] = paramFloat2;
    arrayOfFloat[2] = paramFloat3;
    arrayOfFloat[3] = paramFloat4;
    arrayOfFloat[4] = paramFloat5;
    arrayOfFloat[5] = paramFloat6;
    this.vertexBuffer.put(arrayOfFloat);
    this.vertexBuffer.position(0);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\SimpleOpenGLLine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */