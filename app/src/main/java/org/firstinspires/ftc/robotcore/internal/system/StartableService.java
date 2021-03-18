package org.firstinspires.ftc.robotcore.internal.system;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.Closeable;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.external.Supplier;

public abstract class StartableService extends Service {
  protected Closeable instance;
  
  protected final Supplier<Closeable> instantiator;
  
  protected StartableService(Supplier<Closeable> paramSupplier) {
    this.instantiator = paramSupplier;
  }
  
  public abstract String getTag();
  
  public IBinder onBind(Intent paramIntent) {
    RobotLog.vv(getTag(), "onBind()");
    return null;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    RobotLog.vv(getTag(), "onConfigurationChanged()");
  }
  
  public void onCreate() {
    RobotLog.vv(getTag(), "onCreate()");
  }
  
  public void onDestroy() {
    RobotLog.vv(getTag(), "onDestroy()");
    Closeable closeable = this.instance;
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException iOException) {
        RobotLog.ee(getTag(), iOException, "exception during close; ignored");
      } 
      this.instance = null;
    } 
  }
  
  public void onLowMemory() {
    RobotLog.vv(getTag(), "onLowMemory()");
  }
  
  public void onRebind(Intent paramIntent) {
    RobotLog.vv(getTag(), "onRebind()");
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
    RobotLog.vv(getTag(), "onStartCommand() intent=%s flags=0x%x startId=%d", new Object[] { paramIntent, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    this.instance = (Closeable)this.instantiator.get();
    return 2;
  }
  
  public void onTaskRemoved(Intent paramIntent) {
    RobotLog.vv(getTag(), "onTaskRemoved()");
  }
  
  public void onTrimMemory(int paramInt) {
    RobotLog.vv(getTag(), "onTrimMemory()");
  }
  
  public boolean onUnbind(Intent paramIntent) {
    RobotLog.vv(getTag(), "onUnbind()");
    return super.onUnbind(paramIntent);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\StartableService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */