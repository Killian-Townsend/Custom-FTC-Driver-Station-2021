package org.firstinspires.ftc.robotcore.internal.opmode;

public class OpModeMeta {
  public static final String DefaultGroup = "$$$$$$$";
  
  public final Flavor flavor;
  
  public final String group;
  
  public final String name;
  
  public OpModeMeta() {
    this("");
  }
  
  public OpModeMeta(String paramString) {
    this(paramString, Flavor.TELEOP);
  }
  
  public OpModeMeta(String paramString, Flavor paramFlavor) {
    this(paramString, paramFlavor, "$$$$$$$");
  }
  
  public OpModeMeta(String paramString1, Flavor paramFlavor, String paramString2) {
    this.name = paramString1;
    this.flavor = paramFlavor;
    this.group = paramString2;
  }
  
  public OpModeMeta(Flavor paramFlavor, String paramString) {
    this("", paramFlavor, paramString);
  }
  
  public static OpModeMeta forGroup(String paramString, OpModeMeta paramOpModeMeta) {
    return new OpModeMeta(paramOpModeMeta.name, paramOpModeMeta.flavor, paramString);
  }
  
  public static OpModeMeta forName(String paramString, OpModeMeta paramOpModeMeta) {
    return new OpModeMeta(paramString, paramOpModeMeta.flavor, paramOpModeMeta.group);
  }
  
  public boolean equals(Object paramObject) {
    return (paramObject instanceof OpModeMeta) ? this.name.equals(((OpModeMeta)paramObject).name) : false;
  }
  
  public int hashCode() {
    return this.name.hashCode();
  }
  
  public String toString() {
    return this.name;
  }
  
  public enum Flavor {
    AUTONOMOUS, TELEOP;
    
    static {
      Flavor flavor = new Flavor("TELEOP", 1);
      TELEOP = flavor;
      $VALUES = new Flavor[] { AUTONOMOUS, flavor };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opmode\OpModeMeta.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */