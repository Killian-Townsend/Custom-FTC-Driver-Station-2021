package org.firstinspires.ftc.robotcore.external.navigation;

import com.vuforia.InstanceId;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class VuMarkInstanceId {
  protected byte[] dataValue;
  
  protected int numericValue;
  
  protected String stringValue;
  
  protected Type type;
  
  public VuMarkInstanceId(InstanceId paramInstanceId) {
    this.type = typeFrom(paramInstanceId);
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$VuMarkInstanceId$Type[this.type.ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3)
          return; 
        this.dataValue = dataFrom(paramInstanceId);
        return;
      } 
      this.stringValue = new String(dataFrom(paramInstanceId), Charset.forName("US-ASCII"));
      return;
    } 
    this.numericValue = paramInstanceId.getNumericValue().intValue();
  }
  
  protected static byte[] dataFrom(InstanceId paramInstanceId) {
    ByteBuffer byteBuffer = paramInstanceId.getBuffer();
    byte[] arrayOfByte = new byte[byteBuffer.remaining()];
    byteBuffer.get(arrayOfByte);
    return arrayOfByte;
  }
  
  protected static Type typeFrom(InstanceId paramInstanceId) {
    int i = paramInstanceId.getDataType();
    return (i != 0) ? ((i != 1) ? ((i != 2) ? Type.UNKNOWN : Type.NUMERIC) : Type.STRING) : Type.DATA;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof VuMarkInstanceId;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      paramObject = paramObject;
      bool1 = bool2;
      if (getType() == paramObject.getType()) {
        int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$VuMarkInstanceId$Type[getType().ordinal()];
        if (i != 1)
          return (i != 2) ? ((i != 3) ? false : Arrays.equals(getDataValue(), paramObject.getDataValue())) : getStringValue().equals(paramObject.getStringValue()); 
        bool1 = bool2;
        if (getNumericValue() == paramObject.getNumericValue())
          bool1 = true; 
      } 
    } 
    return bool1;
  }
  
  byte[] getDataValue() {
    return this.dataValue;
  }
  
  int getNumericValue() {
    return this.numericValue;
  }
  
  String getStringValue() {
    return this.stringValue;
  }
  
  public Type getType() {
    return this.type;
  }
  
  Object getValue() {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$VuMarkInstanceId$Type[getType().ordinal()];
    return (i != 1) ? ((i != 2) ? ((i != 3) ? null : getDataValue()) : getStringValue()) : Integer.valueOf(getNumericValue());
  }
  
  public int hashCode() {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$VuMarkInstanceId$Type[getType().ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3)
          return super.hashCode(); 
        i = Arrays.hashCode(getDataValue());
        return i ^ 0x55ADEF;
      } 
      i = getStringValue().hashCode();
      return i ^ 0x55ADEF;
    } 
    i = getNumericValue();
    return i ^ 0x55ADEF;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("VuMarkInstanceId(");
    stringBuilder.append(getType());
    stringBuilder.append(", ");
    stringBuilder.append(getValue());
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public enum Type {
    DATA, NUMERIC, STRING, UNKNOWN;
    
    static {
      Type type = new Type("DATA", 3);
      DATA = type;
      $VALUES = new Type[] { UNKNOWN, NUMERIC, STRING, type };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuMarkInstanceId.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */