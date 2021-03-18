package com.vuforia;

import java.nio.ByteBuffer;

public class Mesh {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected Mesh(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(Mesh paramMesh) {
    return (paramMesh == null) ? 0L : paramMesh.swigCPtr;
  }
  
  protected void delete() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield swigCPtr : J
    //   6: lconst_0
    //   7: lcmp
    //   8: ifeq -> 35
    //   11: aload_0
    //   12: getfield swigCMemOwn : Z
    //   15: ifeq -> 30
    //   18: aload_0
    //   19: iconst_0
    //   20: putfield swigCMemOwn : Z
    //   23: aload_0
    //   24: getfield swigCPtr : J
    //   27: invokestatic delete_Mesh : (J)V
    //   30: aload_0
    //   31: lconst_0
    //   32: putfield swigCPtr : J
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	38	finally
    //   30	35	38	finally
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof Mesh;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((Mesh)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public ByteBuffer getNormals() {
    return VuforiaJNI.Mesh_getNormals(this.swigCPtr, this);
  }
  
  public int getNumTriangles() {
    return VuforiaJNI.Mesh_getNumTriangles(this.swigCPtr, this);
  }
  
  public int getNumVertices() {
    return VuforiaJNI.Mesh_getNumVertices(this.swigCPtr, this);
  }
  
  public ByteBuffer getPositions() {
    return VuforiaJNI.Mesh_getPositions(this.swigCPtr, this);
  }
  
  public ByteBuffer getTriangles() {
    return VuforiaJNI.Mesh_getTriangles(this.swigCPtr, this);
  }
  
  public ByteBuffer getUVs() {
    return VuforiaJNI.Mesh_getUVs(this.swigCPtr, this);
  }
  
  public boolean hasNormals() {
    return VuforiaJNI.Mesh_hasNormals(this.swigCPtr, this);
  }
  
  public boolean hasPositions() {
    return VuforiaJNI.Mesh_hasPositions(this.swigCPtr, this);
  }
  
  public boolean hasUVs() {
    return VuforiaJNI.Mesh_hasUVs(this.swigCPtr, this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\Mesh.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */