package androidx.renderscript;

import android.os.Build;

public class ScriptIntrinsicLUT extends ScriptIntrinsic {
  private static final int INTRINSIC_API_LEVEL = 19;
  
  private final byte[] mCache = new byte[1024];
  
  private boolean mDirty = true;
  
  private final Matrix4f mMatrix = new Matrix4f();
  
  private Allocation mTables;
  
  protected ScriptIntrinsicLUT(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicLUT create(RenderScript paramRenderScript, Element paramElement) {
    boolean bool;
    if (paramRenderScript.isUseNative() && Build.VERSION.SDK_INT < 19) {
      bool = true;
    } else {
      bool = false;
    } 
    ScriptIntrinsicLUT scriptIntrinsicLUT = new ScriptIntrinsicLUT(paramRenderScript.nScriptIntrinsicCreate(3, paramElement.getID(paramRenderScript), bool), paramRenderScript);
    scriptIntrinsicLUT.setIncSupp(bool);
    scriptIntrinsicLUT.mTables = Allocation.createSized(paramRenderScript, Element.U8(paramRenderScript), 1024);
    for (int i = 0; i < 256; i++) {
      byte[] arrayOfByte = scriptIntrinsicLUT.mCache;
      byte b = (byte)i;
      arrayOfByte[i] = b;
      arrayOfByte[i + 256] = b;
      arrayOfByte[i + 512] = b;
      arrayOfByte[i + 768] = b;
    } 
    scriptIntrinsicLUT.setVar(0, scriptIntrinsicLUT.mTables);
    return scriptIntrinsicLUT;
  }
  
  private void validate(int paramInt1, int paramInt2) {
    if (paramInt1 >= 0 && paramInt1 <= 255) {
      if (paramInt2 >= 0 && paramInt2 <= 255)
        return; 
      throw new RSIllegalArgumentException("Value out of range (0-255).");
    } 
    throw new RSIllegalArgumentException("Index out of range (0-255).");
  }
  
  public void forEach(Allocation paramAllocation1, Allocation paramAllocation2) {
    if (this.mDirty) {
      this.mDirty = false;
      this.mTables.copyFromUnchecked(this.mCache);
    } 
    forEach(0, paramAllocation1, paramAllocation2, (FieldPacker)null);
  }
  
  public Script.KernelID getKernelID() {
    return createKernelID(0, 3, null, null);
  }
  
  public void setAlpha(int paramInt1, int paramInt2) {
    validate(paramInt1, paramInt2);
    this.mCache[paramInt1 + 768] = (byte)paramInt2;
    this.mDirty = true;
  }
  
  public void setBlue(int paramInt1, int paramInt2) {
    validate(paramInt1, paramInt2);
    this.mCache[paramInt1 + 512] = (byte)paramInt2;
    this.mDirty = true;
  }
  
  public void setGreen(int paramInt1, int paramInt2) {
    validate(paramInt1, paramInt2);
    this.mCache[paramInt1 + 256] = (byte)paramInt2;
    this.mDirty = true;
  }
  
  public void setRed(int paramInt1, int paramInt2) {
    validate(paramInt1, paramInt2);
    this.mCache[paramInt1] = (byte)paramInt2;
    this.mDirty = true;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\ScriptIntrinsicLUT.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */