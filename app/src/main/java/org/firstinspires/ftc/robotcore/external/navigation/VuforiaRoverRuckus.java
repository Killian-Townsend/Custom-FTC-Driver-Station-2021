package org.firstinspires.ftc.robotcore.external.navigation;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;

public class VuforiaRoverRuckus extends VuforiaBase {
  private static final String ASSET_NAME = "RoverRuckus";
  
  private static final OpenGLMatrix[] LOCATIONS_ON_FIELD;
  
  public static final String[] TRACKABLE_NAMES = new String[] { "BluePerimeter", "RedPerimeter", "FrontPerimeter", "BackPerimeter" };
  
  static {
    LOCATIONS_ON_FIELD = new OpenGLMatrix[] { OpenGLMatrix.translation(0.0F, 1803.4F, 0.0F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XZX, AngleUnit.DEGREES, 90.0F, 0.0F, 0.0F)), OpenGLMatrix.translation(0.0F, -1803.4F, 0.0F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XZX, AngleUnit.DEGREES, 90.0F, 180.0F, 0.0F)), OpenGLMatrix.translation(-1803.4F, 0.0F, 0.0F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XZX, AngleUnit.DEGREES, 90.0F, 90.0F, 0.0F)), OpenGLMatrix.translation(1803.4F, 0.0F, 0.0F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XZX, AngleUnit.DEGREES, 90.0F, 270.0F, 0.0F)) };
  }
  
  public VuforiaRoverRuckus() {
    super("RoverRuckus", TRACKABLE_NAMES, LOCATIONS_ON_FIELD);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuforiaRoverRuckus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */