package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.system.NativeObject;

public class UvcDeviceInfo extends NativeObject {
  protected static int[] fieldOffsets = nativeGetFieldOffsets((Fields.values()).length);
  
  public UvcDeviceInfo(long paramLong) {
    super(paramLong);
  }
  
  protected static native void nativeFreeDeviceInfo(long paramLong);
  
  protected static native int[] nativeGetFieldOffsets(int paramInt);
  
  protected void destructor() {
    if (this.pointer != 0L) {
      nativeFreeDeviceInfo(this.pointer);
      clearPointer();
    } 
    super.destructor();
  }
  
  public List<UvcStreamingInterface> getStreamingInterfaces() {
    ArrayList<UvcStreamingInterface> arrayList = new ArrayList();
    long[] arrayOfLong = nativeGetLinkedList(this.pointer, Fields.stream_ifs.offset());
    int j = arrayOfLong.length;
    for (int i = 0; i < j; i++)
      arrayList.add(new UvcStreamingInterface(arrayOfLong[i], this)); 
    return arrayList;
  }
  
  protected int getStructSize() {
    return fieldOffsets[Fields.sizeof.ordinal()];
  }
  
  protected enum Fields {
    config, ctrl_if, sizeof, stream_ifs;
    
    static {
      Fields fields = new Fields("stream_ifs", 3);
      stream_ifs = fields;
      $VALUES = new Fields[] { sizeof, config, ctrl_if, fields };
    }
    
    public int offset() {
      return UvcDeviceInfo.fieldOffsets[ordinal()];
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\UvcDeviceInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */