package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public abstract class Item {
  public abstract void addContents(DexFile paramDexFile);
  
  public abstract ItemType itemType();
  
  public final String typeName() {
    return itemType().toHuman();
  }
  
  public abstract int writeSize();
  
  public abstract void writeTo(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\Item.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */