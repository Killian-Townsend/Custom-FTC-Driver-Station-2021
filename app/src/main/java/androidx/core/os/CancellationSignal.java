package androidx.core.os;

public final class CancellationSignal {
  private boolean mCancelInProgress;
  
  private Object mCancellationSignalObj;
  
  private boolean mIsCanceled;
  
  private OnCancelListener mOnCancelListener;
  
  private void waitForCancelFinishedLocked() {
    while (true) {
      if (this.mCancelInProgress) {
        try {
          wait();
        } catch (InterruptedException interruptedException) {}
        continue;
      } 
      return;
    } 
  }
  
  public void cancel() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mIsCanceled : Z
    //   6: ifeq -> 12
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: aload_0
    //   13: iconst_1
    //   14: putfield mIsCanceled : Z
    //   17: aload_0
    //   18: iconst_1
    //   19: putfield mCancelInProgress : Z
    //   22: aload_0
    //   23: getfield mOnCancelListener : Landroidx/core/os/CancellationSignal$OnCancelListener;
    //   26: astore_1
    //   27: aload_0
    //   28: getfield mCancellationSignalObj : Ljava/lang/Object;
    //   31: astore_2
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: ifnull -> 47
    //   38: aload_1
    //   39: invokeinterface onCancel : ()V
    //   44: goto -> 47
    //   47: aload_2
    //   48: ifnull -> 89
    //   51: getstatic android/os/Build$VERSION.SDK_INT : I
    //   54: bipush #16
    //   56: if_icmplt -> 89
    //   59: aload_2
    //   60: checkcast android/os/CancellationSignal
    //   63: invokevirtual cancel : ()V
    //   66: goto -> 89
    //   69: aload_0
    //   70: monitorenter
    //   71: aload_0
    //   72: iconst_0
    //   73: putfield mCancelInProgress : Z
    //   76: aload_0
    //   77: invokevirtual notifyAll : ()V
    //   80: aload_0
    //   81: monitorexit
    //   82: aload_1
    //   83: athrow
    //   84: astore_1
    //   85: aload_0
    //   86: monitorexit
    //   87: aload_1
    //   88: athrow
    //   89: aload_0
    //   90: monitorenter
    //   91: aload_0
    //   92: iconst_0
    //   93: putfield mCancelInProgress : Z
    //   96: aload_0
    //   97: invokevirtual notifyAll : ()V
    //   100: aload_0
    //   101: monitorexit
    //   102: return
    //   103: astore_1
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_1
    //   107: athrow
    //   108: astore_1
    //   109: aload_0
    //   110: monitorexit
    //   111: aload_1
    //   112: athrow
    //   113: astore_1
    //   114: goto -> 69
    // Exception table:
    //   from	to	target	type
    //   2	11	108	finally
    //   12	34	108	finally
    //   38	44	113	finally
    //   51	66	113	finally
    //   71	82	84	finally
    //   85	87	84	finally
    //   91	102	103	finally
    //   104	106	103	finally
    //   109	111	108	finally
  }
  
  public Object getCancellationSignalObject() {
    // Byte code:
    //   0: getstatic android/os/Build$VERSION.SDK_INT : I
    //   3: bipush #16
    //   5: if_icmpge -> 10
    //   8: aconst_null
    //   9: areturn
    //   10: aload_0
    //   11: monitorenter
    //   12: aload_0
    //   13: getfield mCancellationSignalObj : Ljava/lang/Object;
    //   16: ifnonnull -> 46
    //   19: new android/os/CancellationSignal
    //   22: dup
    //   23: invokespecial <init> : ()V
    //   26: astore_1
    //   27: aload_0
    //   28: aload_1
    //   29: putfield mCancellationSignalObj : Ljava/lang/Object;
    //   32: aload_0
    //   33: getfield mIsCanceled : Z
    //   36: ifeq -> 46
    //   39: aload_1
    //   40: checkcast android/os/CancellationSignal
    //   43: invokevirtual cancel : ()V
    //   46: aload_0
    //   47: getfield mCancellationSignalObj : Ljava/lang/Object;
    //   50: astore_1
    //   51: aload_0
    //   52: monitorexit
    //   53: aload_1
    //   54: areturn
    //   55: astore_1
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_1
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   12	46	55	finally
    //   46	53	55	finally
    //   56	58	55	finally
  }
  
  public boolean isCanceled() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mIsCanceled : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	11	finally
    //   12	14	11	finally
  }
  
  public void setOnCancelListener(OnCancelListener paramOnCancelListener) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial waitForCancelFinishedLocked : ()V
    //   6: aload_0
    //   7: getfield mOnCancelListener : Landroidx/core/os/CancellationSignal$OnCancelListener;
    //   10: aload_1
    //   11: if_acmpne -> 17
    //   14: aload_0
    //   15: monitorexit
    //   16: return
    //   17: aload_0
    //   18: aload_1
    //   19: putfield mOnCancelListener : Landroidx/core/os/CancellationSignal$OnCancelListener;
    //   22: aload_0
    //   23: getfield mIsCanceled : Z
    //   26: ifeq -> 45
    //   29: aload_1
    //   30: ifnonnull -> 36
    //   33: goto -> 45
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_1
    //   39: invokeinterface onCancel : ()V
    //   44: return
    //   45: aload_0
    //   46: monitorexit
    //   47: return
    //   48: astore_1
    //   49: aload_0
    //   50: monitorexit
    //   51: aload_1
    //   52: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	48	finally
    //   17	29	48	finally
    //   36	38	48	finally
    //   45	47	48	finally
    //   49	51	48	finally
  }
  
  public void throwIfCanceled() {
    if (!isCanceled())
      return; 
    throw new OperationCanceledException();
  }
  
  public static interface OnCancelListener {
    void onCancel();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\core\os\CancellationSignal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */