package androidx.renderscript;

import android.os.Build;

public class ScriptIntrinsicResize extends ScriptIntrinsic {
  private static final int INTRINSIC_API_LEVEL = 21;
  
  private Allocation mInput;
  
  protected ScriptIntrinsicResize(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicResize create(RenderScript paramRenderScript) {
    boolean bool;
    if (paramRenderScript.isUseNative() && Build.VERSION.SDK_INT < 21) {
      bool = true;
    } else {
      bool = false;
    } 
    ScriptIntrinsicResize scriptIntrinsicResize = new ScriptIntrinsicResize(paramRenderScript.nScriptIntrinsicCreate(12, 0L, bool), paramRenderScript);
    scriptIntrinsicResize.setIncSupp(bool);
    return scriptIntrinsicResize;
  }
  
  public void forEach_bicubic(Allocation paramAllocation) {
    if (paramAllocation != this.mInput) {
      forEach_bicubic(paramAllocation, (Script.LaunchOptions)null);
      return;
    } 
    throw new RSIllegalArgumentException("Output cannot be same as Input.");
  }
  
  public void forEach_bicubic(Allocation paramAllocation, Script.LaunchOptions paramLaunchOptions) {
    forEach(0, (Allocation)null, paramAllocation, (FieldPacker)null, paramLaunchOptions);
  }
  
  public Script.FieldID getFieldID_Input() {
    return createFieldID(0, (Element)null);
  }
  
  public Script.KernelID getKernelID_bicubic() {
    return createKernelID(0, 2, (Element)null, (Element)null);
  }
  
  public void setInput(Allocation paramAllocation) {
    Element element = paramAllocation.getElement();
    if (element.isCompatible(Element.U8(this.mRS)) || element.isCompatible(Element.U8_2(this.mRS)) || element.isCompatible(Element.U8_3(this.mRS)) || element.isCompatible(Element.U8_4(this.mRS)) || element.isCompatible(Element.F32(this.mRS)) || element.isCompatible(Element.F32_2(this.mRS)) || element.isCompatible(Element.F32_3(this.mRS)) || element.isCompatible(Element.F32_4(this.mRS))) {
      this.mInput = paramAllocation;
      setVar(0, paramAllocation);
      return;
    } 
    throw new RSIllegalArgumentException("Unsupported element type.");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\ScriptIntrinsicResize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */