package org.firstinspires.ftc.robotcore.external.matrices;

public class VectorF {
  protected float[] data;
  
  public VectorF(float paramFloat) {
    float[] arrayOfFloat = new float[1];
    this.data = arrayOfFloat;
    arrayOfFloat[0] = paramFloat;
  }
  
  public VectorF(float paramFloat1, float paramFloat2) {
    float[] arrayOfFloat = new float[2];
    this.data = arrayOfFloat;
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[1] = paramFloat2;
  }
  
  public VectorF(float paramFloat1, float paramFloat2, float paramFloat3) {
    float[] arrayOfFloat = new float[3];
    this.data = arrayOfFloat;
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[1] = paramFloat2;
    arrayOfFloat[2] = paramFloat3;
  }
  
  public VectorF(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    float[] arrayOfFloat = new float[4];
    this.data = arrayOfFloat;
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[1] = paramFloat2;
    arrayOfFloat[2] = paramFloat3;
    arrayOfFloat[3] = paramFloat4;
  }
  
  public VectorF(float[] paramArrayOffloat) {
    this.data = paramArrayOffloat;
  }
  
  protected static RuntimeException dimensionsError(int paramInt) {
    return new IllegalArgumentException(String.format("vector dimensions are incorrect: length=%d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public static VectorF length(int paramInt) {
    return new VectorF(new float[paramInt]);
  }
  
  public void add(VectorF paramVectorF) {
    if (length() == paramVectorF.length()) {
      for (int i = 0; i < length(); i++)
        put(i, get(i) + paramVectorF.get(i)); 
      return;
    } 
    throw dimensionsError();
  }
  
  public MatrixF added(MatrixF paramMatrixF) {
    return (new RowMatrixF(this)).added(paramMatrixF);
  }
  
  public VectorF added(VectorF paramVectorF) {
    if (length() == paramVectorF.length()) {
      VectorF vectorF = length(length());
      for (int i = 0; i < length(); i++)
        vectorF.put(i, get(i) + paramVectorF.get(i)); 
      return vectorF;
    } 
    throw dimensionsError();
  }
  
  protected RuntimeException dimensionsError() {
    return dimensionsError(length());
  }
  
  public float dotProduct(VectorF paramVectorF) {
    if (length() == paramVectorF.length()) {
      float f = 0.0F;
      for (int i = 0; i < length(); i++)
        f += get(i) * paramVectorF.get(i); 
      return f;
    } 
    throw dimensionsError();
  }
  
  public float get(int paramInt) {
    return this.data[paramInt];
  }
  
  public float[] getData() {
    return this.data;
  }
  
  public int length() {
    return this.data.length;
  }
  
  public float magnitude() {
    return (float)Math.sqrt(dotProduct(this));
  }
  
  public MatrixF multiplied(MatrixF paramMatrixF) {
    return (new RowMatrixF(this)).multiplied(paramMatrixF);
  }
  
  public VectorF multiplied(float paramFloat) {
    VectorF vectorF = length(length());
    for (int i = 0; i < length(); i++)
      vectorF.put(i, get(i) * paramFloat); 
    return vectorF;
  }
  
  public void multiply(float paramFloat) {
    for (int i = 0; i < length(); i++)
      put(i, get(i) * paramFloat); 
  }
  
  public VectorF normalized3D() {
    if (length() == 3)
      return this; 
    if (length() == 4) {
      float[] arrayOfFloat = this.data;
      return new VectorF(arrayOfFloat[0] / arrayOfFloat[3], arrayOfFloat[1] / arrayOfFloat[3], arrayOfFloat[2] / arrayOfFloat[3]);
    } 
    throw dimensionsError();
  }
  
  public void put(int paramInt, float paramFloat) {
    this.data[paramInt] = paramFloat;
  }
  
  public void subtract(VectorF paramVectorF) {
    if (length() == paramVectorF.length()) {
      for (int i = 0; i < length(); i++)
        put(i, get(i) - paramVectorF.get(i)); 
      return;
    } 
    throw dimensionsError();
  }
  
  public MatrixF subtracted(MatrixF paramMatrixF) {
    return (new RowMatrixF(this)).subtracted(paramMatrixF);
  }
  
  public VectorF subtracted(VectorF paramVectorF) {
    if (length() == paramVectorF.length()) {
      VectorF vectorF = length(length());
      for (int i = 0; i < length(); i++)
        vectorF.put(i, get(i) - paramVectorF.get(i)); 
      return vectorF;
    } 
    throw dimensionsError();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{");
    for (int i = 0; i < length(); i++) {
      if (i > 0)
        stringBuilder.append(" "); 
      stringBuilder.append(String.format("%.2f", new Object[] { Float.valueOf(this.data[i]) }));
    } 
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\matrices\VectorF.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */