package androidx.renderscript;

import android.os.Build;

public class ScriptIntrinsic3DLUT extends ScriptIntrinsic {
  private static final int INTRINSIC_API_LEVEL = 19;
  
  private Element mElement;
  
  private Allocation mLUT;
  
  protected ScriptIntrinsic3DLUT(long paramLong, RenderScript paramRenderScript, Element paramElement) {
    super(paramLong, paramRenderScript);
    this.mElement = paramElement;
  }
  
  public static ScriptIntrinsic3DLUT create(RenderScript paramRenderScript, Element paramElement) {
    if (paramElement.isCompatible(Element.U8_4(paramRenderScript))) {
      boolean bool;
      if (paramRenderScript.isUseNative() && Build.VERSION.SDK_INT < 19) {
        bool = true;
      } else {
        bool = false;
      } 
      ScriptIntrinsic3DLUT scriptIntrinsic3DLUT = new ScriptIntrinsic3DLUT(paramRenderScript.nScriptIntrinsicCreate(8, paramElement.getID(paramRenderScript), bool), paramRenderScript, paramElement);
      scriptIntrinsic3DLUT.setIncSupp(bool);
      return scriptIntrinsic3DLUT;
    } 
    throw new RSIllegalArgumentException("Element must be compatible with uchar4.");
  }
  
  public void forEach(Allocation paramAllocation1, Allocation paramAllocation2) {
    forEach(0, paramAllocation1, paramAllocation2, (FieldPacker)null);
  }
  
  public Script.KernelID getKernelID() {
    return createKernelID(0, 3, null, null);
  }
  
  public void setLUT(Allocation paramAllocation) {
    Type type = paramAllocation.getType();
    if (type.getZ() != 0) {
      if (type.getElement().isCompatible(this.mElement)) {
        this.mLUT = paramAllocation;
        setVar(0, paramAllocation);
        return;
      } 
      throw new RSIllegalArgumentException("LUT element type must match.");
    } 
    throw new RSIllegalArgumentException("LUT must be 3d.");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\ScriptIntrinsic3DLUT.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */