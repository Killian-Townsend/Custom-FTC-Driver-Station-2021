package org.firstinspires.ftc.robotcore.external.matrices;

public class RowMatrixF extends MatrixF {
  VectorF vector;
  
  public RowMatrixF(VectorF paramVectorF) {
    super(1, paramVectorF.length());
    this.vector = paramVectorF;
  }
  
  public MatrixF emptyMatrix(int paramInt1, int paramInt2) {
    return new GeneralMatrixF(paramInt1, paramInt2);
  }
  
  public float get(int paramInt1, int paramInt2) {
    return this.vector.get(paramInt2);
  }
  
  public void put(int paramInt1, int paramInt2, float paramFloat) {
    this.vector.put(paramInt2, paramFloat);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\matrices\RowMatrixF.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */