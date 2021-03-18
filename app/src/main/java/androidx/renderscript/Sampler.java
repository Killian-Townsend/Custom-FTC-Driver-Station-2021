package androidx.renderscript;

public class Sampler extends BaseObj {
  float mAniso;
  
  Value mMag;
  
  Value mMin;
  
  Value mWrapR;
  
  Value mWrapS;
  
  Value mWrapT;
  
  Sampler(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
  }
  
  public static Sampler CLAMP_LINEAR(RenderScript paramRenderScript) {
    if (paramRenderScript.mSampler_CLAMP_LINEAR == null) {
      Builder builder = new Builder(paramRenderScript);
      builder.setMinification(Value.LINEAR);
      builder.setMagnification(Value.LINEAR);
      builder.setWrapS(Value.CLAMP);
      builder.setWrapT(Value.CLAMP);
      paramRenderScript.mSampler_CLAMP_LINEAR = builder.create();
    } 
    return paramRenderScript.mSampler_CLAMP_LINEAR;
  }
  
  public static Sampler CLAMP_LINEAR_MIP_LINEAR(RenderScript paramRenderScript) {
    if (paramRenderScript.mSampler_CLAMP_LINEAR_MIP_LINEAR == null) {
      Builder builder = new Builder(paramRenderScript);
      builder.setMinification(Value.LINEAR_MIP_LINEAR);
      builder.setMagnification(Value.LINEAR);
      builder.setWrapS(Value.CLAMP);
      builder.setWrapT(Value.CLAMP);
      paramRenderScript.mSampler_CLAMP_LINEAR_MIP_LINEAR = builder.create();
    } 
    return paramRenderScript.mSampler_CLAMP_LINEAR_MIP_LINEAR;
  }
  
  public static Sampler CLAMP_NEAREST(RenderScript paramRenderScript) {
    if (paramRenderScript.mSampler_CLAMP_NEAREST == null) {
      Builder builder = new Builder(paramRenderScript);
      builder.setMinification(Value.NEAREST);
      builder.setMagnification(Value.NEAREST);
      builder.setWrapS(Value.CLAMP);
      builder.setWrapT(Value.CLAMP);
      paramRenderScript.mSampler_CLAMP_NEAREST = builder.create();
    } 
    return paramRenderScript.mSampler_CLAMP_NEAREST;
  }
  
  public static Sampler MIRRORED_REPEAT_LINEAR(RenderScript paramRenderScript) {
    if (paramRenderScript.mSampler_MIRRORED_REPEAT_LINEAR == null) {
      Builder builder = new Builder(paramRenderScript);
      builder.setMinification(Value.LINEAR);
      builder.setMagnification(Value.LINEAR);
      builder.setWrapS(Value.MIRRORED_REPEAT);
      builder.setWrapT(Value.MIRRORED_REPEAT);
      paramRenderScript.mSampler_MIRRORED_REPEAT_LINEAR = builder.create();
    } 
    return paramRenderScript.mSampler_MIRRORED_REPEAT_LINEAR;
  }
  
  public static Sampler MIRRORED_REPEAT_NEAREST(RenderScript paramRenderScript) {
    if (paramRenderScript.mSampler_MIRRORED_REPEAT_NEAREST == null) {
      Builder builder = new Builder(paramRenderScript);
      builder.setMinification(Value.NEAREST);
      builder.setMagnification(Value.NEAREST);
      builder.setWrapS(Value.MIRRORED_REPEAT);
      builder.setWrapT(Value.MIRRORED_REPEAT);
      paramRenderScript.mSampler_MIRRORED_REPEAT_NEAREST = builder.create();
    } 
    return paramRenderScript.mSampler_MIRRORED_REPEAT_NEAREST;
  }
  
  public static Sampler WRAP_LINEAR(RenderScript paramRenderScript) {
    if (paramRenderScript.mSampler_WRAP_LINEAR == null) {
      Builder builder = new Builder(paramRenderScript);
      builder.setMinification(Value.LINEAR);
      builder.setMagnification(Value.LINEAR);
      builder.setWrapS(Value.WRAP);
      builder.setWrapT(Value.WRAP);
      paramRenderScript.mSampler_WRAP_LINEAR = builder.create();
    } 
    return paramRenderScript.mSampler_WRAP_LINEAR;
  }
  
  public static Sampler WRAP_LINEAR_MIP_LINEAR(RenderScript paramRenderScript) {
    if (paramRenderScript.mSampler_WRAP_LINEAR_MIP_LINEAR == null) {
      Builder builder = new Builder(paramRenderScript);
      builder.setMinification(Value.LINEAR_MIP_LINEAR);
      builder.setMagnification(Value.LINEAR);
      builder.setWrapS(Value.WRAP);
      builder.setWrapT(Value.WRAP);
      paramRenderScript.mSampler_WRAP_LINEAR_MIP_LINEAR = builder.create();
    } 
    return paramRenderScript.mSampler_WRAP_LINEAR_MIP_LINEAR;
  }
  
  public static Sampler WRAP_NEAREST(RenderScript paramRenderScript) {
    if (paramRenderScript.mSampler_WRAP_NEAREST == null) {
      Builder builder = new Builder(paramRenderScript);
      builder.setMinification(Value.NEAREST);
      builder.setMagnification(Value.NEAREST);
      builder.setWrapS(Value.WRAP);
      builder.setWrapT(Value.WRAP);
      paramRenderScript.mSampler_WRAP_NEAREST = builder.create();
    } 
    return paramRenderScript.mSampler_WRAP_NEAREST;
  }
  
  public float getAnisotropy() {
    return this.mAniso;
  }
  
  public Value getMagnification() {
    return this.mMag;
  }
  
  public Value getMinification() {
    return this.mMin;
  }
  
  public Value getWrapS() {
    return this.mWrapS;
  }
  
  public Value getWrapT() {
    return this.mWrapT;
  }
  
  public static class Builder {
    float mAniso;
    
    Sampler.Value mMag;
    
    Sampler.Value mMin;
    
    RenderScript mRS;
    
    Sampler.Value mWrapR;
    
    Sampler.Value mWrapS;
    
    Sampler.Value mWrapT;
    
    public Builder(RenderScript param1RenderScript) {
      this.mRS = param1RenderScript;
      this.mMin = Sampler.Value.NEAREST;
      this.mMag = Sampler.Value.NEAREST;
      this.mWrapS = Sampler.Value.WRAP;
      this.mWrapT = Sampler.Value.WRAP;
      this.mWrapR = Sampler.Value.WRAP;
      this.mAniso = 1.0F;
    }
    
    public Sampler create() {
      this.mRS.validate();
      Sampler sampler = new Sampler(this.mRS.nSamplerCreate(this.mMag.mID, this.mMin.mID, this.mWrapS.mID, this.mWrapT.mID, this.mWrapR.mID, this.mAniso), this.mRS);
      sampler.mMin = this.mMin;
      sampler.mMag = this.mMag;
      sampler.mWrapS = this.mWrapS;
      sampler.mWrapT = this.mWrapT;
      sampler.mWrapR = this.mWrapR;
      sampler.mAniso = this.mAniso;
      return sampler;
    }
    
    public void setAnisotropy(float param1Float) {
      if (param1Float >= 0.0F) {
        this.mAniso = param1Float;
        return;
      } 
      throw new IllegalArgumentException("Invalid value");
    }
    
    public void setMagnification(Sampler.Value param1Value) {
      if (param1Value == Sampler.Value.NEAREST || param1Value == Sampler.Value.LINEAR) {
        this.mMag = param1Value;
        return;
      } 
      throw new IllegalArgumentException("Invalid value");
    }
    
    public void setMinification(Sampler.Value param1Value) {
      if (param1Value == Sampler.Value.NEAREST || param1Value == Sampler.Value.LINEAR || param1Value == Sampler.Value.LINEAR_MIP_LINEAR || param1Value == Sampler.Value.LINEAR_MIP_NEAREST) {
        this.mMin = param1Value;
        return;
      } 
      throw new IllegalArgumentException("Invalid value");
    }
    
    public void setWrapS(Sampler.Value param1Value) {
      if (param1Value == Sampler.Value.WRAP || param1Value == Sampler.Value.CLAMP || param1Value == Sampler.Value.MIRRORED_REPEAT) {
        this.mWrapS = param1Value;
        return;
      } 
      throw new IllegalArgumentException("Invalid value");
    }
    
    public void setWrapT(Sampler.Value param1Value) {
      if (param1Value == Sampler.Value.WRAP || param1Value == Sampler.Value.CLAMP || param1Value == Sampler.Value.MIRRORED_REPEAT) {
        this.mWrapT = param1Value;
        return;
      } 
      throw new IllegalArgumentException("Invalid value");
    }
  }
  
  public enum Value {
    CLAMP,
    LINEAR,
    LINEAR_MIP_LINEAR,
    LINEAR_MIP_NEAREST,
    MIRRORED_REPEAT,
    NEAREST(0),
    WRAP(0);
    
    int mID;
    
    static {
      CLAMP = new Value("CLAMP", 5, 4);
      Value value = new Value("MIRRORED_REPEAT", 6, 6);
      MIRRORED_REPEAT = value;
      $VALUES = new Value[] { NEAREST, LINEAR, LINEAR_MIP_LINEAR, LINEAR_MIP_NEAREST, WRAP, CLAMP, value };
    }
    
    Value(int param1Int1) {
      this.mID = param1Int1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\Sampler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */