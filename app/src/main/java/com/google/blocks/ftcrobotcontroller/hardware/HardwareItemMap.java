package com.google.blocks.ftcrobotcontroller.hardware;

import com.qualcomm.ftccommon.configuration.RobotConfigFileManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LynxModuleConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.xmlpull.v1.XmlPullParser;

public class HardwareItemMap {
  private final Set<DeviceConfiguration> devices = new HashSet<DeviceConfiguration>();
  
  private final SortedMap<HardwareType, List<HardwareItem>> map = new TreeMap<HardwareType, List<HardwareItem>>();
  
  HardwareItemMap() {}
  
  private HardwareItemMap(HardwareMap paramHardwareMap) {
    for (HardwareType hardwareType : HardwareType.values()) {
      Iterator<HardwareDevice> iterator = paramHardwareMap.getAll(hardwareType.deviceType).iterator();
      while (iterator.hasNext()) {
        ArrayList<?> arrayList = new ArrayList(paramHardwareMap.getNamesOf(iterator.next()));
        if (!arrayList.isEmpty()) {
          Collections.sort(arrayList, new Comparator<String>() {
                public int compare(String param1String1, String param1String2) {
                  int j = param1String1.length() - param1String2.length();
                  int i = j;
                  if (j == 0)
                    i = param1String1.compareToIgnoreCase(param1String2); 
                  return i;
                }
              });
          addHardwareItem(null, hardwareType, (String)arrayList.get(0));
        } 
      } 
    } 
  }
  
  HardwareItemMap(Reader paramReader) {
    try {
      Iterator<ControllerConfiguration> iterator = (new ReadXMLFileHandler()).parse(paramReader).iterator();
      while (iterator.hasNext())
        addDevice(null, (DeviceConfiguration)iterator.next()); 
    } catch (RobotCoreException robotCoreException) {
      RobotLog.logStackTrace((Throwable)robotCoreException);
    } 
  }
  
  private HardwareItemMap(XmlPullParser paramXmlPullParser) {
    try {
      Iterator<ControllerConfiguration> iterator = (new ReadXMLFileHandler()).parse(paramXmlPullParser).iterator();
      while (iterator.hasNext())
        addDevice(null, (DeviceConfiguration)iterator.next()); 
    } catch (RobotCoreException robotCoreException) {
      RobotLog.logStackTrace((Throwable)robotCoreException);
    } 
  }
  
  private void addController(HardwareItem paramHardwareItem, ControllerConfiguration<? extends DeviceConfiguration> paramControllerConfiguration) {
    Iterator<DeviceConfiguration> iterator = paramControllerConfiguration.getDevices().iterator();
    while (iterator.hasNext())
      addDevice(paramHardwareItem, iterator.next()); 
    if (paramControllerConfiguration instanceof DeviceInterfaceModuleConfiguration) {
      DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = (DeviceInterfaceModuleConfiguration)paramControllerConfiguration;
      Iterator<DeviceConfiguration> iterator2 = deviceInterfaceModuleConfiguration.getPwmOutputs().iterator();
      while (iterator2.hasNext())
        addDevice(paramHardwareItem, iterator2.next()); 
      iterator2 = deviceInterfaceModuleConfiguration.getI2cDevices().iterator();
      while (iterator2.hasNext())
        addDevice(paramHardwareItem, iterator2.next()); 
      iterator2 = deviceInterfaceModuleConfiguration.getAnalogInputDevices().iterator();
      while (iterator2.hasNext())
        addDevice(paramHardwareItem, iterator2.next()); 
      iterator2 = deviceInterfaceModuleConfiguration.getDigitalDevices().iterator();
      while (iterator2.hasNext())
        addDevice(paramHardwareItem, iterator2.next()); 
      Iterator<DeviceConfiguration> iterator1 = deviceInterfaceModuleConfiguration.getAnalogOutputDevices().iterator();
      while (iterator1.hasNext())
        addDevice(paramHardwareItem, iterator1.next()); 
    } 
    if (paramControllerConfiguration instanceof MatrixControllerConfiguration) {
      MatrixControllerConfiguration matrixControllerConfiguration = (MatrixControllerConfiguration)paramControllerConfiguration;
      Iterator<DeviceConfiguration> iterator2 = matrixControllerConfiguration.getServos().iterator();
      while (iterator2.hasNext())
        addDevice(paramHardwareItem, iterator2.next()); 
      Iterator<DeviceConfiguration> iterator1 = matrixControllerConfiguration.getMotors().iterator();
      while (iterator1.hasNext())
        addDevice(paramHardwareItem, iterator1.next()); 
    } 
    if (paramControllerConfiguration instanceof MotorControllerConfiguration) {
      iterator = ((MotorControllerConfiguration)paramControllerConfiguration).getMotors().iterator();
      while (iterator.hasNext())
        addDevice(paramHardwareItem, iterator.next()); 
    } 
    if (paramControllerConfiguration instanceof ServoControllerConfiguration) {
      iterator = ((ServoControllerConfiguration)paramControllerConfiguration).getServos().iterator();
      while (iterator.hasNext())
        addDevice(paramHardwareItem, iterator.next()); 
    } 
    if (paramControllerConfiguration instanceof LynxModuleConfiguration) {
      LynxModuleConfiguration lynxModuleConfiguration = (LynxModuleConfiguration)paramControllerConfiguration;
      iterator = lynxModuleConfiguration.getServos().iterator();
      while (iterator.hasNext())
        addDevice(paramHardwareItem, iterator.next()); 
      iterator = lynxModuleConfiguration.getMotors().iterator();
      while (iterator.hasNext())
        addDevice(paramHardwareItem, iterator.next()); 
      iterator = lynxModuleConfiguration.getAnalogInputs().iterator();
      while (iterator.hasNext())
        addDevice(paramHardwareItem, iterator.next()); 
      iterator = lynxModuleConfiguration.getPwmOutputs().iterator();
      while (iterator.hasNext())
        addDevice(paramHardwareItem, iterator.next()); 
      iterator = lynxModuleConfiguration.getI2cDevices().iterator();
      while (iterator.hasNext())
        addDevice(paramHardwareItem, iterator.next()); 
      Iterator<DeviceConfiguration> iterator1 = lynxModuleConfiguration.getDigitalDevices().iterator();
      while (iterator1.hasNext())
        addDevice(paramHardwareItem, iterator1.next()); 
    } 
  }
  
  private void addDevice(HardwareItem paramHardwareItem, DeviceConfiguration paramDeviceConfiguration) {
    if (this.devices.add(paramDeviceConfiguration) && paramDeviceConfiguration.isEnabled()) {
      Iterator<HardwareType> iterator = HardwareUtil.getHardwareTypes(paramDeviceConfiguration).iterator();
      HardwareItem hardwareItem = paramHardwareItem;
      while (iterator.hasNext()) {
        HardwareType hardwareType = iterator.next();
        HardwareItem hardwareItem1 = addHardwareItem(paramHardwareItem, hardwareType, paramDeviceConfiguration.getName());
        if (hardwareType.isContainer())
          hardwareItem = hardwareItem1; 
      } 
      if (paramDeviceConfiguration instanceof ControllerConfiguration)
        addController(hardwareItem, (ControllerConfiguration<? extends DeviceConfiguration>)paramDeviceConfiguration); 
    } 
  }
  
  public static HardwareItemMap newHardwareItemMap() {
    try {
      return new HardwareItemMap((new RobotConfigFileManager()).getActiveConfig().getXml());
    } catch (Exception exception) {
      RobotLog.logStackTrace(exception);
      return new HardwareItemMap();
    } 
  }
  
  public static HardwareItemMap newHardwareItemMap(HardwareMap paramHardwareMap) {
    return new HardwareItemMap(paramHardwareMap);
  }
  
  HardwareItem addHardwareItem(HardwareItem paramHardwareItem, HardwareType paramHardwareType, String paramString) {
    StringBuilder stringBuilder;
    if (paramString.isEmpty()) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("Blocks cannot support a hardware device (");
      stringBuilder.append(paramHardwareType.deviceType.getSimpleName());
      stringBuilder.append(") whose name is empty.");
      RobotLog.w(stringBuilder.toString());
      return null;
    } 
    List<HardwareItem> list2 = this.map.get(paramHardwareType);
    List<HardwareItem> list1 = list2;
    if (list2 == null) {
      list1 = new ArrayList();
      this.map.put(paramHardwareType, list1);
    } 
    Iterator<HardwareItem> iterator = list1.iterator();
    while (iterator.hasNext()) {
      if (((HardwareItem)iterator.next()).deviceName.equals(paramString))
        return null; 
    } 
    HardwareItem hardwareItem = new HardwareItem((HardwareItem)stringBuilder, paramHardwareType, paramString);
    list1.add(hardwareItem);
    return hardwareItem;
  }
  
  public boolean contains(HardwareType paramHardwareType) {
    return this.map.containsKey(paramHardwareType);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof HardwareItemMap) {
      paramObject = paramObject;
      return this.map.equals(((HardwareItemMap)paramObject).map);
    } 
    return false;
  }
  
  public Iterable<HardwareItem> getAllHardwareItems() {
    ArrayList<?> arrayList = new ArrayList();
    Iterator<List> iterator = this.map.values().iterator();
    while (iterator.hasNext())
      arrayList.addAll(iterator.next()); 
    Collections.sort(arrayList, new Comparator<HardwareItem>() {
          public int compare(HardwareItem param1HardwareItem1, HardwareItem param1HardwareItem2) {
            return param1HardwareItem1.identifier.compareTo(param1HardwareItem2.identifier);
          }
        });
    return (Iterable)Collections.unmodifiableList(arrayList);
  }
  
  public List<HardwareItem> getHardwareItems(HardwareType paramHardwareType) {
    return this.map.containsKey(paramHardwareType) ? Collections.unmodifiableList(this.map.get(paramHardwareType)) : Collections.emptyList();
  }
  
  public int getHardwareTypeCount() {
    return this.map.size();
  }
  
  public Set<HardwareType> getHardwareTypes() {
    return Collections.unmodifiableSet(this.map.keySet());
  }
  
  public int hashCode() {
    return this.map.hashCode();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\hardware\HardwareItemMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */