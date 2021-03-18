package org.firstinspires.ftc.robotcore.external.matrices;

public class ColumnMatrixF extends MatrixF {
  VectorF vector;
  
  public ColumnMatrixF(VectorF paramVectorF) {
    super(paramVectorF.length(), 1);
    this.vector = paramVectorF;
  }
  
  public MatrixF emptyMatrix(int paramInt1, int paramInt2) {
    return new GeneralMatrixF(paramInt1, paramInt2);
  }
  
  public float get(int paramInt1, int paramInt2) {
    return this.vector.get(paramInt1);
  }
  
  public void put(int paramInt1, int paramInt2, float paramFloat) {
    this.vector.put(paramInt1, paramFloat);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\matrices\ColumnMatrixF.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */