package com.qualcomm.ftccommon.configuration;

import com.google.gson.JsonSyntaxException;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class RobotConfigFile {
  private static final String LOGGER_TAG = "RobotConfigFile";
  
  private boolean isDirty;
  
  private FileLocation location;
  
  private String name;
  
  private int resourceId;
  
  public RobotConfigFile(RobotConfigFileManager paramRobotConfigFileManager, String paramString) {
    FileLocation fileLocation;
    paramString = RobotConfigFileManager.stripFileNameExtension(paramString);
    this.name = paramString;
    this.resourceId = 0;
    if (paramString.equalsIgnoreCase(paramRobotConfigFileManager.noConfig)) {
      fileLocation = FileLocation.NONE;
    } else {
      fileLocation = FileLocation.LOCAL_STORAGE;
    } 
    this.location = fileLocation;
    this.isDirty = false;
  }
  
  public RobotConfigFile(String paramString, int paramInt) {
    this.name = paramString;
    this.resourceId = paramInt;
    this.location = FileLocation.RESOURCE;
    this.isDirty = false;
  }
  
  public static RobotConfigFile fromString(RobotConfigFileManager paramRobotConfigFileManager, String paramString) {
    try {
      RobotConfigFile robotConfigFile = (RobotConfigFile)SimpleGson.getInstance().fromJson(paramString, RobotConfigFile.class);
      return (robotConfigFile == null) ? noConfig(paramRobotConfigFileManager) : robotConfigFile;
    } catch (JsonSyntaxException jsonSyntaxException) {
      RobotLog.ee("RobotConfigFile", "Could not parse the stored config file data from shared settings");
      return noConfig(paramRobotConfigFileManager);
    } 
  }
  
  public static RobotConfigFile noConfig(RobotConfigFileManager paramRobotConfigFileManager) {
    return new RobotConfigFile(paramRobotConfigFileManager, paramRobotConfigFileManager.noConfig);
  }
  
  public boolean containedIn(Collection<RobotConfigFile> paramCollection) {
    Iterator<RobotConfigFile> iterator = paramCollection.iterator();
    while (iterator.hasNext()) {
      if (((RobotConfigFile)iterator.next()).name.equalsIgnoreCase(this.name))
        return true; 
    } 
    return false;
  }
  
  public File getFullPath() {
    return RobotConfigFileManager.getFullPath(getName());
  }
  
  public FileLocation getLocation() {
    return this.location;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getResourceId() {
    return this.resourceId;
  }
  
  public XmlPullParser getXml() {
    int i = null.$SwitchMap$com$qualcomm$ftccommon$configuration$RobotConfigFile$FileLocation[this.location.ordinal()];
    return (i != 1) ? ((i != 2) ? ((i != 3) ? null : getXmlNone()) : getXmlResource()) : getXmlLocalStorage();
  }
  
  protected XmlPullParser getXmlLocalStorage() {
    XmlPullParser xmlPullParser;
    try {
      FileInputStream fileInputStream = new FileInputStream(RobotConfigFileManager.getFullPath(getName()));
      XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
      xmlPullParserFactory.setNamespaceAware(true);
      xmlPullParser = xmlPullParserFactory.newPullParser();
      try {
        xmlPullParser.setInput(fileInputStream, null);
        return xmlPullParser;
      } catch (XmlPullParserException xmlPullParserException) {
      
      } catch (FileNotFoundException null) {}
    } catch (XmlPullParserException xmlPullParserException) {
      xmlPullParser = null;
    } catch (FileNotFoundException fileNotFoundException) {}
    RobotLog.logStacktrace(fileNotFoundException);
    return xmlPullParser;
  }
  
  protected XmlPullParser getXmlNone() {
    XmlPullParser xmlPullParser2 = null;
    XmlPullParser xmlPullParser1 = xmlPullParser2;
    try {
      StringReader stringReader = new StringReader("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n<Robot type=\"FirstInspires-FTC\">\n</Robot>\n");
      xmlPullParser1 = xmlPullParser2;
      XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
      xmlPullParser1 = xmlPullParser2;
      xmlPullParserFactory.setNamespaceAware(true);
      xmlPullParser1 = xmlPullParser2;
      xmlPullParser2 = xmlPullParserFactory.newPullParser();
      xmlPullParser1 = xmlPullParser2;
      xmlPullParser2.setInput(stringReader);
      return xmlPullParser2;
    } catch (XmlPullParserException xmlPullParserException) {
      RobotLog.logStacktrace((Throwable)xmlPullParserException);
      return xmlPullParser1;
    } 
  }
  
  protected XmlPullParser getXmlResource() {
    return (XmlPullParser)AppUtil.getInstance().getApplication().getResources().getXml(this.resourceId);
  }
  
  public boolean isDirty() {
    return this.isDirty;
  }
  
  public boolean isNoConfig() {
    return (this.location == FileLocation.NONE);
  }
  
  public boolean isReadOnly() {
    return (this.location == FileLocation.RESOURCE || this.location == FileLocation.NONE);
  }
  
  public void markClean() {
    this.isDirty = false;
  }
  
  public void markDirty() {
    this.isDirty = true;
  }
  
  public String toString() {
    return SimpleGson.getInstance().toJson(this);
  }
  
  public enum FileLocation {
    LOCAL_STORAGE, NONE, RESOURCE;
    
    static {
      FileLocation fileLocation = new FileLocation("RESOURCE", 2);
      RESOURCE = fileLocation;
      $VALUES = new FileLocation[] { NONE, LOCAL_STORAGE, fileLocation };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\RobotConfigFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */