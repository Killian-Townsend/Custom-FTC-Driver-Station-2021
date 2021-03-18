package org.firstinspires.ftc.robotcore.internal.opengl.shaders;

import android.opengl.GLES20;
import com.qualcomm.robotcore.R;

public class SimpleVertexShader implements PositionAttributeShader {
  public static final int resourceId = R.raw.simple_vertex_shader;
  
  protected final int a_vertexPosition;
  
  protected final int u_modelViewProjectionMatrix;
  
  public SimpleVertexShader(int paramInt) {
    this.a_vertexPosition = GLES20.glGetAttribLocation(paramInt, "vertexPosition");
    this.u_modelViewProjectionMatrix = GLES20.glGetUniformLocation(paramInt, "modelViewProjectionMatrix");
  }
  
  public void disableAttributes() {
    GLES20.glDisableVertexAttribArray(this.a_vertexPosition);
  }
  
  public int getPositionAttributeLocation() {
    return this.a_vertexPosition;
  }
  
  public void setModelViewProjectionMatrix(float[] paramArrayOffloat) {
    GLES20.glUniformMatrix4fv(this.u_modelViewProjectionMatrix, 1, false, paramArrayOffloat, 0);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\SimpleVertexShader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */