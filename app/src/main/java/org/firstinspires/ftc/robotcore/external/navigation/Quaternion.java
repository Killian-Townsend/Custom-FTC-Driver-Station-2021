package org.firstinspires.ftc.robotcore.external.navigation;

public class Quaternion {
  public long acquisitionTime;
  
  public float w;
  
  public float x;
  
  public float y;
  
  public float z;
  
  public Quaternion() {
    this(0.0F, 0.0F, 0.0F, 0.0F, 0L);
  }
  
  public Quaternion(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong) {
    this.w = paramFloat1;
    this.x = paramFloat2;
    this.y = paramFloat3;
    this.z = paramFloat4;
    this.acquisitionTime = paramLong;
  }
  
  public Quaternion congugate() {
    return new Quaternion(this.w, -this.x, -this.y, -this.z, this.acquisitionTime);
  }
  
  public float magnitude() {
    float f1 = this.w;
    float f2 = this.x;
    float f3 = this.y;
    float f4 = this.z;
    return (float)Math.sqrt((f1 * f1 + f2 * f2 + f3 * f3 + f4 * f4));
  }
  
  public Quaternion normalized() {
    float f = magnitude();
    return new Quaternion(this.w / f, this.x / f, this.y / f, this.z / f, this.acquisitionTime);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\Quaternion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */