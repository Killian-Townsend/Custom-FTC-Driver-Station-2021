package org.firstinspires.ftc.robotcore.internal.system;

import java.util.Stack;

public class Finalizer {
  static final Stack<Finalizer> cache = new Stack<Finalizer>();
  
  static int cacheSizeMax = 50;
  
  Finalizable target;
  
  static Finalizer forTarget(Finalizable paramFinalizable) {
    synchronized (cache) {
      Finalizer finalizer;
      if (cache.isEmpty()) {
        finalizer = new Finalizer();
      } else {
        finalizer = cache.pop();
      } 
      finalizer.target = paramFinalizable;
      return finalizer;
    } 
  }
  
  public static String getTag() {
    return Finalizer.class.getSimpleName();
  }
  
  public void dispose() {
    synchronized (cache) {
      if (this.target != null) {
        this.target = null;
        if (cache.size() < cacheSizeMax)
          cache.push(this); 
      } 
      return;
    } 
  }
  
  protected void finalize() throws Throwable {
    Finalizable finalizable = this.target;
    if (finalizable != null)
      finalizable.doFinalize(); 
    dispose();
    super.finalize();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\Finalizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */