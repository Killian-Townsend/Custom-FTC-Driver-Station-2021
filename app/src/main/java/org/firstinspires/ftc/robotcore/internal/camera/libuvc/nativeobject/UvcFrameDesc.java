package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import com.qualcomm.robotcore.util.TypeConversion;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.internal.system.NativeObject;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;

public class UvcFrameDesc extends NativeObject<UvcFormatDesc> {
  protected static int[] fieldOffsets = nativeGetFieldOffsets((Fields.values()).length);
  
  public UvcFrameDesc(long paramLong, UvcFormatDesc paramUvcFormatDesc) {
    super(paramLong, RefCounted.TraceLevel.Verbose);
    setParent((RefCounted)paramUvcFormatDesc);
  }
  
  protected static native int[] nativeGetFieldOffsets(int paramInt);
  
  public long getBytesPerLine() {
    return getUInt(Fields.dwBytesPerLine.offset());
  }
  
  public long getDefaultFrameInterval() {
    return getUInt(Fields.dwDefaultFrameInterval.offset());
  }
  
  public int getFrameIndex() {
    return getUByte(Fields.bFrameIndex.offset());
  }
  
  public long getFrameIntervalStepContinuous() {
    return getUInt(Fields.dwFrameIntervalStep.offset());
  }
  
  public int getFrameIntervalType() {
    return getUByte(Fields.bFrameIntervalType.offset());
  }
  
  public List<Long> getFrameIntervals() {
    ArrayList<Long> arrayList = new ArrayList();
    long l = nativeGetPointer(this.pointer, Fields.rgIntervals.offset());
    if (l != 0L) {
      int i = 0;
      long[] arrayOfLong = nativeGetNullTerminatedList(l, 0, 4);
      int j = arrayOfLong.length;
      while (i < j) {
        arrayList.add(Long.valueOf(TypeConversion.unsignedIntToLong((int)arrayOfLong[i])));
        i++;
      } 
    } else {
      l = getMinFrameIntervalContinuous();
      long l1 = getMaxFrameIntervalContinuous();
      long l2 = getFrameIntervalStepContinuous();
      while (l <= l1) {
        arrayList.add(Long.valueOf(l));
        l += l2;
      } 
    } 
    return arrayList;
  }
  
  public int getHeight() {
    return getUShort(Fields.wHeight.offset());
  }
  
  public long getMaxBitRate() {
    return getUInt(Fields.dwMaxBitRate.offset());
  }
  
  protected long getMaxFrameInterval() {
    if (hasDiscreteFrameIntervals()) {
      List<Long> list = getFrameIntervals();
      return ((Long)list.get(list.size() - 1)).longValue();
    } 
    return getMaxFrameIntervalContinuous();
  }
  
  public long getMaxFrameIntervalContinuous() {
    return getUInt(Fields.dwMaxFrameInterval.offset());
  }
  
  public long getMinBitRate() {
    return getUInt(Fields.dwMinBitRate.offset());
  }
  
  public long getMinFrameInterval() {
    return hasDiscreteFrameIntervals() ? ((Long)getFrameIntervals().get(0)).longValue() : getMinFrameIntervalContinuous();
  }
  
  public long getMinFrameIntervalContinuous() {
    return getUInt(Fields.dwMinFrameInterval.offset());
  }
  
  public Size getSize() {
    return new Size(getWidth(), getHeight());
  }
  
  protected int getStructSize() {
    return fieldOffsets[Fields.sizeof.ordinal()];
  }
  
  public int getWidth() {
    return getUShort(Fields.wWidth.offset());
  }
  
  public boolean hasDiscreteFrameIntervals() {
    return (nativeGetPointer(this.pointer, Fields.rgIntervals.offset()) != 0L);
  }
  
  public boolean isFixedFrameRate() {
    return ((getUByte(Fields.bmCapabilities.offset()) & 0x2) != 0);
  }
  
  public boolean isStillImageSupported() {
    return ((getUByte(Fields.bmCapabilities.offset()) & 0x1) != 0);
  }
  
  protected enum Fields {
    bDescriptorSubtype, bFrameIndex, bFrameIntervalType, bmCapabilities, dwBytesPerLine, dwDefaultFrameInterval, dwFrameIntervalStep, dwMaxBitRate, dwMaxFrameInterval, dwMaxVideoFrameBufferSize, dwMinBitRate, dwMinFrameInterval, rgIntervals, sizeof, wHeight, wWidth;
    
    static {
      wHeight = new Fields("wHeight", 5);
      dwMinBitRate = new Fields("dwMinBitRate", 6);
      dwMaxBitRate = new Fields("dwMaxBitRate", 7);
      dwMaxVideoFrameBufferSize = new Fields("dwMaxVideoFrameBufferSize", 8);
      dwDefaultFrameInterval = new Fields("dwDefaultFrameInterval", 9);
      dwMinFrameInterval = new Fields("dwMinFrameInterval", 10);
      dwMaxFrameInterval = new Fields("dwMaxFrameInterval", 11);
      dwFrameIntervalStep = new Fields("dwFrameIntervalStep", 12);
      bFrameIntervalType = new Fields("bFrameIntervalType", 13);
      dwBytesPerLine = new Fields("dwBytesPerLine", 14);
      Fields fields = new Fields("rgIntervals", 15);
      rgIntervals = fields;
      $VALUES = new Fields[] { 
          sizeof, bDescriptorSubtype, bFrameIndex, bmCapabilities, wWidth, wHeight, dwMinBitRate, dwMaxBitRate, dwMaxVideoFrameBufferSize, dwDefaultFrameInterval, 
          dwMinFrameInterval, dwMaxFrameInterval, dwFrameIntervalStep, bFrameIntervalType, dwBytesPerLine, fields };
    }
    
    public int offset() {
      return UvcFrameDesc.fieldOffsets[ordinal()];
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\UvcFrameDesc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */