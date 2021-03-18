package org.firstinspires.ftc.robotcore.internal.tfod;

import android.util.Log;
import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;

public class Timer {
  private static boolean NO_TIMERS = true;
  
  private final String timerName;
  
  private final ArrayDeque<NamedTimestampNanos> times = new ArrayDeque<NamedTimestampNanos>();
  
  public Timer(String paramString) {
    this.timerName = paramString;
  }
  
  public void end() {
    if (NO_TIMERS)
      return; 
    try {
      String str;
      NamedTimestampNanos namedTimestampNanos = this.times.removeLast();
      long l1 = System.nanoTime() - namedTimestampNanos.timestampNanos;
      long l2 = TimeUnit.MILLISECONDS.convert(l1, TimeUnit.NANOSECONDS);
      if (l2 >= 200L) {
        str = "Timer: section \"%s\" took %d ms (%d ns) latency level 5";
      } else if (l2 >= 150L) {
        str = "Timer: section \"%s\" took %d ms (%d ns) latency level 4";
      } else if (l2 >= 100L) {
        str = "Timer: section \"%s\" took %d ms (%d ns) latency level 3";
      } else if (l2 >= 50L) {
        str = "Timer: section \"%s\" took %d ms (%d ns) latency level 2";
      } else if (l2 >= 10L) {
        str = "Timer: section \"%s\" took %d ms (%d ns) latency level 1";
      } else {
        str = "Timer: section \"%s\" took %d ms (%d ns) latency level 0";
      } 
      Log.v(this.timerName, String.format(str, new Object[] { namedTimestampNanos.name, Long.valueOf(l2), Long.valueOf(l1) }));
      return;
    } catch (Exception exception) {
      Log.v(this.timerName, "timer.end failed");
      return;
    } 
  }
  
  public void start(String paramString) {
    if (NO_TIMERS)
      return; 
    this.times.addLast(new NamedTimestampNanos(paramString, System.nanoTime()));
  }
  
  private class NamedTimestampNanos {
    final String name;
    
    final long timestampNanos;
    
    NamedTimestampNanos(String param1String, long param1Long) {
      this.name = param1String;
      this.timestampNanos = param1Long;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\Timer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */