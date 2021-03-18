package androidx.renderscript;

public class Matrix4f {
  final float[] mMat;
  
  public Matrix4f() {
    this.mMat = new float[16];
    loadIdentity();
  }
  
  public Matrix4f(float[] paramArrayOffloat) {
    float[] arrayOfFloat = new float[16];
    this.mMat = arrayOfFloat;
    System.arraycopy(paramArrayOffloat, 0, arrayOfFloat, 0, arrayOfFloat.length);
  }
  
  private float computeCofactor(int paramInt1, int paramInt2) {
    int i = (paramInt1 + 1) % 4;
    int i4 = (paramInt1 + 2) % 4;
    int n = (paramInt1 + 3) % 4;
    float[] arrayOfFloat = this.mMat;
    int i1 = (paramInt2 + 1) % 4 * 4;
    float f1 = arrayOfFloat[i + i1];
    int i5 = (paramInt2 + 2) % 4 * 4;
    int j = i4 + i5;
    float f2 = arrayOfFloat[j];
    int k = (paramInt2 + 3) % 4 * 4;
    int i2 = n + k;
    float f3 = arrayOfFloat[i2];
    int i3 = i4 + k;
    float f4 = arrayOfFloat[i3];
    int m = n + i5;
    float f5 = arrayOfFloat[m];
    float f6 = arrayOfFloat[i5 + i];
    i4 += i1;
    float f7 = arrayOfFloat[i4];
    float f8 = arrayOfFloat[i2];
    float f9 = arrayOfFloat[i3];
    n += i1;
    f2 = f1 * (f2 * f3 - f4 * f5) - f6 * (f7 * f8 - f9 * arrayOfFloat[n]) + arrayOfFloat[i + k] * (arrayOfFloat[i4] * arrayOfFloat[m] - arrayOfFloat[j] * arrayOfFloat[n]);
    f1 = f2;
    if ((paramInt1 + paramInt2 & 0x1) != 0)
      f1 = -f2; 
    return f1;
  }
  
  public float get(int paramInt1, int paramInt2) {
    return this.mMat[paramInt1 * 4 + paramInt2];
  }
  
  public float[] getArray() {
    return this.mMat;
  }
  
  public boolean inverse() {
    Matrix4f matrix4f = new Matrix4f();
    boolean bool = false;
    int i;
    for (i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++)
        matrix4f.mMat[i * 4 + j] = computeCofactor(i, j); 
    } 
    float[] arrayOfFloat1 = this.mMat;
    float f = arrayOfFloat1[0];
    float[] arrayOfFloat2 = matrix4f.mMat;
    f = f * arrayOfFloat2[0] + arrayOfFloat1[4] * arrayOfFloat2[1] + arrayOfFloat1[8] * arrayOfFloat2[2] + arrayOfFloat1[12] * arrayOfFloat2[3];
    if (Math.abs(f) < 1.0E-6D)
      return false; 
    f = 1.0F / f;
    for (i = bool; i < 16; i++)
      this.mMat[i] = matrix4f.mMat[i] * f; 
    return true;
  }
  
  public boolean inverseTranspose() {
    Matrix4f matrix4f = new Matrix4f();
    boolean bool = false;
    int i;
    for (i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++)
        matrix4f.mMat[j * 4 + i] = computeCofactor(i, j); 
    } 
    float[] arrayOfFloat1 = this.mMat;
    float f = arrayOfFloat1[0];
    float[] arrayOfFloat2 = matrix4f.mMat;
    f = f * arrayOfFloat2[0] + arrayOfFloat1[4] * arrayOfFloat2[4] + arrayOfFloat1[8] * arrayOfFloat2[8] + arrayOfFloat1[12] * arrayOfFloat2[12];
    if (Math.abs(f) < 1.0E-6D)
      return false; 
    f = 1.0F / f;
    for (i = bool; i < 16; i++)
      this.mMat[i] = matrix4f.mMat[i] * f; 
    return true;
  }
  
  public void load(Matrix3f paramMatrix3f) {
    this.mMat[0] = paramMatrix3f.mMat[0];
    this.mMat[1] = paramMatrix3f.mMat[1];
    this.mMat[2] = paramMatrix3f.mMat[2];
    float[] arrayOfFloat2 = this.mMat;
    arrayOfFloat2[3] = 0.0F;
    arrayOfFloat2[4] = paramMatrix3f.mMat[3];
    this.mMat[5] = paramMatrix3f.mMat[4];
    this.mMat[6] = paramMatrix3f.mMat[5];
    arrayOfFloat2 = this.mMat;
    arrayOfFloat2[7] = 0.0F;
    arrayOfFloat2[8] = paramMatrix3f.mMat[6];
    this.mMat[9] = paramMatrix3f.mMat[7];
    this.mMat[10] = paramMatrix3f.mMat[8];
    float[] arrayOfFloat1 = this.mMat;
    arrayOfFloat1[11] = 0.0F;
    arrayOfFloat1[12] = 0.0F;
    arrayOfFloat1[13] = 0.0F;
    arrayOfFloat1[14] = 0.0F;
    arrayOfFloat1[15] = 1.0F;
  }
  
  public void load(Matrix4f paramMatrix4f) {
    float[] arrayOfFloat1 = paramMatrix4f.getArray();
    float[] arrayOfFloat2 = this.mMat;
    System.arraycopy(arrayOfFloat1, 0, arrayOfFloat2, 0, arrayOfFloat2.length);
  }
  
  public void loadFrustum(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    loadIdentity();
    float[] arrayOfFloat = this.mMat;
    float f1 = 2.0F * paramFloat5;
    float f2 = paramFloat2 - paramFloat1;
    arrayOfFloat[0] = f1 / f2;
    float f3 = paramFloat4 - paramFloat3;
    arrayOfFloat[5] = f1 / f3;
    arrayOfFloat[8] = (paramFloat2 + paramFloat1) / f2;
    arrayOfFloat[9] = (paramFloat4 + paramFloat3) / f3;
    paramFloat1 = -(paramFloat6 + paramFloat5);
    paramFloat2 = paramFloat6 - paramFloat5;
    arrayOfFloat[10] = paramFloat1 / paramFloat2;
    arrayOfFloat[11] = -1.0F;
    arrayOfFloat[14] = paramFloat6 * -2.0F * paramFloat5 / paramFloat2;
    arrayOfFloat[15] = 0.0F;
  }
  
  public void loadIdentity() {
    float[] arrayOfFloat = this.mMat;
    arrayOfFloat[0] = 1.0F;
    arrayOfFloat[1] = 0.0F;
    arrayOfFloat[2] = 0.0F;
    arrayOfFloat[3] = 0.0F;
    arrayOfFloat[4] = 0.0F;
    arrayOfFloat[5] = 1.0F;
    arrayOfFloat[6] = 0.0F;
    arrayOfFloat[7] = 0.0F;
    arrayOfFloat[8] = 0.0F;
    arrayOfFloat[9] = 0.0F;
    arrayOfFloat[10] = 1.0F;
    arrayOfFloat[11] = 0.0F;
    arrayOfFloat[12] = 0.0F;
    arrayOfFloat[13] = 0.0F;
    arrayOfFloat[14] = 0.0F;
    arrayOfFloat[15] = 1.0F;
  }
  
  public void loadMultiply(Matrix4f paramMatrix4f1, Matrix4f paramMatrix4f2) {
    int i;
    for (i = 0; i < 4; i++) {
      float f4 = 0.0F;
      int j = 0;
      float f3 = 0.0F;
      float f1 = f3;
      float f2 = f1;
      while (j < 4) {
        float f = paramMatrix4f2.get(i, j);
        f4 += paramMatrix4f1.get(j, 0) * f;
        f3 += paramMatrix4f1.get(j, 1) * f;
        f1 += paramMatrix4f1.get(j, 2) * f;
        f2 += paramMatrix4f1.get(j, 3) * f;
        j++;
      } 
      set(i, 0, f4);
      set(i, 1, f3);
      set(i, 2, f1);
      set(i, 3, f2);
    } 
  }
  
  public void loadOrtho(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    loadIdentity();
    float[] arrayOfFloat = this.mMat;
    float f1 = paramFloat2 - paramFloat1;
    arrayOfFloat[0] = 2.0F / f1;
    float f2 = paramFloat4 - paramFloat3;
    arrayOfFloat[5] = 2.0F / f2;
    float f3 = paramFloat6 - paramFloat5;
    arrayOfFloat[10] = -2.0F / f3;
    arrayOfFloat[12] = -(paramFloat2 + paramFloat1) / f1;
    arrayOfFloat[13] = -(paramFloat4 + paramFloat3) / f2;
    arrayOfFloat[14] = -(paramFloat6 + paramFloat5) / f3;
  }
  
  public void loadOrthoWindow(int paramInt1, int paramInt2) {
    loadOrtho(0.0F, paramInt1, paramInt2, 0.0F, -1.0F, 1.0F);
  }
  
  public void loadPerspective(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    paramFloat1 = paramFloat3 * (float)Math.tan((float)(paramFloat1 * Math.PI / 360.0D));
    float f = -paramFloat1;
    loadFrustum(f * paramFloat2, paramFloat1 * paramFloat2, f, paramFloat1, paramFloat3, paramFloat4);
  }
  
  public void loadProjectionNormalized(int paramInt1, int paramInt2) {
    Matrix4f matrix4f1 = new Matrix4f();
    Matrix4f matrix4f2 = new Matrix4f();
    if (paramInt1 > paramInt2) {
      float f = paramInt1 / paramInt2;
      matrix4f1.loadFrustum(-f, f, -1.0F, 1.0F, 1.0F, 100.0F);
    } else {
      float f = paramInt2 / paramInt1;
      matrix4f1.loadFrustum(-1.0F, 1.0F, -f, f, 1.0F, 100.0F);
    } 
    matrix4f2.loadRotate(180.0F, 0.0F, 1.0F, 0.0F);
    matrix4f1.loadMultiply(matrix4f1, matrix4f2);
    matrix4f2.loadScale(-2.0F, 2.0F, 1.0F);
    matrix4f1.loadMultiply(matrix4f1, matrix4f2);
    matrix4f2.loadTranslate(0.0F, 0.0F, 2.0F);
    matrix4f1.loadMultiply(matrix4f1, matrix4f2);
    load(matrix4f1);
  }
  
  public void loadRotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    float[] arrayOfFloat = this.mMat;
    arrayOfFloat[3] = 0.0F;
    arrayOfFloat[7] = 0.0F;
    arrayOfFloat[11] = 0.0F;
    arrayOfFloat[12] = 0.0F;
    arrayOfFloat[13] = 0.0F;
    arrayOfFloat[14] = 0.0F;
    arrayOfFloat[15] = 1.0F;
    double d = (paramFloat1 * 0.017453292F);
    float f3 = (float)Math.cos(d);
    float f4 = (float)Math.sin(d);
    float f5 = (float)Math.sqrt((paramFloat2 * paramFloat2 + paramFloat3 * paramFloat3 + paramFloat4 * paramFloat4));
    float f2 = paramFloat2;
    float f1 = paramFloat3;
    paramFloat1 = paramFloat4;
    if (f5 == 1.0F) {
      paramFloat1 = 1.0F / f5;
      f2 = paramFloat2 * paramFloat1;
      f1 = paramFloat3 * paramFloat1;
      paramFloat1 = paramFloat4 * paramFloat1;
    } 
    paramFloat2 = 1.0F - f3;
    paramFloat3 = f2 * f4;
    paramFloat4 = f1 * f4;
    f4 *= paramFloat1;
    arrayOfFloat = this.mMat;
    arrayOfFloat[0] = f2 * f2 * paramFloat2 + f3;
    f5 = f2 * f1 * paramFloat2;
    arrayOfFloat[4] = f5 - f4;
    f2 = paramFloat1 * f2 * paramFloat2;
    arrayOfFloat[8] = f2 + paramFloat4;
    arrayOfFloat[1] = f5 + f4;
    arrayOfFloat[5] = f1 * f1 * paramFloat2 + f3;
    f1 = f1 * paramFloat1 * paramFloat2;
    arrayOfFloat[9] = f1 - paramFloat3;
    arrayOfFloat[2] = f2 - paramFloat4;
    arrayOfFloat[6] = f1 + paramFloat3;
    arrayOfFloat[10] = paramFloat1 * paramFloat1 * paramFloat2 + f3;
  }
  
  public void loadScale(float paramFloat1, float paramFloat2, float paramFloat3) {
    loadIdentity();
    float[] arrayOfFloat = this.mMat;
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[5] = paramFloat2;
    arrayOfFloat[10] = paramFloat3;
  }
  
  public void loadTranslate(float paramFloat1, float paramFloat2, float paramFloat3) {
    loadIdentity();
    float[] arrayOfFloat = this.mMat;
    arrayOfFloat[12] = paramFloat1;
    arrayOfFloat[13] = paramFloat2;
    arrayOfFloat[14] = paramFloat3;
  }
  
  public void multiply(Matrix4f paramMatrix4f) {
    Matrix4f matrix4f = new Matrix4f();
    matrix4f.loadMultiply(this, paramMatrix4f);
    load(matrix4f);
  }
  
  public void rotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    Matrix4f matrix4f = new Matrix4f();
    matrix4f.loadRotate(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    multiply(matrix4f);
  }
  
  public void scale(float paramFloat1, float paramFloat2, float paramFloat3) {
    Matrix4f matrix4f = new Matrix4f();
    matrix4f.loadScale(paramFloat1, paramFloat2, paramFloat3);
    multiply(matrix4f);
  }
  
  public void set(int paramInt1, int paramInt2, float paramFloat) {
    this.mMat[paramInt1 * 4 + paramInt2] = paramFloat;
  }
  
  public void translate(float paramFloat1, float paramFloat2, float paramFloat3) {
    Matrix4f matrix4f = new Matrix4f();
    matrix4f.loadTranslate(paramFloat1, paramFloat2, paramFloat3);
    multiply(matrix4f);
  }
  
  public void transpose() {
    for (int i = 0; i < 3; i = j) {
      int j = i + 1;
      int k;
      for (k = j; k < 4; k++) {
        float[] arrayOfFloat = this.mMat;
        int m = i * 4 + k;
        float f = arrayOfFloat[m];
        int n = k * 4 + i;
        arrayOfFloat[m] = arrayOfFloat[n];
        arrayOfFloat[n] = f;
      } 
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\Matrix4f.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */