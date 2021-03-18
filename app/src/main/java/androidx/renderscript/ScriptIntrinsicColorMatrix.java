package androidx.renderscript;

import android.os.Build;

public class ScriptIntrinsicColorMatrix extends ScriptIntrinsic {
  private static final int INTRINSIC_API_LEVEL = 19;
  
  private final Float4 mAdd = new Float4();
  
  private Allocation mInput;
  
  private final Matrix4f mMatrix = new Matrix4f();
  
  protected ScriptIntrinsicColorMatrix(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicColorMatrix create(RenderScript paramRenderScript, Element paramElement) {
    if (paramElement.isCompatible(Element.U8_4(paramRenderScript))) {
      boolean bool;
      if (paramRenderScript.isUseNative() && Build.VERSION.SDK_INT < 19) {
        bool = true;
      } else {
        bool = false;
      } 
      ScriptIntrinsicColorMatrix scriptIntrinsicColorMatrix = new ScriptIntrinsicColorMatrix(paramRenderScript.nScriptIntrinsicCreate(2, paramElement.getID(paramRenderScript), bool), paramRenderScript);
      scriptIntrinsicColorMatrix.setIncSupp(bool);
      return scriptIntrinsicColorMatrix;
    } 
    throw new RSIllegalArgumentException("Unsupported element type.");
  }
  
  private void setMatrix() {
    FieldPacker fieldPacker = new FieldPacker(64);
    fieldPacker.addMatrix(this.mMatrix);
    setVar(0, fieldPacker);
  }
  
  public void forEach(Allocation paramAllocation1, Allocation paramAllocation2) {
    forEach(0, paramAllocation1, paramAllocation2, (FieldPacker)null);
  }
  
  public void forEach(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions) {
    if (paramAllocation1.getElement().isCompatible(Element.U8(this.mRS)) || paramAllocation1.getElement().isCompatible(Element.U8_2(this.mRS)) || paramAllocation1.getElement().isCompatible(Element.U8_3(this.mRS)) || paramAllocation1.getElement().isCompatible(Element.U8_4(this.mRS)) || paramAllocation1.getElement().isCompatible(Element.F32(this.mRS)) || paramAllocation1.getElement().isCompatible(Element.F32_2(this.mRS)) || paramAllocation1.getElement().isCompatible(Element.F32_3(this.mRS)) || paramAllocation1.getElement().isCompatible(Element.F32_4(this.mRS))) {
      if (paramAllocation2.getElement().isCompatible(Element.U8(this.mRS)) || paramAllocation2.getElement().isCompatible(Element.U8_2(this.mRS)) || paramAllocation2.getElement().isCompatible(Element.U8_3(this.mRS)) || paramAllocation2.getElement().isCompatible(Element.U8_4(this.mRS)) || paramAllocation2.getElement().isCompatible(Element.F32(this.mRS)) || paramAllocation2.getElement().isCompatible(Element.F32_2(this.mRS)) || paramAllocation2.getElement().isCompatible(Element.F32_3(this.mRS)) || paramAllocation2.getElement().isCompatible(Element.F32_4(this.mRS))) {
        forEach(0, paramAllocation1, paramAllocation2, (FieldPacker)null, paramLaunchOptions);
        return;
      } 
      throw new RSIllegalArgumentException("Unsupported element type.");
    } 
    throw new RSIllegalArgumentException("Unsupported element type.");
  }
  
  public Script.KernelID getKernelID() {
    return createKernelID(0, 3, (Element)null, (Element)null);
  }
  
  public void setAdd(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    this.mAdd.x = paramFloat1;
    this.mAdd.y = paramFloat2;
    this.mAdd.z = paramFloat3;
    this.mAdd.w = paramFloat4;
    FieldPacker fieldPacker = new FieldPacker(16);
    fieldPacker.addF32(this.mAdd.x);
    fieldPacker.addF32(this.mAdd.y);
    fieldPacker.addF32(this.mAdd.z);
    fieldPacker.addF32(this.mAdd.w);
    setVar(1, fieldPacker);
  }
  
  public void setAdd(Float4 paramFloat4) {
    this.mAdd.x = paramFloat4.x;
    this.mAdd.y = paramFloat4.y;
    this.mAdd.z = paramFloat4.z;
    this.mAdd.w = paramFloat4.w;
    FieldPacker fieldPacker = new FieldPacker(16);
    fieldPacker.addF32(paramFloat4.x);
    fieldPacker.addF32(paramFloat4.y);
    fieldPacker.addF32(paramFloat4.z);
    fieldPacker.addF32(paramFloat4.w);
    setVar(1, fieldPacker);
  }
  
  public void setColorMatrix(Matrix3f paramMatrix3f) {
    this.mMatrix.load(paramMatrix3f);
    setMatrix();
  }
  
  public void setColorMatrix(Matrix4f paramMatrix4f) {
    this.mMatrix.load(paramMatrix4f);
    setMatrix();
  }
  
  public void setGreyscale() {
    this.mMatrix.loadIdentity();
    this.mMatrix.set(0, 0, 0.299F);
    this.mMatrix.set(1, 0, 0.587F);
    this.mMatrix.set(2, 0, 0.114F);
    this.mMatrix.set(0, 1, 0.299F);
    this.mMatrix.set(1, 1, 0.587F);
    this.mMatrix.set(2, 1, 0.114F);
    this.mMatrix.set(0, 2, 0.299F);
    this.mMatrix.set(1, 2, 0.587F);
    this.mMatrix.set(2, 2, 0.114F);
    setMatrix();
  }
  
  public void setRGBtoYUV() {
    this.mMatrix.loadIdentity();
    this.mMatrix.set(0, 0, 0.299F);
    this.mMatrix.set(1, 0, 0.587F);
    this.mMatrix.set(2, 0, 0.114F);
    this.mMatrix.set(0, 1, -0.14713F);
    this.mMatrix.set(1, 1, -0.28886F);
    this.mMatrix.set(2, 1, 0.436F);
    this.mMatrix.set(0, 2, 0.615F);
    this.mMatrix.set(1, 2, -0.51499F);
    this.mMatrix.set(2, 2, -0.10001F);
    setMatrix();
  }
  
  public void setYUVtoRGB() {
    this.mMatrix.loadIdentity();
    this.mMatrix.set(0, 0, 1.0F);
    this.mMatrix.set(1, 0, 0.0F);
    this.mMatrix.set(2, 0, 1.13983F);
    this.mMatrix.set(0, 1, 1.0F);
    this.mMatrix.set(1, 1, -0.39465F);
    this.mMatrix.set(2, 1, -0.5806F);
    this.mMatrix.set(0, 2, 1.0F);
    this.mMatrix.set(1, 2, 2.03211F);
    this.mMatrix.set(2, 2, 0.0F);
    setMatrix();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\ScriptIntrinsicColorMatrix.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */