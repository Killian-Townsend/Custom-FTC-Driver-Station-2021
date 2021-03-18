package com.qualcomm.hardware.broadcom;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cAddrConfig;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDeviceWithParameters;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.Light;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;

public abstract class BroadcomColorSensorImpl extends I2cDeviceSynchDeviceWithParameters<I2cDeviceSynchSimple, BroadcomColorSensor.Parameters> implements BroadcomColorSensor, I2cAddrConfig, Light {
  public static final String TAG = "BroadcomColorSensorImpl";
  
  int alpha = 0;
  
  int blue = 0;
  
  NormalizedRGBA colors = new NormalizedRGBA();
  
  int green = 0;
  
  int red = 0;
  
  float softwareGain = 1.0F;
  
  protected BroadcomColorSensorImpl(BroadcomColorSensor.Parameters paramParameters, I2cDeviceSynchSimple paramI2cDeviceSynchSimple, boolean paramBoolean) {
    super(paramI2cDeviceSynchSimple, paramBoolean, paramParameters);
    this.deviceClient.setLogging(((BroadcomColorSensor.Parameters)this.parameters).loggingEnabled);
    this.deviceClient.setLoggingTag(((BroadcomColorSensor.Parameters)this.parameters).loggingTag);
    registerArmingStateCallback(true);
    engage();
  }
  
  private void updateColors() {
    if (testBits(read8(BroadcomColorSensor.Register.MAIN_STATUS), BroadcomColorSensor.MainStatus.LS_DATA_STATUS.bVal)) {
      byte[] arrayOfByte = read(BroadcomColorSensor.Register.LS_DATA_GREEN, 9);
      this.green = TypeConversion.unsignedShortToInt(TypeConversion.byteArrayToShort(arrayOfByte, 0, ByteOrder.LITTLE_ENDIAN));
      this.blue = Range.clip((int)(TypeConversion.unsignedShortToInt(TypeConversion.byteArrayToShort(arrayOfByte, 3, ByteOrder.LITTLE_ENDIAN)) * 1.55D), 0, 65535);
      int i = Range.clip((int)(TypeConversion.unsignedShortToInt(TypeConversion.byteArrayToShort(arrayOfByte, 6, ByteOrder.LITTLE_ENDIAN)) * 1.07D), 0, 65535);
      this.red = i;
      this.alpha = (this.green + i + this.blue) / 3;
      this.colors.red = Range.clip(i * this.softwareGain / ((BroadcomColorSensor.Parameters)this.parameters).colorSaturation, 0.0F, 1.0F);
      this.colors.green = Range.clip(this.green * this.softwareGain / ((BroadcomColorSensor.Parameters)this.parameters).colorSaturation, 0.0F, 1.0F);
      this.colors.blue = Range.clip(this.blue * this.softwareGain / ((BroadcomColorSensor.Parameters)this.parameters).colorSaturation, 0.0F, 1.0F);
      float f = (this.red + this.green + this.blue) / 3.0F;
      this.colors.alpha = (float)(-(65535.0D / (Math.pow(f, 2.0D) + 65535.0D)) + 1.0D);
    } 
  }
  
  public int alpha() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial updateColors : ()V
    //   6: aload_0
    //   7: getfield alpha : I
    //   10: istore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: iload_1
    //   14: ireturn
    //   15: astore_2
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_2
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	15	finally
  }
  
  public int argb() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getNormalizedColors : ()Lcom/qualcomm/robotcore/hardware/NormalizedRGBA;
    //   6: invokevirtual toColor : ()I
    //   9: istore_1
    //   10: aload_0
    //   11: monitorexit
    //   12: iload_1
    //   13: ireturn
    //   14: astore_2
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_2
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	14	finally
  }
  
  public int blue() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial updateColors : ()V
    //   6: aload_0
    //   7: getfield blue : I
    //   10: istore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: iload_1
    //   14: ireturn
    //   15: astore_2
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_2
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	15	finally
  }
  
  protected void delay(int paramInt) {
    long l = paramInt;
    try {
      Thread.sleep(l);
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } 
  }
  
  protected void disable() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual readMainCtrl : ()B
    //   6: istore_1
    //   7: ldc 'BroadcomColorSensorImpl'
    //   9: ldc 'disable() enabled=0x%02x...'
    //   11: iconst_1
    //   12: anewarray java/lang/Object
    //   15: dup
    //   16: iconst_0
    //   17: iload_1
    //   18: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   21: aastore
    //   22: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   25: aload_0
    //   26: getstatic com/qualcomm/hardware/broadcom/BroadcomColorSensor$Register.MAIN_CTRL : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$Register;
    //   29: iload_1
    //   30: getstatic com/qualcomm/hardware/broadcom/BroadcomColorSensor$MainControl.PS_EN : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$MainControl;
    //   33: getfield bVal : B
    //   36: getstatic com/qualcomm/hardware/broadcom/BroadcomColorSensor$MainControl.LS_EN : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$MainControl;
    //   39: getfield bVal : B
    //   42: ior
    //   43: getstatic com/qualcomm/hardware/broadcom/BroadcomColorSensor$MainControl.RGB_MODE : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$MainControl;
    //   46: getfield bVal : B
    //   49: ior
    //   50: iand
    //   51: invokevirtual write8 : (Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$Register;I)V
    //   54: ldc 'BroadcomColorSensorImpl'
    //   56: ldc '...disable() enabled=0x%02x'
    //   58: iconst_1
    //   59: anewarray java/lang/Object
    //   62: dup
    //   63: iconst_0
    //   64: aload_0
    //   65: invokevirtual readMainCtrl : ()B
    //   68: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   71: aastore
    //   72: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   75: aload_0
    //   76: monitorexit
    //   77: return
    //   78: astore_2
    //   79: aload_0
    //   80: monitorexit
    //   81: aload_2
    //   82: athrow
    // Exception table:
    //   from	to	target	type
    //   2	75	78	finally
  }
  
  protected void dumpState() {
    RobotLog.logBytes("BroadcomColorSensorImpl", "state", read(BroadcomColorSensor.Register.MAIN_CTRL, 7), 7);
  }
  
  protected void enable() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc 'BroadcomColorSensorImpl'
    //   4: ldc 'enable() enabled=0x%02x...'
    //   6: iconst_1
    //   7: anewarray java/lang/Object
    //   10: dup
    //   11: iconst_0
    //   12: aload_0
    //   13: invokevirtual readMainCtrl : ()B
    //   16: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   19: aastore
    //   20: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   23: aload_0
    //   24: getstatic com/qualcomm/hardware/broadcom/BroadcomColorSensor$Register.MAIN_CTRL : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$Register;
    //   27: getstatic com/qualcomm/hardware/broadcom/BroadcomColorSensor$MainControl.PS_EN : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$MainControl;
    //   30: getfield bVal : B
    //   33: getstatic com/qualcomm/hardware/broadcom/BroadcomColorSensor$MainControl.LS_EN : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$MainControl;
    //   36: getfield bVal : B
    //   39: ior
    //   40: getstatic com/qualcomm/hardware/broadcom/BroadcomColorSensor$MainControl.RGB_MODE : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$MainControl;
    //   43: getfield bVal : B
    //   46: ior
    //   47: invokevirtual write8 : (Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$Register;I)V
    //   50: ldc 'BroadcomColorSensorImpl'
    //   52: ldc '...enable() enabled=0x%02x'
    //   54: iconst_1
    //   55: anewarray java/lang/Object
    //   58: dup
    //   59: iconst_0
    //   60: aload_0
    //   61: invokevirtual readMainCtrl : ()B
    //   64: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   67: aastore
    //   68: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   71: aload_0
    //   72: monitorexit
    //   73: return
    //   74: astore_1
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_1
    //   78: athrow
    // Exception table:
    //   from	to	target	type
    //   2	71	74	finally
  }
  
  public void enableLed(boolean paramBoolean) {
    /* monitor enter ThisExpression{ObjectType{com/qualcomm/hardware/broadcom/BroadcomColorSensorImpl}} */
    /* monitor exit ThisExpression{ObjectType{com/qualcomm/hardware/broadcom/BroadcomColorSensorImpl}} */
  }
  
  public byte getDeviceID() {
    return read8(BroadcomColorSensor.Register.PART_ID);
  }
  
  public String getDeviceName() {
    return "Broadcom I2C Color Sensor";
  }
  
  public float getGain() {
    return this.softwareGain;
  }
  
  public I2cAddr getI2cAddress() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   6: invokeinterface getI2cAddress : ()Lcom/qualcomm/robotcore/hardware/I2cAddr;
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: areturn
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Broadcom;
  }
  
  public NormalizedRGBA getNormalizedColors() {
    updateColors();
    return this.colors;
  }
  
  public int green() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial updateColors : ()V
    //   6: aload_0
    //   7: getfield green : I
    //   10: istore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: iload_1
    //   14: ireturn
    //   15: astore_2
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_2
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	15	finally
  }
  
  protected boolean internalInitialize(BroadcomColorSensor.Parameters paramParameters) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc 'BroadcomColorSensorImpl'
    //   4: ldc_w 'internalInitialize()...'
    //   7: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   10: aload_0
    //   11: getfield parameters : Ljava/lang/Object;
    //   14: checkcast com/qualcomm/hardware/broadcom/BroadcomColorSensor$Parameters
    //   17: getfield deviceId : I
    //   20: aload_1
    //   21: getfield deviceId : I
    //   24: if_icmpne -> 230
    //   27: aload_0
    //   28: aload_1
    //   29: invokevirtual clone : ()Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$Parameters;
    //   32: putfield parameters : Ljava/lang/Object;
    //   35: aload_0
    //   36: aload_1
    //   37: getfield i2cAddr : Lcom/qualcomm/robotcore/hardware/I2cAddr;
    //   40: invokevirtual setI2cAddress : (Lcom/qualcomm/robotcore/hardware/I2cAddr;)V
    //   43: aload_0
    //   44: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   47: invokeinterface isArmed : ()Z
    //   52: istore_3
    //   53: iload_3
    //   54: ifne -> 69
    //   57: ldc 'BroadcomColorSensorImpl'
    //   59: ldc_w '...internalInitialize()'
    //   62: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   65: aload_0
    //   66: monitorexit
    //   67: iconst_0
    //   68: ireturn
    //   69: aload_0
    //   70: invokevirtual getDeviceID : ()B
    //   73: istore_2
    //   74: iload_2
    //   75: aload_1
    //   76: getfield deviceId : I
    //   79: if_icmpeq -> 123
    //   82: ldc 'BroadcomColorSensorImpl'
    //   84: ldc_w 'unexpected Broadcom color sensor chipid: found=%d expected=%d'
    //   87: iconst_2
    //   88: anewarray java/lang/Object
    //   91: dup
    //   92: iconst_0
    //   93: iload_2
    //   94: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   97: aastore
    //   98: dup
    //   99: iconst_1
    //   100: aload_1
    //   101: getfield deviceId : I
    //   104: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   107: aastore
    //   108: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   111: ldc 'BroadcomColorSensorImpl'
    //   113: ldc_w '...internalInitialize()'
    //   116: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   119: aload_0
    //   120: monitorexit
    //   121: iconst_0
    //   122: ireturn
    //   123: aload_0
    //   124: invokevirtual dumpState : ()V
    //   127: aload_0
    //   128: aload_1
    //   129: getfield gain : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$Gain;
    //   132: invokevirtual setHardwareGain : (Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$Gain;)V
    //   135: aload_0
    //   136: aload_1
    //   137: getfield pulseModulation : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$LEDPulseModulation;
    //   140: aload_1
    //   141: getfield ledCurrent : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$LEDCurrent;
    //   144: invokevirtual setLEDParameters : (Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$LEDPulseModulation;Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$LEDCurrent;)V
    //   147: aload_0
    //   148: aload_1
    //   149: getfield proximityPulseCount : I
    //   152: invokevirtual setProximityPulseCount : (I)V
    //   155: aload_0
    //   156: getstatic com/qualcomm/hardware/broadcom/BroadcomColorSensor$Parameters.proximityResolution : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$PSResolution;
    //   159: aload_1
    //   160: getfield proximityMeasRate : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$PSMeasurementRate;
    //   163: invokevirtual setPSRateAndRes : (Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$PSResolution;Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$PSMeasurementRate;)V
    //   166: aload_0
    //   167: getstatic com/qualcomm/hardware/broadcom/BroadcomColorSensor$Parameters.lightSensorResolution : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$LSResolution;
    //   170: aload_1
    //   171: getfield lightSensorMeasRate : Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$LSMeasurementRate;
    //   174: invokevirtual setLSRateAndRes : (Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$LSResolution;Lcom/qualcomm/hardware/broadcom/BroadcomColorSensor$LSMeasurementRate;)V
    //   177: aload_0
    //   178: invokevirtual enable : ()V
    //   181: aload_0
    //   182: invokevirtual dumpState : ()V
    //   185: aload_0
    //   186: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   189: instanceof com/qualcomm/robotcore/hardware/I2cDeviceSynch
    //   192: ifeq -> 218
    //   195: aload_1
    //   196: getfield readWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
    //   199: ifnull -> 218
    //   202: aload_0
    //   203: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   206: checkcast com/qualcomm/robotcore/hardware/I2cDeviceSynch
    //   209: aload_1
    //   210: getfield readWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
    //   213: invokeinterface setReadWindow : (Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;)V
    //   218: ldc 'BroadcomColorSensorImpl'
    //   220: ldc_w '...internalInitialize()'
    //   223: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   226: aload_0
    //   227: monitorexit
    //   228: iconst_1
    //   229: ireturn
    //   230: new java/lang/IllegalArgumentException
    //   233: dup
    //   234: ldc_w 'can't change device types (modify existing params instead): old=%d new=%d'
    //   237: iconst_2
    //   238: anewarray java/lang/Object
    //   241: dup
    //   242: iconst_0
    //   243: aload_0
    //   244: getfield parameters : Ljava/lang/Object;
    //   247: checkcast com/qualcomm/hardware/broadcom/BroadcomColorSensor$Parameters
    //   250: getfield deviceId : I
    //   253: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   256: aastore
    //   257: dup
    //   258: iconst_1
    //   259: aload_1
    //   260: getfield deviceId : I
    //   263: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   266: aastore
    //   267: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   270: invokespecial <init> : (Ljava/lang/String;)V
    //   273: athrow
    //   274: astore_1
    //   275: ldc 'BroadcomColorSensorImpl'
    //   277: ldc_w '...internalInitialize()'
    //   280: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   283: aload_1
    //   284: athrow
    //   285: astore_1
    //   286: aload_0
    //   287: monitorexit
    //   288: aload_1
    //   289: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	285	finally
    //   10	53	274	finally
    //   57	65	285	finally
    //   69	111	274	finally
    //   111	119	285	finally
    //   123	218	274	finally
    //   218	226	285	finally
    //   230	274	274	finally
    //   275	285	285	finally
  }
  
  public boolean isLightOn() {
    return true;
  }
  
  public byte[] read(BroadcomColorSensor.Register paramRegister, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   6: aload_1
    //   7: getfield bVal : B
    //   10: iload_2
    //   11: invokeinterface read : (II)[B
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
  
  public byte read8(BroadcomColorSensor.Register paramRegister) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   6: aload_1
    //   7: getfield bVal : B
    //   10: invokeinterface read8 : (I)B
    //   15: istore_2
    //   16: aload_0
    //   17: monitorexit
    //   18: iload_2
    //   19: ireturn
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	20	finally
  }
  
  protected byte readMainCtrl() {
    return read8(BroadcomColorSensor.Register.MAIN_CTRL);
  }
  
  protected int readUnsignedByte(BroadcomColorSensor.Register paramRegister) {
    return TypeConversion.unsignedByteToInt(read8(paramRegister));
  }
  
  protected int readUnsignedShort(BroadcomColorSensor.Register paramRegister, ByteOrder paramByteOrder) {
    return TypeConversion.unsignedShortToInt(TypeConversion.byteArrayToShort(read(paramRegister, 2), 0, paramByteOrder));
  }
  
  public int red() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial updateColors : ()V
    //   6: aload_0
    //   7: getfield red : I
    //   10: istore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: iload_1
    //   14: ireturn
    //   15: astore_2
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_2
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	15	finally
  }
  
  public void resetDeviceConfigurationForOpMode() {
    super.resetDeviceConfigurationForOpMode();
    this.softwareGain = 1.0F;
  }
  
  public void setGain(float paramFloat) {
    this.softwareGain = paramFloat;
  }
  
  protected void setHardwareGain(BroadcomColorSensor.Gain paramGain) {
    RobotLog.vv("BroadcomColorSensorImpl", "setGain(0x%02x)", new Object[] { Byte.valueOf(paramGain.bVal) });
    write8(BroadcomColorSensor.Register.LS_GAIN, paramGain.bVal);
  }
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield parameters : Ljava/lang/Object;
    //   6: checkcast com/qualcomm/hardware/broadcom/BroadcomColorSensor$Parameters
    //   9: aload_1
    //   10: putfield i2cAddr : Lcom/qualcomm/robotcore/hardware/I2cAddr;
    //   13: aload_0
    //   14: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   17: aload_1
    //   18: invokeinterface setI2cAddress : (Lcom/qualcomm/robotcore/hardware/I2cAddr;)V
    //   23: aload_0
    //   24: monitorexit
    //   25: return
    //   26: astore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_1
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	26	finally
  }
  
  protected void setLEDParameters(BroadcomColorSensor.LEDPulseModulation paramLEDPulseModulation, BroadcomColorSensor.LEDCurrent paramLEDCurrent) {
    byte b = (byte)(paramLEDPulseModulation.bVal << 4 | paramLEDCurrent.bVal);
    RobotLog.vv("BroadcomColorSensorImpl", "setLEDParameters(0x%02x)", new Object[] { Byte.valueOf(b) });
    write8(BroadcomColorSensor.Register.PS_LED, b);
  }
  
  protected void setLSRateAndRes(BroadcomColorSensor.LSResolution paramLSResolution, BroadcomColorSensor.LSMeasurementRate paramLSMeasurementRate) {
    byte b = (byte)(paramLSResolution.bVal << 4 | paramLSMeasurementRate.bVal);
    RobotLog.vv("BroadcomColorSensorImpl", "setLSMeasRate(0x%02x)", new Object[] { Byte.valueOf(b) });
    write8(BroadcomColorSensor.Register.LS_MEAS_RATE, b);
  }
  
  protected void setPDrive(BroadcomColorSensor.LEDCurrent paramLEDCurrent) {
    RobotLog.vv("BroadcomColorSensorImpl", "setPDrive(0x%02x)", new Object[] { Byte.valueOf(paramLEDCurrent.bVal) });
    write8(BroadcomColorSensor.Register.PS_LED, paramLEDCurrent.bVal);
  }
  
  protected void setPSRateAndRes(BroadcomColorSensor.PSResolution paramPSResolution, BroadcomColorSensor.PSMeasurementRate paramPSMeasurementRate) {
    byte b = (byte)(paramPSResolution.bVal << 3 | paramPSMeasurementRate.bVal);
    RobotLog.vv("BroadcomColorSensorImpl", "setPSMeasRate(0x%02x)", new Object[] { Byte.valueOf(b) });
    write8(BroadcomColorSensor.Register.PS_MEAS_RATE, b);
  }
  
  protected void setProximityPulseCount(int paramInt) {
    RobotLog.vv("BroadcomColorSensorImpl", "setProximityPulseCount(0x%02x)", new Object[] { Integer.valueOf(paramInt) });
    write8(BroadcomColorSensor.Register.PS_PULSES, paramInt);
  }
  
  protected boolean testBits(byte paramByte1, byte paramByte2) {
    return testBits(paramByte1, paramByte2, paramByte2);
  }
  
  protected boolean testBits(byte paramByte1, byte paramByte2, byte paramByte3) {
    return ((paramByte1 & paramByte2) == paramByte3);
  }
  
  public void write(BroadcomColorSensor.Register paramRegister, byte[] paramArrayOfbyte) {
    this.deviceClient.write(paramRegister.bVal, paramArrayOfbyte, I2cWaitControl.WRITTEN);
  }
  
  public void write8(BroadcomColorSensor.Register paramRegister, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   6: aload_1
    //   7: getfield bVal : B
    //   10: iload_2
    //   11: getstatic com/qualcomm/robotcore/hardware/I2cWaitControl.WRITTEN : Lcom/qualcomm/robotcore/hardware/I2cWaitControl;
    //   14: invokeinterface write8 : (IILcom/qualcomm/robotcore/hardware/I2cWaitControl;)V
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\broadcom\BroadcomColorSensorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */