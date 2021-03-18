package org.firstinspires.ftc.robotcore.external.matrices;

public abstract class RowMajorMatrixF extends DenseMatrixF {
  public RowMajorMatrixF(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  protected int indexFromRowCol(int paramInt1, int paramInt2) {
    return paramInt1 * this.numCols + paramInt2;
  }
  
  public VectorF toVector() {
    return new VectorF(getData());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\matrices\RowMajorMatrixF.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */