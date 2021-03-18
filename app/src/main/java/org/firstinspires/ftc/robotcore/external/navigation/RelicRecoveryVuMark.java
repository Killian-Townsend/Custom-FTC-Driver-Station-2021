package org.firstinspires.ftc.robotcore.external.navigation;

public enum RelicRecoveryVuMark {
  CENTER, LEFT, RIGHT, UNKNOWN;
  
  static {
    LEFT = new RelicRecoveryVuMark("LEFT", 1);
    CENTER = new RelicRecoveryVuMark("CENTER", 2);
    RelicRecoveryVuMark relicRecoveryVuMark = new RelicRecoveryVuMark("RIGHT", 3);
    RIGHT = relicRecoveryVuMark;
    $VALUES = new RelicRecoveryVuMark[] { UNKNOWN, LEFT, CENTER, relicRecoveryVuMark };
  }
  
  public static RelicRecoveryVuMark from(VuMarkInstanceId paramVuMarkInstanceId) {
    RelicRecoveryVuMark relicRecoveryVuMark2 = UNKNOWN;
    RelicRecoveryVuMark relicRecoveryVuMark1 = relicRecoveryVuMark2;
    if (paramVuMarkInstanceId != null) {
      relicRecoveryVuMark1 = relicRecoveryVuMark2;
      if (paramVuMarkInstanceId.getType() == VuMarkInstanceId.Type.NUMERIC) {
        long l = paramVuMarkInstanceId.getNumericValue();
        if (l == 1L)
          return LEFT; 
        if (l == 2L)
          return CENTER; 
        relicRecoveryVuMark1 = relicRecoveryVuMark2;
        if (l == 3L)
          relicRecoveryVuMark1 = RIGHT; 
      } 
    } 
    return relicRecoveryVuMark1;
  }
  
  public static RelicRecoveryVuMark from(VuforiaTrackable.Listener paramListener) {
    return (paramListener instanceof VuforiaTrackableDefaultListener) ? from(((VuforiaTrackableDefaultListener)paramListener).getVuMarkInstanceId()) : UNKNOWN;
  }
  
  public static RelicRecoveryVuMark from(VuforiaTrackable paramVuforiaTrackable) {
    return (paramVuforiaTrackable != null) ? from(paramVuforiaTrackable.getListener()) : UNKNOWN;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\RelicRecoveryVuMark.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */