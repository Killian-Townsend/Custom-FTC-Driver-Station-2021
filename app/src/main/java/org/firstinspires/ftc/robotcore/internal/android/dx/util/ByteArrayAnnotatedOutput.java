package org.firstinspires.ftc.robotcore.internal.android.dx.util;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dex.Leb128;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ByteOutput;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;

public final class ByteArrayAnnotatedOutput implements AnnotatedOutput, ByteOutput {
  private static final int DEFAULT_SIZE = 1000;
  
  private int annotationWidth;
  
  private ArrayList<Annotation> annotations;
  
  private int cursor;
  
  private byte[] data;
  
  private int hexCols;
  
  private final boolean stretchy;
  
  private boolean verbose;
  
  public ByteArrayAnnotatedOutput() {
    this(1000);
  }
  
  public ByteArrayAnnotatedOutput(int paramInt) {
    this(new byte[paramInt], true);
  }
  
  public ByteArrayAnnotatedOutput(byte[] paramArrayOfbyte) {
    this(paramArrayOfbyte, false);
  }
  
  private ByteArrayAnnotatedOutput(byte[] paramArrayOfbyte, boolean paramBoolean) {
    if (paramArrayOfbyte != null) {
      this.stretchy = paramBoolean;
      this.data = paramArrayOfbyte;
      this.cursor = 0;
      this.verbose = false;
      this.annotations = null;
      this.annotationWidth = 0;
      this.hexCols = 0;
      return;
    } 
    throw new NullPointerException("data == null");
  }
  
  private void ensureCapacity(int paramInt) {
    byte[] arrayOfByte = this.data;
    if (arrayOfByte.length < paramInt) {
      byte[] arrayOfByte1 = new byte[paramInt * 2 + 1000];
      System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, this.cursor);
      this.data = arrayOfByte1;
    } 
  }
  
  private static void throwBounds() {
    throw new IndexOutOfBoundsException("attempt to write past the end");
  }
  
  public void alignTo(int paramInt) {
    int i = paramInt - 1;
    if (paramInt >= 0 && (paramInt & i) == 0) {
      paramInt = this.cursor + i & i;
      if (this.stretchy) {
        ensureCapacity(paramInt);
      } else if (paramInt > this.data.length) {
        throwBounds();
        return;
      } 
      this.cursor = paramInt;
      return;
    } 
    throw new IllegalArgumentException("bogus alignment");
  }
  
  public void annotate(int paramInt, String paramString) {
    if (this.annotations == null)
      return; 
    endAnnotation();
    int i = this.annotations.size();
    if (i == 0) {
      i = 0;
    } else {
      i = ((Annotation)this.annotations.get(i - 1)).getEnd();
    } 
    int k = this.cursor;
    int j = i;
    if (i <= k)
      j = k; 
    this.annotations.add(new Annotation(j, paramInt + j, paramString));
  }
  
  public void annotate(String paramString) {
    if (this.annotations == null)
      return; 
    endAnnotation();
    this.annotations.add(new Annotation(this.cursor, paramString));
  }
  
  public boolean annotates() {
    return (this.annotations != null);
  }
  
  public void assertCursor(int paramInt) {
    if (this.cursor == paramInt)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("expected cursor ");
    stringBuilder.append(paramInt);
    stringBuilder.append("; actual value: ");
    stringBuilder.append(this.cursor);
    throw new ExceptionWithContext(stringBuilder.toString());
  }
  
  public void enableAnnotations(int paramInt, boolean paramBoolean) {
    if (this.annotations == null && this.cursor == 0) {
      if (paramInt >= 40) {
        int i;
        int j = (paramInt - 7) / 15 + 1 & 0xFFFFFFFE;
        if (j < 6) {
          i = 6;
        } else {
          i = j;
          if (j > 10)
            i = 10; 
        } 
        this.annotations = new ArrayList<Annotation>(1000);
        this.annotationWidth = paramInt;
        this.hexCols = i;
        this.verbose = paramBoolean;
        return;
      } 
      throw new IllegalArgumentException("annotationWidth < 40");
    } 
    throw new RuntimeException("cannot enable annotations");
  }
  
  public void endAnnotation() {
    ArrayList<Annotation> arrayList = this.annotations;
    if (arrayList == null)
      return; 
    int i = arrayList.size();
    if (i != 0)
      ((Annotation)this.annotations.get(i - 1)).setEndIfUnset(this.cursor); 
  }
  
  public void finishAnnotating() {
    endAnnotation();
    ArrayList<Annotation> arrayList = this.annotations;
    if (arrayList != null) {
      int i = arrayList.size();
      while (i > 0) {
        arrayList = this.annotations;
        int j = i - 1;
        Annotation annotation = arrayList.get(j);
        if (annotation.getStart() > this.cursor) {
          this.annotations.remove(j);
          i--;
          continue;
        } 
        i = annotation.getEnd();
        j = this.cursor;
        if (i > j)
          annotation.setEnd(j); 
      } 
    } 
  }
  
  public int getAnnotationWidth() {
    int i = this.hexCols;
    int j = i / 2;
    return this.annotationWidth - i * 2 + 8 + j;
  }
  
  public byte[] getArray() {
    return this.data;
  }
  
  public int getCursor() {
    return this.cursor;
  }
  
  public boolean isVerbose() {
    return this.verbose;
  }
  
  public byte[] toByteArray() {
    int i = this.cursor;
    byte[] arrayOfByte = new byte[i];
    System.arraycopy(this.data, 0, arrayOfByte, 0, i);
    return arrayOfByte;
  }
  
  public void write(ByteArray paramByteArray) {
    int j = paramByteArray.size();
    int i = this.cursor;
    j += i;
    if (this.stretchy) {
      ensureCapacity(j);
    } else if (j > this.data.length) {
      throwBounds();
      return;
    } 
    paramByteArray.getBytes(this.data, i);
    this.cursor = j;
  }
  
  public void write(byte[] paramArrayOfbyte) {
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    int i = this.cursor;
    int j = i + paramInt2;
    if ((paramInt1 | paramInt2 | j) >= 0 && paramInt1 + paramInt2 <= paramArrayOfbyte.length) {
      if (this.stretchy) {
        ensureCapacity(j);
      } else if (j > this.data.length) {
        throwBounds();
        return;
      } 
      System.arraycopy(paramArrayOfbyte, paramInt1, this.data, i, paramInt2);
      this.cursor = j;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("bytes.length ");
    stringBuilder.append(paramArrayOfbyte.length);
    stringBuilder.append("; ");
    stringBuilder.append(paramInt1);
    stringBuilder.append("..!");
    stringBuilder.append(j);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public void writeAnnotationsTo(Writer paramWriter) throws IOException {
    int i = getAnnotationWidth();
    TwoColumnOutput twoColumnOutput = new TwoColumnOutput(paramWriter, this.annotationWidth - i - 1, i, "|");
    Writer writer1 = twoColumnOutput.getLeft();
    Writer writer2 = twoColumnOutput.getRight();
    int n = this.annotations.size();
    i = 0;
    int j;
    for (j = 0; j < this.cursor && i < n; j = i1) {
      String str;
      int i2;
      Annotation annotation = this.annotations.get(i);
      int i1 = annotation.getStart();
      if (j < i1) {
        str = "";
        i2 = j;
        j = i;
        i = i1;
      } else {
        i2 = str.getEnd();
        str = str.getText();
        j = i + 1;
        i = i2;
        i2 = i1;
      } 
      writer1.write(Hex.dump(this.data, i2, i - i2, i2, this.hexCols, 6));
      writer2.write(str);
      twoColumnOutput.flush();
      i1 = i;
      i = j;
    } 
    int m = this.cursor;
    int k = i;
    if (j < m) {
      writer1.write(Hex.dump(this.data, j, m - j, j, this.hexCols, 6));
      k = i;
    } 
    while (k < n) {
      writer2.write(((Annotation)this.annotations.get(k)).getText());
      k++;
    } 
    twoColumnOutput.flush();
  }
  
  public void writeByte(int paramInt) {
    int i = this.cursor;
    int j = i + 1;
    if (this.stretchy) {
      ensureCapacity(j);
    } else if (j > this.data.length) {
      throwBounds();
      return;
    } 
    this.data[i] = (byte)paramInt;
    this.cursor = j;
  }
  
  public void writeInt(int paramInt) {
    int i = this.cursor;
    int j = i + 4;
    if (this.stretchy) {
      ensureCapacity(j);
    } else if (j > this.data.length) {
      throwBounds();
      return;
    } 
    byte[] arrayOfByte = this.data;
    arrayOfByte[i] = (byte)paramInt;
    arrayOfByte[i + 1] = (byte)(paramInt >> 8);
    arrayOfByte[i + 2] = (byte)(paramInt >> 16);
    arrayOfByte[i + 3] = (byte)(paramInt >> 24);
    this.cursor = j;
  }
  
  public void writeLong(long paramLong) {
    int i = this.cursor;
    int j = i + 8;
    if (this.stretchy) {
      ensureCapacity(j);
    } else if (j > this.data.length) {
      throwBounds();
      return;
    } 
    int k = (int)paramLong;
    byte[] arrayOfByte = this.data;
    arrayOfByte[i] = (byte)k;
    arrayOfByte[i + 1] = (byte)(k >> 8);
    arrayOfByte[i + 2] = (byte)(k >> 16);
    arrayOfByte[i + 3] = (byte)(k >> 24);
    k = (int)(paramLong >> 32L);
    arrayOfByte[i + 4] = (byte)k;
    arrayOfByte[i + 5] = (byte)(k >> 8);
    arrayOfByte[i + 6] = (byte)(k >> 16);
    arrayOfByte[i + 7] = (byte)(k >> 24);
    this.cursor = j;
  }
  
  public void writeShort(int paramInt) {
    int i = this.cursor;
    int j = i + 2;
    if (this.stretchy) {
      ensureCapacity(j);
    } else if (j > this.data.length) {
      throwBounds();
      return;
    } 
    byte[] arrayOfByte = this.data;
    arrayOfByte[i] = (byte)paramInt;
    arrayOfByte[i + 1] = (byte)(paramInt >> 8);
    this.cursor = j;
  }
  
  public int writeSleb128(int paramInt) {
    if (this.stretchy)
      ensureCapacity(this.cursor + 5); 
    int i = this.cursor;
    Leb128.writeSignedLeb128(this, paramInt);
    return this.cursor - i;
  }
  
  public int writeUleb128(int paramInt) {
    if (this.stretchy)
      ensureCapacity(this.cursor + 5); 
    int i = this.cursor;
    Leb128.writeUnsignedLeb128(this, paramInt);
    return this.cursor - i;
  }
  
  public void writeZeroes(int paramInt) {
    if (paramInt >= 0) {
      paramInt = this.cursor + paramInt;
      if (this.stretchy) {
        ensureCapacity(paramInt);
      } else if (paramInt > this.data.length) {
        throwBounds();
        return;
      } 
      this.cursor = paramInt;
      return;
    } 
    throw new IllegalArgumentException("count < 0");
  }
  
  private static class Annotation {
    private int end;
    
    private final int start;
    
    private final String text;
    
    public Annotation(int param1Int1, int param1Int2, String param1String) {
      this.start = param1Int1;
      this.end = param1Int2;
      this.text = param1String;
    }
    
    public Annotation(int param1Int, String param1String) {
      this(param1Int, 2147483647, param1String);
    }
    
    public int getEnd() {
      return this.end;
    }
    
    public int getStart() {
      return this.start;
    }
    
    public String getText() {
      return this.text;
    }
    
    public void setEnd(int param1Int) {
      this.end = param1Int;
    }
    
    public void setEndIfUnset(int param1Int) {
      if (this.end == Integer.MAX_VALUE)
        this.end = param1Int; 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\ByteArrayAnnotatedOutput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */