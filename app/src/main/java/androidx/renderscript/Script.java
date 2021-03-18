package androidx.renderscript;

import android.util.SparseArray;
import java.io.UnsupportedEncodingException;

public class Script extends BaseObj {
  private final SparseArray<FieldID> mFIDs = new SparseArray();
  
  private final SparseArray<InvokeID> mIIDs = new SparseArray();
  
  private final SparseArray<KernelID> mKIDs = new SparseArray();
  
  private boolean mUseIncSupp = false;
  
  Script(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
  }
  
  public void bindAllocation(Allocation paramAllocation, int paramInt) {
    this.mRS.validate();
    if (paramAllocation != null) {
      this.mRS.nScriptBindAllocation(getID(this.mRS), paramAllocation.getID(this.mRS), paramInt, this.mUseIncSupp);
      return;
    } 
    this.mRS.nScriptBindAllocation(getID(this.mRS), 0L, paramInt, this.mUseIncSupp);
  }
  
  protected FieldID createFieldID(int paramInt, Element paramElement) {
    FieldID fieldID = (FieldID)this.mFIDs.get(paramInt);
    if (fieldID != null)
      return fieldID; 
    long l = this.mRS.nScriptFieldIDCreate(getID(this.mRS), paramInt, this.mUseIncSupp);
    if (l != 0L) {
      fieldID = new FieldID(l, this.mRS, this, paramInt);
      this.mFIDs.put(paramInt, fieldID);
      return fieldID;
    } 
    throw new RSDriverException("Failed to create FieldID");
  }
  
  protected InvokeID createInvokeID(int paramInt) {
    InvokeID invokeID = (InvokeID)this.mIIDs.get(paramInt);
    if (invokeID != null)
      return invokeID; 
    long l = this.mRS.nScriptInvokeIDCreate(getID(this.mRS), paramInt);
    if (l != 0L) {
      invokeID = new InvokeID(l, this.mRS, this, paramInt);
      this.mIIDs.put(paramInt, invokeID);
      return invokeID;
    } 
    throw new RSDriverException("Failed to create KernelID");
  }
  
  protected KernelID createKernelID(int paramInt1, int paramInt2, Element paramElement1, Element paramElement2) {
    KernelID kernelID = (KernelID)this.mKIDs.get(paramInt1);
    if (kernelID != null)
      return kernelID; 
    long l = this.mRS.nScriptKernelIDCreate(getID(this.mRS), paramInt1, paramInt2, this.mUseIncSupp);
    if (l != 0L) {
      kernelID = new KernelID(l, this.mRS, this, paramInt1, paramInt2);
      this.mKIDs.put(paramInt1, kernelID);
      return kernelID;
    } 
    throw new RSDriverException("Failed to create KernelID");
  }
  
  protected void forEach(int paramInt, Allocation paramAllocation1, Allocation paramAllocation2, FieldPacker paramFieldPacker) {
    if (paramAllocation1 != null || paramAllocation2 != null) {
      long l1;
      long l2 = 0L;
      if (paramAllocation1 != null) {
        l1 = paramAllocation1.getID(this.mRS);
      } else {
        l1 = 0L;
      } 
      if (paramAllocation2 != null)
        l2 = paramAllocation2.getID(this.mRS); 
      byte[] arrayOfByte = null;
      if (paramFieldPacker != null)
        arrayOfByte = paramFieldPacker.getData(); 
      if (this.mUseIncSupp) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        this.mRS.nScriptForEach(getID(this.mRS), paramInt, l1, l2, arrayOfByte, this.mUseIncSupp);
        return;
      } 
      this.mRS.nScriptForEach(getID(this.mRS), paramInt, l1, l2, arrayOfByte, this.mUseIncSupp);
      return;
    } 
    throw new RSIllegalArgumentException("At least one of ain or aout is required to be non-null.");
  }
  
  protected void forEach(int paramInt, Allocation paramAllocation1, Allocation paramAllocation2, FieldPacker paramFieldPacker, LaunchOptions paramLaunchOptions) {
    if (paramAllocation1 != null || paramAllocation2 != null) {
      long l1;
      if (paramLaunchOptions == null) {
        forEach(paramInt, paramAllocation1, paramAllocation2, paramFieldPacker);
        return;
      } 
      long l2 = 0L;
      if (paramAllocation1 != null) {
        l1 = paramAllocation1.getID(this.mRS);
      } else {
        l1 = 0L;
      } 
      if (paramAllocation2 != null)
        l2 = paramAllocation2.getID(this.mRS); 
      byte[] arrayOfByte = null;
      if (paramFieldPacker != null)
        arrayOfByte = paramFieldPacker.getData(); 
      if (this.mUseIncSupp) {
        l1 = getDummyAlloc(paramAllocation1);
        l2 = getDummyAlloc(paramAllocation2);
        this.mRS.nScriptForEachClipped(getID(this.mRS), paramInt, l1, l2, arrayOfByte, paramLaunchOptions.xstart, paramLaunchOptions.xend, paramLaunchOptions.ystart, paramLaunchOptions.yend, paramLaunchOptions.zstart, paramLaunchOptions.zend, this.mUseIncSupp);
        return;
      } 
      this.mRS.nScriptForEachClipped(getID(this.mRS), paramInt, l1, l2, arrayOfByte, paramLaunchOptions.xstart, paramLaunchOptions.xend, paramLaunchOptions.ystart, paramLaunchOptions.yend, paramLaunchOptions.zstart, paramLaunchOptions.zend, this.mUseIncSupp);
      return;
    } 
    throw new RSIllegalArgumentException("At least one of ain or aout is required to be non-null.");
  }
  
  protected void forEach(int paramInt, Allocation[] paramArrayOfAllocation, Allocation paramAllocation, FieldPacker paramFieldPacker) {
    forEach(paramInt, paramArrayOfAllocation, paramAllocation, paramFieldPacker, (LaunchOptions)null);
  }
  
  protected void forEach(int paramInt, Allocation[] paramArrayOfAllocation, Allocation paramAllocation, FieldPacker paramFieldPacker, LaunchOptions paramLaunchOptions) {
    this.mRS.validate();
    if (paramArrayOfAllocation != null) {
      int j = paramArrayOfAllocation.length;
      int i;
      for (i = 0; i < j; i++) {
        Allocation allocation = paramArrayOfAllocation[i];
        this.mRS.validateObject(allocation);
      } 
    } 
    this.mRS.validateObject(paramAllocation);
    if (paramArrayOfAllocation != null || paramAllocation != null) {
      int[] arrayOfInt;
      long l;
      FieldPacker fieldPacker = null;
      if (paramArrayOfAllocation != null) {
        long[] arrayOfLong2 = new long[paramArrayOfAllocation.length];
        int i;
        for (i = 0; i < paramArrayOfAllocation.length; i++)
          arrayOfLong2[i] = paramArrayOfAllocation[i].getID(this.mRS); 
        long[] arrayOfLong1 = arrayOfLong2;
      } else {
        paramArrayOfAllocation = null;
      } 
      if (paramAllocation != null) {
        l = paramAllocation.getID(this.mRS);
      } else {
        l = 0L;
      } 
      if (paramFieldPacker != null) {
        byte[] arrayOfByte = paramFieldPacker.getData();
      } else {
        paramAllocation = null;
      } 
      paramFieldPacker = fieldPacker;
      if (paramLaunchOptions != null) {
        arrayOfInt = new int[6];
        arrayOfInt[0] = paramLaunchOptions.xstart;
        arrayOfInt[1] = paramLaunchOptions.xend;
        arrayOfInt[2] = paramLaunchOptions.ystart;
        arrayOfInt[3] = paramLaunchOptions.yend;
        arrayOfInt[4] = paramLaunchOptions.zstart;
        arrayOfInt[5] = paramLaunchOptions.zend;
      } 
      this.mRS.nScriptForEach(getID(this.mRS), paramInt, (long[])paramArrayOfAllocation, l, (byte[])paramAllocation, arrayOfInt);
      return;
    } 
    throw new RSIllegalArgumentException("At least one of ain or aout is required to be non-null.");
  }
  
  long getDummyAlloc(Allocation paramAllocation) {
    if (paramAllocation != null) {
      Type type = paramAllocation.getType();
      long l = type.getElement().getDummyElement(this.mRS);
      l = type.getDummyType(this.mRS, l);
      int i = type.getX();
      int j = type.getElement().getBytesSize();
      l = this.mRS.nIncAllocationCreateTyped(paramAllocation.getID(this.mRS), l, i * j);
      paramAllocation.setIncAllocID(l);
      return l;
    } 
    return 0L;
  }
  
  protected void invoke(int paramInt) {
    this.mRS.nScriptInvoke(getID(this.mRS), paramInt, this.mUseIncSupp);
  }
  
  protected void invoke(int paramInt, FieldPacker paramFieldPacker) {
    if (paramFieldPacker != null) {
      this.mRS.nScriptInvokeV(getID(this.mRS), paramInt, paramFieldPacker.getData(), this.mUseIncSupp);
      return;
    } 
    this.mRS.nScriptInvoke(getID(this.mRS), paramInt, this.mUseIncSupp);
  }
  
  protected boolean isIncSupp() {
    return this.mUseIncSupp;
  }
  
  protected void reduce(int paramInt, Allocation[] paramArrayOfAllocation, Allocation paramAllocation, LaunchOptions paramLaunchOptions) {
    this.mRS.validate();
    if (paramArrayOfAllocation != null && paramArrayOfAllocation.length >= 1) {
      if (paramAllocation != null) {
        int[] arrayOfInt;
        int j = paramArrayOfAllocation.length;
        int i;
        for (i = 0; i < j; i++) {
          Allocation allocation = paramArrayOfAllocation[i];
          this.mRS.validateObject(allocation);
        } 
        long[] arrayOfLong = new long[paramArrayOfAllocation.length];
        for (i = 0; i < paramArrayOfAllocation.length; i++)
          arrayOfLong[i] = paramArrayOfAllocation[i].getID(this.mRS); 
        long l = paramAllocation.getID(this.mRS);
        paramArrayOfAllocation = null;
        if (paramLaunchOptions != null) {
          arrayOfInt = new int[6];
          arrayOfInt[0] = paramLaunchOptions.xstart;
          arrayOfInt[1] = paramLaunchOptions.xend;
          arrayOfInt[2] = paramLaunchOptions.ystart;
          arrayOfInt[3] = paramLaunchOptions.yend;
          arrayOfInt[4] = paramLaunchOptions.zstart;
          arrayOfInt[5] = paramLaunchOptions.zend;
        } 
        this.mRS.nScriptReduce(getID(this.mRS), paramInt, arrayOfLong, l, arrayOfInt);
        return;
      } 
      throw new RSIllegalArgumentException("aout is required to be non-null.");
    } 
    throw new RSIllegalArgumentException("At least one input is required.");
  }
  
  protected void setIncSupp(boolean paramBoolean) {
    this.mUseIncSupp = paramBoolean;
  }
  
  public void setTimeZone(String paramString) {
    this.mRS.validate();
    try {
      this.mRS.nScriptSetTimeZone(getID(this.mRS), paramString.getBytes("UTF-8"), this.mUseIncSupp);
      return;
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new RuntimeException(unsupportedEncodingException);
    } 
  }
  
  public void setVar(int paramInt, double paramDouble) {
    this.mRS.nScriptSetVarD(getID(this.mRS), paramInt, paramDouble, this.mUseIncSupp);
  }
  
  public void setVar(int paramInt, float paramFloat) {
    this.mRS.nScriptSetVarF(getID(this.mRS), paramInt, paramFloat, this.mUseIncSupp);
  }
  
  public void setVar(int paramInt1, int paramInt2) {
    this.mRS.nScriptSetVarI(getID(this.mRS), paramInt1, paramInt2, this.mUseIncSupp);
  }
  
  public void setVar(int paramInt, long paramLong) {
    this.mRS.nScriptSetVarJ(getID(this.mRS), paramInt, paramLong, this.mUseIncSupp);
  }
  
  public void setVar(int paramInt, BaseObj paramBaseObj) {
    boolean bool = this.mUseIncSupp;
    long l1 = 0L;
    if (bool) {
      l1 = getDummyAlloc((Allocation)paramBaseObj);
      RenderScript renderScript1 = this.mRS;
      long l = getID(this.mRS);
      if (paramBaseObj == null)
        l1 = 0L; 
      renderScript1.nScriptSetVarObj(l, paramInt, l1, this.mUseIncSupp);
      return;
    } 
    RenderScript renderScript = this.mRS;
    long l2 = getID(this.mRS);
    if (paramBaseObj != null)
      l1 = paramBaseObj.getID(this.mRS); 
    renderScript.nScriptSetVarObj(l2, paramInt, l1, this.mUseIncSupp);
  }
  
  public void setVar(int paramInt, FieldPacker paramFieldPacker) {
    this.mRS.nScriptSetVarV(getID(this.mRS), paramInt, paramFieldPacker.getData(), this.mUseIncSupp);
  }
  
  public void setVar(int paramInt, FieldPacker paramFieldPacker, Element paramElement, int[] paramArrayOfint) {
    if (this.mUseIncSupp) {
      long l = paramElement.getDummyElement(this.mRS);
      this.mRS.nScriptSetVarVE(getID(this.mRS), paramInt, paramFieldPacker.getData(), l, paramArrayOfint, this.mUseIncSupp);
      return;
    } 
    this.mRS.nScriptSetVarVE(getID(this.mRS), paramInt, paramFieldPacker.getData(), paramElement.getID(this.mRS), paramArrayOfint, this.mUseIncSupp);
  }
  
  public void setVar(int paramInt, boolean paramBoolean) {
    throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
  }
  
  public static class Builder {
    RenderScript mRS;
    
    Builder(RenderScript param1RenderScript) {
      this.mRS = param1RenderScript;
    }
  }
  
  public static class FieldBase {
    protected Allocation mAllocation;
    
    protected Element mElement;
    
    public Allocation getAllocation() {
      return this.mAllocation;
    }
    
    public Element getElement() {
      return this.mElement;
    }
    
    public Type getType() {
      return this.mAllocation.getType();
    }
    
    protected void init(RenderScript param1RenderScript, int param1Int) {
      this.mAllocation = Allocation.createSized(param1RenderScript, this.mElement, param1Int, 1);
    }
    
    protected void init(RenderScript param1RenderScript, int param1Int1, int param1Int2) {
      this.mAllocation = Allocation.createSized(param1RenderScript, this.mElement, param1Int1, param1Int2 | 0x1);
    }
    
    public void updateAllocation() {}
  }
  
  public static final class FieldID extends BaseObj {
    android.renderscript.Script.FieldID mN;
    
    Script mScript;
    
    int mSlot;
    
    FieldID(long param1Long, RenderScript param1RenderScript, Script param1Script, int param1Int) {
      super(param1Long, param1RenderScript);
      this.mScript = param1Script;
      this.mSlot = param1Int;
    }
  }
  
  public static final class InvokeID extends BaseObj {
    Script mScript;
    
    int mSlot;
    
    InvokeID(long param1Long, RenderScript param1RenderScript, Script param1Script, int param1Int) {
      super(param1Long, param1RenderScript);
      this.mScript = param1Script;
      this.mSlot = param1Int;
    }
  }
  
  public static final class KernelID extends BaseObj {
    android.renderscript.Script.KernelID mN;
    
    Script mScript;
    
    int mSig;
    
    int mSlot;
    
    KernelID(long param1Long, RenderScript param1RenderScript, Script param1Script, int param1Int1, int param1Int2) {
      super(param1Long, param1RenderScript);
      this.mScript = param1Script;
      this.mSlot = param1Int1;
      this.mSig = param1Int2;
    }
  }
  
  public static final class LaunchOptions {
    private int strategy;
    
    private int xend = 0;
    
    private int xstart = 0;
    
    private int yend = 0;
    
    private int ystart = 0;
    
    private int zend = 0;
    
    private int zstart = 0;
    
    public int getXEnd() {
      return this.xend;
    }
    
    public int getXStart() {
      return this.xstart;
    }
    
    public int getYEnd() {
      return this.yend;
    }
    
    public int getYStart() {
      return this.ystart;
    }
    
    public int getZEnd() {
      return this.zend;
    }
    
    public int getZStart() {
      return this.zstart;
    }
    
    public LaunchOptions setX(int param1Int1, int param1Int2) {
      if (param1Int1 >= 0 && param1Int2 > param1Int1) {
        this.xstart = param1Int1;
        this.xend = param1Int2;
        return this;
      } 
      throw new RSIllegalArgumentException("Invalid dimensions");
    }
    
    public LaunchOptions setY(int param1Int1, int param1Int2) {
      if (param1Int1 >= 0 && param1Int2 > param1Int1) {
        this.ystart = param1Int1;
        this.yend = param1Int2;
        return this;
      } 
      throw new RSIllegalArgumentException("Invalid dimensions");
    }
    
    public LaunchOptions setZ(int param1Int1, int param1Int2) {
      if (param1Int1 >= 0 && param1Int2 > param1Int1) {
        this.zstart = param1Int1;
        this.zend = param1Int2;
        return this;
      } 
      throw new RSIllegalArgumentException("Invalid dimensions");
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\Script.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */