package org.firstinspires.ftc.robotcore.internal.hardware.android;

import com.qualcomm.robotcore.hardware.SwitchableLight;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DragonboardIndicatorLED implements SwitchableLight {
  public static final int LED_FIRST = 1;
  
  public static final int LED_LAST = 4;
  
  public static final String TAG = "DBLED";
  
  protected static DragonboardIndicatorLED[] instances;
  
  protected static String[] ledNames = new String[] { "dummy", "led1", "led2", "led3", "boot" };
  
  protected String ledName;
  
  static {
    instances = new DragonboardIndicatorLED[5];
    for (int i = 1; i <= 4; i++)
      instances[i] = new DragonboardIndicatorLED(i); 
  }
  
  protected DragonboardIndicatorLED(int paramInt) {
    this.ledName = ledNames[paramInt];
  }
  
  public static DragonboardIndicatorLED forIndex(int paramInt) {
    if (paramInt >= 1 && paramInt <= 4)
      return instances[paramInt]; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("illegal LED index: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void enableLight(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: ldc 'trigger'
    //   5: ldc 'none'
    //   7: invokevirtual writeAspect : (Ljava/lang/String;Ljava/lang/String;)V
    //   10: iload_1
    //   11: ifeq -> 42
    //   14: ldc '1'
    //   16: astore_2
    //   17: goto -> 20
    //   20: aload_0
    //   21: ldc 'brightness'
    //   23: aload_2
    //   24: invokevirtual writeAspect : (Ljava/lang/String;Ljava/lang/String;)V
    //   27: goto -> 35
    //   30: astore_2
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_2
    //   34: athrow
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore_2
    //   39: goto -> 35
    //   42: ldc '0'
    //   44: astore_2
    //   45: goto -> 20
    // Exception table:
    //   from	to	target	type
    //   2	10	38	java/io/IOException
    //   2	10	30	finally
    //   20	27	38	java/io/IOException
    //   20	27	30	finally
  }
  
  protected File getLEDPath() {
    return new File(getLEDsPath(), this.ledName);
  }
  
  protected File getLEDsPath() {
    return new File("/sys/class/leds");
  }
  
  public boolean isLightOn() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_2
    //   4: aload_0
    //   5: ldc 'brightness'
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
  
  protected String readAspect(String paramString) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(getLEDPath(), paramString)));
    try {
      return bufferedReader.readLine();
    } finally {
      Exception exception = null;
    } 
  }
  
  protected void writeAspect(String paramString1, String paramString2) throws IOException {
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(getLEDPath(), paramString1)));
    try {
      bufferedWriter.write(paramString2);
      return;
    } finally {
      paramString2 = null;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\hardware\android\DragonboardIndicatorLED.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */