package com.qualcomm.ftccommon.configuration;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;

public class EditUSBDeviceActivity extends EditActivity {
  protected ScannedDevices extraUSBDevices = new ScannedDevices();
  
  protected boolean completeSwapConfiguration(int paramInt1, int paramInt2, Intent paramIntent) {
    if (paramInt2 == -1 && RequestCode.fromValue(paramInt1) == EditSwapUsbDevices.requestCode) {
      SerialNumber serialNumber = ((ControllerConfiguration)EditParameters.<DeviceConfiguration>fromIntent(this, paramIntent).getConfiguration()).getSerialNumber();
      ControllerConfiguration controllerConfiguration = getRobotConfigMap().get(serialNumber);
      if (controllerConfiguration != null) {
        this.robotConfigMap.swapSerialNumbers(this.controllerConfiguration, controllerConfiguration);
      } else {
        this.robotConfigMap.setSerialNumber(this.controllerConfiguration, serialNumber);
        this.controllerConfiguration.setKnownToBeAttached(true);
      } 
      determineExtraUSBDevices();
      refreshAfterSwap();
      return true;
    } 
    return false;
  }
  
  protected void deserialize(EditParameters paramEditParameters) {
    super.deserialize(paramEditParameters);
    determineExtraUSBDevices();
  }
  
  protected void determineExtraUSBDevices() {
    this.extraUSBDevices = new ScannedDevices(this.scannedDevices);
    for (SerialNumber serialNumber : getRobotConfigMap().serialNumbers())
      this.extraUSBDevices.remove(serialNumber); 
    for (ControllerConfiguration controllerConfiguration : getRobotConfigMap().controllerConfigurations())
      controllerConfiguration.setKnownToBeAttached(this.scannedDevices.containsKey(controllerConfiguration.getSerialNumber())); 
  }
  
  protected void fixConfiguration() {
    boolean bool;
    SerialNumber serialNumber = getFixableCandidate();
    if (serialNumber != null) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      this.robotConfigMap.setSerialNumber(this.controllerConfiguration, serialNumber);
      this.controllerConfiguration.setKnownToBeAttached(true);
      determineExtraUSBDevices();
    } else {
      String str1 = getString(R.string.fixFailNoneAvailable);
      String str2 = this.controllerConfiguration.getName();
      String str3 = displayNameOfConfigurationType(ConfigurationType.DisplayNameFlavor.Normal, this.controllerConfiguration.getConfigurationType());
      this.appUtil.showToast(UILocation.ONLY_LOCAL, String.format(str1, new Object[] { str2, str3 }));
    } 
    refreshAfterFix();
  }
  
  protected SerialNumber getFixableCandidate() {
    boolean bool = this.controllerConfiguration.isKnownToBeAttached();
    SerialNumber serialNumber2 = null;
    if (bool)
      return null; 
    DeviceManager.UsbDeviceType usbDeviceType = this.controllerConfiguration.toUSBDeviceType();
    Iterator<Map.Entry> iterator = this.extraUSBDevices.entrySet().iterator();
    boolean bool2 = false;
    SerialNumber serialNumber1 = null;
    boolean bool1 = false;
    while (iterator.hasNext()) {
      Map.Entry entry = iterator.next();
      if (entry.getValue() == usbDeviceType) {
        if (serialNumber1 != null) {
          bool1 = bool2;
          break;
        } 
        serialNumber1 = (SerialNumber)entry.getKey();
        bool1 = true;
      } 
    } 
    if (bool1)
      serialNumber2 = serialNumber1; 
    return serialNumber2;
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
  
  protected boolean isFixable() {
    return (getFixableCandidate() != null);
  }
  
  protected boolean isSwappable() {
    List<ControllerConfiguration> list = getRobotConfigMap().getEligibleSwapTargets(this.controllerConfiguration, this.scannedDevices, (Context)this);
    SerialNumber serialNumber = getFixableCandidate();
    null = list.isEmpty();
    boolean bool = true;
    if (!null) {
      null = bool;
      if (serialNumber != null) {
        null = bool;
        if (list.size() == 1) {
          if (!((ControllerConfiguration)list.get(0)).getSerialNumber().equals(serialNumber))
            return true; 
        } else {
          return null;
        } 
      } else {
        return null;
      } 
    } 
    return false;
  }
  
  protected void refreshAfterFix() {
    showFixSwapButtons();
    this.currentCfgFile.markDirty();
    this.robotConfigFileManager.updateActiveConfigHeader(this.currentCfgFile);
  }
  
  protected void refreshAfterSwap() {
    showFixSwapButtons();
    this.currentCfgFile.markDirty();
    this.robotConfigFileManager.updateActiveConfigHeader(this.currentCfgFile);
  }
  
  protected void refreshSerialNumber() {}
  
  protected void showButton(int paramInt, boolean paramBoolean) {
    View view = findViewById(paramInt);
    if (view != null) {
      if (paramBoolean) {
        paramInt = 0;
      } else {
        paramInt = 8;
      } 
      view.setVisibility(paramInt);
    } 
  }
  
  protected void showFixButton(boolean paramBoolean) {
    showButton(this.idFixButton, paramBoolean);
  }
  
  protected void showFixSwapButtons() {
    showFixButton(isFixable());
    showSwapButton(isSwappable());
    refreshSerialNumber();
  }
  
  protected void showSwapButton(boolean paramBoolean) {
    showButton(this.idSwapButton, paramBoolean);
  }
  
  protected void swapConfiguration() {
    if (getRobotConfigMap().isSwappable(this.controllerConfiguration, this.scannedDevices, (Context)this)) {
      EditParameters<DeviceConfiguration> editParameters = new EditParameters<DeviceConfiguration>(this, (DeviceConfiguration)this.controllerConfiguration);
      editParameters.setRobotConfigMap(getRobotConfigMap());
      editParameters.setScannedDevices(this.scannedDevices);
      handleLaunchEdit(EditSwapUsbDevices.requestCode, EditSwapUsbDevices.class, editParameters);
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditUSBDeviceActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */