package org.firstinspires.ftc.robotcore.internal.opengl.models;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class MeshObject {
  protected Buffer fillBuffer(double[] paramArrayOfdouble) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(paramArrayOfdouble.length * 4);
    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    int j = paramArrayOfdouble.length;
    for (int i = 0; i < j; i++)
      byteBuffer.putFloat((float)paramArrayOfdouble[i]); 
    byteBuffer.rewind();
    return byteBuffer;
  }
  
  protected Buffer fillBuffer(float[] paramArrayOffloat) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(paramArrayOffloat.length * 4);
    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    int j = paramArrayOffloat.length;
    for (int i = 0; i < j; i++)
      byteBuffer.putFloat(paramArrayOffloat[i]); 
    byteBuffer.rewind();
    return byteBuffer;
  }
  
  protected Buffer fillBuffer(short[] paramArrayOfshort) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(paramArrayOfshort.length * 2);
    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    int j = paramArrayOfshort.length;
    for (int i = 0; i < j; i++)
      byteBuffer.putShort(paramArrayOfshort[i]); 
    byteBuffer.rewind();
    return byteBuffer;
  }
  
  public abstract Buffer getBuffer(BUFFER_TYPE paramBUFFER_TYPE);
  
  public Buffer getIndices() {
    return getBuffer(BUFFER_TYPE.BUFFER_TYPE_INDICES);
  }
  
  public Buffer getNormals() {
    return getBuffer(BUFFER_TYPE.BUFFER_TYPE_NORMALS);
  }
  
  public abstract int getNumObjectIndex();
  
  public abstract int getNumObjectVertex();
  
  public Buffer getTexCoords() {
    return getBuffer(BUFFER_TYPE.BUFFER_TYPE_TEXTURE_COORD);
  }
  
  public Buffer getVertices() {
    return getBuffer(BUFFER_TYPE.BUFFER_TYPE_VERTEX);
  }
  
  public enum BUFFER_TYPE {
    BUFFER_TYPE_INDICES, BUFFER_TYPE_NORMALS, BUFFER_TYPE_TEXTURE_COORD, BUFFER_TYPE_VERTEX;
    
    static {
      BUFFER_TYPE_NORMALS = new BUFFER_TYPE("BUFFER_TYPE_NORMALS", 2);
      BUFFER_TYPE bUFFER_TYPE = new BUFFER_TYPE("BUFFER_TYPE_INDICES", 3);
      BUFFER_TYPE_INDICES = bUFFER_TYPE;
      $VALUES = new BUFFER_TYPE[] { BUFFER_TYPE_VERTEX, BUFFER_TYPE_TEXTURE_COORD, BUFFER_TYPE_NORMALS, bUFFER_TYPE };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\MeshObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */