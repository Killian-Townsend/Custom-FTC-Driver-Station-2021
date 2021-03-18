package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.R;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class PWMOutputImpl implements PWMOutput {
  protected PWMOutputController controller = null;
  
  protected int port = -1;
  
  public PWMOutputImpl(PWMOutputController paramPWMOutputController, int paramInt) {
    this.controller = paramPWMOutputController;
    this.port = paramInt;
  }
  
  public void close() {}
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.controller.getConnectionInfo());
    stringBuilder.append("; port ");
    stringBuilder.append(this.port);
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypePulseWidthDevice);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return this.controller.getManufacturer();
  }
  
  public int getPulseWidthOutputTime() {
    return this.controller.getPulseWidthOutputTime(this.port);
  }
  
  public int getPulseWidthPeriod() {
    return this.controller.getPulseWidthPeriod(this.port);
  }
  
  public int getVersion() {
    return 1;
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public void setPulseWidthOutputTime(int paramInt) {
    this.controller.setPulseWidthOutputTime(this.port, paramInt);
  }
  
  public void setPulseWidthPeriod(int paramInt) {
    this.controller.setPulseWidthPeriod(this.port, paramInt);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\PWMOutputImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */