package org.tensorflow.lite;

import java.io.PrintStream;

public final class TensorFlowLite {
  private static final String LIBNAME = "tensorflowlite_jni";
  
  static {
    init();
  }
  
  static boolean init() {
    try {
      System.loadLibrary("tensorflowlite_jni");
      return true;
    } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
      PrintStream printStream = System.err;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("TensorFlowLite: failed to load native library: ");
      stringBuilder.append(unsatisfiedLinkError.getMessage());
      printStream.println(stringBuilder.toString());
      return false;
    } 
  }
  
  public static native String version();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\tensorflow\lite\TensorFlowLite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */