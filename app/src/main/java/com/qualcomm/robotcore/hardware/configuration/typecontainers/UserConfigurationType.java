package com.qualcomm.robotcore.hardware.configuration.typecontainers;

import com.google.gson.annotations.Expose;
import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationTypeManager;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.util.ClassUtil;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.firstinspires.ftc.robotcore.internal.opmode.OnBotJavaDeterminer;

public abstract class UserConfigurationType implements ConfigurationType, Serializable {
  @Expose
  private boolean builtIn = false;
  
  @Expose
  private ControlSystem[] compatibleControlSystems = new ControlSystem[] { ControlSystem.MODERN_ROBOTICS, ControlSystem.REV_HUB };
  
  @Expose
  protected String description;
  
  @Expose
  private final ConfigurationType.DeviceFlavor flavor;
  
  @Expose
  private boolean isDeprecated;
  
  @Expose
  private boolean isOnBotJava;
  
  @Expose
  protected String name = "";
  
  @Expose
  private String xmlTag;
  
  @Expose
  private String[] xmlTagAliases;
  
  protected UserConfigurationType(ConfigurationType.DeviceFlavor paramDeviceFlavor) {
    this.flavor = paramDeviceFlavor;
    this.xmlTag = "";
  }
  
  public UserConfigurationType(Class paramClass, ConfigurationType.DeviceFlavor paramDeviceFlavor, String paramString) {
    this.flavor = paramDeviceFlavor;
    this.xmlTag = paramString;
    this.isOnBotJava = OnBotJavaDeterminer.isOnBotJava(paramClass);
    this.isDeprecated = paramClass.isAnnotationPresent((Class)Deprecated.class);
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws InvalidObjectException {
    throw new InvalidObjectException("proxy required");
  }
  
  private Object writeReplace() {
    return new SerializationProxy(this);
  }
  
  public void finishedAnnotations(Class paramClass) {
    if (this.name.isEmpty())
      this.name = paramClass.getSimpleName(); 
    if (this.xmlTagAliases == null)
      this.xmlTagAliases = new String[0]; 
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public ConfigurationType.DeviceFlavor getDeviceFlavor() {
    return this.flavor;
  }
  
  public String getDisplayName(ConfigurationType.DisplayNameFlavor paramDisplayNameFlavor) {
    return this.name;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getXmlTag() {
    return this.xmlTag;
  }
  
  public String[] getXmlTagAliases() {
    return this.xmlTagAliases;
  }
  
  public boolean isBuiltIn() {
    return this.builtIn;
  }
  
  public boolean isCompatibleWith(ControlSystem paramControlSystem) {
    ControlSystem[] arrayOfControlSystem = this.compatibleControlSystems;
    int j = arrayOfControlSystem.length;
    for (int i = 0; i < j; i++) {
      if (paramControlSystem == arrayOfControlSystem[i])
        return true; 
    } 
    return false;
  }
  
  public boolean isDeprecated() {
    return this.isDeprecated;
  }
  
  public boolean isDeviceFlavor(ConfigurationType.DeviceFlavor paramDeviceFlavor) {
    return (this.flavor == paramDeviceFlavor);
  }
  
  public boolean isOnBotJava() {
    return this.isOnBotJava;
  }
  
  public void processAnnotation(DeviceProperties paramDeviceProperties) {
    this.description = ClassUtil.decodeStringRes(paramDeviceProperties.description());
    this.builtIn = paramDeviceProperties.builtIn();
    this.compatibleControlSystems = paramDeviceProperties.compatibleControlSystems();
    this.xmlTagAliases = paramDeviceProperties.xmlTagAliases();
    if (!paramDeviceProperties.name().isEmpty())
      this.name = ClassUtil.decodeStringRes(paramDeviceProperties.name().trim()); 
  }
  
  public DeviceManager.UsbDeviceType toUSBDeviceType() {
    return DeviceManager.UsbDeviceType.FTDI_USB_UNKNOWN_DEVICE;
  }
  
  protected static class SerializationProxy implements Serializable {
    protected String xmlTag;
    
    public SerializationProxy(UserConfigurationType param1UserConfigurationType) {
      this.xmlTag = param1UserConfigurationType.xmlTag;
    }
    
    private Object readResolve() {
      return ConfigurationTypeManager.getInstance().configurationTypeFromTag(this.xmlTag);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\typecontainers\UserConfigurationType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */