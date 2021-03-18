package org.firstinspires.ftc.robotcore.internal.android.multidex;

import java.io.IOException;
import java.io.InputStream;

interface ClassPathElement {
  public static final char SEPARATOR_CHAR = '/';
  
  void close() throws IOException;
  
  Iterable<String> list();
  
  InputStream open(String paramString) throws IOException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\multidex\ClassPathElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */