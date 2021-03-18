package org.firstinspires.ftc.robotserver.internal.webserver.controlhubupdater.result;

import android.os.Bundle;

public final class Result {
  private Throwable cause;
  
  private String detailMessage;
  
  private final ResultType resultType;
  
  private Result(ResultType paramResultType, String paramString, Throwable paramThrowable) {
    this.resultType = paramResultType;
    this.detailMessage = paramString;
    this.cause = paramThrowable;
  }
  
  public static Result fromBundle(Bundle paramBundle) {
    // Byte code:
    //   0: aload_0
    //   1: ldc 'category'
    //   3: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   6: invokestatic fromString : (Ljava/lang/String;)Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result$Category;
    //   9: astore #5
    //   11: aload_0
    //   12: ldc 'resultCode'
    //   14: invokevirtual getInt : (Ljava/lang/String;)I
    //   17: istore_1
    //   18: aload #5
    //   20: ifnull -> 75
    //   23: getstatic org/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result$1.$SwitchMap$org$firstinspires$ftc$robotserver$internal$webserver$controlhubupdater$result$Result$Category : [I
    //   26: aload #5
    //   28: invokevirtual ordinal : ()I
    //   31: iaload
    //   32: istore_2
    //   33: iload_2
    //   34: iconst_1
    //   35: if_icmpeq -> 67
    //   38: iload_2
    //   39: iconst_2
    //   40: if_icmpeq -> 59
    //   43: iload_2
    //   44: iconst_3
    //   45: if_icmpeq -> 51
    //   48: goto -> 75
    //   51: iload_1
    //   52: invokestatic fromCode : (I)Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/AppResultType;
    //   55: astore_3
    //   56: goto -> 77
    //   59: iload_1
    //   60: invokestatic fromCode : (I)Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/OtaResultType;
    //   63: astore_3
    //   64: goto -> 77
    //   67: iload_1
    //   68: invokestatic fromCode : (I)Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/CommonResultType;
    //   71: astore_3
    //   72: goto -> 77
    //   75: aconst_null
    //   76: astore_3
    //   77: aload_3
    //   78: astore #4
    //   80: aload_3
    //   81: ifnonnull -> 122
    //   84: aload_0
    //   85: ldc 'message'
    //   87: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   90: astore_3
    //   91: new org/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/UnknownResultType
    //   94: dup
    //   95: aload #5
    //   97: iload_1
    //   98: aload_0
    //   99: ldc 'presentationType'
    //   101: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   104: invokestatic fromString : (Ljava/lang/String;)Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result$PresentationType;
    //   107: aload_0
    //   108: ldc 'detailMessageType'
    //   110: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   113: invokestatic fromString : (Ljava/lang/String;)Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result$DetailMessageType;
    //   116: aload_3
    //   117: invokespecial <init> : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result$Category;ILorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result$PresentationType;Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result$DetailMessageType;Ljava/lang/String;)V
    //   120: astore #4
    //   122: new org/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result
    //   125: dup
    //   126: aload #4
    //   128: aload_0
    //   129: ldc 'detailMessage'
    //   131: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   134: aload_0
    //   135: ldc 'cause'
    //   137: invokevirtual getSerializable : (Ljava/lang/String;)Ljava/io/Serializable;
    //   140: checkcast java/lang/Throwable
    //   143: invokespecial <init> : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/ResultType;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   146: areturn
  }
  
  public Category getCategory() {
    return this.resultType.getCategory();
  }
  
  public Throwable getCause() {
    return this.cause;
  }
  
  public int getCode() {
    return this.resultType.getCode();
  }
  
  public String getDetailMessage() {
    return (getDetailMessageType() == DetailMessageType.SUBSTITUTED) ? null : this.detailMessage;
  }
  
  public DetailMessageType getDetailMessageType() {
    return this.resultType.getDetailMessageType();
  }
  
  public String getMessage() {
    String str2 = this.resultType.getMessage();
    String str1 = str2;
    if (getDetailMessageType() == DetailMessageType.SUBSTITUTED) {
      String str = this.detailMessage;
      str1 = str;
      if (str == null)
        str1 = ""; 
      str1 = String.format(str2, new Object[] { str1 });
    } 
    return str1;
  }
  
  public PresentationType getPresentationType() {
    return this.resultType.getPresentationType();
  }
  
  public ResultType getResultType() {
    return this.resultType;
  }
  
  public enum Category {
    APP_UPDATE, COMMON, OTA_UPDATE;
    
    static {
      Category category = new Category("APP_UPDATE", 2);
      APP_UPDATE = category;
      $VALUES = new Category[] { COMMON, OTA_UPDATE, category };
    }
    
    public static Category fromString(String param1String) {
      for (Category category : values()) {
        if (category.name().equals(param1String))
          return category; 
      } 
      return null;
    }
  }
  
  public enum DetailMessageType {
    LOGGED, SUBSTITUTED, DISPLAYED;
    
    static {
      DetailMessageType detailMessageType = new DetailMessageType("SUBSTITUTED", 2);
      SUBSTITUTED = detailMessageType;
      $VALUES = new DetailMessageType[] { LOGGED, DISPLAYED, detailMessageType };
    }
    
    public static DetailMessageType fromString(String param1String) {
      for (DetailMessageType detailMessageType : values()) {
        if (detailMessageType.name().equals(param1String))
          return detailMessageType; 
      } 
      return null;
    }
  }
  
  public enum PresentationType {
    ERROR, PROMPT, STATUS, SUCCESS;
    
    static {
      PresentationType presentationType = new PresentationType("PROMPT", 3);
      PROMPT = presentationType;
      $VALUES = new PresentationType[] { SUCCESS, ERROR, STATUS, presentationType };
    }
    
    public static PresentationType fromString(String param1String) {
      for (PresentationType presentationType : values()) {
        if (presentationType.name().equals(param1String))
          return presentationType; 
      } 
      return null;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\controlhubupdater\result\Result.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */