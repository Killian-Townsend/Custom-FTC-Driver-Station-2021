package org.firstinspires.ftc.robotcore.internal.collections;

import com.google.gson.Gson;

public class SimpleGson {
  public static Gson getInstance() {
    return InstanceHolder.theInstance;
  }
  
  protected static class InstanceHolder {
    public static final Gson theInstance = new Gson();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\collections\SimpleGson.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */