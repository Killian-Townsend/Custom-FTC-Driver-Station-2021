package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.ConstantPool;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;

public final class RawAttribute extends BaseAttribute {
  private final ByteArray data;
  
  private final ConstantPool pool;
  
  public RawAttribute(String paramString, ByteArray paramByteArray, int paramInt1, int paramInt2, ConstantPool paramConstantPool) {
    this(paramString, paramByteArray.slice(paramInt1, paramInt2 + paramInt1), paramConstantPool);
  }
  
  public RawAttribute(String paramString, ByteArray paramByteArray, ConstantPool paramConstantPool) {
    super(paramString);
    if (paramByteArray != null) {
      this.data = paramByteArray;
      this.pool = paramConstantPool;
      return;
    } 
    throw new NullPointerException("data == null");
  }
  
  public int byteLength() {
    return this.data.size() + 6;
  }
  
  public ByteArray getData() {
    return this.data;
  }
  
  public ConstantPool getPool() {
    return this.pool;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\RawAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */