package org.firstinspires.ftc.robotcore.internal.opengl.shaders;

import android.content.Context;
import android.opengl.GLES20;
import org.firstinspires.ftc.robotcore.internal.opengl.TextResourceReader;

public abstract class ShaderProgram {
  protected final int program;
  
  protected ShaderProgram(Context paramContext, int paramInt1, int paramInt2) {
    this.program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(paramContext, paramInt1), TextResourceReader.readTextFileFromResource(paramContext, paramInt2));
  }
  
  public void useProgram() {
    GLES20.glUseProgram(this.program);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\ShaderProgram.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */