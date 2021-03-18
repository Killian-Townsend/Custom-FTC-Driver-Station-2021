package com.qualcomm.hardware.lynx;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class LynxUsbUtil {
  private static final String TAG = "LynxUsbUtil";
  
  private static void logMessageAndThrow(String paramString) throws RobotCoreException {
    RobotLog.ee("LynxUsbUtil", paramString);
    throw new RobotCoreException(paramString);
  }
  
  public static <T> T makePlaceholderValue(T paramT) {
    return paramT;
  }
  
  public static RobotUsbDevice openUsbDevice(boolean paramBoolean, RobotUsbManager paramRobotUsbManager, SerialNumber paramSerialNumber) throws RobotCoreException {
    if (paramBoolean)
      paramRobotUsbManager.scanForDevices(); 
    RobotCoreException robotCoreException2 = null;
    try {
      RobotUsbDevice robotUsbDevice = paramRobotUsbManager.openBySerialNumber(paramSerialNumber);
    } catch (RobotCoreException robotCoreException1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unable to open lynx USB device ");
      stringBuilder.append(paramSerialNumber);
      stringBuilder.append(": ");
      stringBuilder.append(robotCoreException1.getMessage());
      logMessageAndThrow(stringBuilder.toString());
      robotCoreException1 = robotCoreException2;
    } 
    try {
      robotCoreException1.setBaudRate(460800);
      robotCoreException1.setDataCharacteristics((byte)8, (byte)0, (byte)0);
      robotCoreException1.setLatencyTimer(1);
      return (RobotUsbDevice)robotCoreException1;
    } catch (RobotUsbException robotUsbException) {
      robotCoreException1.close();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to open lynx USB device ");
      stringBuilder.append(paramSerialNumber);
      stringBuilder.append(" - ");
      stringBuilder.append(robotCoreException1.getProductName());
      stringBuilder.append(": ");
      stringBuilder.append(robotUsbException.getMessage());
      logMessageAndThrow(stringBuilder.toString());
      return (RobotUsbDevice)robotCoreException1;
    } 
  }
  
  public static class Placeholder<T> {
    private boolean logged;
    
    private String message;
    
    private String tag;
    
    public Placeholder(String param1String1, String param1String2, Object... param1VarArgs) {
      this.tag = param1String1;
      this.message = String.format("placeholder: %s", new Object[] { String.format(param1String2, param1VarArgs) });
      this.logged = false;
    }
    
    public T log(T param1T) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield logged : Z
      //   6: ifne -> 25
      //   9: aload_0
      //   10: getfield tag : Ljava/lang/String;
      //   13: aload_0
      //   14: getfield message : Ljava/lang/String;
      //   17: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;)V
      //   20: aload_0
      //   21: iconst_1
      //   22: putfield logged : Z
      //   25: aload_0
      //   26: monitorexit
      //   27: aload_1
      //   28: areturn
      //   29: astore_1
      //   30: aload_0
      //   31: monitorexit
      //   32: aload_1
      //   33: athrow
      // Exception table:
      //   from	to	target	type
      //   2	25	29	finally
    }
    
    public void reset() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: iconst_0
      //   4: putfield logged : Z
      //   7: aload_0
      //   8: monitorexit
      //   9: return
      //   10: astore_1
      //   11: aload_0
      //   12: monitorexit
      //   13: aload_1
      //   14: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	10	finally
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxUsbUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */