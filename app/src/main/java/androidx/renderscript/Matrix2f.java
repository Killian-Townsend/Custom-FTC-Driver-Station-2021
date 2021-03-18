package androidx.renderscript;

public class Matrix2f {
  final float[] mMat;
  
  public Matrix2f() {
    this.mMat = new float[4];
    loadIdentity();
  }
  
  public Matrix2f(float[] paramArrayOffloat) {
    float[] arrayOfFloat = new float[4];
    this.mMat = arrayOfFloat;
    System.arraycopy(paramArrayOffloat, 0, arrayOfFloat, 0, arrayOfFloat.length);
  }
  
  public float get(int paramInt1, int paramInt2) {
    return this.mMat[paramInt1 * 2 + paramInt2];
  }
  
  public float[] getArray() {
    return this.mMat;
  }
  
  public void load(Matrix2f paramMatrix2f) {
    float[] arrayOfFloat1 = paramMatrix2f.getArray();
    float[] arrayOfFloat2 = this.mMat;
    System.arraycopy(arrayOfFloat1, 0, arrayOfFloat2, 0, arrayOfFloat2.length);
  }
  
  public void loadIdentity() {
    float[] arrayOfFloat = this.mMat;
    arrayOfFloat[0] = 1.0F;
    arrayOfFloat[1] = 0.0F;
    arrayOfFloat[2] = 0.0F;
    arrayOfFloat[3] = 1.0F;
  }
  
  public void loadMultiply(Matrix2f paramMatrix2f1, Matrix2f paramMatrix2f2) {
    int i;
    for (i = 0; i < 2; i++) {
      float f2 = 0.0F;
      int j = 0;
      float f1 = 0.0F;
      while (j < 2) {
        float f = paramMatrix2f2.get(i, j);
        f2 += paramMatrix2f1.get(j, 0) * f;
        f1 += paramMatrix2f1.get(j, 1) * f;
        j++;
      } 
      set(i, 0, f2);
      set(i, 1, f1);
    } 
  }
  
  public void loadRotate(float paramFloat) {
    double d = (paramFloat * 0.017453292F);
    paramFloat = (float)Math.cos(d);
    float f = (float)Math.sin(d);
    float[] arrayOfFloat = this.mMat;
    arrayOfFloat[0] = paramFloat;
    arrayOfFloat[1] = -f;
    arrayOfFloat[2] = f;
    arrayOfFloat[3] = paramFloat;
  }
  
  public void loadScale(float paramFloat1, float paramFloat2) {
    loadIdentity();
    float[] arrayOfFloat = this.mMat;
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[3] = paramFloat2;
  }
  
  public void multiply(Matrix2f paramMatrix2f) {
    Matrix2f matrix2f = new Matrix2f();
    matrix2f.loadMultiply(this, paramMatrix2f);
    load(matrix2f);
  }
  
  public void rotate(float paramFloat) {
    Matrix2f matrix2f = new Matrix2f();
    matrix2f.loadRotate(paramFloat);
    multiply(matrix2f);
  }
  
  public void scale(float paramFloat1, float paramFloat2) {
    Matrix2f matrix2f = new Matrix2f();
    matrix2f.loadScale(paramFloat1, paramFloat2);
    multiply(matrix2f);
  }
  
  public void set(int paramInt1, int paramInt2, float paramFloat) {
    this.mMat[paramInt1 * 2 + paramInt2] = paramFloat;
  }
  
  public void transpose() {
    float[] arrayOfFloat = this.mMat;
    float f = arrayOfFloat[1];
    arrayOfFloat[1] = arrayOfFloat[2];
    arrayOfFloat[2] = f;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\Matrix2f.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */