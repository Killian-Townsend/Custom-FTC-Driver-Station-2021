package org.firstinspires.ftc.robotcore.internal.opengl.shaders;

import android.content.Context;

public class SimpleColorProgram extends ShaderProgram {
  public final ColorFragmentShader fragment = new ColorFragmentShader(this.program);
  
  public final SimpleVertexShader vertex = new SimpleVertexShader(this.program);
  
  public SimpleColorProgram(Context paramContext) {
    super(paramContext, SimpleVertexShader.resourceId, ColorFragmentShader.resourceId);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\SimpleColorProgram.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */