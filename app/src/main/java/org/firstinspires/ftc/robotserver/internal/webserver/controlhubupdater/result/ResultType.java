package org.firstinspires.ftc.robotserver.internal.webserver.controlhubupdater.result;

public interface ResultType {
  Result.Category getCategory();
  
  int getCode();
  
  Result.DetailMessageType getDetailMessageType();
  
  String getMessage();
  
  Result.PresentationType getPresentationType();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\controlhubupdater\result\ResultType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */