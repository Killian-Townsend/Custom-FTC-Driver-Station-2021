package org.firstinspires.ftc.robotcore.external;

public interface Telemetry {
  Object addAction(Runnable paramRunnable);
  
  Item addData(String paramString, Object paramObject);
  
  <T> Item addData(String paramString1, String paramString2, Func<T> paramFunc);
  
  Item addData(String paramString1, String paramString2, Object... paramVarArgs);
  
  <T> Item addData(String paramString, Func<T> paramFunc);
  
  Line addLine();
  
  Line addLine(String paramString);
  
  void clear();
  
  void clearAll();
  
  String getCaptionValueSeparator();
  
  String getItemSeparator();
  
  int getMsTransmissionInterval();
  
  boolean isAutoClear();
  
  Log log();
  
  boolean removeAction(Object paramObject);
  
  boolean removeItem(Item paramItem);
  
  boolean removeLine(Line paramLine);
  
  void setAutoClear(boolean paramBoolean);
  
  void setCaptionValueSeparator(String paramString);
  
  void setDisplayFormat(DisplayFormat paramDisplayFormat);
  
  void setItemSeparator(String paramString);
  
  void setMsTransmissionInterval(int paramInt);
  
  void speak(String paramString);
  
  void speak(String paramString1, String paramString2, String paramString3);
  
  boolean update();
  
  public enum DisplayFormat {
    CLASSIC, HTML, MONOSPACE;
    
    static {
      DisplayFormat displayFormat = new DisplayFormat("HTML", 2);
      HTML = displayFormat;
      $VALUES = new DisplayFormat[] { CLASSIC, MONOSPACE, displayFormat };
    }
  }
  
  public static interface Item {
    Item addData(String param1String, Object param1Object);
    
    <T> Item addData(String param1String1, String param1String2, Func<T> param1Func);
    
    Item addData(String param1String1, String param1String2, Object... param1VarArgs);
    
    <T> Item addData(String param1String, Func<T> param1Func);
    
    String getCaption();
    
    boolean isRetained();
    
    Item setCaption(String param1String);
    
    Item setRetained(Boolean param1Boolean);
    
    Item setValue(Object param1Object);
    
    <T> Item setValue(String param1String, Func<T> param1Func);
    
    Item setValue(String param1String, Object... param1VarArgs);
    
    <T> Item setValue(Func<T> param1Func);
  }
  
  public static interface Line {
    Telemetry.Item addData(String param1String, Object param1Object);
    
    <T> Telemetry.Item addData(String param1String1, String param1String2, Func<T> param1Func);
    
    Telemetry.Item addData(String param1String1, String param1String2, Object... param1VarArgs);
    
    <T> Telemetry.Item addData(String param1String, Func<T> param1Func);
  }
  
  public static interface Log {
    void add(String param1String);
    
    void add(String param1String, Object... param1VarArgs);
    
    void clear();
    
    int getCapacity();
    
    DisplayOrder getDisplayOrder();
    
    void setCapacity(int param1Int);
    
    void setDisplayOrder(DisplayOrder param1DisplayOrder);
    
    public enum DisplayOrder {
      NEWEST_FIRST, OLDEST_FIRST;
      
      static {
        DisplayOrder displayOrder = new DisplayOrder("OLDEST_FIRST", 1);
        OLDEST_FIRST = displayOrder;
        $VALUES = new DisplayOrder[] { NEWEST_FIRST, displayOrder };
      }
    }
  }
  
  public enum DisplayOrder {
    NEWEST_FIRST, OLDEST_FIRST;
    
    static {
      DisplayOrder displayOrder = new DisplayOrder("OLDEST_FIRST", 1);
      OLDEST_FIRST = displayOrder;
      $VALUES = new DisplayOrder[] { NEWEST_FIRST, displayOrder };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\Telemetry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */