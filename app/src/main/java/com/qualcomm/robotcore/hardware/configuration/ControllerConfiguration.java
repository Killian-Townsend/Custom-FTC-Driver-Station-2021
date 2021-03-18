package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public abstract class ControllerConfiguration<ITEM_T extends DeviceConfiguration> extends DeviceConfiguration implements Serializable {
  public static final String XMLATTR_SERIAL_NUMBER = "serialNumber";
  
  private List<ITEM_T> devices;
  
  private boolean isSystemSynthetic = false;
  
  private boolean knownToBeAttached = false;
  
  private SerialNumber serialNumber;
  
  public ControllerConfiguration(String paramString, SerialNumber paramSerialNumber, ConfigurationType paramConfigurationType) {
    this(paramString, new ArrayList<ITEM_T>(), paramSerialNumber, paramConfigurationType);
  }
  
  public ControllerConfiguration(String paramString, List<ITEM_T> paramList, SerialNumber paramSerialNumber, ConfigurationType paramConfigurationType) {
    super(paramConfigurationType);
    setName(paramString);
    this.devices = paramList;
    this.serialNumber = paramSerialNumber;
  }
  
  public static ControllerConfiguration forType(String paramString, SerialNumber paramSerialNumber, ConfigurationType paramConfigurationType) {
    return (ControllerConfiguration)((paramConfigurationType == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE) ? new DeviceInterfaceModuleConfiguration(paramString, paramSerialNumber) : ((paramConfigurationType == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER) ? new LegacyModuleControllerConfiguration(paramString, new LinkedList<DeviceConfiguration>(), paramSerialNumber) : ((paramConfigurationType == BuiltInConfigurationType.MATRIX_CONTROLLER) ? new MatrixControllerConfiguration(paramString, new LinkedList<DeviceConfiguration>(), new LinkedList<DeviceConfiguration>(), paramSerialNumber) : ((paramConfigurationType == BuiltInConfigurationType.MOTOR_CONTROLLER) ? new MotorControllerConfiguration(paramString, new LinkedList<DeviceConfiguration>(), paramSerialNumber) : ((paramConfigurationType == BuiltInConfigurationType.SERVO_CONTROLLER) ? new ServoControllerConfiguration(paramString, new LinkedList<DeviceConfiguration>(), paramSerialNumber) : ((paramConfigurationType == BuiltInConfigurationType.LYNX_USB_DEVICE) ? new LynxUsbDeviceConfiguration(paramString, new LinkedList<LynxModuleConfiguration>(), paramSerialNumber) : ((paramConfigurationType == BuiltInConfigurationType.LYNX_MODULE) ? new LynxModuleConfiguration(paramString) : ((paramConfigurationType == BuiltInConfigurationType.WEBCAM) ? new WebcamConfiguration(paramString, paramSerialNumber) : null))))))));
  }
  
  protected void deserializeAttributes(XmlPullParser paramXmlPullParser) {
    super.deserializeAttributes(paramXmlPullParser);
    String str = paramXmlPullParser.getAttributeValue(null, "serialNumber");
    if (str != null) {
      setSerialNumber(SerialNumber.fromString(str));
      setPort(-1);
      return;
    } 
    setSerialNumber(SerialNumber.createFake());
  }
  
  public ConfigurationType getConfigurationType() {
    return super.getConfigurationType();
  }
  
  public List<ITEM_T> getDevices() {
    return this.devices;
  }
  
  public SerialNumber getSerialNumber() {
    return this.serialNumber;
  }
  
  public boolean isKnownToBeAttached() {
    return this.knownToBeAttached;
  }
  
  public boolean isSystemSynthetic() {
    return this.isSystemSynthetic;
  }
  
  public void setDevices(List<ITEM_T> paramList) {
    this.devices = paramList;
  }
  
  public void setKnownToBeAttached(boolean paramBoolean) {
    this.knownToBeAttached = paramBoolean;
  }
  
  public void setSerialNumber(SerialNumber paramSerialNumber) {
    this.serialNumber = paramSerialNumber;
  }
  
  public void setSystemSynthetic(boolean paramBoolean) {
    this.isSystemSynthetic = paramBoolean;
  }
  
  public DeviceManager.UsbDeviceType toUSBDeviceType() {
    return getConfigurationType().toUSBDeviceType();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\ControllerConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */