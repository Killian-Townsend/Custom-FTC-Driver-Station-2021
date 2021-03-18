package org.firstinspires.ftc.robotcore.internal.files;

import android.os.FileObserver;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class RecursiveFileObserver {
  public static final int ACCESS = 1;
  
  public static final int ALL_FILE_OBSERVER_EVENTS = 4095;
  
  public static final int ATTRIB = 4;
  
  public static final int CLOSE_NOWRITE = 16;
  
  public static final int CLOSE_WRITE = 8;
  
  public static final int CREATE = 256;
  
  public static final int DELETE = 512;
  
  public static final int DELETE_SELF = 1024;
  
  public static final int IN_IGNORED = 32768;
  
  public static final int IN_Q_OVERFLOW = 16384;
  
  public static final int IN_UNMOUNT = 8192;
  
  public static final int MODIFY = 2;
  
  public static final int MOVED_FROM = 64;
  
  public static final int MOVED_TO = 128;
  
  public static final int MOVE_SELF = 2048;
  
  public static final int OPEN = 32;
  
  public static final String TAG = "FileSystemObserver";
  
  protected final Listener listener;
  
  protected final int mask;
  
  protected final Mode mode;
  
  protected final Map<String, SingleDirOrFileObserver> observers = new HashMap<String, SingleDirOrFileObserver>();
  
  protected final String rootPath;
  
  public RecursiveFileObserver(File paramFile, int paramInt, Mode paramMode, Listener paramListener) {
    this(paramFile.getAbsolutePath(), paramInt, paramMode, paramListener);
  }
  
  public RecursiveFileObserver(String paramString, int paramInt, Mode paramMode, Listener paramListener) {
    this.rootPath = paramString;
    this.mask = paramInt;
    this.mode = paramMode;
    this.listener = paramListener;
  }
  
  protected static boolean isWatchableDirectory(File paramFile) {
    return (paramFile.isDirectory() && !paramFile.getName().equals(".") && !paramFile.getName().equals(".."));
  }
  
  protected void notify(int paramInt, File paramFile) {
    this.listener.onEvent(paramInt & 0xFFF, paramFile);
  }
  
  public void startWatching() {
    Stack<String> stack = new Stack();
    stack.push(this.rootPath);
    while (!stack.empty()) {
      String str = stack.pop();
      startWatching(str.equals(this.rootPath), str);
      if (this.mode == Mode.RECURSIVE) {
        File[] arrayOfFile = (new File(str)).listFiles();
        if (arrayOfFile != null) {
          int j = arrayOfFile.length;
          for (int i = 0; i < j; i++) {
            File file = arrayOfFile[i];
            if (isWatchableDirectory(file))
              stack.push(file.getAbsolutePath()); 
          } 
        } 
      } 
    } 
  }
  
  protected void startWatching(boolean paramBoolean, String paramString) {
    synchronized (this.observers) {
      stopWatching(paramString);
      SingleDirOrFileObserver singleDirOrFileObserver = new SingleDirOrFileObserver(paramBoolean, paramString);
      singleDirOrFileObserver.startWatching();
      this.observers.put(paramString, singleDirOrFileObserver);
      return;
    } 
  }
  
  public void stopWatching() {
    synchronized (this.observers) {
      Iterator<SingleDirOrFileObserver> iterator = this.observers.values().iterator();
      while (iterator.hasNext())
        ((SingleDirOrFileObserver)iterator.next()).stopWatching(); 
      this.observers.clear();
      return;
    } 
  }
  
  protected void stopWatching(String paramString) {
    synchronized (this.observers) {
      SingleDirOrFileObserver singleDirOrFileObserver = this.observers.remove(paramString);
      if (singleDirOrFileObserver != null)
        singleDirOrFileObserver.stopWatching(); 
      return;
    } 
  }
  
  protected class FileObserverListener implements FileObserverManager.Listener {
    public void onEvent(int param1Int, String param1String) {
      File file;
      if (param1String == null) {
        file = new File(RecursiveFileObserver.this.rootPath);
      } else {
        file = new File(RecursiveFileObserver.this.rootPath, (String)file);
      } 
      RecursiveFileObserver.this.notify(param1Int, file);
    }
  }
  
  public static interface Listener {
    void onEvent(int param1Int, File param1File);
  }
  
  public enum Mode {
    NONRECURSVIVE, RECURSIVE;
    
    static {
      Mode mode = new Mode("NONRECURSVIVE", 1);
      NONRECURSVIVE = mode;
      $VALUES = new Mode[] { RECURSIVE, mode };
    }
  }
  
  protected class SingleDirOrFileObserver implements FileObserverManager.Listener {
    protected final FileObserver fileObserver;
    
    protected final boolean isRoot;
    
    protected final String thisPath;
    
    public SingleDirOrFileObserver(boolean param1Boolean, String param1String) {
      this.fileObserver = FileObserverManager.from(param1String, RecursiveFileObserver.this.mask | 0x100 | 0x400, this);
      this.isRoot = param1Boolean;
      this.thisPath = param1String;
    }
    
    public void onEvent(int param1Int, String param1String) {
      File file;
      if (param1String == null || param1String.isEmpty()) {
        file = new File(this.thisPath);
      } else {
        file = new File(this.thisPath, (String)file);
      } 
      int i = param1Int & 0xFFF;
      if (i != 256) {
        if (i != 1024)
          RecursiveFileObserver.this.notify(param1Int, file); 
        RecursiveFileObserver.this.stopWatching(this.thisPath);
        if (this.isRoot) {
          RecursiveFileObserver.this.notify(param1Int, file);
          return;
        } 
      } else {
        if (RecursiveFileObserver.this.mode == RecursiveFileObserver.Mode.RECURSIVE && RecursiveFileObserver.isWatchableDirectory(file))
          RecursiveFileObserver.this.startWatching(false, file.getAbsolutePath()); 
        RecursiveFileObserver.this.notify(param1Int, file);
      } 
    }
    
    public void startWatching() {
      this.fileObserver.startWatching();
    }
    
    public void stopWatching() {
      this.fileObserver.stopWatching();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\files\RecursiveFileObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */