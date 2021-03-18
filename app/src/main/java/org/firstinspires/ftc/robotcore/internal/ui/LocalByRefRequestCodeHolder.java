package org.firstinspires.ftc.robotcore.internal.ui;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalByRefRequestCodeHolder<T> {
  public static final String TAG = LocalByRefRequestCodeHolder.class.getSimpleName();
  
  private static final Map<Integer, LocalByRefRequestCodeHolder> mapRequestCodeToHolder;
  
  protected static final AtomicInteger requestCodeGenerator = new AtomicInteger(1000000);
  
  protected int actualRequestCode;
  
  protected T target;
  
  protected int userRequestCode;
  
  static {
    mapRequestCodeToHolder = new ConcurrentHashMap<Integer, LocalByRefRequestCodeHolder>();
  }
  
  public LocalByRefRequestCodeHolder(int paramInt, T paramT) {
    int i = requestCodeGenerator.getAndIncrement();
    this.actualRequestCode = i;
    this.userRequestCode = paramInt;
    this.target = paramT;
    mapRequestCodeToHolder.put(Integer.valueOf(i), this);
  }
  
  public static LocalByRefRequestCodeHolder from(int paramInt) {
    return mapRequestCodeToHolder.get(Integer.valueOf(paramInt));
  }
  
  public int getActualRequestCode() {
    return this.actualRequestCode;
  }
  
  public T getTargetAndForget() {
    mapRequestCodeToHolder.remove(Integer.valueOf(this.actualRequestCode));
    return this.target;
  }
  
  public int getUserRequestCode() {
    return this.userRequestCode;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\ui\LocalByRefRequestCodeHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */