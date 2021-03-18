package org.firstinspires.ftc.robotcore.internal.vuforia;

import com.vuforia.Matrix34F;
import com.vuforia.Tool;
import org.firstinspires.ftc.robotcore.external.matrices.GeneralMatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.RowMajorMatrixF;

public class VuforiaPoseMatrix extends RowMajorMatrixF {
  float[] data = new float[12];
  
  public VuforiaPoseMatrix() {
    super(3, 4);
  }
  
  public VuforiaPoseMatrix(Matrix34F paramMatrix34F) {
    super(3, 4);
  }
  
  public MatrixF emptyMatrix(int paramInt1, int paramInt2) {
    return (MatrixF)((paramInt1 == 3 && paramInt2 == 4) ? new VuforiaPoseMatrix() : new GeneralMatrixF(paramInt1, paramInt2));
  }
  
  public float[] getData() {
    return this.data;
  }
  
  public Matrix34F getMatrix34F() {
    Matrix34F matrix34F = new Matrix34F();
    matrix34F.setData(this.data);
    return matrix34F;
  }
  
  public OpenGLMatrix toOpenGL() {
    return new OpenGLMatrix(Tool.convertPose2GLMatrix(getMatrix34F()));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\VuforiaPoseMatrix.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */