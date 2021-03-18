package org.firstinspires.ftc.robotcore.internal.opengl.shaders;

import android.content.Context;

public class CubeMeshProgram extends ShaderProgram {
  public final CubeMeshFragmentShader fragment = new CubeMeshFragmentShader(this.program);
  
  public final CubeMeshVertexShader vertex = new CubeMeshVertexShader(this.program);
  
  public CubeMeshProgram(Context paramContext) {
    super(paramContext, CubeMeshVertexShader.resourceId, CubeMeshFragmentShader.resourceId);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\CubeMeshProgram.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */