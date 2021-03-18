package com.qualcomm.robotcore.hardware;

public final class I2cAddr {
  private final int i2cAddr7Bit;
  
  public I2cAddr(int paramInt) {
    this.i2cAddr7Bit = paramInt & 0x7F;
  }
  
  public static I2cAddr create7bit(int paramInt) {
    return new I2cAddr(paramInt);
  }
  
  public static I2cAddr create8bit(int paramInt) {
    return new I2cAddr(paramInt / 2);
  }
  
  public static I2cAddr zero() {
    return create7bit(0);
  }
  
  public int get7Bit() {
    return this.i2cAddr7Bit;
  }
  
  public int get8Bit() {
    return this.i2cAddr7Bit * 2;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cAddr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */