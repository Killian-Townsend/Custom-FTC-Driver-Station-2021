package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class DeviceConfiguration implements Serializable, Comparable<DeviceConfiguration> {
  public static final String DISABLED_DEVICE_NAME = "NO$DEVICE$ATTACHED";
  
  public static final String TAG = "DeviceConfiguration";
  
  public static final String XMLATTR_NAME = "name";
  
  public static final String XMLATTR_PORT = "port";
  
  private boolean enabled = false;
  
  protected String name;
  
  private int port;
  
  private ConfigurationType type = BuiltInConfigurationType.NOTHING;
  
  public DeviceConfiguration() {
    this(0);
  }
  
  public DeviceConfiguration(int paramInt) {
    this(paramInt, BuiltInConfigurationType.NOTHING, "NO$DEVICE$ATTACHED", false);
  }
  
  public DeviceConfiguration(int paramInt, ConfigurationType paramConfigurationType) {
    this(paramInt, paramConfigurationType, "NO$DEVICE$ATTACHED", false);
  }
  
  public DeviceConfiguration(int paramInt, ConfigurationType paramConfigurationType, String paramString, boolean paramBoolean) {
    this.port = paramInt;
    this.type = paramConfigurationType;
    this.name = paramString;
    this.enabled = paramBoolean;
  }
  
  public DeviceConfiguration(ConfigurationType paramConfigurationType) {
    this(0, paramConfigurationType, "", false);
  }
  
  public static void sortByName(List<? extends DeviceConfiguration> paramList) {
    Collections.sort(paramList, new Comparator<DeviceConfiguration>() {
          public int compare(DeviceConfiguration param1DeviceConfiguration1, DeviceConfiguration param1DeviceConfiguration2) {
            return param1DeviceConfiguration1.getName().compareToIgnoreCase(param1DeviceConfiguration2.getName());
          }
        });
  }
  
  public int compareTo(DeviceConfiguration paramDeviceConfiguration) {
    return getPort() - paramDeviceConfiguration.getPort();
  }
  
  public final void deserialize(XmlPullParser paramXmlPullParser, ReadXMLFileHandler paramReadXMLFileHandler) throws IOException, XmlPullParserException, RobotCoreException {
    String str2 = paramXmlPullParser.getName();
    deserializeAttributes(paramXmlPullParser);
    setConfigurationType(ReadXMLFileHandler.deform(str2));
    setEnabled(true);
    int i = paramXmlPullParser.next();
    String str1 = paramXmlPullParser.getName();
    for (ConfigurationType configurationType = ReadXMLFileHandler.deform(str1); i != 1; configurationType = ReadXMLFileHandler.deform(str1)) {
      if (i == 3 && str1 != null && str1.equals(str2)) {
        onDeserializationComplete(paramReadXMLFileHandler);
        return;
      } 
      if (i == 2)
        deserializeChildElement(configurationType, paramXmlPullParser, paramReadXMLFileHandler); 
      i = paramXmlPullParser.next();
      str1 = paramXmlPullParser.getName();
    } 
    RobotLog.logAndThrow("Reached the end of the XML file while processing a device.");
  }
  
  protected void deserializeAttributes(XmlPullParser paramXmlPullParser) {
    int i;
    setName(paramXmlPullParser.getAttributeValue(null, "name"));
    String str = paramXmlPullParser.getAttributeValue(null, "port");
    if (str == null) {
      i = -1;
    } else {
      i = Integer.parseInt(str);
    } 
    setPort(i);
  }
  
  protected void deserializeChildElement(ConfigurationType paramConfigurationType, XmlPullParser paramXmlPullParser, ReadXMLFileHandler paramReadXMLFileHandler) throws IOException, XmlPullParserException, RobotCoreException {}
  
  public ConfigurationType getConfigurationType() {
    return this.type;
  }
  
  public I2cChannel getI2cChannel() {
    return new I2cChannel(getPort());
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getPort() {
    return this.port;
  }
  
  public ConfigurationType getSpinnerChoiceType() {
    return getConfigurationType();
  }
  
  public boolean isEnabled() {
    return this.enabled;
  }
  
  protected void onDeserializationComplete(ReadXMLFileHandler paramReadXMLFileHandler) {
    paramReadXMLFileHandler.onDeviceParsed(this);
  }
  
  public void serializeXmlAttributes(XmlSerializer paramXmlSerializer) {
    try {
      paramXmlSerializer.attribute("", "name", getName());
      paramXmlSerializer.attribute("", "port", String.valueOf(getPort()));
      return;
    } catch (Exception exception) {
      RobotLog.ee("DeviceConfiguration", exception, "exception serializing");
      throw new RuntimeException(exception);
    } 
  }
  
  public void setConfigurationType(ConfigurationType paramConfigurationType) {
    this.type = paramConfigurationType;
  }
  
  public void setEnabled(boolean paramBoolean) {
    this.enabled = paramBoolean;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setPort(int paramInt) {
    this.port = paramInt;
  }
  
  public static class I2cChannel {
    public final int channel;
    
    public I2cChannel(int param1Int) {
      this.channel = param1Int;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("channel=");
      stringBuilder.append(this.channel);
      return stringBuilder.toString();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\DeviceConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */