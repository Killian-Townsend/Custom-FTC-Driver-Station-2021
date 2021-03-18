package androidx.renderscript;

public abstract class ScriptIntrinsic extends Script {
  ScriptIntrinsic(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
    if (paramLong != 0L)
      return; 
    throw new RSRuntimeException("Loading of ScriptIntrinsic failed.");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\ScriptIntrinsic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */