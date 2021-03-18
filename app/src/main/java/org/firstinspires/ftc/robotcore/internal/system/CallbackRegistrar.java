package org.firstinspires.ftc.robotcore.internal.system;

import com.qualcomm.robotcore.util.WeakReferenceSet;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.external.Consumer;

public class CallbackRegistrar<T> {
  protected final WeakReferenceSet<T> callbacks = new WeakReferenceSet();
  
  public void callbacksDo(Consumer<T> paramConsumer) {
    synchronized (getCallbacksLock()) {
      Iterator iterator = this.callbacks.iterator();
      while (iterator.hasNext())
        paramConsumer.accept(iterator.next()); 
      return;
    } 
  }
  
  protected Object getCallbacksLock() {
    return this.callbacks;
  }
  
  public void registerCallback(T paramT) {
    synchronized (getCallbacksLock()) {
      this.callbacks.add(paramT);
      return;
    } 
  }
  
  public void unregisterCallback(T paramT) {
    synchronized (getCallbacksLock()) {
      this.callbacks.remove(paramT);
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\CallbackRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */