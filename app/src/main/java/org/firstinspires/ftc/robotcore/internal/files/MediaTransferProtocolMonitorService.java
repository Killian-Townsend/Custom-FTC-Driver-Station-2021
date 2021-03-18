package org.firstinspires.ftc.robotcore.internal.files;

import java.io.Closeable;
import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.internal.system.StartableService;

public class MediaTransferProtocolMonitorService extends StartableService {
  public static final String TAG = "MTPMonitorService";
  
  public MediaTransferProtocolMonitorService() {
    super(new Supplier<Closeable>() {
          public Closeable get() {
            MediaTransferProtocolMonitor mediaTransferProtocolMonitor = MediaTransferProtocolMonitor.getInstance();
            mediaTransferProtocolMonitor.start();
            return mediaTransferProtocolMonitor;
          }
        });
  }
  
  public String getTag() {
    return "MTPMonitorService";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\files\MediaTransferProtocolMonitorService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */