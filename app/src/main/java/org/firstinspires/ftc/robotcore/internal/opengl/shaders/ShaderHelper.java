package org.firstinspires.ftc.robotcore.internal.opengl.shaders;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderHelper {
  public static final String TAG = "ShaderHelper";
  
  public static int buildProgram(String paramString1, String paramString2) {
    return linkProgram(compileVertexShader(paramString1), compileFragmentShader(paramString2));
  }
  
  public static int compileFragmentShader(String paramString) {
    return compileShader(35632, paramString);
  }
  
  private static int compileShader(int paramInt, String paramString) {
    paramInt = GLES20.glCreateShader(paramInt);
    if (paramInt == 0)
      return 0; 
    GLES20.glShaderSource(paramInt, paramString);
    GLES20.glCompileShader(paramInt);
    int[] arrayOfInt = new int[1];
    GLES20.glGetShaderiv(paramInt, 35713, arrayOfInt, 0);
    if (arrayOfInt[0] == 0) {
      GLES20.glDeleteShader(paramInt);
      return 0;
    } 
    return paramInt;
  }
  
  public static int compileVertexShader(String paramString) {
    return compileShader(35633, paramString);
  }
  
  public static int linkProgram(int paramInt1, int paramInt2) {
    int i = GLES20.glCreateProgram();
    if (i == 0)
      return 0; 
    GLES20.glAttachShader(i, paramInt1);
    GLES20.glAttachShader(i, paramInt2);
    GLES20.glLinkProgram(i);
    int[] arrayOfInt = new int[1];
    GLES20.glGetProgramiv(i, 35714, arrayOfInt, 0);
    if (arrayOfInt[0] == 0) {
      GLES20.glDeleteProgram(i);
      return 0;
    } 
    return i;
  }
  
  public static boolean validateProgram(int paramInt) {
    GLES20.glValidateProgram(paramInt);
    int[] arrayOfInt = new int[1];
    GLES20.glGetProgramiv(paramInt, 35715, arrayOfInt, 0);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Results of validating program: ");
    stringBuilder.append(arrayOfInt[0]);
    stringBuilder.append("\nLog:");
    stringBuilder.append(GLES20.glGetProgramInfoLog(paramInt));
    Log.v("ShaderHelper", stringBuilder.toString());
    return (arrayOfInt[0] != 0);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\shaders\ShaderHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */