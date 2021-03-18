package org.firstinspires.ftc.robotcore.internal.hardware;

public class TimeWindow {
  protected long nsFirst;
  
  protected long nsLast;
  
  public TimeWindow() {
    clear();
  }
  
  public void clear() {
    this.nsFirst = 0L;
    this.nsLast = 0L;
  }
  
  public long getNanosecondsFirst() {
    return this.nsFirst;
  }
  
  public long getNanosecondsLast() {
    return this.nsLast;
  }
  
  public boolean isCleared() {
    return (this.nsFirst == 0L && this.nsLast == 0L);
  }
  
  public void setNanosecondsFirst(long paramLong) {
    this.nsFirst = paramLong;
  }
  
  public void setNanosecondsLast(long paramLong) {
    this.nsLast = paramLong;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\hardware\TimeWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */