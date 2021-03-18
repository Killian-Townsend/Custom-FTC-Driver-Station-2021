package org.firstinspires.ftc.robotcore.internal.android.dx.io;

public enum IndexType {
  FIELD_OFFSET, FIELD_REF, INLINE_METHOD, METHOD_REF, NONE, STRING_REF, TYPE_REF, UNKNOWN, VARIES, VTABLE_OFFSET;
  
  static {
    NONE = new IndexType("NONE", 1);
    VARIES = new IndexType("VARIES", 2);
    TYPE_REF = new IndexType("TYPE_REF", 3);
    STRING_REF = new IndexType("STRING_REF", 4);
    METHOD_REF = new IndexType("METHOD_REF", 5);
    FIELD_REF = new IndexType("FIELD_REF", 6);
    INLINE_METHOD = new IndexType("INLINE_METHOD", 7);
    VTABLE_OFFSET = new IndexType("VTABLE_OFFSET", 8);
    IndexType indexType = new IndexType("FIELD_OFFSET", 9);
    FIELD_OFFSET = indexType;
    $VALUES = new IndexType[] { UNKNOWN, NONE, VARIES, TYPE_REF, STRING_REF, METHOD_REF, FIELD_REF, INLINE_METHOD, VTABLE_OFFSET, indexType };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\IndexType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */