package org.java_websocket.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
  private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
  
  private final AtomicInteger threadNumber = new AtomicInteger(1);
  
  private final String threadPrefix;
  
  public NamedThreadFactory(String paramString) {
    this.threadPrefix = paramString;
  }
  
  public Thread newThread(Runnable paramRunnable) {
    paramRunnable = this.defaultThreadFactory.newThread(paramRunnable);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.threadPrefix);
    stringBuilder.append("-");
    stringBuilder.append(this.threadNumber);
    paramRunnable.setName(stringBuilder.toString());
    return (Thread)paramRunnable;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocke\\util\NamedThreadFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */