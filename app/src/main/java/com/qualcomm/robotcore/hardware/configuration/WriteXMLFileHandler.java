package com.qualcomm.robotcore.hardware.configuration;

import android.util.Xml;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.function.ThrowingRunnable;
import org.xmlpull.v1.XmlSerializer;

public class WriteXMLFileHandler {
  private List<String> duplicates = new ArrayList<String>();
  
  private int indent = 0;
  
  private String[] indentation = new String[] { "    ", "        ", "            " };
  
  private HashSet<String> names = new HashSet<String>();
  
  private XmlSerializer serializer = Xml.newSerializer();
  
  private void checkForDuplicates(DeviceConfiguration paramDeviceConfiguration) {
    if (paramDeviceConfiguration.isEnabled()) {
      String str = paramDeviceConfiguration.getName();
      if (this.names.contains(str)) {
        this.duplicates.add(str);
        return;
      } 
      this.names.add(str);
    } 
  }
  
  private String conform(ConfigurationType paramConfigurationType) {
    return paramConfigurationType.getXmlTag();
  }
  
  private <CONTROLLER_T extends ControllerConfiguration<? extends DeviceConfiguration>> void writeController(final CONTROLLER_T controller, final boolean isUsbDevice) throws IOException {
    writeNamedController((ControllerConfiguration)controller, new ThrowingRunnable<IOException>() {
          public void run() throws IOException {
            if (isUsbDevice) {
              WriteXMLFileHandler.this.serializer.attribute("", "serialNumber", controller.getSerialNumber().getString());
              return;
            } 
            WriteXMLFileHandler.this.serializer.attribute("", "port", String.valueOf(controller.getPort()));
          }
        }new ThrowingRunnable<IOException>() {
          public void run() throws IOException {
            if (controller.getConfigurationType() == BuiltInConfigurationType.LYNX_MODULE) {
              LynxModuleConfiguration lynxModuleConfiguration = (LynxModuleConfiguration)controller;
              for (DeviceConfiguration deviceConfiguration : lynxModuleConfiguration.getMotors())
                WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
              for (DeviceConfiguration deviceConfiguration : lynxModuleConfiguration.getServos())
                WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
              for (DeviceConfiguration deviceConfiguration : lynxModuleConfiguration.getAnalogInputs())
                WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
              for (DeviceConfiguration deviceConfiguration : lynxModuleConfiguration.getPwmOutputs())
                WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
              for (DeviceConfiguration deviceConfiguration : lynxModuleConfiguration.getDigitalDevices())
                WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
              for (DeviceConfiguration deviceConfiguration : lynxModuleConfiguration.getI2cDevices())
                WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
            } else if (controller.getConfigurationType() == BuiltInConfigurationType.MATRIX_CONTROLLER) {
              for (DeviceConfiguration deviceConfiguration : ((MatrixControllerConfiguration)controller).getMotors())
                WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
              for (DeviceConfiguration deviceConfiguration : ((MatrixControllerConfiguration)controller).getServos())
                WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
            } else {
              for (DeviceConfiguration deviceConfiguration : controller.getDevices())
                WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
            } 
          }
        });
  }
  
  private void writeDevice(DeviceConfiguration paramDeviceConfiguration, ThrowingRunnable<IOException> paramThrowingRunnable1, ThrowingRunnable<IOException> paramThrowingRunnable2) throws IOException {
    this.serializer.ignorableWhitespace(this.indentation[this.indent]);
    this.serializer.startTag("", conform(paramDeviceConfiguration.getConfigurationType()));
    checkForDuplicates(paramDeviceConfiguration);
    if (paramThrowingRunnable1 != null)
      paramThrowingRunnable1.run(); 
    if (paramThrowingRunnable2 != null) {
      this.serializer.ignorableWhitespace("\n");
      this.indent++;
      paramThrowingRunnable2.run();
      int i = this.indent - 1;
      this.indent = i;
      this.serializer.ignorableWhitespace(this.indentation[i]);
    } 
    this.serializer.endTag("", conform(paramDeviceConfiguration.getConfigurationType()));
    this.serializer.ignorableWhitespace("\n");
  }
  
  private void writeDeviceInterfaceModule(final DeviceInterfaceModuleConfiguration controller) throws IOException {
    writeUsbController(controller, null, new ThrowingRunnable<IOException>() {
          public void run() throws IOException {
            DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = controller;
            for (DeviceConfiguration deviceConfiguration : deviceInterfaceModuleConfiguration.getPwmOutputs())
              WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
            for (DeviceConfiguration deviceConfiguration : deviceInterfaceModuleConfiguration.getI2cDevices())
              WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
            for (DeviceConfiguration deviceConfiguration : deviceInterfaceModuleConfiguration.getAnalogInputDevices())
              WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
            for (DeviceConfiguration deviceConfiguration : deviceInterfaceModuleConfiguration.getDigitalDevices())
              WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
            for (DeviceConfiguration deviceConfiguration : deviceInterfaceModuleConfiguration.getAnalogOutputDevices())
              WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration); 
          }
        });
  }
  
  private void writeDeviceNameAndPort(final DeviceConfiguration device) throws IOException {
    if (!device.isEnabled())
      return; 
    writeDevice(device, new ThrowingRunnable<IOException>() {
          public void run() throws IOException {
            device.serializeXmlAttributes(WriteXMLFileHandler.this.serializer);
          }
        },  null);
  }
  
  private void writeLegacyModuleController(final LegacyModuleControllerConfiguration controller) throws IOException {
    writeUsbController(controller, null, new ThrowingRunnable<IOException>() {
          public void run() throws IOException {
            for (DeviceConfiguration deviceConfiguration : controller.getDevices()) {
              ConfigurationType configurationType = deviceConfiguration.getConfigurationType();
              if (configurationType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
                WriteXMLFileHandler.this.writeController((CONTROLLER_T)deviceConfiguration, false);
                continue;
              } 
              if (configurationType == BuiltInConfigurationType.SERVO_CONTROLLER) {
                WriteXMLFileHandler.this.writeController((CONTROLLER_T)deviceConfiguration, false);
                continue;
              } 
              if (configurationType == BuiltInConfigurationType.MATRIX_CONTROLLER) {
                WriteXMLFileHandler.this.writeController((CONTROLLER_T)deviceConfiguration, false);
                continue;
              } 
              WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration);
            } 
          }
        });
  }
  
  private void writeLynxUSBDevice(final LynxUsbDeviceConfiguration controller) throws IOException {
    writeUsbController(controller, new ThrowingRunnable<IOException>() {
          public void run() throws IOException {
            WriteXMLFileHandler.this.serializer.attribute("", "parentModuleAddress", Integer.toString(controller.getParentModuleAddress()));
          }
        }new ThrowingRunnable<IOException>() {
          public void run() throws IOException {
            for (DeviceConfiguration deviceConfiguration : controller.getDevices()) {
              if (deviceConfiguration.getConfigurationType() == BuiltInConfigurationType.LYNX_MODULE) {
                WriteXMLFileHandler.this.writeController((CONTROLLER_T)deviceConfiguration, false);
                continue;
              } 
              WriteXMLFileHandler.this.writeDeviceNameAndPort(deviceConfiguration);
            } 
          }
        });
  }
  
  private void writeNamedController(final ControllerConfiguration controller, final ThrowingRunnable<IOException> handleAttributes, ThrowingRunnable<IOException> paramThrowingRunnable2) throws IOException {
    writeDevice(controller, new ThrowingRunnable<IOException>() {
          public void run() throws IOException {
            WriteXMLFileHandler.this.serializer.attribute("", "name", controller.getName());
            ThrowingRunnable throwingRunnable = handleAttributes;
            if (throwingRunnable != null)
              throwingRunnable.run(); 
          }
        }paramThrowingRunnable2);
  }
  
  private void writeUsbController(final ControllerConfiguration controller, final ThrowingRunnable<IOException> handleAttributes, ThrowingRunnable<IOException> paramThrowingRunnable2) throws IOException {
    writeNamedController(controller, new ThrowingRunnable<IOException>() {
          public void run() throws IOException {
            WriteXMLFileHandler.this.serializer.attribute("", "serialNumber", controller.getSerialNumber().getString());
            ThrowingRunnable throwingRunnable = handleAttributes;
            if (throwingRunnable != null)
              throwingRunnable.run(); 
          }
        }paramThrowingRunnable2);
  }
  
  private void writeWebcam(final WebcamConfiguration controller) throws IOException {
    writeUsbController(controller, new ThrowingRunnable<IOException>() {
          public void run() throws IOException {
            if (controller.getAutoOpen())
              WriteXMLFileHandler.this.serializer.attribute("", "autoOpen", String.valueOf(controller.getAutoOpen())); 
          }
        }null);
  }
  
  public String toXml(Collection<ControllerConfiguration> paramCollection) {
    return toXml(paramCollection, null, null);
  }
  
  public String toXml(Collection<ControllerConfiguration> paramCollection, String paramString1, String paramString2) {
    this.duplicates = new ArrayList<String>();
    this.names = new HashSet<String>();
    StringWriter stringWriter = new StringWriter();
    try {
      this.serializer.setOutput(stringWriter);
      this.serializer.startDocument("UTF-8", Boolean.valueOf(true));
      this.serializer.ignorableWhitespace("\n");
      this.serializer.startTag("", "Robot");
      if (paramString1 != null)
        this.serializer.attribute("", paramString1, paramString2); 
      this.serializer.ignorableWhitespace("\n");
      for (ControllerConfiguration controllerConfiguration : paramCollection) {
        ConfigurationType configurationType = controllerConfiguration.getConfigurationType();
        if (configurationType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
          writeController((MotorControllerConfiguration)controllerConfiguration, true);
          continue;
        } 
        if (configurationType == BuiltInConfigurationType.SERVO_CONTROLLER) {
          writeController((ServoControllerConfiguration)controllerConfiguration, true);
          continue;
        } 
        if (configurationType == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER) {
          writeLegacyModuleController((LegacyModuleControllerConfiguration)controllerConfiguration);
          continue;
        } 
        if (configurationType == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE) {
          writeDeviceInterfaceModule((DeviceInterfaceModuleConfiguration)controllerConfiguration);
          continue;
        } 
        if (configurationType == BuiltInConfigurationType.LYNX_USB_DEVICE) {
          writeLynxUSBDevice((LynxUsbDeviceConfiguration)controllerConfiguration);
          continue;
        } 
        if (configurationType == BuiltInConfigurationType.WEBCAM)
          writeWebcam((WebcamConfiguration)controllerConfiguration); 
      } 
      this.serializer.endTag("", "Robot");
      this.serializer.ignorableWhitespace("\n");
      this.serializer.endDocument();
      return stringWriter.toString();
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    } 
  }
  
  public void writeToFile(String paramString1, File paramFile, String paramString2) throws RobotCoreException, IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield duplicates : Ljava/util/List;
    //   4: invokeinterface size : ()I
    //   9: ifgt -> 134
    //   12: iconst_1
    //   13: istore #4
    //   15: aload_2
    //   16: invokevirtual exists : ()Z
    //   19: ifne -> 28
    //   22: aload_2
    //   23: invokevirtual mkdir : ()Z
    //   26: istore #4
    //   28: iload #4
    //   30: ifeq -> 123
    //   33: new java/io/File
    //   36: dup
    //   37: aload_2
    //   38: aload_3
    //   39: invokespecial <init> : (Ljava/io/File;Ljava/lang/String;)V
    //   42: astore_3
    //   43: aconst_null
    //   44: astore #5
    //   46: aconst_null
    //   47: astore_2
    //   48: new java/io/FileOutputStream
    //   51: dup
    //   52: aload_3
    //   53: invokespecial <init> : (Ljava/io/File;)V
    //   56: astore_3
    //   57: aload_3
    //   58: aload_1
    //   59: invokevirtual getBytes : ()[B
    //   62: invokevirtual write : ([B)V
    //   65: aload_3
    //   66: invokevirtual close : ()V
    //   69: return
    //   70: astore_1
    //   71: aload_3
    //   72: astore_2
    //   73: goto -> 109
    //   76: astore_2
    //   77: aload_3
    //   78: astore_1
    //   79: aload_2
    //   80: astore_3
    //   81: goto -> 92
    //   84: astore_1
    //   85: goto -> 109
    //   88: astore_3
    //   89: aload #5
    //   91: astore_1
    //   92: aload_1
    //   93: astore_2
    //   94: aload_3
    //   95: invokevirtual printStackTrace : ()V
    //   98: aload_1
    //   99: invokevirtual close : ()V
    //   102: return
    //   103: astore_1
    //   104: aload_1
    //   105: invokevirtual printStackTrace : ()V
    //   108: return
    //   109: aload_2
    //   110: invokevirtual close : ()V
    //   113: goto -> 121
    //   116: astore_2
    //   117: aload_2
    //   118: invokevirtual printStackTrace : ()V
    //   121: aload_1
    //   122: athrow
    //   123: new com/qualcomm/robotcore/exception/RobotCoreException
    //   126: dup
    //   127: ldc_w 'Unable to create directory'
    //   130: invokespecial <init> : (Ljava/lang/String;)V
    //   133: athrow
    //   134: new java/lang/StringBuilder
    //   137: dup
    //   138: invokespecial <init> : ()V
    //   141: astore_1
    //   142: aload_1
    //   143: ldc_w 'Duplicate names: '
    //   146: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: pop
    //   150: aload_1
    //   151: aload_0
    //   152: getfield duplicates : Ljava/util/List;
    //   155: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   158: pop
    //   159: new com/qualcomm/robotcore/exception/DuplicateNameException
    //   162: dup
    //   163: aload_1
    //   164: invokevirtual toString : ()Ljava/lang/String;
    //   167: invokespecial <init> : (Ljava/lang/String;)V
    //   170: athrow
    // Exception table:
    //   from	to	target	type
    //   48	57	88	java/lang/Exception
    //   48	57	84	finally
    //   57	65	76	java/lang/Exception
    //   57	65	70	finally
    //   65	69	103	java/io/IOException
    //   94	98	84	finally
    //   98	102	103	java/io/IOException
    //   109	113	116	java/io/IOException
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\WriteXMLFileHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */