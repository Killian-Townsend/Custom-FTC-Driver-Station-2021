package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaRelicRecovery;

final class VuforiaRelicRecoveryAccess extends VuforiaBaseAccess<VuforiaRelicRecovery> {
  VuforiaRelicRecoveryAccess(BlocksOpMode paramBlocksOpMode, String paramString, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramString, paramHardwareMap, "VuforiaRelicRecovery");
  }
  
  protected VuforiaRelicRecovery createVuforia() {
    return new VuforiaRelicRecovery();
  }
  
  @JavascriptInterface
  public void initialize(String paramString1, String paramString2, boolean paramBoolean, String paramString3, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    initialize_withCameraDirection(paramString1, paramString2, true, paramBoolean, paramString3, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, true);
  }
  
  @JavascriptInterface
  public void initializeExtended(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, String paramString3, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, boolean paramBoolean3) {
    initialize_withCameraDirection(paramString1, paramString2, paramBoolean1, paramBoolean2, paramString3, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramBoolean3);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\VuforiaRelicRecoveryAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */