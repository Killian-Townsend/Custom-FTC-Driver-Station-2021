package org.firstinspires.ftc.robotcore.external.matrices;

import java.util.Arrays;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public abstract class MatrixF {
  protected int numCols;
  
  protected int numRows;
  
  public MatrixF(int paramInt1, int paramInt2) {
    this.numRows = paramInt1;
    this.numCols = paramInt2;
    if (paramInt1 > 0 && paramInt2 > 0)
      return; 
    throw dimensionsError();
  }
  
  public static MatrixF diagonalMatrix(int paramInt, float paramFloat) {
    GeneralMatrixF generalMatrixF = new GeneralMatrixF(paramInt, paramInt);
    for (int i = 0; i < paramInt; i++)
      generalMatrixF.put(i, i, paramFloat); 
    return generalMatrixF;
  }
  
  public static MatrixF diagonalMatrix(VectorF paramVectorF) {
    int j = paramVectorF.length();
    GeneralMatrixF generalMatrixF = new GeneralMatrixF(j, j);
    for (int i = 0; i < j; i++)
      generalMatrixF.put(i, i, paramVectorF.get(i)); 
    return generalMatrixF;
  }
  
  protected static RuntimeException dimensionsError(int paramInt1, int paramInt2) {
    return new IllegalArgumentException(String.format("matrix dimensions are incorrect: rows=%d cols=%d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }));
  }
  
  public static MatrixF identityMatrix(int paramInt) {
    return diagonalMatrix(paramInt, 1.0F);
  }
  
  protected VectorF adaptHomogeneous(VectorF paramVectorF) {
    float[] arrayOfFloat;
    int i = this.numCols;
    if (i == 4) {
      if (paramVectorF.length() == 3) {
        arrayOfFloat = Arrays.copyOf(paramVectorF.getData(), 4);
        arrayOfFloat[3] = 1.0F;
        return new VectorF(arrayOfFloat);
      } 
    } else if (i == 3 && arrayOfFloat.length() == 4) {
      return new VectorF(Arrays.copyOf(arrayOfFloat.normalized3D().getData(), 3));
    } 
    return (VectorF)arrayOfFloat;
  }
  
  public void add(MatrixF paramMatrixF) {
    if (this.numRows == paramMatrixF.numRows && this.numCols == paramMatrixF.numCols) {
      for (int i = 0; i < this.numRows; i++) {
        for (int j = 0; j < this.numCols; j++)
          put(i, j, get(i, j) + paramMatrixF.get(i, j)); 
      } 
      return;
    } 
    throw dimensionsError();
  }
  
  public void add(VectorF paramVectorF) {
    add(new ColumnMatrixF(paramVectorF));
  }
  
  public void add(float[] paramArrayOffloat) {
    add(new VectorF(paramArrayOffloat));
  }
  
  public MatrixF added(MatrixF paramMatrixF) {
    int i = this.numRows;
    if (i == paramMatrixF.numRows) {
      int j = this.numCols;
      if (j == paramMatrixF.numCols) {
        MatrixF matrixF = emptyMatrix(i, j);
        for (i = 0; i < matrixF.numRows; i++) {
          for (j = 0; j < matrixF.numCols; j++)
            matrixF.put(i, j, get(i, j) + paramMatrixF.get(i, j)); 
        } 
        return matrixF;
      } 
    } 
    throw dimensionsError();
  }
  
  public MatrixF added(VectorF paramVectorF) {
    return added(new ColumnMatrixF(paramVectorF));
  }
  
  public MatrixF added(float[] paramArrayOffloat) {
    return added(new VectorF(paramArrayOffloat));
  }
  
  protected RuntimeException dimensionsError() {
    return dimensionsError(this.numRows, this.numCols);
  }
  
  public abstract MatrixF emptyMatrix(int paramInt1, int paramInt2);
  
  public String formatAsTransform() {
    return formatAsTransform(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
  }
  
  public String formatAsTransform(AxesReference paramAxesReference, AxesOrder paramAxesOrder, AngleUnit paramAngleUnit) {
    VectorF vectorF = getTranslation();
    return String.format("%s %s", new Object[] { Orientation.getOrientation(this, paramAxesReference, paramAxesOrder, paramAngleUnit).toString(), vectorF.toString() });
  }
  
  public abstract float get(int paramInt1, int paramInt2);
  
  public VectorF getColumn(int paramInt) {
    VectorF vectorF = VectorF.length(this.numRows);
    for (int i = 0; i < this.numRows; i++)
      vectorF.put(i, get(i, paramInt)); 
    return vectorF;
  }
  
  public VectorF getRow(int paramInt) {
    VectorF vectorF = VectorF.length(this.numCols);
    for (int i = 0; i < this.numCols; i++)
      vectorF.put(i, get(paramInt, i)); 
    return vectorF;
  }
  
  public VectorF getTranslation() {
    return getColumn(3).normalized3D();
  }
  
  public MatrixF inverted() {
    int i = this.numRows;
    if (i == this.numCols) {
      if (i == 4) {
        MatrixF matrixF = emptyMatrix(4, 4);
        float f34 = get(0, 0);
        float f36 = get(0, 1);
        float f53 = get(0, 2);
        float f54 = get(0, 3);
        float f41 = get(1, 0);
        float f40 = get(1, 1);
        float f55 = get(1, 2);
        float f61 = get(1, 3);
        float f37 = get(2, 0);
        float f35 = get(2, 1);
        float f57 = get(2, 2);
        float f52 = get(2, 3);
        float f1 = get(3, 0);
        float f2 = get(3, 1);
        float f3 = get(3, 2);
        float f4 = get(3, 3);
        float f5 = f34 * f40;
        float f6 = f5 * f57;
        float f7 = f34 * f55;
        float f42 = f7 * f52;
        float f8 = f34 * f61;
        float f9 = f8 * f35;
        float f10 = f36 * f41;
        float f11 = f10 * f52;
        float f12 = f36 * f55;
        float f13 = f12 * f37;
        float f14 = f36 * f61;
        float f56 = f14 * f57;
        float f15 = f53 * f41;
        float f16 = f15 * f35;
        float f17 = f53 * f40;
        float f60 = f17 * f52;
        float f43 = f53 * f61;
        float f44 = f43 * f37;
        float f18 = f54 * f41;
        float f45 = f18 * f57;
        float f19 = f54 * f40;
        float f20 = f19 * f37;
        float f46 = f54 * f55;
        float f62 = f46 * f35;
        float f21 = f10 * f57;
        float f22 = f7 * f35;
        float f23 = f17 * f37;
        float f24 = f5 * f52;
        float f25 = f18 * f35;
        float f26 = f14 * f37;
        float f47 = f15 * f52;
        float f48 = f8 * f57;
        float f49 = f46 * f37;
        float f63 = f12 * f52;
        float f64 = f19 * f57;
        float f65 = f43 * f35;
        float f27 = f6 * f4 + f42 * f2 + f9 * f3 + f11 * f3 + f13 * f4 + f56 * f1 + f16 * f4 + f60 * f1 + f44 * f2 + f45 * f2 + f20 * f3 + f62 * f1 - f21 * f4 - f22 * f4 - f23 * f4 - f24 * f3 - f25 * f3 - f26 * f3 - f47 * f2 - f48 * f2 - f49 * f2 - f63 * f1 - f64 * f1 - f65 * f1;
        float f28 = f40 * f57;
        float f58 = f55 * f52;
        float f38 = f61 * f35;
        float f29 = f55 * f35;
        float f39 = f40 * f52;
        float f59 = f61 * f57;
        matrixF.put(0, 0, (f28 * f4 + f58 * f2 + f38 * f3 - f29 * f4 - f39 * f3 - f59 * f2) / f27);
        float f30 = f36 * f52;
        float f31 = f53 * f35;
        float f50 = f54 * f57;
        float f32 = f36 * f57;
        float f33 = f54 * f35;
        float f51 = f53 * f52;
        matrixF.put(0, 1, (f30 * f3 + f31 * f4 + f50 * f2 - f32 * f4 - f33 * f3 - f51 * f2) / f27);
        matrixF.put(0, 2, (f12 * f4 + f43 * f2 + f19 * f3 - f17 * f4 - f14 * f3 - f46 * f2) / f27);
        matrixF.put(0, 3, (f56 + f60 + f62 - f63 - f64 - f65) / f27);
        f60 = f41 * f52;
        f55 *= f37;
        f56 = f41 * f57;
        f61 *= f37;
        matrixF.put(1, 0, (f60 * f3 + f55 * f4 + f59 * f1 - f56 * f4 - f61 * f3 - f58 * f1) / f27);
        f57 *= f34;
        f54 *= f37;
        f53 *= f37;
        f52 *= f34;
        matrixF.put(1, 1, (f57 * f4 + f51 * f1 + f54 * f3 - f53 * f4 - f52 * f3 - f50 * f1) / f27);
        matrixF.put(1, 2, (f8 * f3 + f15 * f4 + f46 * f1 - f7 * f4 - f18 * f3 - f43 * f1) / f27);
        matrixF.put(1, 3, (f42 + f44 + f45 - f47 - f48 - f49) / f27);
        f41 *= f35;
        f40 *= f37;
        matrixF.put(2, 0, (f41 * f4 + f39 * f1 + f61 * f2 - f40 * f4 - f60 * f2 - f38 * f1) / f27);
        f36 *= f37;
        f34 *= f35;
        matrixF.put(2, 1, (f52 * f2 + f36 * f4 + f33 * f1 - f34 * f4 - f54 * f2 - f30 * f1) / f27);
        matrixF.put(2, 2, (f5 * f4 + f14 * f1 + f18 * f2 - f4 * f10 - f8 * f2 - f19 * f1) / f27);
        matrixF.put(2, 3, (f9 + f11 + f20 - f24 - f25 - f26) / f27);
        matrixF.put(3, 0, (f56 * f2 + f40 * f3 + f29 * f1 - f41 * f3 - f55 * f2 - f28 * f1) / f27);
        matrixF.put(3, 1, (f34 * f3 + f32 * f1 + f53 * f2 - f36 * f3 - f57 * f2 - f31 * f1) / f27);
        matrixF.put(3, 2, (f7 * f2 + f10 * f3 + f17 * f1 - f5 * f3 - f15 * f2 - f12 * f1) / f27);
        matrixF.put(3, 3, (f6 + f13 + f16 - f21 - f22 - f23) / f27);
        return matrixF;
      } 
      if (i == 3) {
        MatrixF matrixF = emptyMatrix(3, 3);
        float f1 = get(0, 0);
        float f2 = get(0, 1);
        float f3 = get(0, 2);
        float f4 = get(1, 0);
        float f5 = get(1, 1);
        float f6 = get(1, 2);
        float f7 = get(2, 0);
        float f8 = get(2, 1);
        float f9 = get(2, 2);
        float f10 = f1 * f5;
        float f11 = f2 * f6;
        float f12 = f3 * f4;
        float f13 = f2 * f4;
        float f14 = f1 * f6;
        float f15 = f3 * f5;
        float f16 = f10 * f9 + f11 * f7 + f12 * f8 - f13 * f9 - f14 * f8 - f15 * f7;
        matrixF.put(0, 0, (f5 * f9 - f6 * f8) / f16);
        matrixF.put(0, 1, (f3 * f8 - f2 * f9) / f16);
        matrixF.put(0, 2, (f11 - f15) / f16);
        matrixF.put(1, 0, (f6 * f7 - f4 * f9) / f16);
        matrixF.put(1, 1, (f9 * f1 - f3 * f7) / f16);
        matrixF.put(1, 2, (f12 - f14) / f16);
        matrixF.put(2, 0, (f4 * f8 - f5 * f7) / f16);
        matrixF.put(2, 1, (f2 * f7 - f1 * f8) / f16);
        matrixF.put(2, 2, (f10 - f13) / f16);
        return matrixF;
      } 
      if (i == 2) {
        MatrixF matrixF = emptyMatrix(4, 4);
        float f1 = get(0, 0);
        float f2 = get(0, 1);
        float f3 = get(1, 0);
        float f4 = get(1, 1);
        float f5 = f1 * f4 - f2 * f3;
        matrixF.put(0, 0, f4 / f5);
        matrixF.put(0, 1, -f2 / f5);
        matrixF.put(1, 0, -f3 / f5);
        matrixF.put(1, 1, f1 / f5);
        return matrixF;
      } 
      if (i == 1) {
        MatrixF matrixF = emptyMatrix(4, 4);
        matrixF.put(0, 0, 1.0F / get(0, 0));
        return matrixF;
      } 
      throw dimensionsError();
    } 
    throw dimensionsError();
  }
  
  public MatrixF multiplied(float paramFloat) {
    MatrixF matrixF = emptyMatrix(this.numCols, this.numRows);
    for (int i = 0; i < matrixF.numRows; i++) {
      for (int j = 0; j < matrixF.numCols; j++)
        matrixF.put(i, j, get(i, j) * paramFloat); 
    } 
    return matrixF;
  }
  
  public MatrixF multiplied(MatrixF paramMatrixF) {
    if (this.numCols == paramMatrixF.numRows) {
      MatrixF matrixF = emptyMatrix(this.numRows, paramMatrixF.numCols);
      for (int i = 0; i < matrixF.numRows; i++) {
        int j;
        for (j = 0; j < matrixF.numCols; j++) {
          float f = 0.0F;
          int k;
          for (k = 0; k < this.numCols; k++)
            f += get(i, k) * paramMatrixF.get(k, j); 
          matrixF.put(i, j, f);
        } 
      } 
      return matrixF;
    } 
    throw dimensionsError();
  }
  
  public VectorF multiplied(VectorF paramVectorF) {
    return multiplied(new ColumnMatrixF(paramVectorF)).toVector();
  }
  
  public VectorF multiplied(float[] paramArrayOffloat) {
    return multiplied(new VectorF(paramArrayOffloat));
  }
  
  public void multiply(float paramFloat) {
    for (int i = 0; i < this.numRows; i++) {
      for (int j = 0; j < this.numCols; j++)
        put(i, j, get(i, j) * paramFloat); 
    } 
  }
  
  public void multiply(MatrixF paramMatrixF) {
    int i = this.numCols;
    int j = paramMatrixF.numRows;
    if (i == j) {
      if (j == paramMatrixF.numCols) {
        paramMatrixF = multiplied(paramMatrixF);
        for (i = 0; i < this.numRows; i++) {
          for (j = 0; j < this.numCols; j++)
            put(i, j, paramMatrixF.get(i, j)); 
        } 
        return;
      } 
      throw dimensionsError();
    } 
    throw dimensionsError();
  }
  
  public void multiply(VectorF paramVectorF) {
    paramVectorF = multiplied(new ColumnMatrixF(paramVectorF)).toVector();
    for (int i = 0; i < paramVectorF.length(); i++)
      put(i, 0, paramVectorF.get(i)); 
  }
  
  public void multiply(float[] paramArrayOffloat) {
    VectorF vectorF = multiplied(new VectorF(paramArrayOffloat));
    for (int i = 0; i < vectorF.length(); i++)
      put(i, 0, vectorF.get(i)); 
  }
  
  public int numCols() {
    return this.numCols;
  }
  
  public int numRows() {
    return this.numRows;
  }
  
  public abstract void put(int paramInt1, int paramInt2, float paramFloat);
  
  public SliceMatrixF slice(int paramInt1, int paramInt2) {
    return slice(0, 0, paramInt1, paramInt2);
  }
  
  public SliceMatrixF slice(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    return new SliceMatrixF(this, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void subtract(MatrixF paramMatrixF) {
    if (this.numRows == paramMatrixF.numRows && this.numCols == paramMatrixF.numCols) {
      for (int i = 0; i < this.numRows; i++) {
        for (int j = 0; j < this.numCols; j++)
          put(i, j, get(i, j) - paramMatrixF.get(i, j)); 
      } 
      return;
    } 
    throw dimensionsError();
  }
  
  public void subtract(VectorF paramVectorF) {
    subtract(new ColumnMatrixF(paramVectorF));
  }
  
  public void subtract(float[] paramArrayOffloat) {
    subtract(new VectorF(paramArrayOffloat));
  }
  
  public MatrixF subtracted(MatrixF paramMatrixF) {
    int i = this.numRows;
    if (i == paramMatrixF.numRows) {
      int j = this.numCols;
      if (j == paramMatrixF.numCols) {
        MatrixF matrixF = emptyMatrix(i, j);
        for (i = 0; i < matrixF.numRows; i++) {
          for (j = 0; j < matrixF.numCols; j++)
            matrixF.put(i, j, get(i, j) - paramMatrixF.get(i, j)); 
        } 
        return matrixF;
      } 
    } 
    throw dimensionsError();
  }
  
  public MatrixF subtracted(VectorF paramVectorF) {
    return subtracted(new ColumnMatrixF(paramVectorF));
  }
  
  public MatrixF subtracted(float[] paramArrayOffloat) {
    return subtracted(new VectorF(paramArrayOffloat));
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{");
    for (int i = 0; i < this.numRows; i++) {
      if (i > 0)
        stringBuilder.append(","); 
      stringBuilder.append("{");
      for (int j = 0; j < this.numCols; j++) {
        if (j > 0)
          stringBuilder.append(","); 
        stringBuilder.append(String.format("%.3f", new Object[] { Float.valueOf(get(i, j)) }));
      } 
      stringBuilder.append("}");
    } 
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  public VectorF toVector() {
    int i = this.numCols;
    if (i == 1) {
      VectorF vectorF = VectorF.length(this.numRows);
      for (i = 0; i < this.numRows; i++)
        vectorF.put(i, get(i, 0)); 
      return vectorF;
    } 
    if (this.numRows == 1) {
      VectorF vectorF = VectorF.length(i);
      for (i = 0; i < this.numCols; i++)
        vectorF.put(i, get(0, i)); 
      return vectorF;
    } 
    throw dimensionsError();
  }
  
  public VectorF transform(VectorF paramVectorF) {
    return multiplied(adaptHomogeneous(paramVectorF)).normalized3D();
  }
  
  public MatrixF transposed() {
    MatrixF matrixF = emptyMatrix(this.numCols, this.numRows);
    for (int i = 0; i < matrixF.numRows; i++) {
      for (int j = 0; j < matrixF.numCols; j++)
        matrixF.put(i, j, get(j, i)); 
    } 
    return matrixF;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\matrices\MatrixF.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */