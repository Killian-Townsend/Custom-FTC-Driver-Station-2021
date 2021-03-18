package com.qualcomm.hardware.bosch;

import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cAddrConfig;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDeviceWithParameters;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.TimestampedData;
import com.qualcomm.robotcore.hardware.TimestampedI2cData;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ReadWriteFile;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.util.TypeConversion;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.external.navigation.Temperature;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public abstract class BNO055IMUImpl extends I2cDeviceSynchDeviceWithParameters<I2cDeviceSynch, BNO055IMU.Parameters> implements BNO055IMU, Gyroscope, IntegratingGyroscope, I2cAddrConfig, OpModeManagerNotifier.Notifications {
  static final byte bCHIP_ID_VALUE = -96;
  
  protected static final I2cDeviceSynch.ReadWindow lowerWindow;
  
  protected static final int msAwaitChipId = 2000;
  
  protected static final int msAwaitSelfTest = 2000;
  
  protected static final int msExtra = 50;
  
  protected static final I2cDeviceSynch.ReadMode readMode = I2cDeviceSynch.ReadMode.REPEAT;
  
  protected static final I2cDeviceSynch.ReadWindow upperWindow;
  
  protected BNO055IMU.AccelerationIntegrator accelerationAlgorithm;
  
  protected ExecutorService accelerationMananger;
  
  protected BNO055IMU.SensorMode currentMode;
  
  protected final Object dataLock = new Object();
  
  protected float delayScale = 1.0F;
  
  protected final Object startStopLock = new Object();
  
  static {
    lowerWindow = newWindow(BNO055IMU.Register.CHIP_ID, BNO055IMU.Register.EUL_H_LSB);
    upperWindow = newWindow(BNO055IMU.Register.EUL_H_LSB, BNO055IMU.Register.TEMP);
  }
  
  public BNO055IMUImpl(I2cDeviceSynch paramI2cDeviceSynch) {
    super((I2cDeviceSynchSimple)paramI2cDeviceSynch, true, disabledParameters());
    ((I2cDeviceSynch)this.deviceClient).setReadWindow(lowerWindow);
    ((I2cDeviceSynch)this.deviceClient).engage();
    this.currentMode = null;
    this.accelerationAlgorithm = new NaiveAccelerationIntegrator();
    this.accelerationMananger = null;
    registerArmingStateCallback(false);
  }
  
  protected static BNO055IMU.Parameters disabledParameters() {
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    parameters.mode = BNO055IMU.SensorMode.DISABLED;
    return parameters;
  }
  
  protected static I2cDeviceSynch.ReadWindow newWindow(BNO055IMU.Register paramRegister1, BNO055IMU.Register paramRegister2) {
    return new I2cDeviceSynch.ReadWindow(paramRegister1.bVal, paramRegister2.bVal - paramRegister1.bVal, readMode);
  }
  
  public void close() {
    stopAccelerationIntegration();
    super.close();
  }
  
  protected void delay(int paramInt) {
    try {
      waitForWriteCompletions();
      Thread.sleep((int)(paramInt * this.delayScale));
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } 
  }
  
  protected void delayExtra(int paramInt) {
    delay(paramInt + 50);
  }
  
  protected void delayLore(int paramInt) {
    delay(paramInt);
  }
  
  protected void delayLoreExtra(int paramInt) {
    delayLore(paramInt + 50);
  }
  
  protected void ensureReadWindow(I2cDeviceSynch.ReadWindow paramReadWindow) {
    I2cDeviceSynch.ReadWindow readWindow;
    if (lowerWindow.containsWithSameMode(paramReadWindow)) {
      readWindow = lowerWindow;
    } else if (upperWindow.containsWithSameMode(paramReadWindow)) {
      readWindow = upperWindow;
    } else {
      readWindow = paramReadWindow;
    } 
    ((I2cDeviceSynch)this.deviceClient).ensureReadWindow(paramReadWindow, readWindow);
  }
  
  protected <T> T enterConfigModeFor(Func<T> paramFunc) {
    BNO055IMU.SensorMode sensorMode = this.currentMode;
    setSensorMode(BNO055IMU.SensorMode.CONFIG);
    delayLoreExtra(25);
    try {
      return (T)paramFunc.value();
    } finally {
      setSensorMode(sensorMode);
      delayLoreExtra(20);
    } 
  }
  
  protected void enterConfigModeFor(Runnable paramRunnable) {
    BNO055IMU.SensorMode sensorMode = this.currentMode;
    setSensorMode(BNO055IMU.SensorMode.CONFIG);
    delayLoreExtra(25);
    try {
      paramRunnable.run();
      return;
    } finally {
      setSensorMode(sensorMode);
      delayLoreExtra(20);
    } 
  }
  
  public Acceleration getAcceleration() {
    synchronized (this.dataLock) {
      Acceleration acceleration2 = this.accelerationAlgorithm.getAcceleration();
      Acceleration acceleration1 = acceleration2;
      if (acceleration2 == null)
        acceleration1 = new Acceleration(); 
      return acceleration1;
    } 
  }
  
  protected float getAccelerationScale() {
    return (((BNO055IMU.Parameters)this.parameters).accelUnit == BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC) ? 100.0F : 1.0F;
  }
  
  public Orientation getAngularOrientation() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR.EULER : Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR;
    //   6: aload_0
    //   7: invokevirtual getAngularScale : ()F
    //   10: invokevirtual getVector : (Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR;F)Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VectorData;
    //   13: astore_1
    //   14: aload_0
    //   15: getfield parameters : Ljava/lang/Object;
    //   18: checkcast com/qualcomm/hardware/bosch/BNO055IMU$Parameters
    //   21: getfield angleUnit : Lcom/qualcomm/hardware/bosch/BNO055IMU$AngleUnit;
    //   24: invokevirtual toAngleUnit : ()Lorg/firstinspires/ftc/robotcore/external/navigation/AngleUnit;
    //   27: astore_2
    //   28: new org/firstinspires/ftc/robotcore/external/navigation/Orientation
    //   31: dup
    //   32: getstatic org/firstinspires/ftc/robotcore/external/navigation/AxesReference.INTRINSIC : Lorg/firstinspires/ftc/robotcore/external/navigation/AxesReference;
    //   35: getstatic org/firstinspires/ftc/robotcore/external/navigation/AxesOrder.ZYX : Lorg/firstinspires/ftc/robotcore/external/navigation/AxesOrder;
    //   38: aload_2
    //   39: aload_2
    //   40: aload_1
    //   41: invokevirtual next : ()F
    //   44: fneg
    //   45: invokevirtual normalize : (F)F
    //   48: aload_2
    //   49: aload_1
    //   50: invokevirtual next : ()F
    //   53: invokevirtual normalize : (F)F
    //   56: aload_2
    //   57: aload_1
    //   58: invokevirtual next : ()F
    //   61: invokevirtual normalize : (F)F
    //   64: aload_1
    //   65: getfield data : Lcom/qualcomm/robotcore/hardware/TimestampedData;
    //   68: getfield nanoTime : J
    //   71: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/external/navigation/AxesReference;Lorg/firstinspires/ftc/robotcore/external/navigation/AxesOrder;Lorg/firstinspires/ftc/robotcore/external/navigation/AngleUnit;FFFJ)V
    //   74: astore_1
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_1
    //   78: areturn
    //   79: astore_1
    //   80: aload_0
    //   81: monitorexit
    //   82: aload_1
    //   83: athrow
    // Exception table:
    //   from	to	target	type
    //   2	75	79	finally
  }
  
  public Orientation getAngularOrientation(AxesReference paramAxesReference, AxesOrder paramAxesOrder, AngleUnit paramAngleUnit) {
    return getAngularOrientation().toAxesReference(paramAxesReference).toAxesOrder(paramAxesOrder).toAngleUnit(paramAngleUnit);
  }
  
  public Set<Axis> getAngularOrientationAxes() {
    HashSet<Axis> hashSet = new HashSet();
    hashSet.add(Axis.X);
    hashSet.add(Axis.Y);
    hashSet.add(Axis.Z);
    return hashSet;
  }
  
  protected float getAngularScale() {
    return (((BNO055IMU.Parameters)this.parameters).angleUnit == BNO055IMU.AngleUnit.DEGREES) ? 16.0F : 900.0F;
  }
  
  public AngularVelocity getAngularVelocity() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield parameters : Ljava/lang/Object;
    //   7: checkcast com/qualcomm/hardware/bosch/BNO055IMU$Parameters
    //   10: getfield angleUnit : Lcom/qualcomm/hardware/bosch/BNO055IMU$AngleUnit;
    //   13: invokevirtual toAngleUnit : ()Lorg/firstinspires/ftc/robotcore/external/navigation/AngleUnit;
    //   16: invokevirtual getAngularVelocity : (Lorg/firstinspires/ftc/robotcore/external/navigation/AngleUnit;)Lorg/firstinspires/ftc/robotcore/external/navigation/AngularVelocity;
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
    //   2	20	24	finally
  }
  
  public AngularVelocity getAngularVelocity(AngleUnit paramAngleUnit) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR.GYROSCOPE : Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR;
    //   6: aload_0
    //   7: invokevirtual getAngularScale : ()F
    //   10: invokevirtual getVector : (Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR;F)Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VectorData;
    //   13: astore #5
    //   15: aload #5
    //   17: invokevirtual next : ()F
    //   20: fneg
    //   21: fstore_2
    //   22: aload #5
    //   24: invokevirtual next : ()F
    //   27: fstore_3
    //   28: aload #5
    //   30: invokevirtual next : ()F
    //   33: fstore #4
    //   35: new org/firstinspires/ftc/robotcore/external/navigation/AngularVelocity
    //   38: dup
    //   39: aload_0
    //   40: getfield parameters : Ljava/lang/Object;
    //   43: checkcast com/qualcomm/hardware/bosch/BNO055IMU$Parameters
    //   46: getfield angleUnit : Lcom/qualcomm/hardware/bosch/BNO055IMU$AngleUnit;
    //   49: invokevirtual toAngleUnit : ()Lorg/firstinspires/ftc/robotcore/external/navigation/AngleUnit;
    //   52: fload #4
    //   54: fload_3
    //   55: fload_2
    //   56: aload #5
    //   58: getfield data : Lcom/qualcomm/robotcore/hardware/TimestampedData;
    //   61: getfield nanoTime : J
    //   64: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/external/navigation/AngleUnit;FFFJ)V
    //   67: aload_1
    //   68: invokevirtual toAngleUnit : (Lorg/firstinspires/ftc/robotcore/external/navigation/AngleUnit;)Lorg/firstinspires/ftc/robotcore/external/navigation/AngularVelocity;
    //   71: astore_1
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_1
    //   75: areturn
    //   76: astore_1
    //   77: aload_0
    //   78: monitorexit
    //   79: aload_1
    //   80: athrow
    // Exception table:
    //   from	to	target	type
    //   2	72	76	finally
  }
  
  public Set<Axis> getAngularVelocityAxes() {
    HashSet<Axis> hashSet = new HashSet();
    hashSet.add(Axis.X);
    hashSet.add(Axis.Y);
    hashSet.add(Axis.Z);
    return hashSet;
  }
  
  public BNO055IMU.CalibrationStatus getCalibrationStatus() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new com/qualcomm/hardware/bosch/BNO055IMU$CalibrationStatus
    //   5: dup
    //   6: aload_0
    //   7: getstatic com/qualcomm/hardware/bosch/BNO055IMU$Register.CALIB_STAT : Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;
    //   10: invokevirtual read8 : (Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;)B
    //   13: invokespecial <init> : (I)V
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: areturn
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	21	finally
  }
  
  public abstract String getDeviceName();
  
  protected float getFluxScale() {
    return 1.6E7F;
  }
  
  public Acceleration getGravity() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR.GRAVITY : Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR;
    //   6: aload_0
    //   7: invokevirtual getMetersAccelerationScale : ()F
    //   10: invokevirtual getVector : (Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR;F)Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VectorData;
    //   13: astore_1
    //   14: new org/firstinspires/ftc/robotcore/external/navigation/Acceleration
    //   17: dup
    //   18: getstatic org/firstinspires/ftc/robotcore/external/navigation/DistanceUnit.METER : Lorg/firstinspires/ftc/robotcore/external/navigation/DistanceUnit;
    //   21: aload_1
    //   22: invokevirtual next : ()F
    //   25: f2d
    //   26: aload_1
    //   27: invokevirtual next : ()F
    //   30: f2d
    //   31: aload_1
    //   32: invokevirtual next : ()F
    //   35: f2d
    //   36: aload_1
    //   37: getfield data : Lcom/qualcomm/robotcore/hardware/TimestampedData;
    //   40: getfield nanoTime : J
    //   43: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/external/navigation/DistanceUnit;DDDJ)V
    //   46: astore_1
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_1
    //   50: areturn
    //   51: astore_1
    //   52: aload_0
    //   53: monitorexit
    //   54: aload_1
    //   55: athrow
    // Exception table:
    //   from	to	target	type
    //   2	47	51	finally
  }
  
  public I2cAddr getI2cAddress() {
    return ((BNO055IMU.Parameters)this.parameters).i2cAddr;
  }
  
  public Acceleration getLinearAcceleration() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR.LINEARACCEL : Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR;
    //   6: aload_0
    //   7: invokevirtual getMetersAccelerationScale : ()F
    //   10: invokevirtual getVector : (Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR;F)Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VectorData;
    //   13: astore_1
    //   14: new org/firstinspires/ftc/robotcore/external/navigation/Acceleration
    //   17: dup
    //   18: getstatic org/firstinspires/ftc/robotcore/external/navigation/DistanceUnit.METER : Lorg/firstinspires/ftc/robotcore/external/navigation/DistanceUnit;
    //   21: aload_1
    //   22: invokevirtual next : ()F
    //   25: f2d
    //   26: aload_1
    //   27: invokevirtual next : ()F
    //   30: f2d
    //   31: aload_1
    //   32: invokevirtual next : ()F
    //   35: f2d
    //   36: aload_1
    //   37: getfield data : Lcom/qualcomm/robotcore/hardware/TimestampedData;
    //   40: getfield nanoTime : J
    //   43: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/external/navigation/DistanceUnit;DDDJ)V
    //   46: astore_1
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_1
    //   50: areturn
    //   51: astore_1
    //   52: aload_0
    //   53: monitorexit
    //   54: aload_1
    //   55: athrow
    // Exception table:
    //   from	to	target	type
    //   2	47	51	finally
  }
  
  protected String getLoggingTag() {
    return ((BNO055IMU.Parameters)this.parameters).loggingTag;
  }
  
  public MagneticFlux getMagneticFieldStrength() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR.MAGNETOMETER : Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR;
    //   6: aload_0
    //   7: invokevirtual getFluxScale : ()F
    //   10: invokevirtual getVector : (Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR;F)Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VectorData;
    //   13: astore_1
    //   14: new org/firstinspires/ftc/robotcore/external/navigation/MagneticFlux
    //   17: dup
    //   18: aload_1
    //   19: invokevirtual next : ()F
    //   22: f2d
    //   23: aload_1
    //   24: invokevirtual next : ()F
    //   27: f2d
    //   28: aload_1
    //   29: invokevirtual next : ()F
    //   32: f2d
    //   33: aload_1
    //   34: getfield data : Lcom/qualcomm/robotcore/hardware/TimestampedData;
    //   37: getfield nanoTime : J
    //   40: invokespecial <init> : (DDDJ)V
    //   43: astore_1
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_1
    //   47: areturn
    //   48: astore_1
    //   49: aload_0
    //   50: monitorexit
    //   51: aload_1
    //   52: athrow
    // Exception table:
    //   from	to	target	type
    //   2	44	48	finally
  }
  
  public abstract HardwareDevice.Manufacturer getManufacturer();
  
  protected float getMetersAccelerationScale() {
    return (((BNO055IMU.Parameters)this.parameters).accelUnit == BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC) ? getAccelerationScale() : (getAccelerationScale() * 100.0F);
  }
  
  public Acceleration getOverallAcceleration() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR.ACCELEROMETER : Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR;
    //   6: aload_0
    //   7: invokevirtual getMetersAccelerationScale : ()F
    //   10: invokevirtual getVector : (Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VECTOR;F)Lcom/qualcomm/hardware/bosch/BNO055IMUImpl$VectorData;
    //   13: astore_1
    //   14: new org/firstinspires/ftc/robotcore/external/navigation/Acceleration
    //   17: dup
    //   18: getstatic org/firstinspires/ftc/robotcore/external/navigation/DistanceUnit.METER : Lorg/firstinspires/ftc/robotcore/external/navigation/DistanceUnit;
    //   21: aload_1
    //   22: invokevirtual next : ()F
    //   25: f2d
    //   26: aload_1
    //   27: invokevirtual next : ()F
    //   30: f2d
    //   31: aload_1
    //   32: invokevirtual next : ()F
    //   35: f2d
    //   36: aload_1
    //   37: getfield data : Lcom/qualcomm/robotcore/hardware/TimestampedData;
    //   40: getfield nanoTime : J
    //   43: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/external/navigation/DistanceUnit;DDDJ)V
    //   46: astore_1
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_1
    //   50: areturn
    //   51: astore_1
    //   52: aload_0
    //   53: monitorexit
    //   54: aload_1
    //   55: athrow
    // Exception table:
    //   from	to	target	type
    //   2	47	51	finally
  }
  
  public Position getPosition() {
    synchronized (this.dataLock) {
      Position position2 = this.accelerationAlgorithm.getPosition();
      Position position1 = position2;
      if (position2 == null)
        position1 = new Position(); 
      return position1;
    } 
  }
  
  public Quaternion getQuaternionOrientation() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   6: checkcast com/qualcomm/robotcore/hardware/I2cDeviceSynch
    //   9: new com/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow
    //   12: dup
    //   13: getstatic com/qualcomm/hardware/bosch/BNO055IMU$Register.QUA_DATA_W_LSB : Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;
    //   16: getfield bVal : B
    //   19: bipush #8
    //   21: getstatic com/qualcomm/hardware/bosch/BNO055IMUImpl.readMode : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadMode;
    //   24: invokespecial <init> : (IILcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadMode;)V
    //   27: getstatic com/qualcomm/hardware/bosch/BNO055IMUImpl.upperWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
    //   30: invokeinterface ensureReadWindow : (Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;)V
    //   35: new com/qualcomm/hardware/bosch/BNO055IMUImpl$VectorData
    //   38: dup
    //   39: aload_0
    //   40: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   43: checkcast com/qualcomm/robotcore/hardware/I2cDeviceSynch
    //   46: getstatic com/qualcomm/hardware/bosch/BNO055IMU$Register.QUA_DATA_W_LSB : Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;
    //   49: getfield bVal : B
    //   52: bipush #8
    //   54: invokeinterface readTimeStamped : (II)Lcom/qualcomm/robotcore/hardware/TimestampedData;
    //   59: ldc_w 16384.0
    //   62: invokespecial <init> : (Lcom/qualcomm/robotcore/hardware/TimestampedData;F)V
    //   65: astore_1
    //   66: new org/firstinspires/ftc/robotcore/external/navigation/Quaternion
    //   69: dup
    //   70: aload_1
    //   71: invokevirtual next : ()F
    //   74: aload_1
    //   75: invokevirtual next : ()F
    //   78: aload_1
    //   79: invokevirtual next : ()F
    //   82: aload_1
    //   83: invokevirtual next : ()F
    //   86: aload_1
    //   87: getfield data : Lcom/qualcomm/robotcore/hardware/TimestampedData;
    //   90: getfield nanoTime : J
    //   93: invokespecial <init> : (FFFFJ)V
    //   96: astore_1
    //   97: aload_0
    //   98: monitorexit
    //   99: aload_1
    //   100: areturn
    //   101: astore_1
    //   102: aload_0
    //   103: monitorexit
    //   104: aload_1
    //   105: athrow
    // Exception table:
    //   from	to	target	type
    //   2	97	101	finally
  }
  
  public BNO055IMU.SystemError getSystemError() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/bosch/BNO055IMU$Register.SYS_ERR : Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;
    //   6: invokevirtual read8 : (Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;)B
    //   9: istore_1
    //   10: iload_1
    //   11: invokestatic from : (I)Lcom/qualcomm/hardware/bosch/BNO055IMU$SystemError;
    //   14: astore_2
    //   15: aload_2
    //   16: getstatic com/qualcomm/hardware/bosch/BNO055IMU$SystemError.UNKNOWN : Lcom/qualcomm/hardware/bosch/BNO055IMU$SystemError;
    //   19: if_acmpne -> 40
    //   22: aload_0
    //   23: ldc_w 'unknown system error observed: 0x%08x'
    //   26: iconst_1
    //   27: anewarray java/lang/Object
    //   30: dup
    //   31: iconst_0
    //   32: iload_1
    //   33: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   36: aastore
    //   37: invokevirtual log_w : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_2
    //   43: areturn
    //   44: astore_2
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_2
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	40	44	finally
  }
  
  public BNO055IMU.SystemStatus getSystemStatus() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/bosch/BNO055IMU$Register.SYS_STAT : Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;
    //   6: invokevirtual read8 : (Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;)B
    //   9: istore_1
    //   10: iload_1
    //   11: invokestatic from : (I)Lcom/qualcomm/hardware/bosch/BNO055IMU$SystemStatus;
    //   14: astore_2
    //   15: aload_2
    //   16: getstatic com/qualcomm/hardware/bosch/BNO055IMU$SystemStatus.UNKNOWN : Lcom/qualcomm/hardware/bosch/BNO055IMU$SystemStatus;
    //   19: if_acmpne -> 40
    //   22: aload_0
    //   23: ldc_w 'unknown system status observed: 0x%08x'
    //   26: iconst_1
    //   27: anewarray java/lang/Object
    //   30: dup
    //   31: iconst_0
    //   32: iload_1
    //   33: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   36: aastore
    //   37: invokevirtual log_w : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_2
    //   43: areturn
    //   44: astore_2
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_2
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	40	44	finally
  }
  
  public Temperature getTemperature() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/bosch/BNO055IMU$Register.TEMP : Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;
    //   6: invokevirtual read8 : (Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;)B
    //   9: istore_1
    //   10: new org/firstinspires/ftc/robotcore/external/navigation/Temperature
    //   13: dup
    //   14: aload_0
    //   15: getfield parameters : Ljava/lang/Object;
    //   18: checkcast com/qualcomm/hardware/bosch/BNO055IMU$Parameters
    //   21: getfield temperatureUnit : Lcom/qualcomm/hardware/bosch/BNO055IMU$TempUnit;
    //   24: invokevirtual toTempUnit : ()Lorg/firstinspires/ftc/robotcore/external/navigation/TempUnit;
    //   27: iload_1
    //   28: i2d
    //   29: invokestatic nanoTime : ()J
    //   32: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/external/navigation/TempUnit;DJ)V
    //   35: astore_2
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_2
    //   39: areturn
    //   40: astore_2
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_2
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	36	40	finally
  }
  
  protected VectorData getVector(VECTOR paramVECTOR, float paramFloat) {
    ensureReadWindow(new I2cDeviceSynch.ReadWindow(paramVECTOR.getValue(), 6, readMode));
    return new VectorData(((I2cDeviceSynch)this.deviceClient).readTimeStamped(paramVECTOR.getValue(), 6), paramFloat);
  }
  
  public Velocity getVelocity() {
    synchronized (this.dataLock) {
      Velocity velocity2 = this.accelerationAlgorithm.getVelocity();
      Velocity velocity1 = velocity2;
      if (velocity2 == null)
        velocity1 = new Velocity(); 
      return velocity1;
    } 
  }
  
  public boolean internalInitialize(BNO055IMU.Parameters paramParameters) {
    BNO055IMU.SystemStatus systemStatus;
    if (paramParameters.mode == BNO055IMU.SensorMode.DISABLED)
      return false; 
    BNO055IMU.Parameters parameters = (BNO055IMU.Parameters)this.parameters;
    this.parameters = paramParameters.clone();
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(paramParameters.i2cAddr);
    if (paramParameters.mode.isFusionMode()) {
      systemStatus = BNO055IMU.SystemStatus.RUNNING_FUSION;
    } else {
      systemStatus = BNO055IMU.SystemStatus.RUNNING_NO_FUSION;
    } 
    if (internalInitializeOnce(systemStatus)) {
      this.isInitialized = true;
      return true;
    } 
    log_e("IMU initialization failed", new Object[0]);
    this.parameters = parameters;
    return false;
  }
  
  protected boolean internalInitializeOnce(BNO055IMU.SystemStatus paramSystemStatus) {
    if (BNO055IMU.SensorMode.CONFIG != ((BNO055IMU.Parameters)this.parameters).mode) {
      ElapsedTime elapsedTime = new ElapsedTime();
      if (((BNO055IMU.Parameters)this.parameters).accelerationIntegrationAlgorithm != null)
        this.accelerationAlgorithm = ((BNO055IMU.Parameters)this.parameters).accelerationIntegrationAlgorithm; 
      if (read8(BNO055IMU.Register.CHIP_ID) != -96) {
        delayExtra(650);
        byte b = read8(BNO055IMU.Register.CHIP_ID);
        if (b != -96) {
          log_e("unexpected chip: expected=%d found=%d", new Object[] { Byte.valueOf((byte)-96), Byte.valueOf(b) });
          return false;
        } 
      } 
      setSensorMode(BNO055IMU.SensorMode.CONFIG);
      TimestampedI2cData.suppressNewHealthWarnings(true);
      try {
        elapsedTime.reset();
        write8(BNO055IMU.Register.SYS_TRIGGER, 32, I2cWaitControl.WRITTEN);
        delay(400);
        RobotLog.vv("IMU", "Now polling until IMU comes out of reset. It is normal to see I2C failures below");
        while (!isStopRequested() && read8(BNO055IMU.Register.CHIP_ID) != -96) {
          delayExtra(10);
          if (elapsedTime.milliseconds() > 2000.0D) {
            log_e("failed to retrieve chip id", new Object[0]);
            return false;
          } 
        } 
        delayLoreExtra(50);
        TimestampedI2cData.suppressNewHealthWarnings(false);
        RobotLog.vv("IMU", "IMU has come out of reset. No more I2C failures should occur.");
        write8(BNO055IMU.Register.PWR_MODE, POWER_MODE.NORMAL.getValue(), I2cWaitControl.WRITTEN);
        delayLoreExtra(10);
        write8(BNO055IMU.Register.PAGE_ID, 0);
        byte b1 = ((BNO055IMU.Parameters)this.parameters).pitchMode.bVal;
        byte b2 = ((BNO055IMU.Parameters)this.parameters).temperatureUnit.bVal;
        byte b3 = ((BNO055IMU.Parameters)this.parameters).angleUnit.bVal;
        byte b4 = ((BNO055IMU.Parameters)this.parameters).angleUnit.bVal;
        byte b5 = ((BNO055IMU.Parameters)this.parameters).accelUnit.bVal;
        write8(BNO055IMU.Register.UNIT_SEL, b1 << 7 | b2 << 4 | b3 << 2 | b4 << 1 | b5);
        write8(BNO055IMU.Register.PAGE_ID, 1);
        write8(BNO055IMU.Register.ACC_CONFIG, ((BNO055IMU.Parameters)this.parameters).accelPowerMode.bVal | ((BNO055IMU.Parameters)this.parameters).accelBandwidth.bVal | ((BNO055IMU.Parameters)this.parameters).accelRange.bVal);
        write8(BNO055IMU.Register.MAG_CONFIG, ((BNO055IMU.Parameters)this.parameters).magPowerMode.bVal | ((BNO055IMU.Parameters)this.parameters).magOpMode.bVal | ((BNO055IMU.Parameters)this.parameters).magRate.bVal);
        write8(BNO055IMU.Register.GYR_CONFIG_0, ((BNO055IMU.Parameters)this.parameters).gyroBandwidth.bVal | ((BNO055IMU.Parameters)this.parameters).gyroRange.bVal);
        write8(BNO055IMU.Register.GYR_CONFIG_1, ((BNO055IMU.Parameters)this.parameters).gyroPowerMode.bVal);
        write8(BNO055IMU.Register.PAGE_ID, 0);
        write8(BNO055IMU.Register.SYS_TRIGGER, 0);
        if (((BNO055IMU.Parameters)this.parameters).calibrationData != null) {
          writeCalibrationData(((BNO055IMU.Parameters)this.parameters).calibrationData);
        } else if (((BNO055IMU.Parameters)this.parameters).calibrationDataFile != null) {
          try {
            writeCalibrationData(BNO055IMU.CalibrationData.deserialize(ReadWriteFile.readFileOrThrow(AppUtil.getInstance().getSettingsFile(((BNO055IMU.Parameters)this.parameters).calibrationDataFile))));
          } catch (IOException iOException) {}
        } 
        setSensorMode(((BNO055IMU.Parameters)this.parameters).mode);
        BNO055IMU.SystemStatus systemStatus = getSystemStatus();
        if (systemStatus == paramSystemStatus)
          return true; 
        return false;
      } finally {
        TimestampedI2cData.suppressNewHealthWarnings(false);
      } 
    } 
    throw new IllegalArgumentException("SensorMode.CONFIG illegal for use as initialization mode");
  }
  
  public boolean isAccelerometerCalibrated() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/bosch/BNO055IMU$Register.CALIB_STAT : Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;
    //   6: invokevirtual read8 : (Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;)B
    //   9: istore_1
    //   10: iload_1
    //   11: iconst_2
    //   12: ishr
    //   13: iconst_3
    //   14: iand
    //   15: iconst_3
    //   16: if_icmpne -> 24
    //   19: iconst_1
    //   20: istore_2
    //   21: goto -> 26
    //   24: iconst_0
    //   25: istore_2
    //   26: aload_0
    //   27: monitorexit
    //   28: iload_2
    //   29: ireturn
    //   30: astore_3
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_3
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	30	finally
  }
  
  public boolean isGyroCalibrated() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/bosch/BNO055IMU$Register.CALIB_STAT : Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;
    //   6: invokevirtual read8 : (Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;)B
    //   9: istore_1
    //   10: iload_1
    //   11: iconst_4
    //   12: ishr
    //   13: iconst_3
    //   14: iand
    //   15: iconst_3
    //   16: if_icmpne -> 24
    //   19: iconst_1
    //   20: istore_2
    //   21: goto -> 26
    //   24: iconst_0
    //   25: istore_2
    //   26: aload_0
    //   27: monitorexit
    //   28: iload_2
    //   29: ireturn
    //   30: astore_3
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_3
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	30	finally
  }
  
  public boolean isMagnetometerCalibrated() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/bosch/BNO055IMU$Register.CALIB_STAT : Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;
    //   6: invokevirtual read8 : (Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;)B
    //   9: istore_1
    //   10: iload_1
    //   11: iconst_3
    //   12: iand
    //   13: iconst_3
    //   14: if_icmpne -> 22
    //   17: iconst_1
    //   18: istore_2
    //   19: goto -> 24
    //   22: iconst_0
    //   23: istore_2
    //   24: aload_0
    //   25: monitorexit
    //   26: iload_2
    //   27: ireturn
    //   28: astore_3
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_3
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	28	finally
  }
  
  boolean isStopRequested() {
    return Thread.currentThread().isInterrupted();
  }
  
  public boolean isSystemCalibrated() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/bosch/BNO055IMU$Register.CALIB_STAT : Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;
    //   6: invokevirtual read8 : (Lcom/qualcomm/hardware/bosch/BNO055IMU$Register;)B
    //   9: istore_1
    //   10: iload_1
    //   11: bipush #6
    //   13: ishr
    //   14: iconst_3
    //   15: iand
    //   16: iconst_3
    //   17: if_icmpne -> 25
    //   20: iconst_1
    //   21: istore_2
    //   22: goto -> 27
    //   25: iconst_0
    //   26: istore_2
    //   27: aload_0
    //   28: monitorexit
    //   29: iload_2
    //   30: ireturn
    //   31: astore_3
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_3
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	31	finally
  }
  
  protected void log_d(String paramString, Object... paramVarArgs) {
    if (((BNO055IMU.Parameters)this.parameters).loggingEnabled) {
      paramString = String.format(paramString, paramVarArgs);
      Log.d(getLoggingTag(), paramString);
    } 
  }
  
  protected void log_e(String paramString, Object... paramVarArgs) {
    if (((BNO055IMU.Parameters)this.parameters).loggingEnabled) {
      paramString = String.format(paramString, paramVarArgs);
      Log.e(getLoggingTag(), paramString);
    } 
  }
  
  protected void log_v(String paramString, Object... paramVarArgs) {
    if (((BNO055IMU.Parameters)this.parameters).loggingEnabled) {
      paramString = String.format(paramString, paramVarArgs);
      Log.v(getLoggingTag(), paramString);
    } 
  }
  
  protected void log_w(String paramString, Object... paramVarArgs) {
    if (((BNO055IMU.Parameters)this.parameters).loggingEnabled) {
      paramString = String.format(paramString, paramVarArgs);
      Log.w(getLoggingTag(), paramString);
    } 
  }
  
  public void onOpModePostStop(OpMode paramOpMode) {
    stopAccelerationIntegration();
  }
  
  public void onOpModePreInit(OpMode paramOpMode) {}
  
  public void onOpModePreStart(OpMode paramOpMode) {}
  
  public byte[] read(BNO055IMU.Register paramRegister, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   6: checkcast com/qualcomm/robotcore/hardware/I2cDeviceSynch
    //   9: aload_1
    //   10: getfield bVal : B
    //   13: iload_2
    //   14: invokeinterface read : (II)[B
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
    //   2	20	24	finally
  }
  
  public byte read8(BNO055IMU.Register paramRegister) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   6: checkcast com/qualcomm/robotcore/hardware/I2cDeviceSynch
    //   9: aload_1
    //   10: getfield bVal : B
    //   13: invokeinterface read8 : (I)B
    //   18: istore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: iload_2
    //   22: ireturn
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	23	finally
  }
  
  public BNO055IMU.CalibrationData readCalibrationData() {
    BNO055IMU.SensorMode sensorMode = this.currentMode;
    if (sensorMode != BNO055IMU.SensorMode.CONFIG)
      setSensorMode(BNO055IMU.SensorMode.CONFIG); 
    BNO055IMU.CalibrationData calibrationData = new BNO055IMU.CalibrationData();
    calibrationData.dxAccel = readShort(BNO055IMU.Register.ACC_OFFSET_X_LSB);
    calibrationData.dyAccel = readShort(BNO055IMU.Register.ACC_OFFSET_Y_LSB);
    calibrationData.dzAccel = readShort(BNO055IMU.Register.ACC_OFFSET_Z_LSB);
    calibrationData.dxMag = readShort(BNO055IMU.Register.MAG_OFFSET_X_LSB);
    calibrationData.dyMag = readShort(BNO055IMU.Register.MAG_OFFSET_Y_LSB);
    calibrationData.dzMag = readShort(BNO055IMU.Register.MAG_OFFSET_Z_LSB);
    calibrationData.dxGyro = readShort(BNO055IMU.Register.GYR_OFFSET_X_LSB);
    calibrationData.dyGyro = readShort(BNO055IMU.Register.GYR_OFFSET_Y_LSB);
    calibrationData.dzGyro = readShort(BNO055IMU.Register.GYR_OFFSET_Z_LSB);
    calibrationData.radiusAccel = readShort(BNO055IMU.Register.ACC_RADIUS_LSB);
    calibrationData.radiusMag = readShort(BNO055IMU.Register.MAG_RADIUS_LSB);
    if (sensorMode != BNO055IMU.SensorMode.CONFIG)
      setSensorMode(sensorMode); 
    return calibrationData;
  }
  
  protected short readShort(BNO055IMU.Register paramRegister) {
    return TypeConversion.byteArrayToShort(read(paramRegister, 2), ByteOrder.LITTLE_ENDIAN);
  }
  
  public void resetDeviceConfigurationForOpMode() {
    stopAccelerationIntegration();
    this.parameters = disabledParameters();
    super.resetDeviceConfigurationForOpMode();
  }
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    ((BNO055IMU.Parameters)this.parameters).i2cAddr = paramI2cAddr;
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(paramI2cAddr);
  }
  
  protected void setSensorMode(BNO055IMU.SensorMode paramSensorMode) {
    this.currentMode = paramSensorMode;
    write8(BNO055IMU.Register.OPR_MODE, paramSensorMode.bVal & 0xF, I2cWaitControl.WRITTEN);
    if (paramSensorMode == BNO055IMU.SensorMode.CONFIG) {
      delayExtra(19);
      return;
    } 
    delayExtra(7);
  }
  
  public void startAccelerationIntegration(Position paramPosition, Velocity paramVelocity, int paramInt) {
    synchronized (this.startStopLock) {
      stopAccelerationIntegration();
      this.accelerationAlgorithm.initialize((BNO055IMU.Parameters)this.parameters, paramPosition, paramVelocity);
      ExecutorService executorService = ThreadPool.newSingleThreadExecutor("imu acceleration");
      this.accelerationMananger = executorService;
      executorService.execute(new AccelerationManager(paramInt));
      return;
    } 
  }
  
  public void stopAccelerationIntegration() {
    synchronized (this.startStopLock) {
      if (this.accelerationMananger != null) {
        this.accelerationMananger.shutdownNow();
        ThreadPool.awaitTerminationOrExitApplication(this.accelerationMananger, 10L, TimeUnit.SECONDS, "IMU acceleration", "unresponsive user acceleration code");
        this.accelerationMananger = null;
      } 
      return;
    } 
  }
  
  protected void waitForWriteCompletions() {
    ((I2cDeviceSynch)this.deviceClient).waitForWriteCompletions(I2cWaitControl.ATOMIC);
  }
  
  public void write(BNO055IMU.Register paramRegister, byte[] paramArrayOfbyte) {
    write(paramRegister, paramArrayOfbyte, I2cWaitControl.ATOMIC);
  }
  
  public void write(BNO055IMU.Register paramRegister, byte[] paramArrayOfbyte, I2cWaitControl paramI2cWaitControl) {
    ((I2cDeviceSynch)this.deviceClient).write(paramRegister.bVal, paramArrayOfbyte, paramI2cWaitControl);
  }
  
  public void write8(BNO055IMU.Register paramRegister, int paramInt) {
    write8(paramRegister, paramInt, I2cWaitControl.ATOMIC);
  }
  
  public void write8(BNO055IMU.Register paramRegister, int paramInt, I2cWaitControl paramI2cWaitControl) {
    ((I2cDeviceSynch)this.deviceClient).write8(paramRegister.bVal, paramInt, paramI2cWaitControl);
  }
  
  public void writeCalibrationData(BNO055IMU.CalibrationData paramCalibrationData) {
    BNO055IMU.SensorMode sensorMode = this.currentMode;
    if (sensorMode != BNO055IMU.SensorMode.CONFIG)
      setSensorMode(BNO055IMU.SensorMode.CONFIG); 
    writeShort(BNO055IMU.Register.ACC_OFFSET_X_LSB, paramCalibrationData.dxAccel);
    writeShort(BNO055IMU.Register.ACC_OFFSET_Y_LSB, paramCalibrationData.dyAccel);
    writeShort(BNO055IMU.Register.ACC_OFFSET_Z_LSB, paramCalibrationData.dzAccel);
    writeShort(BNO055IMU.Register.MAG_OFFSET_X_LSB, paramCalibrationData.dxMag);
    writeShort(BNO055IMU.Register.MAG_OFFSET_Y_LSB, paramCalibrationData.dyMag);
    writeShort(BNO055IMU.Register.MAG_OFFSET_Z_LSB, paramCalibrationData.dzMag);
    writeShort(BNO055IMU.Register.GYR_OFFSET_X_LSB, paramCalibrationData.dxGyro);
    writeShort(BNO055IMU.Register.GYR_OFFSET_Y_LSB, paramCalibrationData.dyGyro);
    writeShort(BNO055IMU.Register.GYR_OFFSET_Z_LSB, paramCalibrationData.dzGyro);
    writeShort(BNO055IMU.Register.ACC_RADIUS_LSB, paramCalibrationData.radiusAccel);
    writeShort(BNO055IMU.Register.MAG_RADIUS_LSB, paramCalibrationData.radiusMag);
    if (sensorMode != BNO055IMU.SensorMode.CONFIG)
      setSensorMode(sensorMode); 
  }
  
  protected void writeShort(BNO055IMU.Register paramRegister, short paramShort) {
    write(paramRegister, TypeConversion.shortToByteArray(paramShort, ByteOrder.LITTLE_ENDIAN));
  }
  
  class AccelerationManager implements Runnable {
    protected static final long nsPerMs = 1000000L;
    
    protected final int msPollInterval;
    
    AccelerationManager(int param1Int) {
      this.msPollInterval = param1Int;
    }
    
    public void run() {
      try {
        while (!BNO055IMUImpl.this.isStopRequested()) {
          null = BNO055IMUImpl.this.getLinearAcceleration();
          synchronized (BNO055IMUImpl.this.dataLock) {
            BNO055IMUImpl.this.accelerationAlgorithm.update(null);
            if (this.msPollInterval > 0) {
              long l = (System.nanoTime() - null.acquisitionTime) / 1000000L;
              Thread.sleep(Math.max(0L, this.msPollInterval - l - 5L));
              continue;
            } 
            Thread.yield();
          } 
        } 
        return;
      } catch (InterruptedException|java.util.concurrent.CancellationException interruptedException) {
        return;
      } 
    }
  }
  
  enum POWER_MODE {
    LOWPOWER,
    NORMAL(0),
    SUSPEND(0);
    
    protected byte value;
    
    static {
      POWER_MODE pOWER_MODE = new POWER_MODE("SUSPEND", 2, 2);
      SUSPEND = pOWER_MODE;
      $VALUES = new POWER_MODE[] { NORMAL, LOWPOWER, pOWER_MODE };
    }
    
    POWER_MODE(int param1Int1) {
      this.value = (byte)param1Int1;
    }
    
    public byte getValue() {
      return this.value;
    }
  }
  
  enum VECTOR {
    ACCELEROMETER((String)BNO055IMU.Register.ACC_DATA_X_LSB),
    EULER((String)BNO055IMU.Register.ACC_DATA_X_LSB),
    GRAVITY((String)BNO055IMU.Register.ACC_DATA_X_LSB),
    GYROSCOPE((String)BNO055IMU.Register.ACC_DATA_X_LSB),
    LINEARACCEL((String)BNO055IMU.Register.ACC_DATA_X_LSB),
    MAGNETOMETER((String)BNO055IMU.Register.MAG_DATA_X_LSB);
    
    protected byte value;
    
    static {
      EULER = new VECTOR("EULER", 3, BNO055IMU.Register.EUL_H_LSB);
      LINEARACCEL = new VECTOR("LINEARACCEL", 4, BNO055IMU.Register.LIA_DATA_X_LSB);
      VECTOR vECTOR = new VECTOR("GRAVITY", 5, BNO055IMU.Register.GRV_DATA_X_LSB);
      GRAVITY = vECTOR;
      $VALUES = new VECTOR[] { ACCELEROMETER, MAGNETOMETER, GYROSCOPE, EULER, LINEARACCEL, vECTOR };
    }
    
    VECTOR(int param1Int1) {
      this.value = (byte)param1Int1;
    }
    
    public byte getValue() {
      return this.value;
    }
  }
  
  protected static class VectorData {
    protected ByteBuffer buffer;
    
    public TimestampedData data;
    
    public float scale;
    
    public VectorData(TimestampedData param1TimestampedData, float param1Float) {
      this.data = param1TimestampedData;
      this.scale = param1Float;
      this.buffer = ByteBuffer.wrap(param1TimestampedData.data).order(ByteOrder.LITTLE_ENDIAN);
    }
    
    public float next() {
      return this.buffer.getShort() / this.scale;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\bosch\BNO055IMUImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */