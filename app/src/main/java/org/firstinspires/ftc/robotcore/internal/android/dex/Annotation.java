package org.firstinspires.ftc.robotcore.internal.android.dex;

public final class Annotation implements Comparable<Annotation> {
  private final Dex dex;
  
  private final EncodedValue encodedAnnotation;
  
  private final byte visibility;
  
  public Annotation(Dex paramDex, byte paramByte, EncodedValue paramEncodedValue) {
    this.dex = paramDex;
    this.visibility = paramByte;
    this.encodedAnnotation = paramEncodedValue;
  }
  
  public int compareTo(Annotation paramAnnotation) {
    return this.encodedAnnotation.compareTo(paramAnnotation.encodedAnnotation);
  }
  
  public EncodedValueReader getReader() {
    return new EncodedValueReader(this.encodedAnnotation, 29);
  }
  
  public int getTypeIndex() {
    EncodedValueReader encodedValueReader = getReader();
    encodedValueReader.readAnnotation();
    return encodedValueReader.getAnnotationType();
  }
  
  public byte getVisibility() {
    return this.visibility;
  }
  
  public String toString() {
    if (this.dex == null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(this.visibility);
      stringBuilder1.append(" ");
      stringBuilder1.append(getTypeIndex());
      return stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.visibility);
    stringBuilder.append(" ");
    stringBuilder.append(this.dex.typeNames().get(getTypeIndex()));
    return stringBuilder.toString();
  }
  
  public void writeTo(Dex.Section paramSection) {
    paramSection.writeByte(this.visibility);
    this.encodedAnnotation.writeTo(paramSection);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dex\Annotation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */