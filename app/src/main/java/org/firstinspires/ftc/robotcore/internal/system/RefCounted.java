package org.firstinspires.ftc.robotcore.internal.system;

import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.atomic.AtomicInteger;

public class RefCounted {
  public static TraceLevel currentTraceLevel = TraceLevel.Normal;
  
  public static TraceLevel defaultTraceLevel = TraceLevel.Normal;
  
  public static boolean traceCtor = true;
  
  public static boolean traceDtor = true;
  
  public static boolean traceRefCount = true;
  
  protected boolean destroyed = false;
  
  protected final Object lock = new Object();
  
  protected AtomicInteger refCount = new AtomicInteger(1);
  
  protected TraceLevel traceLevel;
  
  protected RefCounted() {
    this(defaultTraceLevel);
  }
  
  protected RefCounted(TraceLevel paramTraceLevel) {
    this.traceLevel = paramTraceLevel;
    if (traceCtor())
      RobotLog.vv(getTag(), "construct(0x%08x)", new Object[] { Integer.valueOf(hashCode()) }); 
  }
  
  public void addRef() {
    int i = this.refCount.incrementAndGet();
    if (traceRefCount())
      doTraceRefCnt(Misc.formatInvariant("ref:add(after=%d)", new Object[] { Integer.valueOf(i) })); 
  }
  
  protected void destructor() {}
  
  protected final void doLockAndDestruct() {
    synchronized (this.lock) {
      if (!this.destroyed) {
        this.destroyed = true;
        preDestructor();
        if (traceDtor())
          RobotLog.vv(getTag(), "destroy(%s)", new Object[] { getTraceIdentifier() }); 
        destructor();
        postDestructor();
      } 
      return;
    } 
  }
  
  protected void doTraceRefCnt(String paramString) {
    paramString = AppUtil.getInstance().findCaller(Misc.formatInvariant("%s(%s)", new Object[] { paramString, getTraceIdentifier() }), 1);
    RobotLog.vv(getTag(), paramString);
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
  
  public String getTraceIdentifier() {
    return Misc.formatInvariant("hash=0x%08x", new Object[] { Integer.valueOf(hashCode()) });
  }
  
  protected boolean isTraceArmed() {
    return (this.traceLevel.value <= currentTraceLevel.value && currentTraceLevel.value != TraceLevel.None.value);
  }
  
  protected void postDestructor() {}
  
  protected void preDestructor() {}
  
  public int releaseRef() {
    int i = this.refCount.decrementAndGet();
    if (traceRefCount())
      doTraceRefCnt(Misc.formatInvariant("ref:release(after=%d)", new Object[] { Integer.valueOf(i) })); 
    if (i == 0)
      doLockAndDestruct(); 
    return i;
  }
  
  protected boolean traceCtor() {
    return (traceCtor && isTraceArmed());
  }
  
  protected boolean traceDtor() {
    return (traceDtor && isTraceArmed());
  }
  
  protected boolean traceRefCount() {
    return (traceRefCount && isTraceArmed() && this.traceLevel.traceRefCount);
  }
  
  public static class TraceLevel {
    public static final TraceLevel None = new TraceLevel(2147483647);
    
    public static final TraceLevel Normal = new TraceLevel(10);
    
    public static final TraceLevel Verbose = new TraceLevel(20);
    
    public static final TraceLevel VeryVerbose = new TraceLevel(30);
    
    public final boolean traceRefCount;
    
    public final int value;
    
    static {
    
    }
    
    public TraceLevel(int param1Int) {
      this(param1Int, false);
    }
    
    public TraceLevel(int param1Int, boolean param1Boolean) {
      this.value = param1Int;
      this.traceRefCount = param1Boolean;
    }
    
    public TraceLevel traceRefCnt() {
      return new TraceLevel(this.value, true);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\RefCounted.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */