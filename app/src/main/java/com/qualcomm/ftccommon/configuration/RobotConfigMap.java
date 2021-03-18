package com.qualcomm.ftccommon.configuration;

import android.content.Context;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class RobotConfigMap implements Serializable {
  Map<SerialNumber, ControllerConfiguration> map = new HashMap<SerialNumber, ControllerConfiguration>();
  
  public RobotConfigMap() {}
  
  public RobotConfigMap(RobotConfigMap paramRobotConfigMap) {
    this(paramRobotConfigMap.map);
  }
  
  public RobotConfigMap(Collection<ControllerConfiguration> paramCollection) {
    for (ControllerConfiguration controllerConfiguration : paramCollection)
      put(controllerConfiguration.getSerialNumber(), controllerConfiguration); 
  }
  
  public RobotConfigMap(Map<SerialNumber, ControllerConfiguration> paramMap) {
    this.map = new HashMap<SerialNumber, ControllerConfiguration>(paramMap);
  }
  
  boolean allControllersAreBound() {
    Iterator<ControllerConfiguration> iterator = controllerConfigurations().iterator();
    while (iterator.hasNext()) {
      if (((ControllerConfiguration)iterator.next()).getSerialNumber().isFake())
        return false; 
    } 
    return true;
  }
  
  public void bindUnboundControllers(ScannedDevices paramScannedDevices) {
    paramScannedDevices = new ScannedDevices(paramScannedDevices);
    Iterator<ControllerConfiguration> iterator = controllerConfigurations().iterator();
    while (iterator.hasNext())
      paramScannedDevices.remove(((ControllerConfiguration)iterator.next()).getSerialNumber()); 
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    for (Map.Entry entry : paramScannedDevices.entrySet()) {
      ConfigurationType configurationType = BuiltInConfigurationType.fromUSBDeviceType((DeviceManager.UsbDeviceType)entry.getValue());
      if (configurationType != BuiltInConfigurationType.UNKNOWN) {
        List list2 = (List)hashMap.get(configurationType);
        List list1 = list2;
        if (list2 == null) {
          list1 = new LinkedList();
          hashMap.put(configurationType, list1);
        } 
        list1.add(entry.getKey());
      } 
    } 
    for (ControllerConfiguration controllerConfiguration : controllerConfigurations()) {
      if (controllerConfiguration.getSerialNumber().isFake()) {
        List<SerialNumber> list = (List)hashMap.get(controllerConfiguration.getConfigurationType());
        if (list != null && !list.isEmpty())
          controllerConfiguration.setSerialNumber(list.remove(0)); 
      } 
    } 
    ArrayList<ControllerConfiguration> arrayList = new ArrayList<ControllerConfiguration>(controllerConfigurations());
    this.map.clear();
    for (ControllerConfiguration controllerConfiguration : arrayList)
      put(controllerConfiguration.getSerialNumber(), controllerConfiguration); 
  }
  
  public boolean contains(SerialNumber paramSerialNumber) {
    return this.map.containsKey(paramSerialNumber);
  }
  
  protected boolean containsSerialNumber(List<ControllerConfiguration> paramList, SerialNumber paramSerialNumber) {
    Iterator<ControllerConfiguration> iterator = paramList.iterator();
    while (iterator.hasNext()) {
      if (((ControllerConfiguration)iterator.next()).getSerialNumber().equals(paramSerialNumber))
        return true; 
    } 
    return false;
  }
  
  public Collection<ControllerConfiguration> controllerConfigurations() {
    return this.map.values();
  }
  
  protected String generateName(Context paramContext, ConfigurationType paramConfigurationType, List<ControllerConfiguration> paramList) {
    int i;
    for (i = 1;; i++) {
      String str = Misc.formatForUser("%s %d", new Object[] { paramConfigurationType.getDisplayName(ConfigurationType.DisplayNameFlavor.Normal), Integer.valueOf(i) });
      if (!nameExists(str, paramList))
        return str; 
    } 
  }
  
  public ControllerConfiguration get(SerialNumber paramSerialNumber) {
    return this.map.get(paramSerialNumber);
  }
  
  public List<ControllerConfiguration> getEligibleSwapTargets(ControllerConfiguration paramControllerConfiguration, ScannedDevices paramScannedDevices, Context paramContext) {
    LinkedList<ControllerConfiguration> linkedList = new LinkedList();
    ConfigurationType configurationType = paramControllerConfiguration.getConfigurationType();
    if (configurationType != BuiltInConfigurationType.MOTOR_CONTROLLER && configurationType != BuiltInConfigurationType.SERVO_CONTROLLER && configurationType != BuiltInConfigurationType.DEVICE_INTERFACE_MODULE && configurationType != BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER)
      return linkedList; 
    if (paramControllerConfiguration.getSerialNumber().isFake())
      return linkedList; 
    for (ControllerConfiguration controllerConfiguration : controllerConfigurations()) {
      SerialNumber serialNumber = controllerConfiguration.getSerialNumber();
      if (!serialNumber.isFake() && !serialNumber.equals(paramControllerConfiguration.getSerialNumber()) && !containsSerialNumber(linkedList, serialNumber) && controllerConfiguration.getConfigurationType() == paramControllerConfiguration.getConfigurationType())
        linkedList.add(controllerConfiguration); 
    } 
    for (Map.Entry entry : paramScannedDevices.entrySet()) {
      SerialNumber serialNumber = (SerialNumber)entry.getKey();
      if (!serialNumber.isFake() && !serialNumber.equals(paramControllerConfiguration.getSerialNumber()) && !containsSerialNumber(linkedList, serialNumber) && entry.getValue() == paramControllerConfiguration.toUSBDeviceType()) {
        ControllerConfiguration controllerConfiguration = ControllerConfiguration.forType(generateName(paramContext, paramControllerConfiguration.getConfigurationType(), linkedList), (SerialNumber)entry.getKey(), paramControllerConfiguration.getConfigurationType());
        controllerConfiguration.setKnownToBeAttached(paramScannedDevices.containsKey(controllerConfiguration.getSerialNumber()));
        linkedList.add(controllerConfiguration);
      } 
    } 
    return linkedList;
  }
  
  public boolean isSwappable(ControllerConfiguration paramControllerConfiguration, ScannedDevices paramScannedDevices, Context paramContext) {
    return getEligibleSwapTargets(paramControllerConfiguration, paramScannedDevices, paramContext).isEmpty() ^ true;
  }
  
  protected boolean nameExists(String paramString, List<ControllerConfiguration> paramList) {
    Iterator<ControllerConfiguration> iterator = paramList.iterator();
    while (iterator.hasNext()) {
      if (((ControllerConfiguration)iterator.next()).getName().equalsIgnoreCase(paramString))
        return true; 
    } 
    iterator = controllerConfigurations().iterator();
    while (iterator.hasNext()) {
      if (((ControllerConfiguration)iterator.next()).getName().equalsIgnoreCase(paramString))
        return true; 
    } 
    return false;
  }
  
  public void put(SerialNumber paramSerialNumber, ControllerConfiguration paramControllerConfiguration) {
    this.map.put(paramSerialNumber, paramControllerConfiguration);
  }
  
  public boolean remove(SerialNumber paramSerialNumber) {
    return (this.map.remove(paramSerialNumber) != null);
  }
  
  public Collection<SerialNumber> serialNumbers() {
    return this.map.keySet();
  }
  
  public void setSerialNumber(ControllerConfiguration paramControllerConfiguration, SerialNumber paramSerialNumber) {
    remove(paramControllerConfiguration.getSerialNumber());
    paramControllerConfiguration.setSerialNumber(paramSerialNumber);
    put(paramSerialNumber, paramControllerConfiguration);
  }
  
  public int size() {
    return this.map.size();
  }
  
  public void swapSerialNumbers(ControllerConfiguration paramControllerConfiguration1, ControllerConfiguration paramControllerConfiguration2) {
    SerialNumber serialNumber = paramControllerConfiguration1.getSerialNumber();
    paramControllerConfiguration1.setSerialNumber(paramControllerConfiguration2.getSerialNumber());
    paramControllerConfiguration2.setSerialNumber(serialNumber);
    put(paramControllerConfiguration1.getSerialNumber(), paramControllerConfiguration1);
    put(paramControllerConfiguration2.getSerialNumber(), paramControllerConfiguration2);
    boolean bool = paramControllerConfiguration1.isKnownToBeAttached();
    paramControllerConfiguration1.setKnownToBeAttached(paramControllerConfiguration2.isKnownToBeAttached());
    paramControllerConfiguration2.setKnownToBeAttached(bool);
  }
  
  public void writeToLog(String paramString1, String paramString2) {
    RobotLog.vv(paramString1, "robotConfigMap: %s", new Object[] { paramString2 });
    for (ControllerConfiguration controllerConfiguration : controllerConfigurations()) {
      RobotLog.vv(paramString1, "   serial=%s id=0x%08x name='%s' ", new Object[] { controllerConfiguration.getSerialNumber(), Integer.valueOf(controllerConfiguration.hashCode()), controllerConfiguration.getName() });
    } 
  }
  
  public void writeToLog(String paramString1, String paramString2, ControllerConfiguration paramControllerConfiguration) {
    writeToLog(paramString1, paramString2);
    RobotLog.vv(paramString1, "  :serial=%s id=0x%08x name='%s' ", new Object[] { paramControllerConfiguration.getSerialNumber(), Integer.valueOf(paramControllerConfiguration.hashCode()), paramControllerConfiguration.getName() });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\RobotConfigMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */