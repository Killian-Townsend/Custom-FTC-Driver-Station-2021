package org.firstinspires.ftc.robotcore.internal.hardware.android;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.util.LastKnown;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class GpioPin implements DigitalChannel {
  protected String TAG;
  
  protected final Active active;
  
  protected DigitalChannel.Mode defaultMode;
  
  protected boolean defaultStateIfOutput;
  
  protected LastKnown<DigitalChannel.Mode> lastKnownMode;
  
  protected final File path;
  
  protected final int rawGpioNumber;
  
  private GpioPin(int paramInt, DigitalChannel.Mode paramMode, boolean paramBoolean, Active paramActive, String paramString) {
    this.rawGpioNumber = paramInt;
    this.path = new File(String.format(Locale.US, "/sys/class/gpio/gpio%d", new Object[] { Integer.valueOf(paramInt) }));
    this.active = paramActive;
    LastKnown<DigitalChannel.Mode> lastKnown = new LastKnown();
    this.lastKnownMode = lastKnown;
    lastKnown.invalidate();
    this.defaultMode = paramMode;
    this.defaultStateIfOutput = paramBoolean;
    this.TAG = paramString;
  }
  
  public GpioPin(int paramInt, String paramString) {
    this(paramInt, DigitalChannel.Mode.INPUT, false, Active.HIGH, paramString);
  }
  
  public GpioPin(int paramInt, boolean paramBoolean, Active paramActive, String paramString) {
    this(paramInt, DigitalChannel.Mode.OUTPUT, paramBoolean, paramActive, paramString);
  }
  
  protected boolean adjustActive(boolean paramBoolean) {
    return (this.active == Active.HIGH) ? paramBoolean : (!paramBoolean);
  }
  
  public void close() {}
  
  public String getConnectionInfo() {
    return String.format("GPIO #", new Object[] { Integer.valueOf(this.rawGpioNumber) });
  }
  
  public String getDeviceName() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("DB GPIO Pin ");
    stringBuilder.append(this.TAG);
    return stringBuilder.toString();
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Other;
  }
  
  public DigitalChannel.Mode getMode() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield lastKnownMode : Lcom/qualcomm/robotcore/util/LastKnown;
    //   6: invokevirtual getValue : ()Ljava/lang/Object;
    //   9: checkcast com/qualcomm/robotcore/hardware/DigitalChannel$Mode
    //   12: astore_2
    //   13: aload_2
    //   14: astore_1
    //   15: aload_2
    //   16: ifnonnull -> 33
    //   19: aload_0
    //   20: invokevirtual getRawMode : ()Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   23: astore_1
    //   24: aload_0
    //   25: getfield lastKnownMode : Lcom/qualcomm/robotcore/util/LastKnown;
    //   28: aload_1
    //   29: invokevirtual setValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   32: pop
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_1
    //   36: areturn
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	37	finally
    //   19	33	37	finally
  }
  
  protected File getPath() {
    return this.path;
  }
  
  protected DigitalChannel.Mode getRawMode() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: ldc 'direction'
    //   5: invokevirtual readAspect : (Ljava/lang/String;)Ljava/lang/String;
    //   8: astore_3
    //   9: iconst_m1
    //   10: istore_1
    //   11: aload_3
    //   12: invokevirtual hashCode : ()I
    //   15: istore_2
    //   16: iload_2
    //   17: sipush #3365
    //   20: if_icmpeq -> 46
    //   23: iload_2
    //   24: ldc 110414
    //   26: if_icmpeq -> 32
    //   29: goto -> 57
    //   32: aload_3
    //   33: ldc 'out'
    //   35: invokevirtual equals : (Ljava/lang/Object;)Z
    //   38: ifeq -> 57
    //   41: iconst_0
    //   42: istore_1
    //   43: goto -> 57
    //   46: aload_3
    //   47: ldc 'in'
    //   49: invokevirtual equals : (Ljava/lang/Object;)Z
    //   52: ifeq -> 57
    //   55: iconst_1
    //   56: istore_1
    //   57: iload_1
    //   58: ifeq -> 69
    //   61: getstatic com/qualcomm/robotcore/hardware/DigitalChannel$Mode.INPUT : Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   64: astore_3
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_3
    //   68: areturn
    //   69: getstatic com/qualcomm/robotcore/hardware/DigitalChannel$Mode.OUTPUT : Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   72: astore_3
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_3
    //   76: areturn
    //   77: astore_3
    //   78: goto -> 89
    //   81: getstatic com/qualcomm/robotcore/hardware/DigitalChannel$Mode.INPUT : Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   84: astore_3
    //   85: aload_0
    //   86: monitorexit
    //   87: aload_3
    //   88: areturn
    //   89: aload_0
    //   90: monitorexit
    //   91: aload_3
    //   92: athrow
    //   93: astore_3
    //   94: goto -> 81
    // Exception table:
    //   from	to	target	type
    //   2	9	93	java/io/IOException
    //   2	9	77	finally
    //   11	16	93	java/io/IOException
    //   11	16	77	finally
    //   32	41	93	java/io/IOException
    //   32	41	77	finally
    //   46	55	93	java/io/IOException
    //   46	55	77	finally
    //   61	65	93	java/io/IOException
    //   61	65	77	finally
    //   69	73	93	java/io/IOException
    //   69	73	77	finally
    //   81	85	77	finally
  }
  
  protected boolean getRawState() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_2
    //   4: aload_0
    //   5: ldc 'value'
    //   7: invokevirtual readAspect : (Ljava/lang/String;)Ljava/lang/String;
    //   10: invokestatic parseInt : (Ljava/lang/String;)I
    //   13: istore_1
    //   14: iload_1
    //   15: ifeq -> 20
    //   18: iconst_1
    //   19: istore_2
    //   20: aload_0
    //   21: monitorexit
    //   22: iload_2
    //   23: ireturn
    //   24: astore_3
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_3
    //   28: athrow
    //   29: aload_0
    //   30: monitorexit
    //   31: iconst_0
    //   32: ireturn
    //   33: astore_3
    //   34: goto -> 29
    // Exception table:
    //   from	to	target	type
    //   4	14	33	java/io/IOException
    //   4	14	24	finally
  }
  
  public boolean getState() {
    return adjustActive(getRawState());
  }
  
  public int getVersion() {
    return 1;
  }
  
  protected String readAspect(String paramString) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(getPath(), paramString)));
    try {
      return bufferedReader.readLine();
    } finally {
      Exception exception = null;
    } 
  }
  
  public void resetDeviceConfigurationForOpMode() {
    /* monitor enter ThisExpression{ObjectType{org/firstinspires/ftc/robotcore/internal/hardware/android/GpioPin}} */
    /* monitor exit ThisExpression{ObjectType{org/firstinspires/ftc/robotcore/internal/hardware/android/GpioPin}} */
  }
  
  public void setDefaultState() {
    setMode(this.defaultMode);
    if (this.defaultMode == DigitalChannel.Mode.OUTPUT)
      setState(this.defaultStateIfOutput); 
  }
  
  public void setMode(DigitalChannel.Mode paramMode) {
    /* monitor enter ThisExpression{ObjectType{org/firstinspires/ftc/robotcore/internal/hardware/android/GpioPin}} */
    try {
      String str;
      if (paramMode == DigitalChannel.Mode.INPUT) {
        str = "in";
      } else {
        str = "out";
      } 
      writeAspect("direction", str);
      this.lastKnownMode.setValue(paramMode);
    } catch (IOException iOException) {
      RobotLog.logExceptionHeader(this.TAG, iOException, "exception in setMode(); ignored", new Object[0]);
    } finally {}
    /* monitor exit ThisExpression{ObjectType{org/firstinspires/ftc/robotcore/internal/hardware/android/GpioPin}} */
  }
  
  @Deprecated
  public void setMode(DigitalChannelController.Mode paramMode) {
    setMode(paramMode.migrate());
  }
  
  public void setState(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getMode : ()Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   6: astore_2
    //   7: getstatic com/qualcomm/robotcore/hardware/DigitalChannel$Mode.OUTPUT : Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   10: astore_3
    //   11: aload_2
    //   12: aload_3
    //   13: if_acmpne -> 37
    //   16: aload_0
    //   17: iload_1
    //   18: invokevirtual adjustActive : (Z)Z
    //   21: ifeq -> 49
    //   24: ldc '1'
    //   26: astore_2
    //   27: goto -> 30
    //   30: aload_0
    //   31: ldc 'value'
    //   33: aload_2
    //   34: invokevirtual writeAspect : (Ljava/lang/String;Ljava/lang/String;)V
    //   37: aload_0
    //   38: monitorexit
    //   39: return
    //   40: astore_2
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_2
    //   44: athrow
    //   45: astore_2
    //   46: goto -> 37
    //   49: ldc '0'
    //   51: astore_2
    //   52: goto -> 30
    // Exception table:
    //   from	to	target	type
    //   2	11	40	finally
    //   16	24	45	java/io/IOException
    //   16	24	40	finally
    //   30	37	45	java/io/IOException
    //   30	37	40	finally
  }
  
  protected void writeAspect(String paramString1, String paramString2) throws IOException {
    File file = new File(getPath(), paramString1);
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
    try {
      RobotLog.vv(this.TAG, "writing aspect=%s value=%s", new Object[] { file.getAbsolutePath(), paramString2 });
      bufferedWriter.write(paramString2);
      return;
    } finally {
      paramString2 = null;
    } 
  }
  
  public enum Active {
    HIGH, LOW;
    
    static {
      Active active = new Active("HIGH", 1);
      HIGH = active;
      $VALUES = new Active[] { LOW, active };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\hardware\android\GpioPin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */