package org.firstinspires.ftc.robotcore.external.tfod;

public class TfodCurrentGame extends TfodBase {
  public static final String[] LABELS = new String[] { "Quad", "Single" };
  
  public static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
  
  public TfodCurrentGame() {
    super("UltimateGoal.tflite", LABELS);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\tfod\TfodCurrentGame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */