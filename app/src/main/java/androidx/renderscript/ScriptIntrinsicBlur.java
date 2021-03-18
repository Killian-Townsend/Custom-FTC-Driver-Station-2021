package androidx.renderscript;

import android.os.Build;

public class ScriptIntrinsicBlur extends ScriptIntrinsic {
  private static final int INTRINSIC_API_LEVEL = 19;
  
  private Allocation mInput;
  
  private final float[] mValues = new float[9];
  
  protected ScriptIntrinsicBlur(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicBlur create(RenderScript paramRenderScript, Element paramElement) {
    if (paramElement.isCompatible(Element.U8_4(paramRenderScript)) || paramElement.isCompatible(Element.U8(paramRenderScript))) {
      boolean bool;
      if (paramRenderScript.isUseNative() && Build.VERSION.SDK_INT < 19) {
        bool = true;
      } else {
        bool = false;
      } 
      ScriptIntrinsicBlur scriptIntrinsicBlur = new ScriptIntrinsicBlur(paramRenderScript.nScriptIntrinsicCreate(5, paramElement.getID(paramRenderScript), bool), paramRenderScript);
      scriptIntrinsicBlur.setIncSupp(bool);
      scriptIntrinsicBlur.setRadius(5.0F);
      return scriptIntrinsicBlur;
    } 
    throw new RSIllegalArgumentException("Unsupported element type.");
  }
  
  public void forEach(Allocation paramAllocation) {
    if (paramAllocation.getType().getY() != 0) {
      forEach(0, (Allocation)null, paramAllocation, (FieldPacker)null);
      return;
    } 
    throw new RSIllegalArgumentException("Output is a 1D Allocation");
  }
  
  public Script.FieldID getFieldID_Input() {
    return createFieldID(1, null);
  }
  
  public Script.KernelID getKernelID() {
    return createKernelID(0, 2, null, null);
  }
  
  public void setInput(Allocation paramAllocation) {
    if (paramAllocation.getType().getY() != 0) {
      this.mInput = paramAllocation;
      setVar(1, paramAllocation);
      return;
    } 
    throw new RSIllegalArgumentException("Input set to a 1D Allocation");
  }
  
  public void setRadius(float paramFloat) {
    if (paramFloat > 0.0F && paramFloat <= 25.0F) {
      setVar(0, paramFloat);
      return;
    } 
    throw new RSIllegalArgumentException("Radius out of range (0 < r <= 25).");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\ScriptIntrinsicBlur.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */