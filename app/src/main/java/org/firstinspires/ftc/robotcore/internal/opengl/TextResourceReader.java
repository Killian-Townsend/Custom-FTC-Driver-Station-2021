package org.firstinspires.ftc.robotcore.internal.opengl;

import android.content.Context;
import android.content.res.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextResourceReader {
  public static String readTextFileFromResource(Context paramContext, int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    try {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramContext.getResources().openRawResource(paramInt)));
      while (true) {
        String str = bufferedReader.readLine();
        if (str != null) {
          stringBuilder.append(str);
          stringBuilder.append('\n');
          continue;
        } 
        return stringBuilder.toString();
      } 
    } catch (IOException iOException) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("Could not open resource: ");
      stringBuilder.append(paramInt);
      throw new RuntimeException(stringBuilder.toString(), iOException);
    } catch (android.content.res.Resources.NotFoundException notFoundException) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("Resource not found: ");
      stringBuilder.append(paramInt);
      throw new RuntimeException(stringBuilder.toString(), notFoundException);
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\TextResourceReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */