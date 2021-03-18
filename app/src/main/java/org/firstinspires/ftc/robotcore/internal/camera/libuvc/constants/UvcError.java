package org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants;

public enum UvcError {
  ACCESS,
  BUSY,
  CALLBACK_EXISTS,
  INTERRUPTED,
  INVALID_DEVICE,
  INVALID_MODE,
  INVALID_PARAM,
  IO,
  NOT_FOUND,
  NOT_SUPPORTED,
  NO_DEVICE,
  NO_MEM,
  OTHER,
  OVERFLOW,
  PIPE,
  SUCCESS(0),
  TIMEOUT(0);
  
  protected int value;
  
  static {
    IO = new UvcError("IO", 1, -1);
    INVALID_PARAM = new UvcError("INVALID_PARAM", 2, -2);
    ACCESS = new UvcError("ACCESS", 3, -3);
    NO_DEVICE = new UvcError("NO_DEVICE", 4, -4);
    NOT_FOUND = new UvcError("NOT_FOUND", 5, -5);
    BUSY = new UvcError("BUSY", 6, -6);
    TIMEOUT = new UvcError("TIMEOUT", 7, -7);
    OVERFLOW = new UvcError("OVERFLOW", 8, -8);
    PIPE = new UvcError("PIPE", 9, -9);
    INTERRUPTED = new UvcError("INTERRUPTED", 10, -10);
    NO_MEM = new UvcError("NO_MEM", 11, -11);
    NOT_SUPPORTED = new UvcError("NOT_SUPPORTED", 12, -12);
    INVALID_DEVICE = new UvcError("INVALID_DEVICE", 13, -50);
    INVALID_MODE = new UvcError("INVALID_MODE", 14, -51);
    CALLBACK_EXISTS = new UvcError("CALLBACK_EXISTS", 15, -52);
    UvcError uvcError = new UvcError("OTHER", 16, -99);
    OTHER = uvcError;
    $VALUES = new UvcError[] { 
        SUCCESS, IO, INVALID_PARAM, ACCESS, NO_DEVICE, NOT_FOUND, BUSY, TIMEOUT, OVERFLOW, PIPE, 
        INTERRUPTED, NO_MEM, NOT_SUPPORTED, INVALID_DEVICE, INVALID_MODE, CALLBACK_EXISTS, uvcError };
  }
  
  UvcError(int paramInt1) {
    this.value = paramInt1;
  }
  
  public static UvcError from(int paramInt) {
    for (UvcError uvcError : values()) {
      if (uvcError.value == paramInt)
        return uvcError; 
    } 
    return OTHER;
  }
  
  public int getValue() {
    return this.value;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\constants\UvcError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */