package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.util.GlobalWarningSource;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class ReadXMLFileHandler extends ConfigurationUtility {
  public static final String TAG = "ReadXMLFileHandler";
  
  private static WarningManager warningManager;
  
  private DeviceManager deviceManager = null;
  
  private XmlPullParser parser;
  
  static {
    WarningManager warningManager = new WarningManager();
    warningManager = warningManager;
    RobotLog.registerGlobalWarningSource(warningManager);
  }
  
  public ReadXMLFileHandler() {}
  
  public ReadXMLFileHandler(DeviceManager paramDeviceManager) {
    this();
    this.deviceManager = paramDeviceManager;
  }
  
  private void addEmbeddedLynxModuleIfNecessary(List<ControllerConfiguration> paramList) {
    if (LynxConstants.isRevControlHub()) {
      Iterator<ControllerConfiguration> iterator = paramList.iterator();
      while (iterator.hasNext()) {
        if (LynxConstants.isEmbeddedSerialNumber(((ControllerConfiguration)iterator.next()).getSerialNumber())) {
          RobotLog.vv("ReadXMLFileHandler", "embedded lynx USB device is already present");
          return;
        } 
      } 
      RobotLog.vv("ReadXMLFileHandler", "auto-configuring embedded lynx USB device");
      paramList.add(buildNewEmbeddedLynxUsbDevice(this.deviceManager));
    } 
  }
  
  public static ConfigurationType deform(String paramString) {
    return (paramString != null) ? ConfigurationTypeManager.getInstance().configurationTypeFromTag(paramString) : null;
  }
  
  private void handleDeprecation(DeviceConfiguration paramDeviceConfiguration) {
    if (paramDeviceConfiguration.getConfigurationType().isDeprecated()) {
      warningManager.addWarning(String.format("%s is a deprecated configuration type and may be removed in a future release", new Object[] { paramDeviceConfiguration.getConfigurationType().getDisplayName(ConfigurationType.DisplayNameFlavor.Normal) }));
      return;
    } 
    if (paramDeviceConfiguration.getConfigurationType() == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER)
      warningManager.addWarning("The Legacy Module is illegal for competition and support may be removed from this app in a future release."); 
  }
  
  private List<ControllerConfiguration> parseDocument() throws RobotCoreException {
    warningManager.actuallyClearWarning();
    List<ControllerConfiguration> list2 = null;
    List<ControllerConfiguration> list3 = null;
    List<ControllerConfiguration> list1 = null;
    try {
      int i = this.parser.getEventType();
      while (true) {
        list2 = list1;
        if (i != 1) {
          List<ControllerConfiguration> list = list1;
          if (i == 2) {
            list2 = list1;
            list3 = list1;
            if (deform(this.parser.getName()) == BuiltInConfigurationType.ROBOT) {
              list2 = list1;
              list3 = list1;
              list = parseRobot();
            } else {
              list2 = list1;
              list3 = list1;
              parseIgnoreElementChildren();
              list = list1;
            } 
          } 
          list2 = list;
          list3 = list;
          i = this.parser.next();
          list1 = list;
          continue;
        } 
        break;
      } 
    } catch (XmlPullParserException xmlPullParserException) {
      RobotLog.w("XmlPullParserException");
      xmlPullParserException.printStackTrace();
      list2 = list3;
    } catch (IOException iOException) {
      RobotLog.w("IOException");
      iOException.printStackTrace();
    } 
    list1 = list2;
    if (list2 == null)
      list1 = new ArrayList<ControllerConfiguration>(); 
    addEmbeddedLynxModuleIfNecessary(list1);
    return list1;
  }
  
  private void parseIgnoreElementChildren() throws IOException, XmlPullParserException {
    boolean bool;
    if (this.parser.getEventType() == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    Assert.assertTrue(bool);
    for (int i = this.parser.next(); i != 3; i = this.parser.next()) {
      if (i == 1)
        return; 
      if (i == 2)
        parseIgnoreElementChildren(); 
    } 
  }
  
  private List<ControllerConfiguration> parseRobot() throws XmlPullParserException, IOException, RobotCoreException {
    boolean bool;
    if (this.parser.getEventType() == 2 && deform(this.parser.getName()) == BuiltInConfigurationType.ROBOT) {
      bool = true;
    } else {
      bool = false;
    } 
    Assert.assertTrue(bool);
    ArrayList<WebcamConfiguration> arrayList = new ArrayList();
    for (int i = this.parser.next(); i != 3; i = this.parser.next()) {
      if (i == 2) {
        WebcamConfiguration webcamConfiguration;
        ConfigurationType configurationType = deform(this.parser.getName());
        MotorControllerConfiguration motorControllerConfiguration = null;
        if (configurationType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
          motorControllerConfiguration = new MotorControllerConfiguration();
        } else if (configurationType == BuiltInConfigurationType.SERVO_CONTROLLER) {
          ServoControllerConfiguration servoControllerConfiguration = new ServoControllerConfiguration();
        } else if (configurationType == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER) {
          LegacyModuleControllerConfiguration legacyModuleControllerConfiguration = new LegacyModuleControllerConfiguration();
        } else if (configurationType == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE) {
          DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = new DeviceInterfaceModuleConfiguration();
        } else if (configurationType == BuiltInConfigurationType.LYNX_USB_DEVICE) {
          LynxUsbDeviceConfiguration lynxUsbDeviceConfiguration = new LynxUsbDeviceConfiguration();
        } else if (configurationType == BuiltInConfigurationType.WEBCAM) {
          webcamConfiguration = new WebcamConfiguration();
        } else {
          parseIgnoreElementChildren();
        } 
        if (webcamConfiguration != null) {
          webcamConfiguration.deserialize(this.parser, this);
          arrayList.add(webcamConfiguration);
        } 
      } 
    } 
    return (List)arrayList;
  }
  
  public static XmlPullParser xmlPullParserFromReader(Reader paramReader) {
    XmlPullParser xmlPullParser2 = null;
    XmlPullParser xmlPullParser1 = xmlPullParser2;
    try {
      XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
      xmlPullParser1 = xmlPullParser2;
      xmlPullParserFactory.setNamespaceAware(true);
      xmlPullParser1 = xmlPullParser2;
      xmlPullParser2 = xmlPullParserFactory.newPullParser();
      xmlPullParser1 = xmlPullParser2;
      xmlPullParser2.setInput(paramReader);
      return xmlPullParser2;
    } catch (XmlPullParserException xmlPullParserException) {
      xmlPullParserException.printStackTrace();
      return xmlPullParser1;
    } 
  }
  
  public void onDeviceParsed(DeviceConfiguration paramDeviceConfiguration) {
    noteExistingName(paramDeviceConfiguration.getConfigurationType(), paramDeviceConfiguration.getName());
    handleDeprecation(paramDeviceConfiguration);
    if (paramDeviceConfiguration instanceof LynxModuleConfiguration) {
      paramDeviceConfiguration = paramDeviceConfiguration;
      if (paramDeviceConfiguration.getModuleAddress() > 10 && paramDeviceConfiguration.getModuleAddress() != 173)
        warningManager.addWarning(String.format(Locale.ENGLISH, "A module is configured with address %d. Addresses higher than %d are reserved for system use", new Object[] { Integer.valueOf(paramDeviceConfiguration.getModuleAddress()), Integer.valueOf(10) })); 
    } 
  }
  
  public List<ControllerConfiguration> parse(InputStream paramInputStream) throws RobotCoreException {
    this.parser = null;
    try {
      XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
      xmlPullParserFactory.setNamespaceAware(true);
      XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
      this.parser = xmlPullParser;
      xmlPullParser.setInput(paramInputStream, null);
    } catch (XmlPullParserException xmlPullParserException) {
      xmlPullParserException.printStackTrace();
    } 
    return parseDocument();
  }
  
  public List<ControllerConfiguration> parse(Reader paramReader) throws RobotCoreException {
    this.parser = xmlPullParserFromReader(paramReader);
    return parseDocument();
  }
  
  public List<ControllerConfiguration> parse(XmlPullParser paramXmlPullParser) throws RobotCoreException {
    this.parser = paramXmlPullParser;
    return parseDocument();
  }
  
  private static class WarningManager implements GlobalWarningSource {
    private String warningMessage = "";
    
    private int warningMessageSuppressionCount = 0;
    
    private WarningManager() {}
    
    private void actuallyClearWarning() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: invokevirtual clearGlobalWarning : ()V
      //   6: aload_0
      //   7: ldc ''
      //   9: putfield warningMessage : Ljava/lang/String;
      //   12: aload_0
      //   13: monitorexit
      //   14: return
      //   15: astore_1
      //   16: aload_0
      //   17: monitorexit
      //   18: aload_1
      //   19: athrow
      // Exception table:
      //   from	to	target	type
      //   2	12	15	finally
    }
    
    private void addWarning(String param1String) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield warningMessage : Ljava/lang/String;
      //   6: invokevirtual isEmpty : ()Z
      //   9: ifeq -> 20
      //   12: aload_0
      //   13: aload_1
      //   14: putfield warningMessage : Ljava/lang/String;
      //   17: goto -> 63
      //   20: new java/lang/StringBuilder
      //   23: dup
      //   24: invokespecial <init> : ()V
      //   27: astore_2
      //   28: aload_2
      //   29: aload_0
      //   30: getfield warningMessage : Ljava/lang/String;
      //   33: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   36: pop
      //   37: aload_2
      //   38: ldc '; %s'
      //   40: iconst_1
      //   41: anewarray java/lang/Object
      //   44: dup
      //   45: iconst_0
      //   46: aload_1
      //   47: aastore
      //   48: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   51: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   54: pop
      //   55: aload_0
      //   56: aload_2
      //   57: invokevirtual toString : ()Ljava/lang/String;
      //   60: putfield warningMessage : Ljava/lang/String;
      //   63: aload_0
      //   64: monitorexit
      //   65: return
      //   66: astore_1
      //   67: aload_0
      //   68: monitorexit
      //   69: aload_1
      //   70: athrow
      // Exception table:
      //   from	to	target	type
      //   2	17	66	finally
      //   20	63	66	finally
    }
    
    public void clearGlobalWarning() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: iconst_0
      //   4: putfield warningMessageSuppressionCount : I
      //   7: aload_0
      //   8: monitorexit
      //   9: return
      //   10: astore_1
      //   11: aload_0
      //   12: monitorexit
      //   13: aload_1
      //   14: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	10	finally
    }
    
    public String getGlobalWarning() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield warningMessageSuppressionCount : I
      //   6: ifle -> 15
      //   9: ldc ''
      //   11: astore_1
      //   12: goto -> 20
      //   15: aload_0
      //   16: getfield warningMessage : Ljava/lang/String;
      //   19: astore_1
      //   20: aload_0
      //   21: monitorexit
      //   22: aload_1
      //   23: areturn
      //   24: astore_1
      //   25: aload_0
      //   26: monitorexit
      //   27: aload_1
      //   28: athrow
      // Exception table:
      //   from	to	target	type
      //   2	9	24	finally
      //   15	20	24	finally
    }
    
    public void setGlobalWarning(String param1String) {
      /* monitor enter ThisExpression{InnerObjectType{ObjectType{com/qualcomm/robotcore/hardware/configuration/ReadXMLFileHandler}.Lcom/qualcomm/robotcore/hardware/configuration/ReadXMLFileHandler$WarningManager;}} */
      /* monitor exit ThisExpression{InnerObjectType{ObjectType{com/qualcomm/robotcore/hardware/configuration/ReadXMLFileHandler}.Lcom/qualcomm/robotcore/hardware/configuration/ReadXMLFileHandler$WarningManager;}} */
    }
    
    public void suppressGlobalWarning(boolean param1Boolean) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: iload_1
      //   3: ifeq -> 19
      //   6: aload_0
      //   7: aload_0
      //   8: getfield warningMessageSuppressionCount : I
      //   11: iconst_1
      //   12: iadd
      //   13: putfield warningMessageSuppressionCount : I
      //   16: goto -> 29
      //   19: aload_0
      //   20: aload_0
      //   21: getfield warningMessageSuppressionCount : I
      //   24: iconst_1
      //   25: isub
      //   26: putfield warningMessageSuppressionCount : I
      //   29: aload_0
      //   30: monitorexit
      //   31: return
      //   32: astore_2
      //   33: aload_0
      //   34: monitorexit
      //   35: aload_2
      //   36: athrow
      // Exception table:
      //   from	to	target	type
      //   6	16	32	finally
      //   19	29	32	finally
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\ReadXMLFileHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */