package com.qualcomm.robotcore.hardware;

import android.content.Context;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class HardwareMap implements Iterable<HardwareDevice> {
  private static final String LOG_FORMAT = "%-50s %-30s %s";
  
  public DeviceMapping<AccelerationSensor> accelerationSensor = new DeviceMapping<AccelerationSensor>(AccelerationSensor.class);
  
  public final List<DeviceMapping<? extends HardwareDevice>> allDeviceMappings;
  
  protected List<HardwareDevice> allDevicesList = null;
  
  protected Map<String, List<HardwareDevice>> allDevicesMap = new HashMap<String, List<HardwareDevice>>();
  
  public DeviceMapping<AnalogInput> analogInput = new DeviceMapping<AnalogInput>(AnalogInput.class);
  
  public DeviceMapping<AnalogOutput> analogOutput = new DeviceMapping<AnalogOutput>(AnalogOutput.class);
  
  public final Context appContext;
  
  public DeviceMapping<ColorSensor> colorSensor = new DeviceMapping<ColorSensor>(ColorSensor.class);
  
  public DeviceMapping<CompassSensor> compassSensor = new DeviceMapping<CompassSensor>(CompassSensor.class);
  
  public DeviceMapping<CRServo> crservo = new DeviceMapping<CRServo>(CRServo.class);
  
  public DeviceMapping<DcMotor> dcMotor = new DeviceMapping<DcMotor>(DcMotor.class);
  
  public DeviceMapping<DcMotorController> dcMotorController = new DeviceMapping<DcMotorController>(DcMotorController.class);
  
  public DeviceMapping<DeviceInterfaceModule> deviceInterfaceModule = new DeviceMapping<DeviceInterfaceModule>(DeviceInterfaceModule.class);
  
  protected Map<HardwareDevice, Set<String>> deviceNames = new HashMap<HardwareDevice, Set<String>>();
  
  public DeviceMapping<DigitalChannel> digitalChannel = new DeviceMapping<DigitalChannel>(DigitalChannel.class);
  
  public DeviceMapping<GyroSensor> gyroSensor = new DeviceMapping<GyroSensor>(GyroSensor.class);
  
  public DeviceMapping<I2cDevice> i2cDevice = new DeviceMapping<I2cDevice>(I2cDevice.class);
  
  public DeviceMapping<I2cDeviceSynch> i2cDeviceSynch = new DeviceMapping<I2cDeviceSynch>(I2cDeviceSynch.class);
  
  public DeviceMapping<IrSeekerSensor> irSeekerSensor = new DeviceMapping<IrSeekerSensor>(IrSeekerSensor.class);
  
  public DeviceMapping<LED> led = new DeviceMapping<LED>(LED.class);
  
  public DeviceMapping<LegacyModule> legacyModule = new DeviceMapping<LegacyModule>(LegacyModule.class);
  
  public DeviceMapping<LightSensor> lightSensor = new DeviceMapping<LightSensor>(LightSensor.class);
  
  protected final Object lock = new Object();
  
  public DeviceMapping<OpticalDistanceSensor> opticalDistanceSensor = new DeviceMapping<OpticalDistanceSensor>(OpticalDistanceSensor.class);
  
  public DeviceMapping<PWMOutput> pwmOutput = new DeviceMapping<PWMOutput>(PWMOutput.class);
  
  protected Map<SerialNumber, HardwareDevice> serialNumberMap = new HashMap<SerialNumber, HardwareDevice>();
  
  public DeviceMapping<Servo> servo = new DeviceMapping<Servo>(Servo.class);
  
  public DeviceMapping<ServoController> servoController = new DeviceMapping<ServoController>(ServoController.class);
  
  public DeviceMapping<TouchSensor> touchSensor = new DeviceMapping<TouchSensor>(TouchSensor.class);
  
  public DeviceMapping<TouchSensorMultiplexer> touchSensorMultiplexer = new DeviceMapping<TouchSensorMultiplexer>(TouchSensorMultiplexer.class);
  
  public DeviceMapping<UltrasonicSensor> ultrasonicSensor = new DeviceMapping<UltrasonicSensor>(UltrasonicSensor.class);
  
  public DeviceMapping<VoltageSensor> voltageSensor = new DeviceMapping<VoltageSensor>(VoltageSensor.class);
  
  public HardwareMap(Context paramContext) {
    this.appContext = paramContext;
    ArrayList<DeviceMapping<? extends HardwareDevice>> arrayList = new ArrayList(30);
    this.allDeviceMappings = arrayList;
    arrayList.add(this.dcMotorController);
    this.allDeviceMappings.add(this.dcMotor);
    this.allDeviceMappings.add(this.servoController);
    this.allDeviceMappings.add(this.servo);
    this.allDeviceMappings.add(this.crservo);
    this.allDeviceMappings.add(this.legacyModule);
    this.allDeviceMappings.add(this.touchSensorMultiplexer);
    this.allDeviceMappings.add(this.deviceInterfaceModule);
    this.allDeviceMappings.add(this.analogInput);
    this.allDeviceMappings.add(this.digitalChannel);
    this.allDeviceMappings.add(this.opticalDistanceSensor);
    this.allDeviceMappings.add(this.touchSensor);
    this.allDeviceMappings.add(this.pwmOutput);
    this.allDeviceMappings.add(this.i2cDevice);
    this.allDeviceMappings.add(this.i2cDeviceSynch);
    this.allDeviceMappings.add(this.analogOutput);
    this.allDeviceMappings.add(this.colorSensor);
    this.allDeviceMappings.add(this.led);
    this.allDeviceMappings.add(this.accelerationSensor);
    this.allDeviceMappings.add(this.compassSensor);
    this.allDeviceMappings.add(this.gyroSensor);
    this.allDeviceMappings.add(this.irSeekerSensor);
    this.allDeviceMappings.add(this.lightSensor);
    this.allDeviceMappings.add(this.ultrasonicSensor);
    this.allDeviceMappings.add(this.voltageSensor);
  }
  
  private void buildAllDevicesList() {
    if (this.allDevicesList == null) {
      HashSet<? extends HardwareDevice> hashSet = new HashSet();
      for (String str : this.allDevicesMap.keySet())
        hashSet.addAll(this.allDevicesMap.get(str)); 
      this.allDevicesList = new ArrayList<HardwareDevice>(hashSet);
    } 
  }
  
  public HardwareDevice get(String paramString) {
    synchronized (this.lock) {
      HardwareDevice hardwareDevice;
      paramString = paramString.trim();
      List list = this.allDevicesMap.get(paramString);
      if (list != null) {
        Iterator<HardwareDevice> iterator = list.iterator();
        if (iterator.hasNext()) {
          hardwareDevice = iterator.next();
          return hardwareDevice;
        } 
      } 
      throw new IllegalArgumentException(String.format("Unable to find a hardware device with name \"%s\"", new Object[] { hardwareDevice }));
    } 
  }
  
  public <T> T get(Class<? extends T> paramClass, SerialNumber paramSerialNumber) {
    synchronized (this.lock) {
      paramSerialNumber = (SerialNumber)this.serialNumberMap.get(paramSerialNumber);
      if (paramSerialNumber != null && paramClass.isInstance(paramSerialNumber)) {
        paramClass = (Class<? extends T>)paramClass.cast(paramSerialNumber);
        return (T)paramClass;
      } 
      return null;
    } 
  }
  
  public <T> T get(Class<? extends T> paramClass, String paramString) {
    synchronized (this.lock) {
      paramString = paramString.trim();
      T t = (T)tryGet((Class)paramClass, paramString);
      if (t != null)
        return t; 
      throw new IllegalArgumentException(String.format("Unable to find a hardware device with name \"%s\" and type %s", new Object[] { paramString, paramClass.getSimpleName() }));
    } 
  }
  
  public <T> List<T> getAll(Class<? extends T> paramClass) {
    synchronized (this.lock) {
      LinkedList<T> linkedList = new LinkedList();
      for (HardwareDevice hardwareDevice : this) {
        if (paramClass.isInstance(hardwareDevice))
          linkedList.add(paramClass.cast(hardwareDevice)); 
      } 
      return linkedList;
    } 
  }
  
  public Set<String> getNamesOf(HardwareDevice paramHardwareDevice) {
    synchronized (this.lock) {
      rebuildDeviceNamesIfNecessary();
      Set<String> set2 = this.deviceNames.get(paramHardwareDevice);
      Set<String> set1 = set2;
      if (set2 == null)
        set1 = new HashSet(); 
      return set1;
    } 
  }
  
  protected void internalPut(SerialNumber paramSerialNumber, String paramString, HardwareDevice paramHardwareDevice) {
    synchronized (this.lock) {
      String str = paramString.trim();
      List<HardwareDevice> list2 = this.allDevicesMap.get(str);
      List<HardwareDevice> list1 = list2;
      if (list2 == null) {
        list1 = new ArrayList(1);
        this.allDevicesMap.put(str, list1);
      } 
      if (!list1.contains(paramHardwareDevice)) {
        this.allDevicesList = null;
        list1.add(paramHardwareDevice);
      } 
      if (paramSerialNumber != null)
        this.serialNumberMap.put(paramSerialNumber, paramHardwareDevice); 
      rebuildDeviceNamesIfNecessary();
      recordDeviceName(str, paramHardwareDevice);
      return;
    } 
  }
  
  public Iterator<HardwareDevice> iterator() {
    synchronized (this.lock) {
      buildAllDevicesList();
      return (Iterator)(new ArrayList(this.allDevicesList)).iterator();
    } 
  }
  
  public void logDevices() {
    RobotLog.i("========= Device Information ===================================================");
    RobotLog.i(String.format("%-50s %-30s %s", new Object[] { "Type", "Name", "Connection" }));
    for (Map.Entry<String, List<HardwareDevice>> entry : this.allDevicesMap.entrySet()) {
      for (HardwareDevice hardwareDevice : entry.getValue()) {
        String str1 = hardwareDevice.getConnectionInfo();
        String str2 = (String)entry.getKey();
        RobotLog.i(String.format("%-50s %-30s %s", new Object[] { hardwareDevice.getDeviceName(), str2, str1 }));
      } 
    } 
  }
  
  public void put(SerialNumber paramSerialNumber, String paramString, HardwareDevice paramHardwareDevice) {
    Assert.assertNotNull(paramSerialNumber);
    internalPut(paramSerialNumber, paramString, paramHardwareDevice);
  }
  
  public void put(String paramString, HardwareDevice paramHardwareDevice) {
    internalPut(null, paramString, paramHardwareDevice);
  }
  
  protected void rebuildDeviceNamesIfNecessary() {
    if (this.deviceNames == null) {
      this.deviceNames = new ConcurrentHashMap<HardwareDevice, Set<String>>();
      for (Map.Entry<String, List<HardwareDevice>> entry : this.allDevicesMap.entrySet()) {
        for (HardwareDevice hardwareDevice : entry.getValue())
          recordDeviceName((String)entry.getKey(), hardwareDevice); 
      } 
    } 
  }
  
  protected void recordDeviceName(String paramString, HardwareDevice paramHardwareDevice) {
    String str = paramString.trim();
    Set<String> set2 = this.deviceNames.get(paramHardwareDevice);
    Set<String> set1 = set2;
    if (set2 == null) {
      set1 = new HashSet();
      this.deviceNames.put(paramHardwareDevice, set1);
    } 
    set1.add(str);
  }
  
  public boolean remove(SerialNumber paramSerialNumber, String paramString, HardwareDevice paramHardwareDevice) {
    synchronized (this.lock) {
      paramString = paramString.trim();
      List list = this.allDevicesMap.get(paramString);
      if (list != null) {
        list.remove(paramHardwareDevice);
        if (list.isEmpty())
          this.allDevicesMap.remove(paramString); 
        this.allDevicesList = null;
        this.deviceNames = null;
        if (paramSerialNumber != null)
          this.serialNumberMap.remove(paramSerialNumber); 
        return true;
      } 
      return false;
    } 
  }
  
  public boolean remove(String paramString, HardwareDevice paramHardwareDevice) {
    return remove(null, paramString, paramHardwareDevice);
  }
  
  public int size() {
    synchronized (this.lock) {
      buildAllDevicesList();
      return this.allDevicesList.size();
    } 
  }
  
  public <T> T tryGet(Class<? extends T> paramClass, String paramString) {
    synchronized (this.lock) {
      paramString = paramString.trim();
      List list = this.allDevicesMap.get(paramString);
      if (list != null)
        for (HardwareDevice hardwareDevice : list) {
          if (paramClass.isInstance(hardwareDevice)) {
            paramClass = (Class<? extends T>)paramClass.cast(hardwareDevice);
            return (T)paramClass;
          } 
        }  
      return null;
    } 
  }
  
  public class DeviceMapping<DEVICE_TYPE extends HardwareDevice> implements Iterable<DEVICE_TYPE> {
    private Class<DEVICE_TYPE> deviceTypeClass;
    
    private Map<String, DEVICE_TYPE> map = new HashMap<String, DEVICE_TYPE>();
    
    public DeviceMapping(Class<DEVICE_TYPE> param1Class) {
      this.deviceTypeClass = param1Class;
    }
    
    public DEVICE_TYPE cast(Object param1Object) {
      return this.deviceTypeClass.cast(param1Object);
    }
    
    public boolean contains(String param1String) {
      synchronized (HardwareMap.this.lock) {
        param1String = param1String.trim();
        return this.map.containsKey(param1String);
      } 
    }
    
    public Set<Map.Entry<String, DEVICE_TYPE>> entrySet() {
      synchronized (HardwareMap.this.lock) {
        return new HashSet(this.map.entrySet());
      } 
    }
    
    public DEVICE_TYPE get(String param1String) {
      synchronized (HardwareMap.this.lock) {
        param1String = param1String.trim();
        HardwareDevice hardwareDevice = (HardwareDevice)this.map.get(param1String);
        if (hardwareDevice != null)
          return (DEVICE_TYPE)hardwareDevice; 
        throw new IllegalArgumentException(String.format("Unable to find a hardware device with the name \"%s\"", new Object[] { param1String }));
      } 
    }
    
    public Class<DEVICE_TYPE> getDeviceTypeClass() {
      return this.deviceTypeClass;
    }
    
    protected void internalPut(SerialNumber param1SerialNumber, String param1String, DEVICE_TYPE param1DEVICE_TYPE) {
      synchronized (HardwareMap.this.lock) {
        param1String = param1String.trim();
        remove(param1SerialNumber, param1String);
        HardwareMap.this.internalPut(param1SerialNumber, param1String, (HardwareDevice)param1DEVICE_TYPE);
        putLocal(param1String, param1DEVICE_TYPE);
        return;
      } 
    }
    
    public Iterator<DEVICE_TYPE> iterator() {
      synchronized (HardwareMap.this.lock) {
        return (Iterator)(new ArrayList(this.map.values())).iterator();
      } 
    }
    
    public void put(SerialNumber param1SerialNumber, String param1String, DEVICE_TYPE param1DEVICE_TYPE) {
      internalPut(param1SerialNumber, param1String, param1DEVICE_TYPE);
    }
    
    public void put(String param1String, DEVICE_TYPE param1DEVICE_TYPE) {
      internalPut(null, param1String, param1DEVICE_TYPE);
    }
    
    public void putLocal(String param1String, DEVICE_TYPE param1DEVICE_TYPE) {
      synchronized (HardwareMap.this.lock) {
        param1String = param1String.trim();
        this.map.put(param1String, param1DEVICE_TYPE);
        return;
      } 
    }
    
    public boolean remove(SerialNumber param1SerialNumber, String param1String) {
      synchronized (HardwareMap.this.lock) {
        param1String = param1String.trim();
        HardwareDevice hardwareDevice = (HardwareDevice)this.map.remove(param1String);
        if (hardwareDevice != null) {
          HardwareMap.this.remove(param1SerialNumber, param1String, hardwareDevice);
          return true;
        } 
        return false;
      } 
    }
    
    public boolean remove(String param1String) {
      return remove(null, param1String);
    }
    
    public int size() {
      synchronized (HardwareMap.this.lock) {
        return this.map.size();
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\HardwareMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */