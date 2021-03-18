package org.firstinspires.ftc.robotcore.internal.opengl.shaders;

import android.graphics.Color;
import android.opengl.GLES20;
import com.qualcomm.robotcore.R;

public class ColorFragmentShader {
  public static final int resourceId = R.raw.color_fragment_shader;
  
  protected final int u_Color;
  
  public ColorFragmentShader(int paramInt) {
    this.u_Color = GLES20.glGetUniformLocation(paramInt, "u_Color");
  }
  
  private static float rescale(int paramInt) {
    return paramInt / 255.0F;
  }
  
  public void setColor(float paramFloat1, float paramFloat2, float paramFloat3) {
    setColor(paramFloat1, paramFloat2, paramFloat3, 1.0F);
  }
  
  public void setColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    GLES20.glUniform4f(this.u_Color, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void setColor(int paramInt) {
    setColor(rescale(Color.red(paramInt)), rescale(Color.green(paramInt)), rescale(Color.blue(paramInt)), rescale(Color.alpha(paramInt)));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\ColorFragmentShader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */