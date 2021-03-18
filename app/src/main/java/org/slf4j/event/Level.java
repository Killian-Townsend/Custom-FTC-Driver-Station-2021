package org.slf4j.event;

public enum Level {
  DEBUG,
  ERROR(40, "ERROR"),
  INFO(40, "ERROR"),
  TRACE(40, "ERROR"),
  WARN(30, "WARN");
  
  private int levelInt;
  
  private String levelStr;
  
  static {
    INFO = new Level("INFO", 2, 20, "INFO");
    DEBUG = new Level("DEBUG", 3, 10, "DEBUG");
    Level level = new Level("TRACE", 4, 0, "TRACE");
    TRACE = level;
    $VALUES = new Level[] { ERROR, WARN, INFO, DEBUG, level };
  }
  
  Level(int paramInt1, String paramString1) {
    this.levelInt = paramInt1;
    this.levelStr = paramString1;
  }
  
  public int toInt() {
    return this.levelInt;
  }
  
  public String toString() {
    return this.levelStr;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\event\Level.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */