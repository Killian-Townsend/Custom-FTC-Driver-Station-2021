package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class LynxUsbDeviceConfiguration extends ControllerConfiguration<LynxModuleConfiguration> {
  private static final boolean ASSUME_EMBEDDED_MODULE_ADDRESS = AppUtil.getInstance().isRobotController();
  
  public static final String XMLATTR_PARENT_MODULE_ADDRESS = "parentModuleAddress";
  
  int parentModuleAddress = 1;
  
  private int recordedParentModuleAddress = 1;
  
  public LynxUsbDeviceConfiguration() {
    super("", new LinkedList<LynxModuleConfiguration>(), SerialNumber.createFake(), BuiltInConfigurationType.LYNX_USB_DEVICE);
  }
  
  public LynxUsbDeviceConfiguration(String paramString, List<LynxModuleConfiguration> paramList, SerialNumber paramSerialNumber) {
    super(paramString, new LinkedList<LynxModuleConfiguration>(paramList), paramSerialNumber, BuiltInConfigurationType.LYNX_USB_DEVICE);
    finishInitialization();
  }
  
  private void finishInitialization() {
    Object object;
    Collections.sort(getModules(), (Comparator)new Comparator<DeviceConfiguration>() {
          public int compare(DeviceConfiguration param1DeviceConfiguration1, DeviceConfiguration param1DeviceConfiguration2) {
            return (param1DeviceConfiguration1.getPort() == 173) ? -1 : (param1DeviceConfiguration1.getPort() - param1DeviceConfiguration2.getPort());
          }
        });
    Iterator<LynxModuleConfiguration> iterator = getModules().iterator();
    boolean bool = false;
    while (iterator.hasNext()) {
      LynxModuleConfiguration lynxModuleConfiguration = iterator.next();
      lynxModuleConfiguration.setUsbDeviceSerialNumber(getSerialNumber());
      if (lynxModuleConfiguration.isParent())
        setParentModuleAddress(lynxModuleConfiguration.getModuleAddress()); 
      if (lynxModuleConfiguration.getModuleAddress() == 173) {
        int i = object + 1;
        if (getSerialNumber().isEmbedded()) {
          int j = i;
          if (i > 1)
            continue; 
          continue;
        } 
        continue;
      } 
      continue;
      RobotLog.setGlobalErrorMsg("An Expansion Hub is configured with address 173, which is reserved for the Control Hub. You need to change the Expansion Hub's address, and make a new configuration file");
      object = SYNTHETIC_LOCAL_VARIABLE_2;
    } 
  }
  
  protected void deserializeAttributes(XmlPullParser paramXmlPullParser) {
    super.deserializeAttributes(paramXmlPullParser);
    String str = paramXmlPullParser.getAttributeValue(null, "parentModuleAddress");
    if (str != null && !str.isEmpty())
      this.recordedParentModuleAddress = Integer.parseInt(str); 
    if (ASSUME_EMBEDDED_MODULE_ADDRESS && getSerialNumber().isEmbedded()) {
      setParentModuleAddress(173);
      return;
    } 
    setParentModuleAddress(this.recordedParentModuleAddress);
  }
  
  protected void deserializeChildElement(ConfigurationType paramConfigurationType, XmlPullParser paramXmlPullParser, ReadXMLFileHandler paramReadXMLFileHandler) throws IOException, XmlPullParserException, RobotCoreException {
    super.deserializeChildElement(paramConfigurationType, paramXmlPullParser, paramReadXMLFileHandler);
    if (paramConfigurationType == BuiltInConfigurationType.LYNX_MODULE) {
      boolean bool;
      LynxModuleConfiguration lynxModuleConfiguration = new LynxModuleConfiguration();
      lynxModuleConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      if (ASSUME_EMBEDDED_MODULE_ADDRESS && getSerialNumber().isEmbedded() && lynxModuleConfiguration.getModuleAddress() == this.recordedParentModuleAddress)
        lynxModuleConfiguration.setModuleAddress(173); 
      if (lynxModuleConfiguration.getModuleAddress() == this.parentModuleAddress) {
        bool = true;
      } else {
        bool = false;
      } 
      lynxModuleConfiguration.setIsParent(bool);
      getModules().add(lynxModuleConfiguration);
    } 
  }
  
  public List<LynxModuleConfiguration> getModules() {
    return getDevices();
  }
  
  public int getParentModuleAddress() {
    return this.parentModuleAddress;
  }
  
  protected void onDeserializationComplete(ReadXMLFileHandler paramReadXMLFileHandler) {
    finishInitialization();
    super.onDeserializationComplete(paramReadXMLFileHandler);
  }
  
  public void setParentModuleAddress(int paramInt) {
    this.parentModuleAddress = paramInt;
  }
  
  public void setSerialNumber(SerialNumber paramSerialNumber) {
    super.setSerialNumber(paramSerialNumber);
    Iterator<LynxModuleConfiguration> iterator = getModules().iterator();
    while (iterator.hasNext())
      ((LynxModuleConfiguration)iterator.next()).setUsbDeviceSerialNumber(paramSerialNumber); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\LynxUsbDeviceConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */