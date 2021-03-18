package org.firstinspires.ftc.robotcore.external.stream;

import android.graphics.Bitmap;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;

public interface CameraStreamSource {
  void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> paramContinuation);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\stream\CameraStreamSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */