package org.firstinspires.ftc.robotcore.external.matrices;

public abstract class ColumnMajorMatrixF extends DenseMatrixF {
  public ColumnMajorMatrixF(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  protected int indexFromRowCol(int paramInt1, int paramInt2) {
    return paramInt2 * this.numRows + paramInt1;
  }
  
  public VectorF toVector() {
    return new VectorF(getData());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\matrices\ColumnMajorMatrixF.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */