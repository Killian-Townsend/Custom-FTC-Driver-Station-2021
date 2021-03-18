package org.firstinspires.ftc.robotserver.internal.webserver.controlhubupdater.result;

class UnknownResultType implements ResultType {
  private final Result.Category category;
  
  private final int code;
  
  private final Result.DetailMessageType detailMessageType;
  
  private final String message;
  
  private final Result.PresentationType presentationType;
  
  public UnknownResultType(Result.Category paramCategory, int paramInt, Result.PresentationType paramPresentationType, Result.DetailMessageType paramDetailMessageType, String paramString) {
    this.category = paramCategory;
    this.code = paramInt;
    this.presentationType = paramPresentationType;
    this.detailMessageType = paramDetailMessageType;
    this.message = paramString;
  }
  
  public Result.Category getCategory() {
    return this.category;
  }
  
  public int getCode() {
    return this.code;
  }
  
  public Result.DetailMessageType getDetailMessageType() {
    return this.detailMessageType;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public Result.PresentationType getPresentationType() {
    return this.presentationType;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\controlhubupdater\result\UnknownResultType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */