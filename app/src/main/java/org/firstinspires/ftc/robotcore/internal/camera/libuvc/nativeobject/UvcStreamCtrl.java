package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import org.firstinspires.ftc.robotcore.internal.system.NativeObject;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;

public class UvcStreamCtrl extends NativeObject<UvcDeviceHandle> {
  public static final String TAG = UvcStreamCtrl.class.getSimpleName();
  
  protected static int[] fieldOffsets = nativeGetFieldOffsets((Fields.values()).length);
  
  public UvcStreamCtrl(UvcDeviceHandle paramUvcDeviceHandle) {
    allocateMemory(getStructSize());
    setParent((RefCounted)paramUvcDeviceHandle);
  }
  
  protected static native int[] nativeGetFieldOffsets(int paramInt);
  
  protected static native long nativeOpen(long paramLong1, long paramLong2);
  
  protected static native String nativePrint(long paramLong1, long paramLong2);
  
  public long getClockFrequency() {
    return getUInt(Fields.dwClockFrequency.offset());
  }
  
  public int getCompQuality() {
    return getUShort(Fields.wCompQuality.offset());
  }
  
  public int getCompWindowSize() {
    return getUShort(Fields.wCompWindowSize.offset());
  }
  
  public int getDelay() {
    return getUShort(Fields.wDelay.offset());
  }
  
  public int getFormatIndex() {
    return getUByte(Fields.bFormatIndex.offset());
  }
  
  public int getFrameIndex() {
    return getUByte(Fields.bFrameIndex.offset());
  }
  
  public int getFrameRate() {
    return getUShort(Fields.wPFrameRate.offset());
  }
  
  public int getFramingInfo() {
    return getUByte(Fields.bmFramingInfo.offset());
  }
  
  public int getHint() {
    return getUShort(Fields.bmHint.offset());
  }
  
  public int getInterfaceNumber() {
    return getUByte(Fields.bInterfaceNumber.offset());
  }
  
  public int getKeyFrameRate() {
    return getUShort(Fields.wKeyFrameRate.offset());
  }
  
  public long getMaxPayloadTransferSize() {
    return getUInt(Fields.dwMaxPayloadTransferSize.offset());
  }
  
  public int getMaxVersion() {
    return getUByte(Fields.bMaxVersion.offset());
  }
  
  public long getMaxVideoFrameSize() {
    return getUInt(Fields.dwMaxVideoFrameSize.offset());
  }
  
  public int getMinVersion() {
    return getUByte(Fields.bMinVersion.offset());
  }
  
  public long getNsFrameInterval() {
    return getUInt(Fields.dwFrameInterval.offset());
  }
  
  protected long getPointer() {
    return this.pointer;
  }
  
  public int getPreferredVersion() {
    return getUByte(Fields.bPreferredVersion.offset());
  }
  
  protected int getStructSize() {
    return fieldOffsets[Fields.sizeof.ordinal()];
  }
  
  public String getTag() {
    return TAG;
  }
  
  public UvcStreamHandle open() {
    long l = nativeOpen(((UvcDeviceHandle)getParent()).getPointer(), this.pointer);
    return (l != 0L) ? new UvcStreamHandle(l, (UvcDeviceHandle)getParent()) : null;
  }
  
  public String toStringVerbose() {
    return nativePrint(this.pointer, ((UvcDeviceHandle)this.parent).getUvcContext().getPointer());
  }
  
  protected enum Fields {
    bFormatIndex, bFrameIndex, bInterfaceNumber, bMaxVersion, bMinVersion, bPreferredVersion, bmFramingInfo, bmHint, dwClockFrequency, dwFrameInterval, dwMaxPayloadTransferSize, dwMaxVideoFrameSize, sizeof, wCompQuality, wCompWindowSize, wDelay, wKeyFrameRate, wPFrameRate;
    
    static {
      bFormatIndex = new Fields("bFormatIndex", 2);
      bFrameIndex = new Fields("bFrameIndex", 3);
      dwFrameInterval = new Fields("dwFrameInterval", 4);
      wKeyFrameRate = new Fields("wKeyFrameRate", 5);
      wPFrameRate = new Fields("wPFrameRate", 6);
      wCompQuality = new Fields("wCompQuality", 7);
      wCompWindowSize = new Fields("wCompWindowSize", 8);
      wDelay = new Fields("wDelay", 9);
      dwMaxVideoFrameSize = new Fields("dwMaxVideoFrameSize", 10);
      dwMaxPayloadTransferSize = new Fields("dwMaxPayloadTransferSize", 11);
      dwClockFrequency = new Fields("dwClockFrequency", 12);
      bmFramingInfo = new Fields("bmFramingInfo", 13);
      bPreferredVersion = new Fields("bPreferredVersion", 14);
      bMinVersion = new Fields("bMinVersion", 15);
      bMaxVersion = new Fields("bMaxVersion", 16);
      Fields fields = new Fields("bInterfaceNumber", 17);
      bInterfaceNumber = fields;
      $VALUES = new Fields[] { 
          sizeof, bmHint, bFormatIndex, bFrameIndex, dwFrameInterval, wKeyFrameRate, wPFrameRate, wCompQuality, wCompWindowSize, wDelay, 
          dwMaxVideoFrameSize, dwMaxPayloadTransferSize, dwClockFrequency, bmFramingInfo, bPreferredVersion, bMinVersion, bMaxVersion, fields };
    }
    
    public int offset() {
      return UvcStreamCtrl.fieldOffsets[ordinal()];
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\UvcStreamCtrl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */