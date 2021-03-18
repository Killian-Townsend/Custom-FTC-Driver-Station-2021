package com.qualcomm.robotcore.hardware;

public interface I2cDeviceSynch extends I2cDeviceSynchSimple, Engagable {
  void ensureReadWindow(ReadWindow paramReadWindow1, ReadWindow paramReadWindow2);
  
  HeartbeatAction getHeartbeatAction();
  
  int getHeartbeatInterval();
  
  ReadWindow getReadWindow();
  
  TimestampedData readTimeStamped(int paramInt1, int paramInt2, ReadWindow paramReadWindow1, ReadWindow paramReadWindow2);
  
  void setHeartbeatAction(HeartbeatAction paramHeartbeatAction);
  
  void setHeartbeatInterval(int paramInt);
  
  void setReadWindow(ReadWindow paramReadWindow);
  
  public static class HeartbeatAction {
    public final I2cDeviceSynch.ReadWindow heartbeatReadWindow;
    
    public final boolean rereadLastRead;
    
    public final boolean rewriteLastWritten;
    
    public HeartbeatAction(boolean param1Boolean1, boolean param1Boolean2, I2cDeviceSynch.ReadWindow param1ReadWindow) {
      this.rereadLastRead = param1Boolean1;
      this.rewriteLastWritten = param1Boolean2;
      this.heartbeatReadWindow = param1ReadWindow;
    }
  }
  
  public enum ReadMode {
    BALANCED, ONLY_ONCE, REPEAT;
    
    static {
      ReadMode readMode = new ReadMode("ONLY_ONCE", 2);
      ONLY_ONCE = readMode;
      $VALUES = new ReadMode[] { REPEAT, BALANCED, readMode };
    }
  }
  
  public static class ReadWindow {
    public static final int READ_REGISTER_COUNT_MAX = 26;
    
    public static final int WRITE_REGISTER_COUNT_MAX = 26;
    
    private final int creg;
    
    private final int iregFirst;
    
    private final I2cDeviceSynch.ReadMode readMode;
    
    private boolean usedForRead;
    
    public ReadWindow(int param1Int1, int param1Int2, I2cDeviceSynch.ReadMode param1ReadMode) {
      this.readMode = param1ReadMode;
      this.usedForRead = false;
      this.iregFirst = param1Int1;
      this.creg = param1Int2;
      if (param1Int2 >= 0 && param1Int2 <= 26)
        return; 
      throw new IllegalArgumentException(String.format("buffer length %d invalid; max is %d", new Object[] { Integer.valueOf(param1Int2), Integer.valueOf(26) }));
    }
    
    public boolean canBeUsedToRead() {
      return (!this.usedForRead || this.readMode != I2cDeviceSynch.ReadMode.ONLY_ONCE);
    }
    
    public boolean contains(int param1Int1, int param1Int2) {
      return containsWithSameMode(new ReadWindow(param1Int1, param1Int2, getReadMode()));
    }
    
    public boolean contains(ReadWindow param1ReadWindow) {
      boolean bool2 = false;
      if (param1ReadWindow == null)
        return false; 
      boolean bool1 = bool2;
      if (getRegisterFirst() <= param1ReadWindow.getRegisterFirst()) {
        bool1 = bool2;
        if (param1ReadWindow.getRegisterMax() <= getRegisterMax())
          bool1 = true; 
      } 
      return bool1;
    }
    
    public boolean containsWithSameMode(ReadWindow param1ReadWindow) {
      return (contains(param1ReadWindow) && getReadMode() == param1ReadWindow.getReadMode());
    }
    
    public I2cDeviceSynch.ReadMode getReadMode() {
      return this.readMode;
    }
    
    public int getRegisterCount() {
      return this.creg;
    }
    
    public int getRegisterFirst() {
      return this.iregFirst;
    }
    
    public int getRegisterMax() {
      return this.iregFirst + this.creg;
    }
    
    public boolean hasWindowBeenUsedForRead() {
      return this.usedForRead;
    }
    
    public boolean mayInitiateSwitchToReadMode() {
      return (!this.usedForRead || this.readMode == I2cDeviceSynch.ReadMode.REPEAT);
    }
    
    public void noteWindowUsedForRead() {
      this.usedForRead = true;
    }
    
    public ReadWindow readableCopy() {
      return new ReadWindow(this.iregFirst, this.creg, this.readMode);
    }
    
    public boolean sameAsIncludingMode(ReadWindow param1ReadWindow) {
      boolean bool2 = false;
      if (param1ReadWindow == null)
        return false; 
      boolean bool1 = bool2;
      if (getRegisterFirst() == param1ReadWindow.getRegisterFirst()) {
        bool1 = bool2;
        if (getRegisterCount() == param1ReadWindow.getRegisterCount()) {
          bool1 = bool2;
          if (getReadMode() == param1ReadWindow.getReadMode())
            bool1 = true; 
        } 
      } 
      return bool1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cDeviceSynch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */