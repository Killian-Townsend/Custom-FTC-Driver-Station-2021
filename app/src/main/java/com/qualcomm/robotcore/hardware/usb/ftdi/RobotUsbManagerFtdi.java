package com.qualcomm.robotcore.hardware.usb.ftdi;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.ftdi.FtDevice;
import org.firstinspires.ftc.robotcore.internal.ftdi.FtDeviceManager;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class RobotUsbManagerFtdi implements RobotUsbManager {
  public static final String TAG = "RobotUsbManagerFtdi";
  
  private FtDeviceManager ftDeviceManager = FtDeviceManager.getInstance();
  
  private int numberOfDevices;
  
  public static SerialNumber getSerialNumber(FtDevice paramFtDevice) {
    return SerialNumber.fromString((paramFtDevice.getDeviceInfo()).serialNumber);
  }
  
  protected SerialNumber getDeviceSerialNumberByIndex(int paramInt) throws RobotCoreException {
    return SerialNumber.fromString((this.ftDeviceManager.getDeviceInfoListDetail(paramInt)).serialNumber);
  }
  
  public RobotUsbDevice openBySerialNumber(SerialNumber paramSerialNumber) throws RobotCoreException {
    FtDevice ftDevice = this.ftDeviceManager.openBySerialNumber(paramSerialNumber.getString());
    if (ftDevice != null) {
      try {
        ftDevice.resetDevice();
      } catch (RobotUsbException robotUsbException) {
        RobotLog.ee("RobotUsbManagerFtdi", (Throwable)robotUsbException, "unable to reset FtDevice(%s): ignoring");
      } 
      return new RobotUsbDeviceFtdi(ftDevice, paramSerialNumber);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("FTDI driver failed to open USB device with serial number ");
    stringBuilder.append(paramSerialNumber);
    stringBuilder.append(" (returned null device)");
    throw new RobotCoreException(stringBuilder.toString());
  }
  
  public List<SerialNumber> scanForDevices() throws RobotCoreException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield ftDeviceManager : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManager;
    //   7: invokevirtual createDeviceInfoList : ()I
    //   10: putfield numberOfDevices : I
    //   13: new java/util/ArrayList
    //   16: dup
    //   17: aload_0
    //   18: getfield numberOfDevices : I
    //   21: invokespecial <init> : (I)V
    //   24: astore_2
    //   25: iconst_0
    //   26: istore_1
    //   27: iload_1
    //   28: aload_0
    //   29: getfield numberOfDevices : I
    //   32: if_icmpge -> 54
    //   35: aload_2
    //   36: aload_0
    //   37: iload_1
    //   38: invokevirtual getDeviceSerialNumberByIndex : (I)Lcom/qualcomm/robotcore/util/SerialNumber;
    //   41: invokeinterface add : (Ljava/lang/Object;)Z
    //   46: pop
    //   47: iload_1
    //   48: iconst_1
    //   49: iadd
    //   50: istore_1
    //   51: goto -> 27
    //   54: aload_0
    //   55: monitorexit
    //   56: aload_2
    //   57: areturn
    //   58: astore_2
    //   59: aload_0
    //   60: monitorexit
    //   61: aload_2
    //   62: athrow
    // Exception table:
    //   from	to	target	type
    //   2	25	58	finally
    //   27	47	58	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardwar\\usb\ftdi\RobotUsbManagerFtdi.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */