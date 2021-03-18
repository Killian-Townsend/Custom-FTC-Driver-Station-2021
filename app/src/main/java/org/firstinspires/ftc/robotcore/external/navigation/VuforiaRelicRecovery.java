package org.firstinspires.ftc.robotcore.external.navigation;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;

public class VuforiaRelicRecovery extends VuforiaBase {
  private static final String ASSET_NAME = "RelicVuMark";
  
  private static final OpenGLMatrix[] LOCATIONS_ON_FIELD;
  
  private static final String[] TRACKABLE_NAMES = new String[] { "Relic" };
  
  static {
    LOCATIONS_ON_FIELD = new OpenGLMatrix[] { null };
  }
  
  public VuforiaRelicRecovery() {
    super("RelicVuMark", TRACKABLE_NAMES, LOCATIONS_ON_FIELD);
  }
  
  public VuforiaBase.TrackingResults emptyTrackingResults(String paramString) {
    return new TrackingResults(paramString);
  }
  
  public VuforiaBase.TrackingResults track(String paramString) {
    return new TrackingResults(super.track(paramString), RelicRecoveryVuMark.from(getListener(paramString)));
  }
  
  public VuforiaBase.TrackingResults trackPose(String paramString) {
    return new TrackingResults(super.trackPose(paramString), RelicRecoveryVuMark.from(getListener(paramString)));
  }
  
  public static class TrackingResults extends VuforiaBase.TrackingResults {
    public RelicRecoveryVuMark relicRecoveryVuMark = RelicRecoveryVuMark.UNKNOWN;
    
    TrackingResults(String param1String) {
      super(param1String);
    }
    
    TrackingResults(VuforiaBase.TrackingResults param1TrackingResults, RelicRecoveryVuMark param1RelicRecoveryVuMark) {
      super(param1TrackingResults);
      this.relicRecoveryVuMark = param1RelicRecoveryVuMark;
    }
    
    public String toJson() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("{ \"Name\":\"");
      stringBuilder.append(this.name);
      stringBuilder.append("\", \"IsVisible\":");
      stringBuilder.append(this.isVisible);
      stringBuilder.append(", \"RelicRecoveryVuMark\":\"");
      stringBuilder.append(this.relicRecoveryVuMark);
      stringBuilder.append("\", \"IsUpdatedRobotLocation\":");
      stringBuilder.append(this.isUpdatedRobotLocation);
      stringBuilder.append(", \"X\":");
      stringBuilder.append(this.x);
      stringBuilder.append(", \"Y\":");
      stringBuilder.append(this.y);
      stringBuilder.append(", \"Z\":");
      stringBuilder.append(this.z);
      stringBuilder.append(", \"XAngle\":");
      stringBuilder.append(this.xAngle);
      stringBuilder.append(", \"YAngle\":");
      stringBuilder.append(this.yAngle);
      stringBuilder.append(", \"ZAngle\":");
      stringBuilder.append(this.zAngle);
      stringBuilder.append(" }");
      return stringBuilder.toString();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuforiaRelicRecovery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */