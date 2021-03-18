package org.firstinspires.ftc.robotcore.external.matrices;

public class GeneralMatrixF extends RowMajorMatrixF {
  float[] data;
  
  public GeneralMatrixF(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
    this.data = new float[paramInt1 * paramInt2];
  }
  
  private GeneralMatrixF(int paramInt1, int paramInt2, int paramInt3) {
    super(paramInt1, paramInt2);
  }
  
  public GeneralMatrixF(int paramInt1, int paramInt2, float[] paramArrayOffloat) {
    super(paramInt1, paramInt2);
    if (paramArrayOffloat.length == paramInt1 * paramInt2) {
      this.data = paramArrayOffloat;
      return;
    } 
    throw dimensionsError(paramInt1, paramInt2);
  }
  
  public MatrixF emptyMatrix(int paramInt1, int paramInt2) {
    return new GeneralMatrixF(paramInt1, paramInt2);
  }
  
  public float[] getData() {
    return this.data;
  }
  
  public GeneralMatrixF transposed() {
    return (GeneralMatrixF)super.transposed();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\matrices\GeneralMatrixF.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */