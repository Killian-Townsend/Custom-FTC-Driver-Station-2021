package org.firstinspires.ftc.robotcore.external.matrices;

public abstract class DenseMatrixF extends MatrixF {
  protected DenseMatrixF(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public float get(int paramInt1, int paramInt2) {
    return getData()[indexFromRowCol(paramInt1, paramInt2)];
  }
  
  public abstract float[] getData();
  
  protected abstract int indexFromRowCol(int paramInt1, int paramInt2);
  
  public void put(int paramInt1, int paramInt2, float paramFloat) {
    getData()[indexFromRowCol(paramInt1, paramInt2)] = paramFloat;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\matrices\DenseMatrixF.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */