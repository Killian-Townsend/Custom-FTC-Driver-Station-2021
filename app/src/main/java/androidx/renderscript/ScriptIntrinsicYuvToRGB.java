package androidx.renderscript;

import android.os.Build;

public class ScriptIntrinsicYuvToRGB extends ScriptIntrinsic {
  private static final int INTRINSIC_API_LEVEL = 19;
  
  private Allocation mInput;
  
  ScriptIntrinsicYuvToRGB(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicYuvToRGB create(RenderScript paramRenderScript, Element paramElement) {
    boolean bool;
    if (paramRenderScript.isUseNative() && Build.VERSION.SDK_INT < 19) {
      bool = true;
    } else {
      bool = false;
    } 
    ScriptIntrinsicYuvToRGB scriptIntrinsicYuvToRGB = new ScriptIntrinsicYuvToRGB(paramRenderScript.nScriptIntrinsicCreate(6, paramElement.getID(paramRenderScript), bool), paramRenderScript);
    scriptIntrinsicYuvToRGB.setIncSupp(bool);
    return scriptIntrinsicYuvToRGB;
  }
  
  public void forEach(Allocation paramAllocation) {
    forEach(0, (Allocation)null, paramAllocation, (FieldPacker)null);
  }
  
  public Script.FieldID getFieldID_Input() {
    return createFieldID(0, null);
  }
  
  public Script.KernelID getKernelID() {
    return createKernelID(0, 2, null, null);
  }
  
  public void setInput(Allocation paramAllocation) {
    this.mInput = paramAllocation;
    setVar(0, paramAllocation);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\ScriptIntrinsicYuvToRGB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */