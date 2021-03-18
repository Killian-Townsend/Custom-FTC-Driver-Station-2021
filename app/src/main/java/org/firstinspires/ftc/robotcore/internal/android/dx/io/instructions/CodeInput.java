package org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions;

import java.io.EOFException;

public interface CodeInput extends CodeCursor {
  boolean hasMore();
  
  int read() throws EOFException;
  
  int readInt() throws EOFException;
  
  long readLong() throws EOFException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\instructions\CodeInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */