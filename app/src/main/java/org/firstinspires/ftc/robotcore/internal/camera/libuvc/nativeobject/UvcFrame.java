package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import android.graphics.Bitmap;
import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;
import androidx.renderscript.Type;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.camera.ScriptC_format_convert;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants.UvcFrameFormat;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.NativeObject;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;

public class UvcFrame extends NativeObject<UvcContext> {
  protected static int[] fieldOffsets = nativeGetFieldOffsets((Fields.values()).length);
  
  public UvcFrame(long paramLong, NativeObject.MemoryAllocator paramMemoryAllocator, UvcContext paramUvcContext) {
    super(paramLong, paramMemoryAllocator, RefCounted.TraceLevel.VeryVerbose);
    setParent((RefCounted)paramUvcContext);
  }
  
  protected static native long nativeCopyFrame(long paramLong);
  
  protected static native void nativeCopyImageData(long paramLong, byte[] paramArrayOfbyte, int paramInt);
  
  protected static native void nativeFreeFrame(long paramLong);
  
  protected static native int[] nativeGetFieldOffsets(int paramInt);
  
  protected static native Object nativeGetImageByteBuffer(long paramLong);
  
  public UvcFrame copy() {
    return new UvcFrame(checkAlloc(nativeCopyFrame(this.pointer)), NativeObject.MemoryAllocator.EXTERNAL, (UvcContext)getParent());
  }
  
  public void copyToBitmap(Bitmap paramBitmap) {
    if (null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$camera$libuvc$constants$UvcFrameFormat[getFrameFormat().ordinal()] != 1)
      return; 
    yuy2ToBitmap(paramBitmap);
  }
  
  protected void destructor() {
    if (this.memoryAllocator == NativeObject.MemoryAllocator.EXTERNAL && this.pointer != 0L) {
      nativeFreeFrame(this.pointer);
      clearPointer();
    } 
    super.destructor();
  }
  
  public long getCaptureTime() {
    return getLong(Fields.captureTime.offset());
  }
  
  public UvcContext getContext() {
    return (UvcContext)getParent();
  }
  
  public UvcFrameFormat getFrameFormat() {
    return UvcFrameFormat.from(getInt(Fields.frameFormat.offset()));
  }
  
  public long getFrameNumber() {
    return getUInt(Fields.frameNumber.offset());
  }
  
  public int getHeight() {
    return getInt(Fields.height.offset());
  }
  
  public long getImageBuffer() {
    return getLong(Fields.pbData.offset());
  }
  
  public ByteBuffer getImageByteBuffer() {
    ByteBuffer byteBuffer = (ByteBuffer)nativeGetImageByteBuffer(this.pointer);
    byteBuffer.order(this.byteOrder);
    return byteBuffer;
  }
  
  public byte[] getImageData() {
    return getImageData(new byte[getImageSize()]);
  }
  
  public byte[] getImageData(byte[] paramArrayOfbyte) {
    int i = getImageSize();
    byte[] arrayOfByte = paramArrayOfbyte;
    if (paramArrayOfbyte.length != i)
      arrayOfByte = new byte[i]; 
    nativeCopyImageData(this.pointer, arrayOfByte, arrayOfByte.length);
    return arrayOfByte;
  }
  
  public int getImageSize() {
    return getSizet(Fields.cbData.offset());
  }
  
  public long getPointer() {
    return this.pointer;
  }
  
  public int getStride() {
    return getInt(Fields.stride.offset());
  }
  
  protected int getStructSize() {
    return fieldOffsets[Fields.sizeof.ordinal()];
  }
  
  public int getWidth() {
    return getInt(Fields.width.offset());
  }
  
  protected void yuy2ToBitmap(final Bitmap bitmap) {
    if (!getContext().lockRenderScriptWhile(1L, TimeUnit.SECONDS, new Runnable() {
          public void run() {
            RenderScript renderScript = UvcFrame.this.getContext().getRenderScript();
            int i = UvcFrame.this.getWidth();
            Assert.assertTrue(Misc.isEven(i));
            int j = UvcFrame.this.getHeight();
            Allocation allocation1 = Allocation.createTyped(renderScript, (new Type.Builder(renderScript, Element.U8_4(renderScript))).setX(i / 2).setY(j).create(), 1);
            allocation1.copyFromUnchecked(UvcFrame.this.getImageData());
            Allocation allocation2 = Allocation.createFromBitmap(renderScript, bitmap, Allocation.MipmapControl.MIPMAP_NONE, 129);
            ScriptC_format_convert scriptC_format_convert = new ScriptC_format_convert(renderScript);
            scriptC_format_convert.set_inputAllocation(allocation1);
            scriptC_format_convert.set_outputWidth(i);
            scriptC_format_convert.set_outputHeight(j);
            if (UvcFrame.null.$SwitchMap$android$graphics$Bitmap$Config[bitmap.getConfig().ordinal()] != 1) {
              RobotLog.ee(UvcFrame.this.getTag(), "conversion to %s not yet implemented; ignored", new Object[] { this.val$bitmap.getConfig() });
            } else {
              scriptC_format_convert.forEach_yuv2_to_argb8888(allocation2);
            } 
            allocation2.copyTo(bitmap);
            allocation2.destroy();
          }
        }))
      RobotLog.ee(getTag(), "failed to access RenderScript: frameNumber=%d", new Object[] { Long.valueOf(getFrameNumber()) }); 
  }
  
  protected enum Fields {
    captureTime, cbAllocated, cbData, frameFormat, frameNumber, height, pContext, pbData, pts, sizeof, sourceClockReference, stride, width;
    
    static {
      cbData = new Fields("cbData", 2);
      cbAllocated = new Fields("cbAllocated", 3);
      width = new Fields("width", 4);
      height = new Fields("height", 5);
      frameFormat = new Fields("frameFormat", 6);
      stride = new Fields("stride", 7);
      frameNumber = new Fields("frameNumber", 8);
      pts = new Fields("pts", 9);
      captureTime = new Fields("captureTime", 10);
      sourceClockReference = new Fields("sourceClockReference", 11);
      Fields fields = new Fields("pContext", 12);
      pContext = fields;
      $VALUES = new Fields[] { 
          sizeof, pbData, cbData, cbAllocated, width, height, frameFormat, stride, frameNumber, pts, 
          captureTime, sourceClockReference, fields };
    }
    
    public int offset() {
      return UvcFrame.fieldOffsets[ordinal()];
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\UvcFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */