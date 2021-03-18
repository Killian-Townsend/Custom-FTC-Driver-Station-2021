package com.qualcomm.robotcore.hardware;

public class I2cDeviceReader {
  private final I2cDevice i2cDevice;
  
  public I2cDeviceReader(I2cDevice paramI2cDevice, I2cAddr paramI2cAddr, int paramInt1, int paramInt2) {
    this.i2cDevice = paramI2cDevice;
    paramI2cDevice.enableI2cReadMode(paramI2cAddr, paramInt1, paramInt2);
    paramI2cDevice.setI2cPortActionFlag();
    paramI2cDevice.writeI2cCacheToController();
    paramI2cDevice.registerForI2cPortReadyCallback(new I2cController.I2cPortReadyCallback() {
          public void portIsReady(int param1Int) {
            I2cDeviceReader.this.handleCallback();
          }
        });
  }
  
  private void handleCallback() {
    this.i2cDevice.setI2cPortActionFlag();
    this.i2cDevice.readI2cCacheFromController();
    this.i2cDevice.writeI2cPortFlagOnlyToController();
  }
  
  public byte[] getReadBuffer() {
    return this.i2cDevice.getCopyOfReadBuffer();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cDeviceReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */