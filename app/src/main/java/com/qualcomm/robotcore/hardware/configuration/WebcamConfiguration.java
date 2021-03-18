package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.util.SerialNumber;
import java.util.LinkedList;
import org.xmlpull.v1.XmlPullParser;

public class WebcamConfiguration extends ControllerConfiguration<DeviceConfiguration> {
  public static final String XMLATTR_AUTO_OPEN_CAMERA = "autoOpen";
  
  protected boolean autoOpen;
  
  public WebcamConfiguration() {
    this("", SerialNumber.createFake());
  }
  
  public WebcamConfiguration(String paramString, SerialNumber paramSerialNumber) {
    this(paramString, paramSerialNumber, false);
  }
  
  public WebcamConfiguration(String paramString, SerialNumber paramSerialNumber, boolean paramBoolean) {
    super(paramString, new LinkedList<DeviceConfiguration>(), paramSerialNumber, BuiltInConfigurationType.WEBCAM);
    this.autoOpen = paramBoolean;
  }
  
  private void setAutoOpen(boolean paramBoolean) {
    this.autoOpen = paramBoolean;
  }
  
  protected void deserializeAttributes(XmlPullParser paramXmlPullParser) {
    super.deserializeAttributes(paramXmlPullParser);
    String str = paramXmlPullParser.getAttributeValue(null, "autoOpen");
    if (str != null && !str.isEmpty())
      setAutoOpen(Boolean.parseBoolean(str)); 
  }
  
  public boolean getAutoOpen() {
    return this.autoOpen;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\WebcamConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */