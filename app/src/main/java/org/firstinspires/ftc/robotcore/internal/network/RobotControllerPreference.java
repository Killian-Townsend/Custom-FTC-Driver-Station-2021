package org.firstinspires.ftc.robotcore.internal.network;

import java.util.Set;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;

public class RobotControllerPreference {
  private Boolean booleanValue = null;
  
  private Float floatValue = null;
  
  private Integer intValue = null;
  
  private Long longValue = null;
  
  private String prefName;
  
  private Set<String> stringSetValue = null;
  
  private String stringValue = null;
  
  public RobotControllerPreference(String paramString, Object paramObject) {
    this.prefName = paramString;
    if (paramObject instanceof String) {
      this.stringValue = (String)paramObject;
      return;
    } 
    if (paramObject instanceof Boolean) {
      this.booleanValue = (Boolean)paramObject;
      return;
    } 
    if (paramObject instanceof Integer) {
      this.intValue = (Integer)paramObject;
      return;
    } 
    if (paramObject instanceof Long) {
      this.longValue = (Long)paramObject;
      return;
    } 
    if (paramObject instanceof Float) {
      this.floatValue = (Float)paramObject;
      return;
    } 
    if (paramObject instanceof Set)
      this.stringSetValue = (Set<String>)paramObject; 
  }
  
  public static RobotControllerPreference deserialize(String paramString) {
    return (RobotControllerPreference)SimpleGson.getInstance().fromJson(paramString, RobotControllerPreference.class);
  }
  
  public String getPrefName() {
    return this.prefName;
  }
  
  public Object getValue() {
    String str = this.stringValue;
    if (str != null)
      return str; 
    Boolean bool = this.booleanValue;
    if (bool != null)
      return bool; 
    Integer integer = this.intValue;
    if (integer != null)
      return integer; 
    Long long_ = this.longValue;
    if (long_ != null)
      return long_; 
    Float float_ = this.floatValue;
    if (float_ != null)
      return float_; 
    Set<String> set = this.stringSetValue;
    return (set != null) ? set : null;
  }
  
  public String serialize() {
    return SimpleGson.getInstance().toJson(this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\RobotControllerPreference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */