package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.internal.camera.ImageFormatMapper;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.NativeObject;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;

public class UvcFormatDesc extends NativeObject<UvcStreamingInterface> {
  protected static Charset charset = Charset.forName("UTF8");
  
  protected static int[] fieldOffsets = nativeGetFieldOffsets((Fields.values()).length);
  
  public UvcFormatDesc(long paramLong, UvcStreamingInterface paramUvcStreamingInterface) {
    super(paramLong, RefCounted.TraceLevel.VeryVerbose);
    setParent((RefCounted)paramUvcStreamingInterface);
  }
  
  protected static native int[] nativeGetFieldOffsets(int paramInt);
  
  public Size getAspectRatio() {
    return new Size(getAspectRatioX(), getAspectRatioY());
  }
  
  public int getAspectRatioX() {
    return getUByte(fieldOffsets[Fields.bAspectRatioX.ordinal()]);
  }
  
  public int getAspectRatioY() {
    return getUByte(fieldOffsets[Fields.bAspectRatioY.ordinal()]);
  }
  
  public int getBitsPerPixel() {
    return getUByte(fieldOffsets[Fields.bBitsPerPixel.ordinal()]);
  }
  
  public boolean getCopyProtect() {
    return (getUByte(fieldOffsets[Fields.bCopyProtect.ordinal()]) != 0);
  }
  
  public UvcFrameDesc getDefaultFrameDesc() {
    int i = getDefaultFrameIndex();
    Iterator<UvcFrameDesc> iterator = getFrameDescriptors().iterator();
    UvcFrameDesc uvcFrameDesc = null;
    while (iterator.hasNext()) {
      UvcFrameDesc uvcFrameDesc1 = iterator.next();
      if (uvcFrameDesc1.getFrameIndex() == i) {
        uvcFrameDesc1.addRef();
        uvcFrameDesc = uvcFrameDesc1;
      } 
      uvcFrameDesc1.releaseRef();
    } 
    return uvcFrameDesc;
  }
  
  public int getDefaultFrameIndex() {
    return getUByte(fieldOffsets[Fields.bDefaultFrameIndex.ordinal()]);
  }
  
  public int getFlags() {
    return getUByte(fieldOffsets[Fields.bmFlags.ordinal()]);
  }
  
  public int getFormatIndex() {
    return getUByte(fieldOffsets[Fields.bFormatIndex.ordinal()]);
  }
  
  public String getFourCCFormat() {
    byte[] arrayOfByte;
    for (arrayOfByte = getBytes(fieldOffsets[Fields.fourccFormat.ordinal()], 4); arrayOfByte.length > 0 && arrayOfByte[arrayOfByte.length - 1] == 0; arrayOfByte = Arrays.copyOfRange(arrayOfByte, 0, arrayOfByte.length - 1));
    return charset.decode(ByteBuffer.wrap(arrayOfByte)).toString();
  }
  
  public int getFrameDescriptorCount() {
    return getUByte(fieldOffsets[Fields.bNumFrameDescriptors.ordinal()]);
  }
  
  public List<UvcFrameDesc> getFrameDescriptors() {
    ArrayList<UvcFrameDesc> arrayList = new ArrayList();
    long[] arrayOfLong = nativeGetLinkedList(this.pointer, Fields.frame_descs.offset());
    int j = arrayOfLong.length;
    for (int i = 0; i < j; i++)
      arrayList.add(new UvcFrameDesc(arrayOfLong[i], this)); 
    return arrayList;
  }
  
  public UUID getGuidFormat() {
    return Misc.uuidFromBytes(getBytes(fieldOffsets[Fields.guidFormat.ordinal()], 16), ByteOrder.LITTLE_ENDIAN);
  }
  
  public int getInterlaceFlags() {
    return getUByte(fieldOffsets[Fields.bmInterlaceFlags.ordinal()]);
  }
  
  protected int getStructSize() {
    return fieldOffsets[Fields.sizeof.ordinal()];
  }
  
  public Subtype getSubtype() {
    return Subtype.from(getUByte(fieldOffsets[Fields.bDescriptorSubtype.ordinal()]));
  }
  
  public boolean getVariableSize() {
    return (getUByte(fieldOffsets[Fields.bVariableSize.ordinal()]) != 0);
  }
  
  public boolean isAndroidFormat(int paramInt) {
    Iterator iterator = ImageFormatMapper.allFromGuid(getGuidFormat()).iterator();
    while (iterator.hasNext()) {
      if (((ImageFormatMapper.Format)iterator.next()).android == paramInt)
        return true; 
    } 
    return false;
  }
  
  protected enum Fields {
    bAspectRatioX, bAspectRatioY, bBitsPerPixel, bCopyProtect, bDefaultFrameIndex, bDescriptorSubtype, bFormatIndex, bNumFrameDescriptors, bVariableSize, bmFlags, bmInterlaceFlags, fourccFormat, frame_descs, guidFormat, sizeof;
    
    static {
      fourccFormat = new Fields("fourccFormat", 5);
      bBitsPerPixel = new Fields("bBitsPerPixel", 6);
      bmFlags = new Fields("bmFlags", 7);
      bDefaultFrameIndex = new Fields("bDefaultFrameIndex", 8);
      bAspectRatioX = new Fields("bAspectRatioX", 9);
      bAspectRatioY = new Fields("bAspectRatioY", 10);
      bmInterlaceFlags = new Fields("bmInterlaceFlags", 11);
      bCopyProtect = new Fields("bCopyProtect", 12);
      bVariableSize = new Fields("bVariableSize", 13);
      Fields fields = new Fields("frame_descs", 14);
      frame_descs = fields;
      $VALUES = new Fields[] { 
          sizeof, bDescriptorSubtype, bFormatIndex, bNumFrameDescriptors, guidFormat, fourccFormat, bBitsPerPixel, bmFlags, bDefaultFrameIndex, bAspectRatioX, 
          bAspectRatioY, bmInterlaceFlags, bCopyProtect, bVariableSize, fields };
    }
    
    public int offset() {
      return UvcFormatDesc.fieldOffsets[ordinal()];
    }
  }
  
  public enum Subtype {
    COLORFORMAT,
    FORMAT_DV,
    FORMAT_FRAME_BASED,
    FORMAT_MJPEG,
    FORMAT_MPEG2TS,
    FORMAT_STREAM_BASED,
    FORMAT_UNCOMPRESSED,
    FRAME_FRAME_BASED,
    FRAME_MJPEG,
    FRAME_UNCOMPRESSED,
    INPUT_HEADER,
    OUTPUT_HEADER,
    STILL_IMAGE_FRAME,
    UNDEFINED(0);
    
    byte value;
    
    static {
      FORMAT_UNCOMPRESSED = new Subtype("FORMAT_UNCOMPRESSED", 4, 4);
      FRAME_UNCOMPRESSED = new Subtype("FRAME_UNCOMPRESSED", 5, 5);
      FORMAT_MJPEG = new Subtype("FORMAT_MJPEG", 6, 6);
      FRAME_MJPEG = new Subtype("FRAME_MJPEG", 7, 7);
      FORMAT_MPEG2TS = new Subtype("FORMAT_MPEG2TS", 8, 10);
      FORMAT_DV = new Subtype("FORMAT_DV", 9, 12);
      COLORFORMAT = new Subtype("COLORFORMAT", 10, 13);
      FORMAT_FRAME_BASED = new Subtype("FORMAT_FRAME_BASED", 11, 16);
      FRAME_FRAME_BASED = new Subtype("FRAME_FRAME_BASED", 12, 17);
      Subtype subtype = new Subtype("FORMAT_STREAM_BASED", 13, 18);
      FORMAT_STREAM_BASED = subtype;
      $VALUES = new Subtype[] { 
          UNDEFINED, INPUT_HEADER, OUTPUT_HEADER, STILL_IMAGE_FRAME, FORMAT_UNCOMPRESSED, FRAME_UNCOMPRESSED, FORMAT_MJPEG, FRAME_MJPEG, FORMAT_MPEG2TS, FORMAT_DV, 
          COLORFORMAT, FORMAT_FRAME_BASED, FRAME_FRAME_BASED, subtype };
    }
    
    Subtype(int param1Int1) {
      this.value = (byte)param1Int1;
    }
    
    public static Subtype from(int param1Int) {
      for (Subtype subtype : values()) {
        if (subtype.value == param1Int)
          return subtype; 
      } 
      return UNDEFINED;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\UvcFormatDesc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */