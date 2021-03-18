package com.qualcomm.robotcore.hardware;

import android.text.TextUtils;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.SerialNumber;
import org.firstinspires.ftc.robotcore.internal.hardware.CachedLynxFirmwareVersions;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public final class USBAccessibleLynxModule {
  private static final String FW_UNAVAILABLE;
  
  protected String firmwareVersionString = "";
  
  protected String formattedFirmwareVersionString = FW_UNAVAILABLE;
  
  protected int moduleAddress = 0;
  
  protected boolean moduleAddressChangeable = true;
  
  protected SerialNumber serialNumber = null;
  
  static {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("(");
    stringBuilder.append(AppUtil.getDefContext().getString(R.string.lynxUnavailableFWVersionString));
    stringBuilder.append(")");
    FW_UNAVAILABLE = stringBuilder.toString();
  }
  
  public USBAccessibleLynxModule(SerialNumber paramSerialNumber) {
    setSerialNumber(paramSerialNumber);
  }
  
  public USBAccessibleLynxModule(SerialNumber paramSerialNumber, boolean paramBoolean) {
    setSerialNumber(paramSerialNumber);
    setModuleAddressChangeable(paramBoolean);
  }
  
  public String getFinishedFirmwareVersionString() {
    String str = getFirmwareVersionString();
    return TextUtils.isEmpty(str) ? FW_UNAVAILABLE : CachedLynxFirmwareVersions.formatFirmwareVersion(str);
  }
  
  public String getFirmwareVersionString() {
    return this.firmwareVersionString;
  }
  
  public int getModuleAddress() {
    return this.moduleAddress;
  }
  
  public SerialNumber getSerialNumber() {
    return this.serialNumber;
  }
  
  public boolean isModuleAddressChangeable() {
    return this.moduleAddressChangeable;
  }
  
  public void setFirmwareVersionString(String paramString) {
    this.firmwareVersionString = paramString;
    this.formattedFirmwareVersionString = getFinishedFirmwareVersionString();
  }
  
  public void setModuleAddress(int paramInt) {
    this.moduleAddress = paramInt;
  }
  
  public void setModuleAddressChangeable(boolean paramBoolean) {
    this.moduleAddressChangeable = paramBoolean;
  }
  
  public void setSerialNumber(SerialNumber paramSerialNumber) {
    this.serialNumber = paramSerialNumber;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\USBAccessibleLynxModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */