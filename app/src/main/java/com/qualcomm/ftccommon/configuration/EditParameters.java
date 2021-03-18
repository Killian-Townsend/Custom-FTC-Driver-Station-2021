package com.qualcomm.ftccommon.configuration;

import android.content.Intent;
import android.os.Bundle;
import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class EditParameters<ITEM_T extends DeviceConfiguration> implements Serializable {
  private DeviceConfiguration configuration = null;
  
  private ControlSystem controlSystem = null;
  
  private RobotConfigFile currentCfgFile = null;
  
  private List<ITEM_T> currentItems = null;
  
  private List<RobotConfigFile> extantRobotConfigurations = new ArrayList<RobotConfigFile>();
  
  private boolean growable = false;
  
  private boolean haveRobotConfigMapParameter = false;
  
  private int i2cBus = 0;
  
  private int initialPortNumber = 0;
  
  private boolean isConfigDirty = false;
  
  private Class<ITEM_T> itemClass = null;
  
  private int maxItemCount = 0;
  
  private RobotConfigMap robotConfigMap = new RobotConfigMap();
  
  private ScannedDevices scannedDevices = new ScannedDevices();
  
  public EditParameters() {}
  
  public EditParameters(EditActivity paramEditActivity) {
    this.isConfigDirty = paramEditActivity.currentCfgFile.isDirty();
  }
  
  public EditParameters(EditActivity paramEditActivity, DeviceConfiguration paramDeviceConfiguration) {
    this(paramEditActivity);
    this.configuration = paramDeviceConfiguration;
  }
  
  public EditParameters(EditActivity paramEditActivity, DeviceConfiguration paramDeviceConfiguration, RobotConfigMap paramRobotConfigMap) {
    this(paramEditActivity);
    this.configuration = paramDeviceConfiguration;
    this.robotConfigMap = paramRobotConfigMap;
    this.haveRobotConfigMapParameter = true;
  }
  
  public EditParameters(EditActivity paramEditActivity, DeviceConfiguration paramDeviceConfiguration, Class<ITEM_T> paramClass, List<ITEM_T> paramList) {
    this(paramEditActivity);
    this.configuration = paramDeviceConfiguration;
    setItems(paramClass, paramList);
  }
  
  public EditParameters(EditActivity paramEditActivity, Class<ITEM_T> paramClass, List<ITEM_T> paramList) {
    this(paramEditActivity);
    setItems(paramClass, paramList);
  }
  
  public EditParameters(EditActivity paramEditActivity, Class<ITEM_T> paramClass, List<ITEM_T> paramList, int paramInt) {
    this(paramEditActivity);
    setItems(paramClass, paramList);
    this.maxItemCount = paramInt;
  }
  
  public static <RESULT_ITEM extends DeviceConfiguration> EditParameters<RESULT_ITEM> fromBundle(EditActivity paramEditActivity, Bundle paramBundle) {
    EditParameters<DeviceConfiguration> editParameters = new EditParameters<DeviceConfiguration>();
    if (paramBundle == null)
      return (EditParameters)editParameters; 
    Iterator<String> iterator = paramBundle.keySet().iterator();
    while (true) {
      if (iterator.hasNext()) {
        String str = iterator.next();
        if (str.equals("configuration")) {
          editParameters.configuration = (DeviceConfiguration)paramBundle.getSerializable(str);
          continue;
        } 
        if (str.equals("scannedDevices")) {
          editParameters.scannedDevices = ScannedDevices.fromSerializationString(paramBundle.getString(str));
          continue;
        } 
        if (str.equals("robotConfigMap")) {
          editParameters.robotConfigMap = (RobotConfigMap)paramBundle.getSerializable(str);
          continue;
        } 
        if (str.equals("haveRobotConfigMap")) {
          editParameters.haveRobotConfigMapParameter = paramBundle.getBoolean(str);
          continue;
        } 
        if (str.equals("extantRobotConfigurations")) {
          editParameters.extantRobotConfigurations = RobotConfigFileManager.deserializeXMLConfigList(paramBundle.getString(str));
          continue;
        } 
        if (str.equals("controlSystem")) {
          editParameters.controlSystem = (ControlSystem)paramBundle.getSerializable(str);
          continue;
        } 
        if (str.equals("currentCfgFile")) {
          editParameters.currentCfgFile = RobotConfigFileManager.deserializeConfig(paramBundle.getString(str));
          continue;
        } 
        if (str.equals("initialPortNumber")) {
          editParameters.initialPortNumber = paramBundle.getInt(str);
          continue;
        } 
        if (str.equals("i2cBus")) {
          editParameters.i2cBus = paramBundle.getInt(str);
          continue;
        } 
        if (str.equals("maxItemCount")) {
          editParameters.maxItemCount = paramBundle.getInt(str);
          continue;
        } 
        if (str.equals("growable")) {
          editParameters.growable = paramBundle.getBoolean(str);
          continue;
        } 
        if (str.equals("isConfigDirty")) {
          editParameters.isConfigDirty = paramBundle.getBoolean(str);
          continue;
        } 
        if (str.equals("itemClass")) {
          try {
            editParameters.itemClass = (Class)Class.forName(paramBundle.getString(str));
          } catch (ClassNotFoundException classNotFoundException) {
            editParameters.itemClass = null;
          } 
          continue;
        } 
        try {
          int i = Integer.parseInt((String)classNotFoundException);
          DeviceConfiguration deviceConfiguration = (DeviceConfiguration)paramBundle.getSerializable((String)classNotFoundException);
          if (editParameters.currentItems == null)
            editParameters.currentItems = new ArrayList<DeviceConfiguration>(); 
          editParameters.currentItems.add(i, deviceConfiguration);
        } catch (NumberFormatException numberFormatException) {}
        continue;
      } 
      if (editParameters.isConfigDirty)
        paramEditActivity.currentCfgFile.markDirty(); 
      return (EditParameters)editParameters;
    } 
  }
  
  public static <RESULT_ITEM extends DeviceConfiguration> EditParameters<RESULT_ITEM> fromIntent(EditActivity paramEditActivity, Intent paramIntent) {
    return fromBundle(paramEditActivity, paramIntent.getExtras());
  }
  
  private void setItems(Class<ITEM_T> paramClass, List<ITEM_T> paramList) {
    this.itemClass = paramClass;
    this.currentItems = paramList;
    Iterator<ITEM_T> iterator = paramList.iterator();
    while (iterator.hasNext())
      Assert.assertTrue(paramClass.isInstance((DeviceConfiguration)iterator.next())); 
  }
  
  public DeviceConfiguration getConfiguration() {
    return this.configuration;
  }
  
  public ControlSystem getControlSystem() {
    return this.controlSystem;
  }
  
  public RobotConfigFile getCurrentCfgFile() {
    return this.currentCfgFile;
  }
  
  public List<ITEM_T> getCurrentItems() {
    List<ITEM_T> list2 = this.currentItems;
    List<ITEM_T> list1 = list2;
    if (list2 == null)
      list1 = new LinkedList<ITEM_T>(); 
    return list1;
  }
  
  public List<RobotConfigFile> getExtantRobotConfigurations() {
    return this.extantRobotConfigurations;
  }
  
  public int getI2cBus() {
    return this.i2cBus;
  }
  
  public int getInitialPortNumber() {
    return this.initialPortNumber;
  }
  
  public Class<ITEM_T> getItemClass() {
    Assert.assertNotNull(this.itemClass);
    return this.itemClass;
  }
  
  public int getMaxItemCount() {
    List<ITEM_T> list = this.currentItems;
    return (list == null) ? this.maxItemCount : Math.max(this.maxItemCount, list.size());
  }
  
  public RobotConfigMap getRobotConfigMap() {
    return this.robotConfigMap;
  }
  
  public ScannedDevices getScannedDevices() {
    return this.scannedDevices;
  }
  
  public boolean haveRobotConfigMapParameter() {
    return this.haveRobotConfigMapParameter;
  }
  
  public boolean isGrowable() {
    return this.growable;
  }
  
  public void putIntent(Intent paramIntent) {
    paramIntent.putExtras(toBundle());
  }
  
  public void setControlSystem(ControlSystem paramControlSystem) {
    this.controlSystem = paramControlSystem;
  }
  
  public void setCurrentCfgFile(RobotConfigFile paramRobotConfigFile) {
    this.currentCfgFile = paramRobotConfigFile;
  }
  
  public void setExtantRobotConfigurations(List<RobotConfigFile> paramList) {
    this.extantRobotConfigurations = paramList;
  }
  
  public void setGrowable(boolean paramBoolean) {
    this.growable = paramBoolean;
  }
  
  public void setI2cBus(int paramInt) {
    this.i2cBus = paramInt;
  }
  
  public void setInitialPortNumber(int paramInt) {
    this.initialPortNumber = paramInt;
  }
  
  public void setRobotConfigMap(RobotConfigMap paramRobotConfigMap) {
    this.robotConfigMap = paramRobotConfigMap;
    this.haveRobotConfigMapParameter = true;
  }
  
  public void setScannedDevices(ScannedDevices paramScannedDevices) {
    this.scannedDevices = paramScannedDevices;
  }
  
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    DeviceConfiguration deviceConfiguration = this.configuration;
    if (deviceConfiguration != null)
      bundle.putSerializable("configuration", (Serializable)deviceConfiguration); 
    ScannedDevices scannedDevices = this.scannedDevices;
    if (scannedDevices != null && scannedDevices.size() > 0)
      bundle.putString("scannedDevices", this.scannedDevices.toSerializationString()); 
    RobotConfigMap robotConfigMap = this.robotConfigMap;
    if (robotConfigMap != null && robotConfigMap.size() > 0)
      bundle.putSerializable("robotConfigMap", this.robotConfigMap); 
    List<RobotConfigFile> list = this.extantRobotConfigurations;
    if (list != null && list.size() > 0)
      bundle.putString("extantRobotConfigurations", RobotConfigFileManager.serializeXMLConfigList(this.extantRobotConfigurations)); 
    ControlSystem controlSystem = this.controlSystem;
    if (controlSystem != null)
      bundle.putSerializable("controlSystem", (Serializable)controlSystem); 
    RobotConfigFile robotConfigFile = this.currentCfgFile;
    if (robotConfigFile != null)
      bundle.putString("currentCfgFile", RobotConfigFileManager.serializeConfig(robotConfigFile)); 
    bundle.putBoolean("haveRobotConfigMap", this.haveRobotConfigMapParameter);
    bundle.putInt("initialPortNumber", this.initialPortNumber);
    bundle.putInt("maxItemCount", this.maxItemCount);
    bundle.putInt("i2cBus", this.i2cBus);
    bundle.putBoolean("growable", this.growable);
    bundle.putBoolean("isConfigDirty", this.isConfigDirty);
    Class<ITEM_T> clazz = this.itemClass;
    if (clazz != null)
      bundle.putString("itemClass", clazz.getCanonicalName()); 
    if (this.currentItems != null)
      for (int i = 0; i < this.currentItems.size(); i++)
        bundle.putSerializable(String.valueOf(i), (Serializable)this.currentItems.get(i));  
    return bundle;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditParameters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */