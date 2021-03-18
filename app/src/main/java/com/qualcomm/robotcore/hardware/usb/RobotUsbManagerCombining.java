package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;
import java.util.List;

public class RobotUsbManagerCombining implements RobotUsbManager {
  public static final String TAG = "RobotUsbManagerCombining";
  
  protected List<ManagerInfo> managers = new ArrayList<ManagerInfo>();
  
  public void addManager(RobotUsbManager paramRobotUsbManager) {
    ManagerInfo managerInfo = new ManagerInfo();
    managerInfo.manager = paramRobotUsbManager;
    managerInfo.scanCount = 0;
    this.managers.add(managerInfo);
  }
  
  public RobotUsbDevice openBySerialNumber(SerialNumber paramSerialNumber) throws RobotCoreException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aconst_null
    //   3: astore_2
    //   4: aload_0
    //   5: getfield managers : Ljava/util/List;
    //   8: invokeinterface iterator : ()Ljava/util/Iterator;
    //   13: astore #4
    //   15: aload_2
    //   16: astore_3
    //   17: aload #4
    //   19: invokeinterface hasNext : ()Z
    //   24: ifeq -> 55
    //   27: aload #4
    //   29: invokeinterface next : ()Ljava/lang/Object;
    //   34: checkcast com/qualcomm/robotcore/hardware/usb/RobotUsbManagerCombining$ManagerInfo
    //   37: astore_3
    //   38: aload_3
    //   39: getfield manager : Lcom/qualcomm/robotcore/hardware/usb/RobotUsbManager;
    //   42: aload_1
    //   43: invokeinterface openBySerialNumber : (Lcom/qualcomm/robotcore/util/SerialNumber;)Lcom/qualcomm/robotcore/hardware/usb/RobotUsbDevice;
    //   48: astore_3
    //   49: aload_3
    //   50: astore_2
    //   51: aload_3
    //   52: ifnull -> 15
    //   55: aload_3
    //   56: ifnull -> 63
    //   59: aload_0
    //   60: monitorexit
    //   61: aload_3
    //   62: areturn
    //   63: new java/lang/StringBuilder
    //   66: dup
    //   67: invokespecial <init> : ()V
    //   70: astore_2
    //   71: aload_2
    //   72: ldc 'Combiner unable to open device with serialNumber = '
    //   74: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: pop
    //   78: aload_2
    //   79: aload_1
    //   80: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: new com/qualcomm/robotcore/exception/RobotCoreException
    //   87: dup
    //   88: aload_2
    //   89: invokevirtual toString : ()Ljava/lang/String;
    //   92: invokespecial <init> : (Ljava/lang/String;)V
    //   95: athrow
    //   96: astore_1
    //   97: aload_0
    //   98: monitorexit
    //   99: aload_1
    //   100: athrow
    //   101: astore_3
    //   102: goto -> 15
    // Exception table:
    //   from	to	target	type
    //   4	15	96	finally
    //   17	38	96	finally
    //   38	49	101	com/qualcomm/robotcore/exception/RobotCoreException
    //   38	49	96	finally
    //   63	96	96	finally
  }
  
  public List<SerialNumber> scanForDevices() throws RobotCoreException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: dup
    //   6: invokespecial <init> : ()V
    //   9: astore_1
    //   10: aload_0
    //   11: getfield managers : Ljava/util/List;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore_2
    //   20: aload_2
    //   21: invokeinterface hasNext : ()Z
    //   26: ifeq -> 60
    //   29: aload_2
    //   30: invokeinterface next : ()Ljava/lang/Object;
    //   35: checkcast com/qualcomm/robotcore/hardware/usb/RobotUsbManagerCombining$ManagerInfo
    //   38: astore_3
    //   39: aload_3
    //   40: getfield manager : Lcom/qualcomm/robotcore/hardware/usb/RobotUsbManager;
    //   43: invokeinterface scanForDevices : ()Ljava/util/List;
    //   48: astore_3
    //   49: aload_1
    //   50: aload_3
    //   51: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   56: pop
    //   57: goto -> 20
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_1
    //   63: areturn
    //   64: astore_1
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_1
    //   68: athrow
    //   69: astore_3
    //   70: goto -> 20
    // Exception table:
    //   from	to	target	type
    //   2	20	64	finally
    //   20	39	64	finally
    //   39	49	69	com/qualcomm/robotcore/exception/RobotCoreException
    //   39	49	64	finally
    //   49	57	64	finally
  }
  
  protected class ManagerInfo {
    public RobotUsbManager manager;
    
    public int scanCount;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardwar\\usb\RobotUsbManagerCombining.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */