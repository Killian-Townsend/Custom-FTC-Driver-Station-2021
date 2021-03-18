package org.firstinspires.ftc.robotserver.internal.webserver;

import com.google.gson.annotations.SerializedName;
import com.qualcomm.robotcore.util.Device;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;

public class PingResponse {
  private static final Comparator<ConnectedHttpDevice> CONNECTED_HTTP_DEVICE_COMPARATOR = new Comparator<ConnectedHttpDevice>() {
      public int compare(ConnectedHttpDevice param1ConnectedHttpDevice1, ConnectedHttpDevice param1ConnectedHttpDevice2) {
        int j = param1ConnectedHttpDevice1.machineName.compareToIgnoreCase(param1ConnectedHttpDevice2.machineName);
        int i = j;
        if (j == 0)
          i = param1ConnectedHttpDevice1.currentPage.compareToIgnoreCase(param1ConnectedHttpDevice2.currentPage); 
        return i;
      }
    };
  
  public static final long EXPIRATION_DURATION_SECONDS = 3L;
  
  private final transient List<ConnectedHttpDevice> connectedDevices = new LinkedList<ConnectedHttpDevice>();
  
  private final transient Object pingLock = new Object();
  
  private final transient List<Long> pingTimes = new LinkedList<Long>();
  
  private final String serial = Device.getSerialNumberOrUnknown();
  
  @SerializedName("connectedDevices")
  private List<ConnectedHttpDevice> sortedConnectedDevices = new ArrayList<ConnectedHttpDevice>();
  
  private void updateSortedConnectedDevices() {
    synchronized (this.pingLock) {
      ArrayList<ConnectedHttpDevice> arrayList = new ArrayList<ConnectedHttpDevice>(this.connectedDevices);
      this.sortedConnectedDevices = arrayList;
      Collections.sort(arrayList, CONNECTED_HTTP_DEVICE_COMPARATOR);
      return;
    } 
  }
  
  public void noteDevicePing(ConnectedHttpDevice paramConnectedHttpDevice) {
    synchronized (this.pingLock) {
      long l = System.nanoTime();
      int i = this.connectedDevices.indexOf(paramConnectedHttpDevice);
      if (i != -1) {
        this.pingTimes.remove(i);
        this.connectedDevices.remove(i);
      } 
      this.pingTimes.add(Long.valueOf(l));
      this.connectedDevices.add(paramConnectedHttpDevice);
      return;
    } 
  }
  
  public void removeOldPings() {
    synchronized (this.pingLock) {
      long l1 = System.nanoTime();
      long l2 = TimeUnit.SECONDS.toNanos(3L);
      while (!this.pingTimes.isEmpty() && ((Long)this.pingTimes.get(0)).longValue() < l1 - l2) {
        this.pingTimes.remove(0);
        this.connectedDevices.remove(0);
      } 
      return;
    } 
  }
  
  public String toJson() {
    updateSortedConnectedDevices();
    return SimpleGson.getInstance().toJson(this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\PingResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */