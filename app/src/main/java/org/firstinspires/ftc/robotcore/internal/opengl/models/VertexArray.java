package org.firstinspires.ftc.robotcore.internal.opengl.models;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class VertexArray {
  public static final int bytesPerFloat = 4;
  
  private final FloatBuffer vertexBuffer;
  
  public VertexArray(float[] paramArrayOffloat) {
    this.vertexBuffer = ByteBuffer.allocateDirect(paramArrayOffloat.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(paramArrayOffloat);
  }
  
  public void setVertexAttribPointer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.vertexBuffer.position(paramInt1);
    GLES20.glVertexAttribPointer(paramInt2, paramInt3, 5126, false, paramInt4, this.vertexBuffer);
    GLES20.glEnableVertexAttribArray(paramInt2);
    this.vertexBuffer.position(0);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\VertexArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */