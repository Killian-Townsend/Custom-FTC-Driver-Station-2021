package org.firstinspires.ftc.robotcore.internal.ftdi;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import com.qualcomm.robotcore.util.RobotLog;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class FtDeviceManager extends FtConstants {
  protected static final String ACTION_FTDI_USB_PERMISSION = "org.firstinspires.ftc.ftdi.permission";
  
  public static final String TAG = "FtDeviceManager";
  
  private static final Context mContext = AppUtil.getDefContext();
  
  private static PendingIntent mPendingIntent = null;
  
  private static List<VendorAndProductIds> mSupportedDevices = new ArrayList<VendorAndProductIds>(Arrays.asList(new VendorAndProductIds[] { 
          new VendorAndProductIds(1027, 24597), new VendorAndProductIds(1027, 24596), new VendorAndProductIds(1027, 24593), new VendorAndProductIds(1027, 24592), new VendorAndProductIds(1027, 24577), new VendorAndProductIds(1027, 24582), new VendorAndProductIds(1027, 24604), new VendorAndProductIds(1027, 64193), new VendorAndProductIds(1027, 64194), new VendorAndProductIds(1027, 64195), 
          new VendorAndProductIds(1027, 64196), new VendorAndProductIds(1027, 64197), new VendorAndProductIds(1027, 64198), new VendorAndProductIds(1027, 24594), new VendorAndProductIds(2220, 4133), new VendorAndProductIds(5590, 1), new VendorAndProductIds(1027, 24599) }));
  
  private static BroadcastReceiver mUsbDevicePermissions = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        // Byte code:
        //   0: ldc 'org.firstinspires.ftc.ftdi.permission'
        //   2: aload_2
        //   3: invokevirtual getAction : ()Ljava/lang/String;
        //   6: invokevirtual equals : (Ljava/lang/Object;)Z
        //   9: ifeq -> 111
        //   12: aload_0
        //   13: monitorenter
        //   14: aload_2
        //   15: ldc 'device'
        //   17: invokevirtual getParcelableExtra : (Ljava/lang/String;)Landroid/os/Parcelable;
        //   20: checkcast android/hardware/usb/UsbDevice
        //   23: astore_1
        //   24: aload_2
        //   25: ldc 'permission'
        //   27: iconst_0
        //   28: invokevirtual getBooleanExtra : (Ljava/lang/String;Z)Z
        //   31: ifeq -> 70
        //   34: new java/lang/StringBuilder
        //   37: dup
        //   38: invokespecial <init> : ()V
        //   41: astore_2
        //   42: aload_2
        //   43: ldc 'permission granted for device '
        //   45: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   48: pop
        //   49: aload_2
        //   50: aload_1
        //   51: invokevirtual getDeviceName : ()Ljava/lang/String;
        //   54: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   57: pop
        //   58: ldc 'FtDeviceManager'
        //   60: aload_2
        //   61: invokevirtual toString : ()Ljava/lang/String;
        //   64: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
        //   67: goto -> 103
        //   70: new java/lang/StringBuilder
        //   73: dup
        //   74: invokespecial <init> : ()V
        //   77: astore_2
        //   78: aload_2
        //   79: ldc 'permission denied for device '
        //   81: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   84: pop
        //   85: aload_2
        //   86: aload_1
        //   87: invokevirtual getDeviceName : ()Ljava/lang/String;
        //   90: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   93: pop
        //   94: ldc 'FtDeviceManager'
        //   96: aload_2
        //   97: invokevirtual toString : ()Ljava/lang/String;
        //   100: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;)V
        //   103: aload_0
        //   104: monitorexit
        //   105: return
        //   106: astore_1
        //   107: aload_0
        //   108: monitorexit
        //   109: aload_1
        //   110: athrow
        //   111: return
        // Exception table:
        //   from	to	target	type
        //   14	67	106	finally
        //   70	103	106	finally
        //   103	105	106	finally
        //   107	109	106	finally
      }
    };
  
  private static UsbManager mUsbManager;
  
  private static FtDeviceManager theInstance;
  
  private ArrayList<FtDevice> mFtdiDevices;
  
  private BroadcastReceiver mUsbPlugEvents = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        String str = param1Intent.getAction();
        if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(str)) {
          UsbDevice usbDevice = (UsbDevice)param1Intent.getParcelableExtra("device");
          RobotLog.vv("FtDeviceManager", "ACTION_USB_DEVICE_DETACHED: %s", new Object[] { usbDevice.getDeviceName() });
          while (true) {
            FtDevice ftDevice = FtDeviceManager.this.findDevice(usbDevice);
            if (ftDevice != null) {
              ftDevice.close();
              synchronized (FtDeviceManager.this.mFtdiDevices) {
                FtDeviceManager.this.mFtdiDevices.remove(ftDevice);
              } 
              continue;
            } 
            break;
          } 
        } else if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(str)) {
          UsbDevice usbDevice = (UsbDevice)param1Intent.getParcelableExtra("device");
          RobotLog.vv("FtDeviceManager", "ACTION_USB_DEVICE_ATTACHED: %s", new Object[] { usbDevice.getDeviceName() });
          FtDeviceManager.this.addOrUpdateUsbDevice(usbDevice);
        } 
      }
    };
  
  private FtDeviceManager() {
    if (findUsbManager()) {
      this.mFtdiDevices = new ArrayList<FtDevice>();
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
      intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
      mPendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), 0, new Intent("org.firstinspires.ftc.ftdi.permission"), 134217728);
      mContext.getApplicationContext().registerReceiver(mUsbDevicePermissions, new IntentFilter("org.firstinspires.ftc.ftdi.permission"));
      mContext.getApplicationContext().registerReceiver(this.mUsbPlugEvents, intentFilter);
      return;
    } 
    throw new RuntimeException("unable to find usb manager");
  }
  
  private void clearDevices() {
    synchronized (this.mFtdiDevices) {
      int j = this.mFtdiDevices.size();
      for (int i = 0; i < j; i++)
        this.mFtdiDevices.remove(0); 
      return;
    } 
  }
  
  private FtDevice findDevice(UsbDevice paramUsbDevice) {
    synchronized (this.mFtdiDevices) {
      for (FtDevice ftDevice : this.mFtdiDevices) {
        if (ftDevice.getUsbDevice().equals(paramUsbDevice))
          return ftDevice; 
      } 
      return null;
    } 
  }
  
  private static boolean findUsbManager() {
    if (mUsbManager == null) {
      Context context = mContext;
      if (context != null)
        mUsbManager = (UsbManager)context.getApplicationContext().getSystemService("usb"); 
    } 
    return (mUsbManager != null);
  }
  
  public static FtDeviceManager getInstance() {
    // Byte code:
    //   0: ldc org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManager
    //   2: monitorenter
    //   3: getstatic org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManager.theInstance : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManager;
    //   6: ifnonnull -> 19
    //   9: new org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManager
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: putstatic org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManager.theInstance : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManager;
    //   19: getstatic org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManager.theInstance : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManager;
    //   22: astore_0
    //   23: ldc org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManager
    //   25: monitorexit
    //   26: aload_0
    //   27: areturn
    //   28: astore_0
    //   29: ldc org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManager
    //   31: monitorexit
    //   32: aload_0
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   3	19	28	finally
    //   19	23	28	finally
  }
  
  public static int getLibraryVersion() {
    return 540016640;
  }
  
  private boolean isPermitted(UsbDevice paramUsbDevice) {
    if (!mUsbManager.hasPermission(paramUsbDevice)) {
      RobotLog.vv("FtDeviceManager", "requesting permissions for device=%s", new Object[] { paramUsbDevice.getDeviceName() });
      mUsbManager.requestPermission(paramUsbDevice, mPendingIntent);
    } 
    return mUsbManager.hasPermission(paramUsbDevice);
  }
  
  private boolean tryOpen(FtDevice paramFtDevice, FtDeviceManagerParams paramFtDeviceManagerParams) {
    boolean bool2 = false;
    if (paramFtDevice == null)
      return false; 
    paramFtDevice.setContext(mContext);
    if (paramFtDeviceManagerParams != null)
      paramFtDevice.setDriverParameters(paramFtDeviceManagerParams); 
    boolean bool1 = bool2;
    try {
      if (paramFtDevice.openDevice(mUsbManager)) {
        boolean bool = paramFtDevice.isOpen();
        bool1 = bool2;
        if (bool)
          bool1 = true; 
      } 
      return bool1;
    } catch (FtDeviceIOException ftDeviceIOException) {
      return false;
    } 
  }
  
  protected boolean addOrUpdatePermittedUsbDevice(List<FtDevice> paramList, UsbDevice paramUsbDevice, UsbInterface paramUsbInterface) {
    synchronized (this.mFtdiDevices) {
      FtDevice ftDevice = findDevice(paramUsbDevice);
      if (ftDevice == null) {
        ftDevice = new FtDevice(mContext, mUsbManager, paramUsbDevice, paramUsbInterface);
      } else {
        ftDevice.setContext(mContext);
        this.mFtdiDevices.remove(ftDevice);
      } 
      paramList.add(ftDevice);
      return true;
    } 
    RobotLog.ee("FtDeviceManager", (Throwable)paramList, "can't open FT_Device(%s) on interface(%s)", new Object[] { paramUsbDevice, paramUsbInterface });
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_5} */
    return false;
  }
  
  public int addOrUpdateUsbDevice(UsbDevice paramUsbDevice) {
    VendorAndProductIds vendorAndProductIds = VendorAndProductIds.from(paramUsbDevice);
    int i = 0;
    int j = 0;
    RobotLog.vv("FtDeviceManager", "addOrUpdateUsbDevice(%s)", new Object[] { vendorAndProductIds });
    AppUtil.getInstance().setUsbFileSystemRoot(paramUsbDevice);
    if (isFtDevice(paramUsbDevice)) {
      int k = paramUsbDevice.getInterfaceCount();
      for (i = 0; j < k; i = m) {
        int m = i;
        if (isPermitted(paramUsbDevice)) {
          m = i;
          if (addOrUpdatePermittedUsbDevice(this.mFtdiDevices, paramUsbDevice, paramUsbDevice.getInterface(j)))
            m = i + 1; 
        } 
        j++;
      } 
    } 
    return i;
  }
  
  public int createDeviceInfoList() {
    Iterator<UsbDevice> iterator = mUsbManager.getDeviceList().values().iterator();
    ArrayList<FtDevice> arrayList = new ArrayList();
    while (true) {
      boolean bool = iterator.hasNext();
      int i = 0;
      if (!bool)
        synchronized (this.mFtdiDevices) {
          clearDevices();
          this.mFtdiDevices = arrayList;
          i = arrayList.size();
          RobotLog.vv("FtDeviceManager", "createDeviceInfoList(): %d USB devices", new Object[] { Integer.valueOf(i) });
          return i;
        }  
      UsbDevice usbDevice = iterator.next();
      if (isFtDevice(usbDevice)) {
        int j = usbDevice.getInterfaceCount();
        while (i < j) {
          if (isPermitted(usbDevice))
            addOrUpdatePermittedUsbDevice(arrayList, usbDevice, usbDevice.getInterface(i)); 
          i++;
        } 
      } 
    } 
  }
  
  public int getDeviceInfoList(int paramInt, FtDeviceInfo[] paramArrayOfFtDeviceInfo) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_3
    //   4: iload_3
    //   5: iload_1
    //   6: if_icmpge -> 33
    //   9: aload_2
    //   10: iload_3
    //   11: aload_0
    //   12: getfield mFtdiDevices : Ljava/util/ArrayList;
    //   15: iload_3
    //   16: invokevirtual get : (I)Ljava/lang/Object;
    //   19: checkcast org/firstinspires/ftc/robotcore/internal/ftdi/FtDevice
    //   22: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   25: aastore
    //   26: iload_3
    //   27: iconst_1
    //   28: iadd
    //   29: istore_3
    //   30: goto -> 4
    //   33: aload_0
    //   34: getfield mFtdiDevices : Ljava/util/ArrayList;
    //   37: invokevirtual size : ()I
    //   40: istore_1
    //   41: aload_0
    //   42: monitorexit
    //   43: iload_1
    //   44: ireturn
    //   45: astore_2
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_2
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   9	26	45	finally
    //   33	41	45	finally
  }
  
  public FtDeviceInfo getDeviceInfoListDetail(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_1
    //   3: aload_0
    //   4: getfield mFtdiDevices : Ljava/util/ArrayList;
    //   7: invokevirtual size : ()I
    //   10: if_icmpgt -> 35
    //   13: iload_1
    //   14: iflt -> 35
    //   17: aload_0
    //   18: getfield mFtdiDevices : Ljava/util/ArrayList;
    //   21: iload_1
    //   22: invokevirtual get : (I)Ljava/lang/Object;
    //   25: checkcast org/firstinspires/ftc/robotcore/internal/ftdi/FtDevice
    //   28: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   31: astore_2
    //   32: goto -> 37
    //   35: aconst_null
    //   36: astore_2
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_2
    //   40: areturn
    //   41: astore_2
    //   42: aload_0
    //   43: monitorexit
    //   44: aload_2
    //   45: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	41	finally
    //   17	32	41	finally
  }
  
  public int[][] getVIDPID() {
    int j = mSupportedDevices.size();
    int[][] arrayOfInt = (int[][])Array.newInstance(int.class, new int[] { 2, j });
    for (int i = 0; i < j; i++) {
      VendorAndProductIds vendorAndProductIds = mSupportedDevices.get(i);
      arrayOfInt[0][i] = vendorAndProductIds.getVendorId();
      arrayOfInt[1][i] = vendorAndProductIds.getProductId();
    } 
    return arrayOfInt;
  }
  
  public boolean isFtDevice(UsbDevice paramUsbDevice) {
    if (mContext == null)
      return false; 
    VendorAndProductIds vendorAndProductIds = new VendorAndProductIds(paramUsbDevice.getVendorId(), paramUsbDevice.getProductId());
    return mSupportedDevices.contains(vendorAndProductIds);
  }
  
  public FtDevice openByDescription(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aconst_null
    //   5: checkcast org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams
    //   8: invokevirtual openByDescription : (Ljava/lang/String;Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams;)Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;
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
  
  public FtDevice openByDescription(String paramString, FtDeviceManagerParams paramFtDeviceManagerParams) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_3
    //   4: aload_0
    //   5: getfield mFtdiDevices : Ljava/util/ArrayList;
    //   8: invokevirtual size : ()I
    //   11: istore #4
    //   13: aconst_null
    //   14: astore #6
    //   16: iload_3
    //   17: iload #4
    //   19: if_icmpge -> 116
    //   22: aload_0
    //   23: getfield mFtdiDevices : Ljava/util/ArrayList;
    //   26: iload_3
    //   27: invokevirtual get : (I)Ljava/lang/Object;
    //   30: checkcast org/firstinspires/ftc/robotcore/internal/ftdi/FtDevice
    //   33: astore #7
    //   35: aload #7
    //   37: ifnull -> 109
    //   40: aload #7
    //   42: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   45: astore #8
    //   47: aload #8
    //   49: ifnonnull -> 63
    //   52: ldc 'FtDeviceManager'
    //   54: ldc_w '***devInfo cannot be null***'
    //   57: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;)V
    //   60: goto -> 109
    //   63: aload #8
    //   65: getfield productName : Ljava/lang/String;
    //   68: aload_1
    //   69: invokevirtual equals : (Ljava/lang/Object;)Z
    //   72: ifeq -> 109
    //   75: aload #7
    //   77: astore_1
    //   78: goto -> 81
    //   81: aload_0
    //   82: aload_1
    //   83: aload_2
    //   84: invokespecial tryOpen : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams;)Z
    //   87: istore #5
    //   89: iload #5
    //   91: ifne -> 100
    //   94: aload #6
    //   96: astore_1
    //   97: goto -> 100
    //   100: aload_0
    //   101: monitorexit
    //   102: aload_1
    //   103: areturn
    //   104: astore_1
    //   105: aload_0
    //   106: monitorexit
    //   107: aload_1
    //   108: athrow
    //   109: iload_3
    //   110: iconst_1
    //   111: iadd
    //   112: istore_3
    //   113: goto -> 4
    //   116: aconst_null
    //   117: astore_1
    //   118: goto -> 81
    // Exception table:
    //   from	to	target	type
    //   4	13	104	finally
    //   22	35	104	finally
    //   40	47	104	finally
    //   52	60	104	finally
    //   63	75	104	finally
    //   81	89	104	finally
  }
  
  public FtDevice openByIndex(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: aconst_null
    //   5: checkcast org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams
    //   8: invokevirtual openByIndex : (ILorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams;)Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: areturn
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public FtDevice openByIndex(int paramInt, FtDeviceManagerParams paramFtDeviceManagerParams) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aconst_null
    //   3: astore #4
    //   5: iload_1
    //   6: ifge -> 15
    //   9: aload #4
    //   11: astore_2
    //   12: goto -> 49
    //   15: aload_0
    //   16: getfield mFtdiDevices : Ljava/util/ArrayList;
    //   19: iload_1
    //   20: invokevirtual get : (I)Ljava/lang/Object;
    //   23: checkcast org/firstinspires/ftc/robotcore/internal/ftdi/FtDevice
    //   26: astore #5
    //   28: aload_0
    //   29: aload #5
    //   31: aload_2
    //   32: invokespecial tryOpen : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams;)Z
    //   35: istore_3
    //   36: iload_3
    //   37: ifne -> 46
    //   40: aload #4
    //   42: astore_2
    //   43: goto -> 49
    //   46: aload #5
    //   48: astore_2
    //   49: aload_0
    //   50: monitorexit
    //   51: aload_2
    //   52: areturn
    //   53: astore_2
    //   54: aload_0
    //   55: monitorexit
    //   56: aload_2
    //   57: athrow
    // Exception table:
    //   from	to	target	type
    //   15	36	53	finally
  }
  
  public FtDevice openByLocation(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: aconst_null
    //   5: checkcast org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams
    //   8: invokevirtual openByLocation : (ILorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams;)Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: areturn
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public FtDevice openByLocation(int paramInt, FtDeviceManagerParams paramFtDeviceManagerParams) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_3
    //   4: aload_0
    //   5: getfield mFtdiDevices : Ljava/util/ArrayList;
    //   8: invokevirtual size : ()I
    //   11: istore #4
    //   13: aconst_null
    //   14: astore #7
    //   16: iload_3
    //   17: iload #4
    //   19: if_icmpge -> 113
    //   22: aload_0
    //   23: getfield mFtdiDevices : Ljava/util/ArrayList;
    //   26: iload_3
    //   27: invokevirtual get : (I)Ljava/lang/Object;
    //   30: checkcast org/firstinspires/ftc/robotcore/internal/ftdi/FtDevice
    //   33: astore #6
    //   35: aload #6
    //   37: ifnull -> 106
    //   40: aload #6
    //   42: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   45: astore #8
    //   47: aload #8
    //   49: ifnonnull -> 63
    //   52: ldc 'FtDeviceManager'
    //   54: ldc_w '***devInfo cannot be null***'
    //   57: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;)V
    //   60: goto -> 106
    //   63: aload #8
    //   65: getfield location : I
    //   68: iload_1
    //   69: if_icmpne -> 106
    //   72: goto -> 75
    //   75: aload_0
    //   76: aload #6
    //   78: aload_2
    //   79: invokespecial tryOpen : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams;)Z
    //   82: istore #5
    //   84: iload #5
    //   86: ifne -> 96
    //   89: aload #7
    //   91: astore #6
    //   93: goto -> 96
    //   96: aload_0
    //   97: monitorexit
    //   98: aload #6
    //   100: areturn
    //   101: astore_2
    //   102: aload_0
    //   103: monitorexit
    //   104: aload_2
    //   105: athrow
    //   106: iload_3
    //   107: iconst_1
    //   108: iadd
    //   109: istore_3
    //   110: goto -> 4
    //   113: aconst_null
    //   114: astore #6
    //   116: goto -> 75
    // Exception table:
    //   from	to	target	type
    //   4	13	101	finally
    //   22	35	101	finally
    //   40	47	101	finally
    //   52	60	101	finally
    //   63	72	101	finally
    //   75	84	101	finally
  }
  
  public FtDevice openBySerialNumber(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aconst_null
    //   5: checkcast org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams
    //   8: invokevirtual openBySerialNumber : (Ljava/lang/String;Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams;)Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;
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
  
  public FtDevice openBySerialNumber(String paramString, FtDeviceManagerParams paramFtDeviceManagerParams) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_3
    //   4: aload_0
    //   5: getfield mFtdiDevices : Ljava/util/ArrayList;
    //   8: invokevirtual size : ()I
    //   11: istore #4
    //   13: aconst_null
    //   14: astore #6
    //   16: iload_3
    //   17: iload #4
    //   19: if_icmpge -> 116
    //   22: aload_0
    //   23: getfield mFtdiDevices : Ljava/util/ArrayList;
    //   26: iload_3
    //   27: invokevirtual get : (I)Ljava/lang/Object;
    //   30: checkcast org/firstinspires/ftc/robotcore/internal/ftdi/FtDevice
    //   33: astore #7
    //   35: aload #7
    //   37: ifnull -> 109
    //   40: aload #7
    //   42: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   45: astore #8
    //   47: aload #8
    //   49: ifnonnull -> 63
    //   52: ldc 'FtDeviceManager'
    //   54: ldc_w '***devInfo cannot be null***'
    //   57: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;)V
    //   60: goto -> 109
    //   63: aload #8
    //   65: getfield serialNumber : Ljava/lang/String;
    //   68: aload_1
    //   69: invokevirtual equals : (Ljava/lang/Object;)Z
    //   72: ifeq -> 109
    //   75: aload #7
    //   77: astore_1
    //   78: goto -> 81
    //   81: aload_0
    //   82: aload_1
    //   83: aload_2
    //   84: invokespecial tryOpen : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams;)Z
    //   87: istore #5
    //   89: iload #5
    //   91: ifne -> 100
    //   94: aload #6
    //   96: astore_1
    //   97: goto -> 100
    //   100: aload_0
    //   101: monitorexit
    //   102: aload_1
    //   103: areturn
    //   104: astore_1
    //   105: aload_0
    //   106: monitorexit
    //   107: aload_1
    //   108: athrow
    //   109: iload_3
    //   110: iconst_1
    //   111: iadd
    //   112: istore_3
    //   113: goto -> 4
    //   116: aconst_null
    //   117: astore_1
    //   118: goto -> 81
    // Exception table:
    //   from	to	target	type
    //   4	13	104	finally
    //   22	35	104	finally
    //   40	47	104	finally
    //   52	60	104	finally
    //   63	75	104	finally
    //   81	89	104	finally
  }
  
  public FtDevice openByUsbDevice(UsbDevice paramUsbDevice) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aconst_null
    //   5: checkcast org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams
    //   8: invokevirtual openByUsbDevice : (Landroid/hardware/usb/UsbDevice;Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams;)Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;
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
  
  public FtDevice openByUsbDevice(UsbDevice paramUsbDevice, FtDeviceManagerParams paramFtDeviceManagerParams) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc 'FtDeviceManager'
    //   4: ldc_w 'openByUsbDevice(%s)'
    //   7: iconst_1
    //   8: anewarray java/lang/Object
    //   11: dup
    //   12: iconst_0
    //   13: aload_1
    //   14: invokestatic from : (Landroid/hardware/usb/UsbDevice;)Lorg/firstinspires/ftc/robotcore/internal/ftdi/VendorAndProductIds;
    //   17: aastore
    //   18: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   21: aload_0
    //   22: aload_1
    //   23: invokevirtual isFtDevice : (Landroid/hardware/usb/UsbDevice;)Z
    //   26: istore_3
    //   27: aconst_null
    //   28: astore #5
    //   30: aload #5
    //   32: astore #4
    //   34: iload_3
    //   35: ifeq -> 111
    //   38: aload_0
    //   39: aload_1
    //   40: invokespecial findDevice : (Landroid/hardware/usb/UsbDevice;)Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;
    //   43: astore #6
    //   45: aload #6
    //   47: astore #4
    //   49: aload #6
    //   51: ifnonnull -> 92
    //   54: ldc 'FtDeviceManager'
    //   56: ldc_w 'device not found: adding it on the fly'
    //   59: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   62: aload_0
    //   63: aload_1
    //   64: invokevirtual addOrUpdateUsbDevice : (Landroid/hardware/usb/UsbDevice;)I
    //   67: pop
    //   68: aload_0
    //   69: aload_1
    //   70: invokespecial findDevice : (Landroid/hardware/usb/UsbDevice;)Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;
    //   73: astore_1
    //   74: aload_1
    //   75: astore #4
    //   77: aload_1
    //   78: ifnonnull -> 92
    //   81: ldc 'FtDeviceManager'
    //   83: ldc_w 'add failed'
    //   86: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;)V
    //   89: aload_1
    //   90: astore #4
    //   92: aload_0
    //   93: aload #4
    //   95: aload_2
    //   96: invokespecial tryOpen : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams;)Z
    //   99: istore_3
    //   100: iload_3
    //   101: ifne -> 111
    //   104: aload #5
    //   106: astore #4
    //   108: goto -> 111
    //   111: aload_0
    //   112: monitorexit
    //   113: aload #4
    //   115: areturn
    //   116: astore_1
    //   117: aload_0
    //   118: monitorexit
    //   119: aload_1
    //   120: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	116	finally
    //   38	45	116	finally
    //   54	74	116	finally
    //   81	89	116	finally
    //   92	100	116	finally
  }
  
  public boolean setVIDPID(int paramInt1, int paramInt2) {
    if (paramInt1 != 0 && paramInt2 != 0) {
      VendorAndProductIds vendorAndProductIds = new VendorAndProductIds(paramInt1, paramInt2);
      return mSupportedDevices.contains(vendorAndProductIds) ? true : mSupportedDevices.add(vendorAndProductIds);
    } 
    RobotLog.ee("FtDeviceManager", "Invalid parameter to setVIDPID");
    return false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\FtDeviceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */