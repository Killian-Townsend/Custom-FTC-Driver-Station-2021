package com.qualcomm.hardware.ams;

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
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

public abstract class AMSColorSensorImpl extends I2cDeviceSynchDeviceWithParameters<I2cDeviceSynchSimple, AMSColorSensor.Parameters> implements AMSColorSensor, I2cAddrConfig, Light {
  public static final String TAG = "AMSColorSensorImpl";
  
  private float softwareGain = 1.0F;
  
  protected AMSColorSensorImpl(AMSColorSensor.Parameters paramParameters, I2cDeviceSynchSimple paramI2cDeviceSynchSimple, boolean paramBoolean) {
    super(paramI2cDeviceSynchSimple, paramBoolean, paramParameters);
    this.deviceClient.setLogging(((AMSColorSensor.Parameters)this.parameters).loggingEnabled);
    this.deviceClient.setLoggingTag(((AMSColorSensor.Parameters)this.parameters).loggingTag);
    registerArmingStateCallback(true);
    engage();
  }
  
  public int alpha() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: invokevirtual getNormalizedColors : ()Lcom/qualcomm/robotcore/hardware/NormalizedRGBA;
    //   7: getfield alpha : F
    //   10: invokevirtual normalToUnsignedShort : (F)I
    //   13: istore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: iload_1
    //   17: ireturn
    //   18: astore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_2
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	18	finally
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
    //   3: aload_0
    //   4: invokevirtual getNormalizedColors : ()Lcom/qualcomm/robotcore/hardware/NormalizedRGBA;
    //   7: getfield blue : F
    //   10: invokevirtual normalToUnsignedShort : (F)I
    //   13: istore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: iload_1
    //   17: ireturn
    //   18: astore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_2
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	18	finally
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
    //   3: invokevirtual readEnable : ()B
    //   6: istore_1
    //   7: ldc 'AMSColorSensorImpl'
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
    //   26: iload_1
    //   27: getstatic com/qualcomm/hardware/ams/AMSColorSensor$Enable.PON : Lcom/qualcomm/hardware/ams/AMSColorSensor$Enable;
    //   30: getfield bVal : B
    //   33: getstatic com/qualcomm/hardware/ams/AMSColorSensor$Enable.AEN : Lcom/qualcomm/hardware/ams/AMSColorSensor$Enable;
    //   36: getfield bVal : B
    //   39: ior
    //   40: getstatic com/qualcomm/hardware/ams/AMSColorSensor$Enable.PEN : Lcom/qualcomm/hardware/ams/AMSColorSensor$Enable;
    //   43: getfield bVal : B
    //   46: ior
    //   47: iand
    //   48: invokevirtual writeEnable : (I)V
    //   51: ldc 'AMSColorSensorImpl'
    //   53: ldc '...disable() enabled=0x%02x'
    //   55: iconst_1
    //   56: anewarray java/lang/Object
    //   59: dup
    //   60: iconst_0
    //   61: aload_0
    //   62: invokevirtual readEnableAfterWrite : ()B
    //   65: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   68: aastore
    //   69: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   72: aload_0
    //   73: monitorexit
    //   74: return
    //   75: astore_2
    //   76: aload_0
    //   77: monitorexit
    //   78: aload_2
    //   79: athrow
    // Exception table:
    //   from	to	target	type
    //   2	72	75	finally
  }
  
  protected void dumpState() {
    RobotLog.logBytes("AMSColorSensorImpl", "state", read(AMSColorSensor.Register.ENABLE, 25), 25);
  }
  
  protected void enable() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual readEnable : ()B
    //   6: istore_1
    //   7: ldc 'AMSColorSensorImpl'
    //   9: ldc 'enable() enabled=0x%02x...'
    //   11: iconst_1
    //   12: anewarray java/lang/Object
    //   15: dup
    //   16: iconst_0
    //   17: iload_1
    //   18: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   21: aastore
    //   22: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   25: aload_0
    //   26: iload_1
    //   27: getstatic com/qualcomm/hardware/ams/AMSColorSensor$Enable.PON : Lcom/qualcomm/hardware/ams/AMSColorSensor$Enable;
    //   30: invokevirtual testBits : (BLcom/qualcomm/hardware/ams/AMSColorSensor$Enable;)Z
    //   33: iconst_1
    //   34: ixor
    //   35: istore #4
    //   37: iload #4
    //   39: ifne -> 211
    //   42: aload_0
    //   43: iload_1
    //   44: getstatic com/qualcomm/hardware/ams/AMSColorSensor$Enable.AEN : Lcom/qualcomm/hardware/ams/AMSColorSensor$Enable;
    //   47: invokevirtual testBits : (BLcom/qualcomm/hardware/ams/AMSColorSensor$Enable;)Z
    //   50: ifne -> 206
    //   53: goto -> 211
    //   56: aload_0
    //   57: invokevirtual is3782 : ()Z
    //   60: ifeq -> 216
    //   63: aload_0
    //   64: getfield parameters : Ljava/lang/Object;
    //   67: checkcast com/qualcomm/hardware/ams/AMSColorSensor$Parameters
    //   70: getfield useProximityIfAvailable : Z
    //   73: ifeq -> 216
    //   76: iconst_1
    //   77: istore_3
    //   78: goto -> 81
    //   81: iload_3
    //   82: ifeq -> 221
    //   85: aload_0
    //   86: iload_1
    //   87: getstatic com/qualcomm/hardware/ams/AMSColorSensor$Enable.PEN : Lcom/qualcomm/hardware/ams/AMSColorSensor$Enable;
    //   90: invokevirtual testBits : (BLcom/qualcomm/hardware/ams/AMSColorSensor$Enable;)Z
    //   93: ifne -> 221
    //   96: iconst_1
    //   97: istore_3
    //   98: goto -> 101
    //   101: iload #4
    //   103: ifeq -> 116
    //   106: aload_0
    //   107: getstatic com/qualcomm/hardware/ams/AMSColorSensor$Enable.PON : Lcom/qualcomm/hardware/ams/AMSColorSensor$Enable;
    //   110: getfield bVal : B
    //   113: invokevirtual writeEnable : (I)V
    //   116: aload_0
    //   117: iconst_3
    //   118: invokevirtual delay : (I)V
    //   121: iload_2
    //   122: ifne -> 129
    //   125: iload_3
    //   126: ifeq -> 175
    //   129: getstatic com/qualcomm/hardware/ams/AMSColorSensor$Enable.PON : Lcom/qualcomm/hardware/ams/AMSColorSensor$Enable;
    //   132: getfield bVal : B
    //   135: istore #4
    //   137: iload_2
    //   138: ifeq -> 226
    //   141: getstatic com/qualcomm/hardware/ams/AMSColorSensor$Enable.AEN : Lcom/qualcomm/hardware/ams/AMSColorSensor$Enable;
    //   144: getfield bVal : B
    //   147: istore_2
    //   148: goto -> 151
    //   151: iload_3
    //   152: ifeq -> 231
    //   155: getstatic com/qualcomm/hardware/ams/AMSColorSensor$Enable.PEN : Lcom/qualcomm/hardware/ams/AMSColorSensor$Enable;
    //   158: getfield bVal : B
    //   161: istore_3
    //   162: goto -> 165
    //   165: aload_0
    //   166: iload_3
    //   167: iload #4
    //   169: iload_2
    //   170: ior
    //   171: ior
    //   172: invokevirtual writeEnable : (I)V
    //   175: ldc 'AMSColorSensorImpl'
    //   177: ldc '...enable() enabled=0x%02x'
    //   179: iconst_1
    //   180: anewarray java/lang/Object
    //   183: dup
    //   184: iconst_0
    //   185: aload_0
    //   186: invokevirtual readEnableAfterWrite : ()B
    //   189: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   192: aastore
    //   193: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   196: aload_0
    //   197: monitorexit
    //   198: return
    //   199: astore #5
    //   201: aload_0
    //   202: monitorexit
    //   203: aload #5
    //   205: athrow
    //   206: iconst_0
    //   207: istore_2
    //   208: goto -> 56
    //   211: iconst_1
    //   212: istore_2
    //   213: goto -> 56
    //   216: iconst_0
    //   217: istore_3
    //   218: goto -> 81
    //   221: iconst_0
    //   222: istore_3
    //   223: goto -> 101
    //   226: iconst_0
    //   227: istore_2
    //   228: goto -> 151
    //   231: iconst_0
    //   232: istore_3
    //   233: goto -> 165
    // Exception table:
    //   from	to	target	type
    //   2	37	199	finally
    //   42	53	199	finally
    //   56	76	199	finally
    //   85	96	199	finally
    //   106	116	199	finally
    //   116	121	199	finally
    //   129	137	199	finally
    //   141	148	199	finally
    //   155	162	199	finally
    //   165	175	199	finally
    //   175	196	199	finally
  }
  
  public void enableLed(boolean paramBoolean) {
    /* monitor enter ThisExpression{ObjectType{com/qualcomm/hardware/ams/AMSColorSensorImpl}} */
    /* monitor exit ThisExpression{ObjectType{com/qualcomm/hardware/ams/AMSColorSensorImpl}} */
  }
  
  public byte getDeviceID() {
    return read8(AMSColorSensor.Register.DEVICE_ID);
  }
  
  public String getDeviceName() {
    return "AMS I2C Color Sensor";
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
    return HardwareDevice.Manufacturer.AMS;
  }
  
  public NormalizedRGBA getNormalizedColors() {
    Deadline deadline = new Deadline(2L, TimeUnit.SECONDS);
    while (true) {
      NormalizedRGBA normalizedRGBA;
      byte b1 = AMSColorSensor.Register.PDATA.bVal;
      byte b2 = AMSColorSensor.Register.STATUS.bVal;
      byte[] arrayOfByte = read(AMSColorSensor.Register.STATUS, b1 - b2);
      if (testBits(arrayOfByte[0], AMSColorSensor.Status.AVALID.bVal)) {
        int i = TypeConversion.unsignedShortToInt(TypeConversion.byteArrayToShort(arrayOfByte, 1, ByteOrder.LITTLE_ENDIAN));
        int j = TypeConversion.unsignedShortToInt(TypeConversion.byteArrayToShort(arrayOfByte, 3, ByteOrder.LITTLE_ENDIAN));
        int k = TypeConversion.unsignedShortToInt(TypeConversion.byteArrayToShort(arrayOfByte, 5, ByteOrder.LITTLE_ENDIAN));
        int m = TypeConversion.unsignedShortToInt(TypeConversion.byteArrayToShort(arrayOfByte, 7, ByteOrder.LITTLE_ENDIAN));
        float f = 1.0F / ((AMSColorSensor.Parameters)this.parameters).getMaximumReading();
        normalizedRGBA = new NormalizedRGBA();
        normalizedRGBA.alpha = Range.clip(i * this.softwareGain * f, 0.0F, 1.0F);
        normalizedRGBA.red = Range.clip(j * this.softwareGain * f, 0.0F, 1.0F);
        normalizedRGBA.green = Range.clip(k * this.softwareGain * f, 0.0F, 1.0F);
        normalizedRGBA.blue = Range.clip(m * this.softwareGain * f, 0.0F, 1.0F);
        return normalizedRGBA;
      } 
      if (Thread.currentThread().isInterrupted() || !isConnectedAndEnabled() || normalizedRGBA.hasExpired())
        break; 
      delay(3);
    } 
    return new NormalizedRGBA();
  }
  
  public int green() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: invokevirtual getNormalizedColors : ()Lcom/qualcomm/robotcore/hardware/NormalizedRGBA;
    //   7: getfield green : F
    //   10: invokevirtual normalToUnsignedShort : (F)I
    //   13: istore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: iload_1
    //   17: ireturn
    //   18: astore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_2
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	18	finally
  }
  
  protected boolean internalInitialize(AMSColorSensor.Parameters paramParameters) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc 'AMSColorSensorImpl'
    //   4: ldc_w 'internalInitialize()...'
    //   7: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   10: aload_0
    //   11: getfield parameters : Ljava/lang/Object;
    //   14: checkcast com/qualcomm/hardware/ams/AMSColorSensor$Parameters
    //   17: getfield deviceId : I
    //   20: aload_1
    //   21: getfield deviceId : I
    //   24: if_icmpne -> 230
    //   27: aload_0
    //   28: aload_1
    //   29: invokevirtual clone : ()Lcom/qualcomm/hardware/ams/AMSColorSensor$Parameters;
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
    //   57: ldc 'AMSColorSensorImpl'
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
    //   82: ldc 'AMSColorSensorImpl'
    //   84: ldc_w 'unexpected AMS color sensor chipid: found=%d expected=%d'
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
    //   111: ldc 'AMSColorSensorImpl'
    //   113: ldc_w '...internalInitialize()'
    //   116: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   119: aload_0
    //   120: monitorexit
    //   121: iconst_0
    //   122: ireturn
    //   123: aload_0
    //   124: invokevirtual dumpState : ()V
    //   127: aload_0
    //   128: invokevirtual disable : ()V
    //   131: aload_0
    //   132: aload_1
    //   133: getfield atime : I
    //   136: invokevirtual setIntegrationTime : (I)V
    //   139: aload_0
    //   140: aload_1
    //   141: getfield gain : Lcom/qualcomm/hardware/ams/AMSColorSensor$Gain;
    //   144: invokevirtual setHardwareGain : (Lcom/qualcomm/hardware/ams/AMSColorSensor$Gain;)V
    //   147: aload_0
    //   148: aload_1
    //   149: getfield ledDrive : Lcom/qualcomm/hardware/ams/AMSColorSensor$LEDDrive;
    //   152: invokevirtual setPDrive : (Lcom/qualcomm/hardware/ams/AMSColorSensor$LEDDrive;)V
    //   155: aload_0
    //   156: invokevirtual is3782 : ()Z
    //   159: ifeq -> 177
    //   162: aload_1
    //   163: getfield useProximityIfAvailable : Z
    //   166: ifeq -> 177
    //   169: aload_0
    //   170: aload_1
    //   171: getfield proximityPulseCount : I
    //   174: invokevirtual setProximityPulseCount : (I)V
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
    //   218: ldc 'AMSColorSensorImpl'
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
    //   247: checkcast com/qualcomm/hardware/ams/AMSColorSensor$Parameters
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
    //   275: ldc 'AMSColorSensorImpl'
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
    //   123	177	274	finally
    //   177	218	274	finally
    //   218	226	285	finally
    //   230	274	274	finally
    //   275	285	285	finally
  }
  
  protected boolean is3782() {
    return (((AMSColorSensor.Parameters)this.parameters).deviceId == 96 || ((AMSColorSensor.Parameters)this.parameters).deviceId == 105);
  }
  
  protected boolean isConnectedAndEnabled() {
    return testBits(readEnable(), AMSColorSensor.Enable.PON);
  }
  
  public boolean isLightOn() {
    return true;
  }
  
  protected int normalToUnsignedShort(float paramFloat) {
    return (int)(paramFloat * ((AMSColorSensor.Parameters)this.parameters).getMaximumReading());
  }
  
  public byte[] read(AMSColorSensor.Register paramRegister, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   6: aload_1
    //   7: getfield bVal : B
    //   10: sipush #128
    //   13: ior
    //   14: iload_2
    //   15: invokeinterface read : (II)[B
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: areturn
    //   25: astore_1
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_1
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	25	finally
  }
  
  public byte read8(AMSColorSensor.Register paramRegister) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   6: aload_1
    //   7: getfield bVal : B
    //   10: sipush #128
    //   13: ior
    //   14: invokeinterface read8 : (I)B
    //   19: istore_2
    //   20: aload_0
    //   21: monitorexit
    //   22: iload_2
    //   23: ireturn
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	24	finally
  }
  
  protected byte readEnable() {
    return read8(AMSColorSensor.Register.ENABLE);
  }
  
  protected byte readEnableAfterWrite() {
    delay(5);
    return readEnable();
  }
  
  protected int readUnsignedByte(AMSColorSensor.Register paramRegister) {
    return TypeConversion.unsignedByteToInt(read8(paramRegister));
  }
  
  protected int readUnsignedShort(AMSColorSensor.Register paramRegister, ByteOrder paramByteOrder) {
    return TypeConversion.unsignedShortToInt(TypeConversion.byteArrayToShort(read(paramRegister, 2), 0, paramByteOrder));
  }
  
  public int red() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: invokevirtual getNormalizedColors : ()Lcom/qualcomm/robotcore/hardware/NormalizedRGBA;
    //   7: getfield red : F
    //   10: invokevirtual normalToUnsignedShort : (F)I
    //   13: istore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: iload_1
    //   17: ireturn
    //   18: astore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_2
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	18	finally
  }
  
  public void resetDeviceConfigurationForOpMode() {
    super.resetDeviceConfigurationForOpMode();
    this.softwareGain = 1.0F;
  }
  
  public void setGain(float paramFloat) {
    this.softwareGain = paramFloat;
  }
  
  protected void setHardwareGain(AMSColorSensor.Gain paramGain) {
    RobotLog.vv("AMSColorSensorImpl", "setGain(%s)", new Object[] { paramGain });
    updateControl(AMSColorSensor.Gain.MASK.bVal, paramGain.bVal);
  }
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield parameters : Ljava/lang/Object;
    //   6: checkcast com/qualcomm/hardware/ams/AMSColorSensor$Parameters
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
  
  protected void setIntegrationTime(int paramInt) {
    RobotLog.vv("AMSColorSensorImpl", "setIntegrationTime(0x%02x)", new Object[] { Integer.valueOf(paramInt) });
    write8(AMSColorSensor.Register.ATIME, paramInt);
  }
  
  protected void setPDrive(AMSColorSensor.LEDDrive paramLEDDrive) {
    RobotLog.vv("AMSColorSensorImpl", "setPDrive(%s)", new Object[] { paramLEDDrive });
    updateControl(AMSColorSensor.LEDDrive.MASK.bVal, paramLEDDrive.bVal);
  }
  
  protected void setProximityPulseCount(int paramInt) {
    RobotLog.vv("AMSColorSensorImpl", "setProximityPulseCount(0x%02x)", new Object[] { Integer.valueOf(paramInt) });
    write8(AMSColorSensor.Register.PPLUSE, paramInt);
  }
  
  protected boolean testBits(byte paramByte1, byte paramByte2) {
    return testBits(paramByte1, paramByte2, paramByte2);
  }
  
  protected boolean testBits(byte paramByte1, byte paramByte2, byte paramByte3) {
    return ((paramByte1 & paramByte2) == paramByte3);
  }
  
  protected boolean testBits(byte paramByte, AMSColorSensor.Enable paramEnable) {
    return testBits(paramByte, paramEnable, paramEnable);
  }
  
  protected boolean testBits(byte paramByte, AMSColorSensor.Enable paramEnable1, AMSColorSensor.Enable paramEnable2) {
    return testBits(paramByte, paramEnable1.bVal, paramEnable2.bVal);
  }
  
  protected void updateControl(int paramInt1, int paramInt2) {
    byte b = read8(AMSColorSensor.Register.CONTROL);
    int i = b;
    if (is3782())
      i = b | 0x20; 
    write8(AMSColorSensor.Register.CONTROL, paramInt1 & paramInt2 | i & paramInt1);
  }
  
  public void write(AMSColorSensor.Register paramRegister, byte[] paramArrayOfbyte) {
    this.deviceClient.write(paramRegister.bVal | 0x80 | 0x20, paramArrayOfbyte, I2cWaitControl.WRITTEN);
  }
  
  public void write8(AMSColorSensor.Register paramRegister, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   6: aload_1
    //   7: getfield bVal : B
    //   10: sipush #128
    //   13: ior
    //   14: iload_2
    //   15: getstatic com/qualcomm/robotcore/hardware/I2cWaitControl.WRITTEN : Lcom/qualcomm/robotcore/hardware/I2cWaitControl;
    //   18: invokeinterface write8 : (IILcom/qualcomm/robotcore/hardware/I2cWaitControl;)V
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
  
  protected void writeEnable(int paramInt) {
    byte b;
    if (is3782()) {
      b = AMSColorSensor.Enable.PIEN.bVal;
    } else {
      b = 0;
    } 
    byte b1 = AMSColorSensor.Enable.RES7.bVal;
    byte b2 = AMSColorSensor.Enable.RES6.bVal;
    byte b3 = AMSColorSensor.Enable.PIEN.bVal;
    byte b4 = AMSColorSensor.Enable.AIEN.bVal;
    write8(AMSColorSensor.Register.ENABLE, paramInt & (b | b1 | b2 | b3 | b4));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\ams\AMSColorSensorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */