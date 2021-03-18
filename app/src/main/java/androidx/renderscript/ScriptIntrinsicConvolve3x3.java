package androidx.renderscript;

import android.os.Build;

public class ScriptIntrinsicConvolve3x3 extends ScriptIntrinsic {
  private static final int INTRINSIC_API_LEVEL = 19;
  
  private Allocation mInput;
  
  private final float[] mValues = new float[9];
  
  ScriptIntrinsicConvolve3x3(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicConvolve3x3 create(RenderScript paramRenderScript, Element paramElement) {
    if (paramElement.isCompatible(Element.U8(paramRenderScript)) || paramElement.isCompatible(Element.U8_2(paramRenderScript)) || paramElement.isCompatible(Element.U8_3(paramRenderScript)) || paramElement.isCompatible(Element.U8_4(paramRenderScript)) || paramElement.isCompatible(Element.F32(paramRenderScript)) || paramElement.isCompatible(Element.F32_2(paramRenderScript)) || paramElement.isCompatible(Element.F32_3(paramRenderScript)) || paramElement.isCompatible(Element.F32_4(paramRenderScript))) {
      boolean bool;
      if (paramRenderScript.isUseNative() && Build.VERSION.SDK_INT < 19) {
        bool = true;
      } else {
        bool = false;
      } 
      ScriptIntrinsicConvolve3x3 scriptIntrinsicConvolve3x3 = new ScriptIntrinsicConvolve3x3(paramRenderScript.nScriptIntrinsicCreate(1, paramElement.getID(paramRenderScript), bool), paramRenderScript);
      scriptIntrinsicConvolve3x3.setIncSupp(bool);
      scriptIntrinsicConvolve3x3.setCoefficients(new float[] { 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F });
      return scriptIntrinsicConvolve3x3;
    } 
    throw new RSIllegalArgumentException("Unsupported element type.");
  }
  
  public void forEach(Allocation paramAllocation) {
    forEach(0, (Allocation)null, paramAllocation, (FieldPacker)null);
  }
  
  public void forEach(Allocation paramAllocation, Script.LaunchOptions paramLaunchOptions) {
    forEach(0, (Allocation)null, paramAllocation, (FieldPacker)null, paramLaunchOptions);
  }
  
  public Script.FieldID getFieldID_Input() {
    return createFieldID(1, null);
  }
  
  public Script.KernelID getKernelID() {
    return createKernelID(0, 2, null, null);
  }
  
  public void setCoefficients(float[] paramArrayOffloat) {
    FieldPacker fieldPacker = new FieldPacker(36);
    int i = 0;
    while (true) {
      float[] arrayOfFloat = this.mValues;
      if (i < arrayOfFloat.length) {
        arrayOfFloat[i] = paramArrayOffloat[i];
        fieldPacker.addF32(arrayOfFloat[i]);
        i++;
        continue;
      } 
      setVar(0, fieldPacker);
      return;
    } 
  }
  
  public void setInput(Allocation paramAllocation) {
    this.mInput = paramAllocation;
    setVar(1, paramAllocation);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\ScriptIntrinsicConvolve3x3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */