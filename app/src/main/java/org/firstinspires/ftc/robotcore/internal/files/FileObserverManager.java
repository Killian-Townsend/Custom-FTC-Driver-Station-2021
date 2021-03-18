package org.firstinspires.ftc.robotcore.internal.files;

import android.os.FileObserver;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.WeakReferenceSet;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FileObserverManager {
  public static final String TAG = FileObserverManager.class.getSimpleName();
  
  protected static final Map<String, WeakReference<OmniscientObserver>> omnicientObservers = new HashMap<String, WeakReference<OmniscientObserver>>();
  
  public static FileObserver from(String paramString, int paramInt, Listener paramListener) {
    synchronized (omnicientObservers) {
      String str;
      OmniscientObserver omniscientObserver;
      File file = new File(paramString);
      try {
        str = file.getCanonicalPath();
      } catch (IOException iOException) {
        RobotLog.ww(TAG, "canonical path failed; using absolute instead: abspath=%s", new Object[] { file.getAbsolutePath() });
        str = file.getAbsolutePath();
      } 
      WeakReference<OmniscientObserver> weakReference1 = omnicientObservers.get(str);
      if (weakReference1 != null) {
        OmniscientObserver omniscientObserver1 = weakReference1.get();
      } else {
        weakReference1 = null;
      } 
      WeakReference<OmniscientObserver> weakReference2 = weakReference1;
      if (weakReference1 == null) {
        omniscientObserver = new OmniscientObserver(str);
        omnicientObservers.put(str, new WeakReference<OmniscientObserver>(omniscientObserver));
      } 
      return new FakeObserver(omniscientObserver, paramInt, paramListener);
    } 
  }
  
  protected static class FakeObserver extends FileObserver {
    protected boolean isWatching;
    
    protected final FileObserverManager.Listener listener;
    
    protected final int mask;
    
    protected final FileObserverManager.OmniscientObserver omniscientObserver;
    
    protected FakeObserver(FileObserverManager.OmniscientObserver param1OmniscientObserver, int param1Int, FileObserverManager.Listener param1Listener) {
      super("/dev/null", 0);
      this.omniscientObserver = param1OmniscientObserver;
      this.mask = param1Int;
      this.listener = param1Listener;
      this.isWatching = false;
      param1OmniscientObserver.addFakeObserver(this);
    }
    
    protected void finalize() {
      this.omniscientObserver.removeFakeObserver(this);
      super.finalize();
    }
    
    public void onEvent(int param1Int, String param1String) {
      if ((this.mask & param1Int) != 0)
        this.listener.onEvent(param1Int, param1String); 
    }
    
    public void startWatching() {
      this.omniscientObserver.startWatching(this);
    }
    
    public void stopWatching() {
      this.omniscientObserver.stopWatching(this);
    }
  }
  
  public static interface Listener {
    void onEvent(int param1Int, String param1String);
  }
  
  protected static class OmniscientObserver {
    protected static final int defaultMask = 4092;
    
    protected final WeakReferenceSet<FileObserverManager.FakeObserver> fakeObservers = new WeakReferenceSet();
    
    protected FileObserver fileObserver;
    
    protected final String inodePath;
    
    protected int mask;
    
    protected final AtomicInteger startCount = new AtomicInteger(0);
    
    public OmniscientObserver(String param1String) {
      this.inodePath = param1String;
      this.mask = 4092;
      this.fileObserver = newFileObserver(param1String, 4092);
    }
    
    public void addFakeObserver(FileObserverManager.FakeObserver param1FakeObserver) {
      synchronized (this.fakeObservers) {
        this.fakeObservers.add(param1FakeObserver);
        if ((param1FakeObserver.mask & this.mask) != 0) {
          boolean bool;
          int i = param1FakeObserver.mask | this.mask;
          if (this.startCount.get() > 0) {
            bool = true;
          } else {
            bool = false;
          } 
          if (bool) {
            RobotLog.ww(FileObserverManager.TAG, "upgrading mask: path=%s old=0x%08x new=0x%08x: might possibly miss event", new Object[] { this.inodePath, Integer.valueOf(this.mask), Integer.valueOf(i) });
            this.fileObserver.stopWatching();
          } 
          FileObserver fileObserver = newFileObserver(this.inodePath, i);
          this.fileObserver = fileObserver;
          if (bool)
            fileObserver.startWatching(); 
        } 
        return;
      } 
    }
    
    protected void finalize() throws Throwable {
      synchronized (FileObserverManager.omnicientObservers) {
        FileObserverManager.omnicientObservers.remove(this.inodePath);
        super.finalize();
        return;
      } 
    }
    
    protected FileObserver newFileObserver(String param1String, int param1Int) {
      return new FileObserver(param1String, param1Int) {
          public void onEvent(int param2Int, String param2String) {
            FileObserverManager.OmniscientObserver.this.onEvent(param2Int, param2String);
          }
        };
    }
    
    protected void onEvent(int param1Int, String param1String) {
      synchronized (this.fakeObservers) {
        Iterator<FileObserverManager.FakeObserver> iterator = this.fakeObservers.iterator();
        while (iterator.hasNext())
          ((FileObserverManager.FakeObserver)iterator.next()).onEvent(param1Int, param1String); 
        return;
      } 
    }
    
    public void removeFakeObserver(FileObserverManager.FakeObserver param1FakeObserver) {
      synchronized (this.fakeObservers) {
        stopWatching(param1FakeObserver);
        this.fakeObservers.remove(param1FakeObserver);
        return;
      } 
    }
    
    protected void startWatching(FileObserverManager.FakeObserver param1FakeObserver) {
      synchronized (this.fakeObservers) {
        if (!param1FakeObserver.isWatching) {
          param1FakeObserver.isWatching = true;
          if (this.startCount.getAndIncrement() == 0)
            this.fileObserver.startWatching(); 
        } 
        return;
      } 
    }
    
    protected void stopWatching(FileObserverManager.FakeObserver param1FakeObserver) {
      synchronized (this.fakeObservers) {
        if (param1FakeObserver.isWatching) {
          if (this.startCount.decrementAndGet() == 0)
            this.fileObserver.stopWatching(); 
          param1FakeObserver.isWatching = false;
        } 
        return;
      } 
    }
  }
  
  class null extends FileObserver {
    null(String param1String, int param1Int) {
      super(param1String, param1Int);
    }
    
    public void onEvent(int param1Int, String param1String) {
      this.this$0.onEvent(param1Int, param1String);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\files\FileObserverManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */