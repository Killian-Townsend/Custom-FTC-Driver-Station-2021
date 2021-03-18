package org.firstinspires.ftc.robotcore.external.matrices;

import android.opengl.Matrix;
import com.vuforia.Matrix44F;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class OpenGLMatrix extends ColumnMajorMatrixF {
  float[] data;
  
  public OpenGLMatrix() {
    super(4, 4);
    float[] arrayOfFloat = new float[16];
    this.data = arrayOfFloat;
    Matrix.setIdentityM(arrayOfFloat, 0);
  }
  
  public OpenGLMatrix(Matrix44F paramMatrix44F) {
    this(paramMatrix44F.getData());
  }
  
  public OpenGLMatrix(MatrixF paramMatrixF) {
    this();
    if (paramMatrixF.numRows <= 4 && paramMatrixF.numCols <= 4) {
      for (int i = 0; i < Math.min(4, paramMatrixF.numRows); i++) {
        for (int j = 0; j < Math.min(4, paramMatrixF.numCols); j++)
          put(i, j, paramMatrixF.get(i, j)); 
      } 
      return;
    } 
    throw paramMatrixF.dimensionsError();
  }
  
  public OpenGLMatrix(float[] paramArrayOffloat) {
    super(4, 4);
    this.data = paramArrayOffloat;
    if (paramArrayOffloat.length == 16)
      return; 
    throw dimensionsError();
  }
  
  public static OpenGLMatrix identityMatrix() {
    return new OpenGLMatrix();
  }
  
  public static OpenGLMatrix rotation(AngleUnit paramAngleUnit, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    float[] arrayOfFloat = new float[16];
    Matrix.setRotateM(arrayOfFloat, 0, paramAngleUnit.toDegrees(paramFloat1), paramFloat2, paramFloat3, paramFloat4);
    return new OpenGLMatrix(arrayOfFloat);
  }
  
  public static OpenGLMatrix rotation(AxesReference paramAxesReference, AxesOrder paramAxesOrder, AngleUnit paramAngleUnit, float paramFloat1, float paramFloat2, float paramFloat3) {
    OpenGLMatrix openGLMatrix = Orientation.getRotationMatrix(paramAxesReference, paramAxesOrder, paramAngleUnit, paramFloat1, paramFloat2, paramFloat3);
    return identityMatrix().multiplied(openGLMatrix);
  }
  
  public static OpenGLMatrix translation(float paramFloat1, float paramFloat2, float paramFloat3) {
    OpenGLMatrix openGLMatrix = new OpenGLMatrix();
    openGLMatrix.translate(paramFloat1, paramFloat2, paramFloat3);
    return openGLMatrix;
  }
  
  public MatrixF emptyMatrix(int paramInt1, int paramInt2) {
    return (MatrixF)((paramInt1 == 4 && paramInt2 == 4) ? new OpenGLMatrix() : new GeneralMatrixF(paramInt1, paramInt2));
  }
  
  public float[] getData() {
    return this.data;
  }
  
  public OpenGLMatrix inverted() {
    OpenGLMatrix openGLMatrix = new OpenGLMatrix();
    Matrix.invertM(openGLMatrix.data, 0, this.data, 0);
    return openGLMatrix;
  }
  
  public MatrixF multiplied(MatrixF paramMatrixF) {
    return (paramMatrixF instanceof OpenGLMatrix) ? multiplied((OpenGLMatrix)paramMatrixF) : super.multiplied(paramMatrixF);
  }
  
  public OpenGLMatrix multiplied(OpenGLMatrix paramOpenGLMatrix) {
    OpenGLMatrix openGLMatrix = new OpenGLMatrix();
    Matrix.multiplyMM(openGLMatrix.data, 0, this.data, 0, paramOpenGLMatrix.getData(), 0);
    return openGLMatrix;
  }
  
  public void multiply(MatrixF paramMatrixF) {
    if (paramMatrixF instanceof OpenGLMatrix) {
      multiply((OpenGLMatrix)paramMatrixF);
      return;
    } 
    super.multiply(paramMatrixF);
  }
  
  public void multiply(OpenGLMatrix paramOpenGLMatrix) {
    this.data = multiplied(paramOpenGLMatrix).getData();
  }
  
  public void rotate(AngleUnit paramAngleUnit, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    Matrix.rotateM(this.data, 0, paramAngleUnit.toDegrees(paramFloat1), paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void rotate(AxesReference paramAxesReference, AxesOrder paramAxesOrder, AngleUnit paramAngleUnit, float paramFloat1, float paramFloat2, float paramFloat3) {
    this.data = multiplied(Orientation.getRotationMatrix(paramAxesReference, paramAxesOrder, paramAngleUnit, paramFloat1, paramFloat2, paramFloat3)).getData();
  }
  
  public OpenGLMatrix rotated(AngleUnit paramAngleUnit, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    OpenGLMatrix openGLMatrix = new OpenGLMatrix();
    Matrix.rotateM(openGLMatrix.data, 0, this.data, 0, paramAngleUnit.toDegrees(paramFloat1), paramFloat2, paramFloat3, paramFloat4);
    return openGLMatrix;
  }
  
  public OpenGLMatrix rotated(AxesReference paramAxesReference, AxesOrder paramAxesOrder, AngleUnit paramAngleUnit, float paramFloat1, float paramFloat2, float paramFloat3) {
    return multiplied(Orientation.getRotationMatrix(paramAxesReference, paramAxesOrder, paramAngleUnit, paramFloat1, paramFloat2, paramFloat3));
  }
  
  public void scale(float paramFloat) {
    scale(paramFloat, paramFloat, paramFloat);
  }
  
  public void scale(float paramFloat1, float paramFloat2, float paramFloat3) {
    Matrix.scaleM(this.data, 0, paramFloat1, paramFloat2, paramFloat3);
  }
  
  public OpenGLMatrix scaled(float paramFloat) {
    return scaled(paramFloat, paramFloat, paramFloat);
  }
  
  public OpenGLMatrix scaled(float paramFloat1, float paramFloat2, float paramFloat3) {
    OpenGLMatrix openGLMatrix = new OpenGLMatrix();
    Matrix.scaleM(openGLMatrix.data, 0, this.data, 0, paramFloat1, paramFloat2, paramFloat3);
    return openGLMatrix;
  }
  
  public void translate(float paramFloat1, float paramFloat2, float paramFloat3) {
    Matrix.translateM(this.data, 0, paramFloat1, paramFloat2, paramFloat3);
  }
  
  public OpenGLMatrix translated(float paramFloat1, float paramFloat2, float paramFloat3) {
    OpenGLMatrix openGLMatrix = new OpenGLMatrix();
    Matrix.translateM(openGLMatrix.data, 0, this.data, 0, paramFloat1, paramFloat2, paramFloat3);
    return openGLMatrix;
  }
  
  public OpenGLMatrix transposed() {
    return (OpenGLMatrix)super.transposed();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\matrices\OpenGLMatrix.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */