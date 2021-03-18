package org.tensorflow.lite;

enum DataType {
  BYTEBUFFER,
  FLOAT32(1),
  INT32(2),
  INT64(2),
  UINT8(3);
  
  private static final DataType[] values;
  
  private final int value;
  
  static {
    INT64 = new DataType("INT64", 3, 4);
    DataType dataType = new DataType("BYTEBUFFER", 4, 999);
    BYTEBUFFER = dataType;
    $VALUES = new DataType[] { FLOAT32, INT32, UINT8, INT64, dataType };
    values = values();
  }
  
  DataType(int paramInt1) {
    this.value = paramInt1;
  }
  
  static DataType fromNumber(int paramInt) {
    for (DataType dataType : values) {
      if (dataType.value == paramInt)
        return dataType; 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("DataType error: DataType ");
    stringBuilder.append(paramInt);
    stringBuilder.append(" is not recognized in Java (version ");
    stringBuilder.append(TensorFlowLite.version());
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  int elemByteSize() {
    int i = null.$SwitchMap$org$tensorflow$lite$DataType[ordinal()];
    if (i != 1 && i != 2) {
      if (i != 3) {
        if (i != 4) {
          if (i == 5)
            return 1; 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("DataType error: DataType ");
          stringBuilder.append(this);
          stringBuilder.append(" is not supported yet");
          throw new IllegalArgumentException(stringBuilder.toString());
        } 
        return 8;
      } 
      return 1;
    } 
    return 4;
  }
  
  int getNumber() {
    return this.value;
  }
  
  String toStringName() {
    int i = null.$SwitchMap$org$tensorflow$lite$DataType[ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          if (i != 4) {
            if (i == 5)
              return "ByteBuffer"; 
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DataType error: DataType ");
            stringBuilder.append(this);
            stringBuilder.append(" is not supported yet");
            throw new IllegalArgumentException(stringBuilder.toString());
          } 
          return "long";
        } 
        return "byte";
      } 
      return "int";
    } 
    return "float";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\tensorflow\lite\DataType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */