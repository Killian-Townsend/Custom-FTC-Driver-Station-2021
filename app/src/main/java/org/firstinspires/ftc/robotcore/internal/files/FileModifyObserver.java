package org.firstinspires.ftc.robotcore.internal.files;

import java.io.File;

public class FileModifyObserver {
  public static final String TAG = FileModifyObserver.class.getSimpleName();
  
  protected RecursiveFileObserver directoryObserver;
  
  protected RecursiveFileObserver fileObserver;
  
  protected Listener listener;
  
  protected File monitoredFile;
  
  public FileModifyObserver(final File monitoredFile, final Listener listener) {
    this.monitoredFile = monitoredFile;
    this.listener = listener;
    this.directoryObserver = new RecursiveFileObserver(monitoredFile.getParentFile(), 960, RecursiveFileObserver.Mode.NONRECURSVIVE, new RecursiveFileObserver.Listener() {
          public void onEvent(int param1Int, File param1File) {
            if (param1File.getName().equals(monitoredFile.getName())) {
              if ((param1Int & 0x180) != 0) {
                FileModifyObserver.this.fileObserver.stopWatching();
                FileModifyObserver.this.fileObserver.startWatching();
                listener.onFileChanged(param1Int, param1File);
                return;
              } 
              if ((param1Int & 0x240) != 0)
                FileModifyObserver.this.fileObserver.stopWatching(); 
            } 
          }
        });
    RecursiveFileObserver recursiveFileObserver = new RecursiveFileObserver(monitoredFile, 2, RecursiveFileObserver.Mode.NONRECURSVIVE, new RecursiveFileObserver.Listener() {
          public void onEvent(int param1Int, File param1File) {
            if ((param1Int & 0x2) != 0)
              listener.onFileChanged(param1Int, param1File); 
          }
        });
    this.fileObserver = recursiveFileObserver;
    recursiveFileObserver.startWatching();
    this.directoryObserver.startWatching();
  }
  
  public void close() {
    this.directoryObserver.stopWatching();
    this.fileObserver.stopWatching();
  }
  
  public static interface Listener {
    void onFileChanged(int param1Int, File param1File);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\files\FileModifyObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */