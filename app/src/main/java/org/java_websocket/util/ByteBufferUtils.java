package org.java_websocket.util;

import java.nio.ByteBuffer;

public class ByteBufferUtils {
  public static ByteBuffer getEmptyByteBuffer() {
    return ByteBuffer.allocate(0);
  }
  
  public static int transferByteBuffer(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2) {
    if (paramByteBuffer1 != null && paramByteBuffer2 != null) {
      int i = paramByteBuffer1.remaining();
      int j = paramByteBuffer2.remaining();
      if (i > j) {
        i = Math.min(i, j);
        paramByteBuffer1.limit(i);
        paramByteBuffer2.put(paramByteBuffer1);
        return i;
      } 
      paramByteBuffer2.put(paramByteBuffer1);
      return i;
    } 
    throw new IllegalArgumentException();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocke\\util\ByteBufferUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */