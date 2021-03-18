package androidx.renderscript;

import android.os.Build;

public class ScriptIntrinsicHistogram extends ScriptIntrinsic {
  private static final int INTRINSIC_API_LEVEL = 19;
  
  private Allocation mOut;
  
  protected ScriptIntrinsicHistogram(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicHistogram create(RenderScript paramRenderScript, Element paramElement) {
    if (paramElement.isCompatible(Element.U8_4(paramRenderScript)) || paramElement.isCompatible(Element.U8_3(paramRenderScript)) || paramElement.isCompatible(Element.U8_2(paramRenderScript)) || paramElement.isCompatible(Element.U8(paramRenderScript))) {
      boolean bool;
      if (paramRenderScript.isUseNative() && Build.VERSION.SDK_INT < 19) {
        bool = true;
      } else {
        bool = false;
      } 
      ScriptIntrinsicHistogram scriptIntrinsicHistogram = new ScriptIntrinsicHistogram(paramRenderScript.nScriptIntrinsicCreate(9, paramElement.getID(paramRenderScript), bool), paramRenderScript);
      scriptIntrinsicHistogram.setIncSupp(bool);
      return scriptIntrinsicHistogram;
    } 
    throw new RSIllegalArgumentException("Unsupported element type.");
  }
  
  public void forEach(Allocation paramAllocation) {
    forEach(paramAllocation, (Script.LaunchOptions)null);
  }
  
  public void forEach(Allocation paramAllocation, Script.LaunchOptions paramLaunchOptions) {
    if (paramAllocation.getType().getElement().getVectorSize() >= this.mOut.getType().getElement().getVectorSize()) {
      if (paramAllocation.getType().getElement().isCompatible(Element.U8(this.mRS)) || paramAllocation.getType().getElement().isCompatible(Element.U8_2(this.mRS)) || paramAllocation.getType().getElement().isCompatible(Element.U8_3(this.mRS)) || paramAllocation.getType().getElement().isCompatible(Element.U8_4(this.mRS))) {
        forEach(0, paramAllocation, (Allocation)null, (FieldPacker)null, paramLaunchOptions);
        return;
      } 
      throw new RSIllegalArgumentException("Input type must be U8, U8_1, U8_2 or U8_4.");
    } 
    throw new RSIllegalArgumentException("Input vector size must be >= output vector size.");
  }
  
  public void forEach_Dot(Allocation paramAllocation) {
    forEach_Dot(paramAllocation, (Script.LaunchOptions)null);
  }
  
  public void forEach_Dot(Allocation paramAllocation, Script.LaunchOptions paramLaunchOptions) {
    if (this.mOut.getType().getElement().getVectorSize() == 1) {
      if (paramAllocation.getType().getElement().isCompatible(Element.U8(this.mRS)) || paramAllocation.getType().getElement().isCompatible(Element.U8_2(this.mRS)) || paramAllocation.getType().getElement().isCompatible(Element.U8_3(this.mRS)) || paramAllocation.getType().getElement().isCompatible(Element.U8_4(this.mRS))) {
        forEach(1, paramAllocation, (Allocation)null, (FieldPacker)null, paramLaunchOptions);
        return;
      } 
      throw new RSIllegalArgumentException("Input type must be U8, U8_1, U8_2 or U8_4.");
    } 
    throw new RSIllegalArgumentException("Output vector size must be one.");
  }
  
  public Script.FieldID getFieldID_Input() {
    return createFieldID(1, (Element)null);
  }
  
  public Script.KernelID getKernelID_Separate() {
    return createKernelID(0, 3, (Element)null, (Element)null);
  }
  
  public void setDotCoefficients(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    if (paramFloat1 >= 0.0F && paramFloat2 >= 0.0F && paramFloat3 >= 0.0F && paramFloat4 >= 0.0F) {
      if (paramFloat1 + paramFloat2 + paramFloat3 + paramFloat4 <= 1.0F) {
        FieldPacker fieldPacker = new FieldPacker(16);
        fieldPacker.addF32(paramFloat1);
        fieldPacker.addF32(paramFloat2);
        fieldPacker.addF32(paramFloat3);
        fieldPacker.addF32(paramFloat4);
        setVar(0, fieldPacker);
        return;
      } 
      throw new RSIllegalArgumentException("Sum of coefficients must be 1.0 or less.");
    } 
    throw new RSIllegalArgumentException("Coefficient may not be negative.");
  }
  
  public void setOutput(Allocation paramAllocation) {
    this.mOut = paramAllocation;
    if (paramAllocation.getType().getElement() == Element.U32(this.mRS) || this.mOut.getType().getElement() == Element.U32_2(this.mRS) || this.mOut.getType().getElement() == Element.U32_3(this.mRS) || this.mOut.getType().getElement() == Element.U32_4(this.mRS) || this.mOut.getType().getElement() == Element.I32(this.mRS) || this.mOut.getType().getElement() == Element.I32_2(this.mRS) || this.mOut.getType().getElement() == Element.I32_3(this.mRS) || this.mOut.getType().getElement() == Element.I32_4(this.mRS)) {
      if (this.mOut.getType().getX() == 256 && this.mOut.getType().getY() == 0 && !this.mOut.getType().hasMipmaps() && this.mOut.getType().getYuv() == 0) {
        setVar(1, paramAllocation);
        return;
      } 
      throw new RSIllegalArgumentException("Output must be 1D, 256 elements.");
    } 
    throw new RSIllegalArgumentException("Output type must be U32 or I32.");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\ScriptIntrinsicHistogram.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */