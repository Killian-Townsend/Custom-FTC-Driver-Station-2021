package org.firstinspires.ftc.robotcore.internal.network;

public class StartResult {
  private int startCount;
  
  private WifiStartStoppable startStoppable;
  
  public StartResult() {
    this(null, 0);
  }
  
  public StartResult(WifiStartStoppable paramWifiStartStoppable, int paramInt) {
    this.startStoppable = paramWifiStartStoppable;
    this.startCount = paramInt;
  }
  
  public void decrementStartCount() {
    this.startCount--;
  }
  
  public int getStartCount() {
    return this.startCount;
  }
  
  public WifiStartStoppable getStartStoppable() {
    return this.startStoppable;
  }
  
  public void incrementStartCount() {
    this.startCount++;
  }
  
  public void setStartCount(int paramInt) {
    this.startCount = paramInt;
  }
  
  public void setStartStoppable(WifiStartStoppable paramWifiStartStoppable) {
    this.startStoppable = paramWifiStartStoppable;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\StartResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */