package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.system.NativeObject;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;

public class UvcStreamingInterface extends NativeObject<UvcDeviceInfo> {
  protected static int[] fieldOffsets = nativeGetFieldOffsets((Fields.values()).length);
  
  public UvcStreamingInterface(long paramLong, UvcDeviceInfo paramUvcDeviceInfo) {
    super(paramLong);
    setParent((RefCounted)paramUvcDeviceInfo);
  }
  
  protected static native int[] nativeGetFieldOffsets(int paramInt);
  
  public int getEndpointAddress() {
    return getUByte(Fields.bEndpointAddress.offset());
  }
  
  public List<UvcFormatDesc> getFormatDescriptors() {
    ArrayList<UvcFormatDesc> arrayList = new ArrayList();
    long[] arrayOfLong = nativeGetLinkedList(this.pointer, Fields.format_descs.offset());
    int j = arrayOfLong.length;
    for (int i = 0; i < j; i++)
      arrayList.add(new UvcFormatDesc(arrayOfLong[i], this)); 
    return arrayList;
  }
  
  public int getInterfaceNumber() {
    return getUByte(Fields.bInterfaceNumber.offset());
  }
  
  protected int getStructSize() {
    return fieldOffsets[Fields.sizeof.ordinal()];
  }
  
  public int getTerminalLink() {
    return getUByte(Fields.bTerminalLink.offset());
  }
  
  protected enum Fields {
    bEndpointAddress, bInterfaceNumber, bTerminalLink, format_descs, sizeof;
    
    static {
      bEndpointAddress = new Fields("bEndpointAddress", 3);
      Fields fields = new Fields("bTerminalLink", 4);
      bTerminalLink = fields;
      $VALUES = new Fields[] { sizeof, bInterfaceNumber, format_descs, bEndpointAddress, fields };
    }
    
    public int offset() {
      return UvcStreamingInterface.fieldOffsets[ordinal()];
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\UvcStreamingInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */