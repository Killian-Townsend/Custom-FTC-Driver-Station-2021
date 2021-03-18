package com.vuforia;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ViewerParametersList implements Iterable<ViewerParameters> {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected ViewerParametersList(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  private ViewerParameters begin() {
    long l = VuforiaJNI.ViewerParametersList_begin(this.swigCPtr, this);
    return (l == 0L) ? null : new ViewerParameters(l, false);
  }
  
  private ViewerParameters end() {
    long l = VuforiaJNI.ViewerParametersList_end(this.swigCPtr, this);
    return (l == 0L) ? null : new ViewerParameters(l, false);
  }
  
  protected static long getCPtr(ViewerParametersList paramViewerParametersList) {
    return (paramViewerParametersList == null) ? 0L : paramViewerParametersList.swigCPtr;
  }
  
  public static ViewerParametersList getListForAuthoringTools() {
    return new ViewerParametersList(VuforiaJNI.ViewerParametersList_getListForAuthoringTools(), false);
  }
  
  private ViewerParameters next(ViewerParameters paramViewerParameters) {
    long l = VuforiaJNI.ViewerParametersList_next(this.swigCPtr, this, ViewerParameters.getCPtr(paramViewerParameters), paramViewerParameters);
    return (l == 0L) ? null : new ViewerParameters(l, false);
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
    //   27: invokestatic delete_ViewerParametersList : (J)V
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
  
  protected void finalize() {
    delete();
  }
  
  public ViewerParameters get(long paramLong) {
    paramLong = VuforiaJNI.ViewerParametersList_get__SWIG_0(this.swigCPtr, this, paramLong);
    return (paramLong == 0L) ? null : new ViewerParameters(paramLong, false);
  }
  
  public ViewerParameters get(String paramString1, String paramString2) {
    long l = VuforiaJNI.ViewerParametersList_get__SWIG_1(this.swigCPtr, this, paramString1, paramString2);
    return (l == 0L) ? null : new ViewerParameters(l, false);
  }
  
  public Iterator<ViewerParameters> iterator() {
    return new VPIterator();
  }
  
  public void setSDKFilter(String paramString) {
    VuforiaJNI.ViewerParametersList_setSDKFilter(this.swigCPtr, this, paramString);
  }
  
  public long size() {
    return VuforiaJNI.ViewerParametersList_size(this.swigCPtr, this);
  }
  
  private class VPIterator implements Iterator<ViewerParameters> {
    private ViewerParameters next = null;
    
    VPIterator() {
      if (ViewerParametersList.this.size() > 0L)
        this.next = ViewerParametersList.this.begin(); 
    }
    
    public boolean hasNext() {
      return (this.next != null);
    }
    
    public ViewerParameters next() throws NoSuchElementException {
      ViewerParameters viewerParameters = this.next;
      if (viewerParameters != null) {
        this.next = ViewerParametersList.this.next(viewerParameters);
        return viewerParameters;
      } 
      throw new NoSuchElementException();
    }
    
    public void remove() throws UnsupportedOperationException, IllegalStateException {
      throw new UnsupportedOperationException();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ViewerParametersList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */