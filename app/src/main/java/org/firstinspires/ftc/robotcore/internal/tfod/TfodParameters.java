package org.firstinspires.ftc.robotcore.internal.tfod;

public class TfodParameters {
  public final int inputSize;
  
  public final boolean isModelQuantized;
  
  public final double maxFrameRate;
  
  public final int maxNumDetections;
  
  public final float minResultConfidence;
  
  public final int numExecutorThreads;
  
  public final int numInterpreterThreads;
  
  public final int timingBufferSize;
  
  public final boolean trackerDisable;
  
  public final float trackerMarginalCorrelation;
  
  public final float trackerMaxOverlap;
  
  public final float trackerMinCorrelation;
  
  public final float trackerMinSize;
  
  private TfodParameters(boolean paramBoolean1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble, int paramInt5, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, boolean paramBoolean2) {
    this.isModelQuantized = paramBoolean1;
    this.inputSize = paramInt1;
    this.numInterpreterThreads = paramInt2;
    this.numExecutorThreads = paramInt3;
    this.timingBufferSize = paramInt4;
    this.maxFrameRate = paramDouble;
    this.maxNumDetections = paramInt5;
    this.minResultConfidence = paramFloat1;
    this.trackerMaxOverlap = paramFloat2;
    this.trackerMinSize = paramFloat3;
    this.trackerMarginalCorrelation = paramFloat4;
    this.trackerMinCorrelation = paramFloat5;
    this.trackerDisable = paramBoolean2;
  }
  
  public static class Builder {
    private int inputSize = 300;
    
    private boolean isModelQuantized = true;
    
    private double maxFrameRate = 30.0D;
    
    private int maxNumDetections = 10;
    
    private float minResultConfidence = 0.4F;
    
    private int numExecutorThreads = 2;
    
    private int numInterpreterThreads = 1;
    
    private int timingBufferSize = 10;
    
    private boolean trackerDisable = false;
    
    private float trackerMarginalCorrelation = 0.75F;
    
    private float trackerMaxOverlap = 0.2F;
    
    private float trackerMinCorrelation = 0.3F;
    
    private float trackerMinSize = 16.0F;
    
    public Builder() {}
    
    public Builder(boolean param1Boolean, int param1Int) {
      this.isModelQuantized = param1Boolean;
      this.inputSize = param1Int;
    }
    
    public TfodParameters build() {
      return new TfodParameters(this.isModelQuantized, this.inputSize, this.numInterpreterThreads, this.numExecutorThreads, this.timingBufferSize, this.maxFrameRate, this.maxNumDetections, this.minResultConfidence, this.trackerMaxOverlap, this.trackerMinSize, this.trackerMarginalCorrelation, this.trackerMinCorrelation, this.trackerDisable);
    }
    
    public Builder maxFrameRate(double param1Double) {
      if (param1Double > 0.0D && param1Double <= 100.0D) {
        this.maxFrameRate = param1Double;
        return this;
      } 
      throw new IllegalArgumentException("maxFrameRate must be in range (0, 100] (Hz)");
    }
    
    public Builder maxNumDetections(int param1Int) {
      if (param1Int > 0) {
        this.maxNumDetections = param1Int;
        return this;
      } 
      throw new IllegalArgumentException("maxNumDetections must be at least 1");
    }
    
    public Builder minResultConfidence(float param1Float) {
      if (!Float.isNaN(param1Float)) {
        this.minResultConfidence = param1Float;
        return this;
      } 
      throw new IllegalArgumentException("minResultConfidence cannot be NaN");
    }
    
    public Builder numExecutorThreads(int param1Int) {
      if (param1Int > 0) {
        this.numExecutorThreads = param1Int;
        return this;
      } 
      throw new IllegalArgumentException("Must have at least 1 executor worker thread");
    }
    
    public Builder numInterpreterThreads(int param1Int) {
      if (param1Int > 0) {
        this.numInterpreterThreads = param1Int;
        return this;
      } 
      throw new IllegalArgumentException("Must have at least 1 thread per interpreter");
    }
    
    public Builder timingBufferSize(int param1Int) {
      if (param1Int > 0) {
        this.timingBufferSize = param1Int;
        return this;
      } 
      throw new IllegalArgumentException("timingBufferSize must be at least 1");
    }
    
    public Builder trackerDisable(boolean param1Boolean) {
      this.trackerDisable = param1Boolean;
      return this;
    }
    
    public Builder trackerMarginalCorrelation(float param1Float) {
      this.trackerMarginalCorrelation = param1Float;
      return this;
    }
    
    public Builder trackerMaxOverlap(float param1Float) {
      this.trackerMaxOverlap = param1Float;
      return this;
    }
    
    public Builder trackerMinCorrelation(float param1Float) {
      this.trackerMinCorrelation = param1Float;
      return this;
    }
    
    public Builder trackerMinSize(float param1Float) {
      this.trackerMinSize = param1Float;
      return this;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\TfodParameters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */