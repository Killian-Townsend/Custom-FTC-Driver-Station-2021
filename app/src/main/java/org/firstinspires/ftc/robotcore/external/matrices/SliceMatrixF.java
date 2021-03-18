package org.firstinspires.ftc.robotcore.external.matrices;

public class SliceMatrixF extends MatrixF {
  protected int col;
  
  protected MatrixF matrix;
  
  protected int row;
  
  public SliceMatrixF(MatrixF paramMatrixF, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super(paramInt3, paramInt4);
    this.matrix = paramMatrixF;
    this.row = paramInt1;
    this.col = paramInt2;
    if (paramInt1 + paramInt3 < paramMatrixF.numRows) {
      if (paramInt2 + paramInt4 < paramMatrixF.numCols)
        return; 
      throw dimensionsError();
    } 
    throw dimensionsError();
  }
  
  public MatrixF emptyMatrix(int paramInt1, int paramInt2) {
    return this.matrix.emptyMatrix(paramInt1, paramInt2);
  }
  
  public float get(int paramInt1, int paramInt2) {
    return this.matrix.get(this.row + paramInt1, this.col + paramInt2);
  }
  
  public void put(int paramInt1, int paramInt2, float paramFloat) {
    this.matrix.put(this.row + paramInt1, this.col + paramInt2, paramFloat);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\matrices\SliceMatrixF.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */