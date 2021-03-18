package org.firstinspires.ftc.robotcore.internal.opengl.shaders;

import android.opengl.GLES20;
import com.qualcomm.robotcore.R;
import com.vuforia.GLTextureUnit;
import org.firstinspires.ftc.robotcore.internal.opengl.Texture;

public class CubeMeshFragmentShader {
  public static final int resourceId = R.raw.cube_mesh_fragment_shader;
  
  protected final int u_texSampler2DHandle;
  
  public CubeMeshFragmentShader(int paramInt) {
    this.u_texSampler2DHandle = GLES20.glGetUniformLocation(paramInt, "texSampler2D");
  }
  
  public void setTexture(Texture paramTexture) {
    GLES20.glActiveTexture(33984);
    GLES20.glBindTexture(3553, paramTexture.mTextureID[0]);
    GLES20.glUniform1i(this.u_texSampler2DHandle, 0);
  }
  
  public void setTextureUnit(GLTextureUnit paramGLTextureUnit) {
    GLES20.glUniform1i(this.u_texSampler2DHandle, paramGLTextureUnit.getTextureUnit());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\CubeMeshFragmentShader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */