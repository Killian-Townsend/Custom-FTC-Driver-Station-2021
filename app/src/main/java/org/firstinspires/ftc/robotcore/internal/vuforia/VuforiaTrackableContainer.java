package org.firstinspires.ftc.robotcore.internal.vuforia;

import com.vuforia.VuMarkTarget;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

public interface VuforiaTrackableContainer {
  List<VuforiaTrackable> children();
  
  VuforiaTrackable getChild(VuMarkTarget paramVuMarkTarget);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\VuforiaTrackableContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */