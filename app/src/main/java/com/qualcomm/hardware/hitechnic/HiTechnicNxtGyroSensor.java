package com.qualcomm.hardware.hitechnic;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.AnalogSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.LegacyModulePortDeviceImpl;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.HashSet;
import java.util.Set;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class HiTechnicNxtGyroSensor extends LegacyModulePortDeviceImpl implements GyroSensor, Gyroscope, AnalogSensor {
  public static final String TAG = "HiTechnicNxtGyroSensor";
  
  protected double biasVoltage = getDefaultBiasVoltage();
  
  protected double degreesPerSecondPerVolt = getDefaultDegreesPerSecondPerVolt();
  
  protected boolean isCalibrating = false;
  
  public HiTechnicNxtGyroSensor(LegacyModule paramLegacyModule, int paramInt) {
    super(paramLegacyModule, paramInt);
    finishConstruction();
  }
  
  public void calibrate() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: sipush #2500
    //   6: bipush #50
    //   8: invokevirtual calibrate : (II)V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  public void calibrate(int paramInt1, int paramInt2) {
    calibrate(paramInt1, paramInt2, 500);
  }
  
  public void calibrate(int paramInt1, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_1
    //   3: iload_3
    //   4: isub
    //   5: iload_2
    //   6: idiv
    //   7: istore #4
    //   9: aload_0
    //   10: iconst_1
    //   11: putfield isCalibrating : Z
    //   14: aload_0
    //   15: iload_3
    //   16: invokevirtual sleep : (I)V
    //   19: new com/qualcomm/robotcore/util/Statistics
    //   22: dup
    //   23: invokespecial <init> : ()V
    //   26: astore #5
    //   28: iconst_0
    //   29: istore_1
    //   30: invokestatic currentThread : ()Ljava/lang/Thread;
    //   33: invokevirtual isInterrupted : ()Z
    //   36: ifne -> 74
    //   39: iload_1
    //   40: iload #4
    //   42: if_icmpge -> 74
    //   45: iload_1
    //   46: ifle -> 58
    //   49: iload_2
    //   50: ifle -> 58
    //   53: aload_0
    //   54: iload_2
    //   55: invokevirtual sleep : (I)V
    //   58: aload #5
    //   60: aload_0
    //   61: invokevirtual readRawVoltage : ()D
    //   64: invokevirtual add : (D)V
    //   67: iload_1
    //   68: iconst_1
    //   69: iadd
    //   70: istore_1
    //   71: goto -> 30
    //   74: aload_0
    //   75: aload #5
    //   77: invokevirtual getMean : ()D
    //   80: invokevirtual setBiasVoltage : (D)V
    //   83: aload_0
    //   84: iconst_0
    //   85: putfield isCalibrating : Z
    //   88: aload_0
    //   89: monitorexit
    //   90: return
    //   91: astore #5
    //   93: aload_0
    //   94: iconst_0
    //   95: putfield isCalibrating : Z
    //   98: aload #5
    //   100: athrow
    //   101: astore #5
    //   103: aload_0
    //   104: monitorexit
    //   105: aload #5
    //   107: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	101	finally
    //   19	28	91	finally
    //   30	39	91	finally
    //   53	58	91	finally
    //   58	67	91	finally
    //   74	83	91	finally
    //   83	88	101	finally
    //   93	101	101	finally
  }
  
  public void close() {}
  
  public AngularVelocity getAngularVelocity(AngleUnit paramAngleUnit) {
    return new AngularVelocity(paramAngleUnit, 0.0F, 0.0F, getAngularZVelocity(paramAngleUnit), System.nanoTime());
  }
  
  public Set<Axis> getAngularVelocityAxes() {
    HashSet<Axis> hashSet = new HashSet();
    hashSet.add(Axis.Z);
    return hashSet;
  }
  
  protected float getAngularZVelocity(AngleUnit paramAngleUnit) {
    double d = -((readRawVoltage() - this.biasVoltage) * this.degreesPerSecondPerVolt);
    return (float)paramAngleUnit.fromUnit(AngleUnit.DEGREES, d);
  }
  
  public double getBiasVoltage() {
    return this.biasVoltage;
  }
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.module.getConnectionInfo());
    stringBuilder.append("; port ");
    stringBuilder.append(this.physicalPort);
    return stringBuilder.toString();
  }
  
  public double getDefaultBiasVoltage() {
    return 2.908D;
  }
  
  public double getDefaultDegreesPerSecondPerVolt() {
    return 1024.0D / getMaxVoltage() / 1.0D;
  }
  
  public double getDegreesPerSecondPerVolt() {
    return this.degreesPerSecondPerVolt;
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeHTGyro);
  }
  
  public int getHeading() {
    notSupported();
    return 0;
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.HiTechnic;
  }
  
  public double getMaxVoltage() {
    return 5.0D;
  }
  
  public double getRotationFraction() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual readRawVoltage : ()D
    //   6: dstore_1
    //   7: aload_0
    //   8: invokevirtual getMaxVoltage : ()D
    //   11: dstore_3
    //   12: dload_1
    //   13: dload_3
    //   14: ddiv
    //   15: dstore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: dload_1
    //   19: dreturn
    //   20: astore #5
    //   22: aload_0
    //   23: monitorexit
    //   24: aload #5
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	20	finally
  }
  
  public int getVersion() {
    return 1;
  }
  
  public boolean isCalibrating() {
    return this.isCalibrating;
  }
  
  protected void moduleNowArmedOrPretending() {
    this.module.enableAnalogReadMode(this.physicalPort);
  }
  
  protected void notSupported() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("This method is not supported for ");
    stringBuilder.append(getDeviceName());
    throw new UnsupportedOperationException(stringBuilder.toString());
  }
  
  public int rawX() {
    notSupported();
    return 0;
  }
  
  public int rawY() {
    notSupported();
    return 0;
  }
  
  public int rawZ() {
    notSupported();
    return 0;
  }
  
  public double readRawVoltage() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield module : Lcom/qualcomm/robotcore/hardware/LegacyModule;
    //   6: aload_0
    //   7: getfield physicalPort : I
    //   10: invokeinterface readAnalogVoltage : (I)D
    //   15: dstore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: dload_1
    //   19: dreturn
    //   20: astore_3
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	20	finally
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public void resetZAxisIntegrator() {}
  
  public void setBiasVoltage(double paramDouble) {
    this.biasVoltage = paramDouble;
    RobotLog.vv("HiTechnicNxtGyroSensor", "biasVoltage=%.3f", new Object[] { Double.valueOf(paramDouble) });
  }
  
  public void setDegreesPerSecondPerVolt(double paramDouble) {
    this.degreesPerSecondPerVolt = paramDouble;
  }
  
  protected void sleep(int paramInt) {
    long l = paramInt;
    try {
      Thread.sleep(l);
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } 
  }
  
  public String status() {
    return String.format("NXT Gyro Sensor, connected via device %s, port %d", new Object[] { this.module.getSerialNumber(), Integer.valueOf(this.physicalPort) });
  }
  
  public String toString() {
    return String.format("Gyro: %3.1f", new Object[] { Float.valueOf(getAngularZVelocity(AngleUnit.DEGREES)) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtGyroSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */