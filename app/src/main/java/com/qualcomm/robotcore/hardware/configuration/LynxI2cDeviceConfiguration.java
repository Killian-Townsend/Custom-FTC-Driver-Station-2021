package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.util.RobotLog;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public class LynxI2cDeviceConfiguration extends DeviceConfiguration {
  public static final String TAG = "LynxI2cDeviceConfiguration";
  
  public static final String XMLATTR_BUS = "bus";
  
  protected int bus = 0;
  
  public void deserializeAttributes(XmlPullParser paramXmlPullParser) {
    int i;
    super.deserializeAttributes(paramXmlPullParser);
    String str = paramXmlPullParser.getAttributeValue(null, "bus");
    if (str == null) {
      i = getPort();
    } else {
      i = Integer.parseInt(str);
    } 
    setBus(i);
  }
  
  public int getBus() {
    return this.bus;
  }
  
  public DeviceConfiguration.I2cChannel getI2cChannel() {
    return new DeviceConfiguration.I2cChannel(getBus());
  }
  
  public void serializeXmlAttributes(XmlSerializer paramXmlSerializer) {
    try {
      super.serializeXmlAttributes(paramXmlSerializer);
      paramXmlSerializer.attribute("", "bus", String.valueOf(getBus()));
      return;
    } catch (Exception exception) {
      RobotLog.ee("LynxI2cDeviceConfiguration", exception, "exception serializing");
      throw new RuntimeException(exception);
    } 
  }
  
  public void setBus(int paramInt) {
    this.bus = paramInt;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\LynxI2cDeviceConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */