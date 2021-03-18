package org.firstinspires.ftc.robotcore.internal.system;

public class FrequentErrorReporter<T> {
  protected T value;
  
  public FrequentErrorReporter() {
    reset();
  }
  
  public void aa(T paramT, String paramString1, String paramString2, Object... paramVarArgs) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic assertNotNull : (Ljava/lang/Object;)V
    //   6: aload_0
    //   7: getfield value : Ljava/lang/Object;
    //   10: ifnull -> 24
    //   13: aload_0
    //   14: getfield value : Ljava/lang/Object;
    //   17: aload_1
    //   18: invokevirtual equals : (Ljava/lang/Object;)Z
    //   21: ifne -> 36
    //   24: aload_0
    //   25: aload_1
    //   26: putfield value : Ljava/lang/Object;
    //   29: aload_2
    //   30: aload_3
    //   31: aload #4
    //   33: invokestatic aa : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	39	finally
    //   24	36	39	finally
  }
  
  public void dd(T paramT, String paramString1, String paramString2, Object... paramVarArgs) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic assertNotNull : (Ljava/lang/Object;)V
    //   6: aload_0
    //   7: getfield value : Ljava/lang/Object;
    //   10: ifnull -> 24
    //   13: aload_0
    //   14: getfield value : Ljava/lang/Object;
    //   17: aload_1
    //   18: invokevirtual equals : (Ljava/lang/Object;)Z
    //   21: ifne -> 36
    //   24: aload_0
    //   25: aload_1
    //   26: putfield value : Ljava/lang/Object;
    //   29: aload_2
    //   30: aload_3
    //   31: aload #4
    //   33: invokestatic dd : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	39	finally
    //   24	36	39	finally
  }
  
  public void ee(T paramT, String paramString1, String paramString2, Object... paramVarArgs) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic assertNotNull : (Ljava/lang/Object;)V
    //   6: aload_0
    //   7: getfield value : Ljava/lang/Object;
    //   10: ifnull -> 24
    //   13: aload_0
    //   14: getfield value : Ljava/lang/Object;
    //   17: aload_1
    //   18: invokevirtual equals : (Ljava/lang/Object;)Z
    //   21: ifne -> 36
    //   24: aload_0
    //   25: aload_1
    //   26: putfield value : Ljava/lang/Object;
    //   29: aload_2
    //   30: aload_3
    //   31: aload #4
    //   33: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	39	finally
    //   24	36	39	finally
  }
  
  public void ii(T paramT, String paramString1, String paramString2, Object... paramVarArgs) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic assertNotNull : (Ljava/lang/Object;)V
    //   6: aload_0
    //   7: getfield value : Ljava/lang/Object;
    //   10: ifnull -> 24
    //   13: aload_0
    //   14: getfield value : Ljava/lang/Object;
    //   17: aload_1
    //   18: invokevirtual equals : (Ljava/lang/Object;)Z
    //   21: ifne -> 36
    //   24: aload_0
    //   25: aload_1
    //   26: putfield value : Ljava/lang/Object;
    //   29: aload_2
    //   30: aload_3
    //   31: aload #4
    //   33: invokestatic ii : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	39	finally
    //   24	36	39	finally
  }
  
  public void reset() {
    this.value = null;
  }
  
  public void vv(T paramT, String paramString1, String paramString2, Object... paramVarArgs) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic assertNotNull : (Ljava/lang/Object;)V
    //   6: aload_0
    //   7: getfield value : Ljava/lang/Object;
    //   10: ifnull -> 24
    //   13: aload_0
    //   14: getfield value : Ljava/lang/Object;
    //   17: aload_1
    //   18: invokevirtual equals : (Ljava/lang/Object;)Z
    //   21: ifne -> 36
    //   24: aload_0
    //   25: aload_1
    //   26: putfield value : Ljava/lang/Object;
    //   29: aload_2
    //   30: aload_3
    //   31: aload #4
    //   33: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	39	finally
    //   24	36	39	finally
  }
  
  public void ww(T paramT, String paramString1, String paramString2, Object... paramVarArgs) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic assertNotNull : (Ljava/lang/Object;)V
    //   6: aload_0
    //   7: getfield value : Ljava/lang/Object;
    //   10: ifnull -> 24
    //   13: aload_0
    //   14: getfield value : Ljava/lang/Object;
    //   17: aload_1
    //   18: invokevirtual equals : (Ljava/lang/Object;)Z
    //   21: ifne -> 36
    //   24: aload_0
    //   25: aload_1
    //   26: putfield value : Ljava/lang/Object;
    //   29: aload_2
    //   30: aload_3
    //   31: aload #4
    //   33: invokestatic ww : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	39	finally
    //   24	36	39	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\FrequentErrorReporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */