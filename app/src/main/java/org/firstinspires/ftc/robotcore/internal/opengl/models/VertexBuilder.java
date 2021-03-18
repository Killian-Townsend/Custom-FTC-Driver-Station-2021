package org.firstinspires.ftc.robotcore.internal.opengl.models;

import android.opengl.GLES20;
import java.util.ArrayList;
import java.util.List;

public class VertexBuilder {
  private static final int coordinatesPerVertex = 3;
  
  private final List<DrawCommand> drawList = new ArrayList<DrawCommand>();
  
  private int offset = 0;
  
  private final float[] vertexData;
  
  private VertexBuilder(int paramInt) {
    this.vertexData = new float[paramInt * 3];
  }
  
  private void appendCircle(Geometry.Circle paramCircle, int paramInt, boolean paramBoolean) {
    final int startVertex = this.offset / 3;
    final int numVertices = sizeOfCircleInVertices(paramInt);
    float[] arrayOfFloat = this.vertexData;
    int i = this.offset;
    this.offset = i + 1;
    arrayOfFloat[i] = paramCircle.center.x;
    arrayOfFloat = this.vertexData;
    i = this.offset;
    this.offset = i + 1;
    arrayOfFloat[i] = paramCircle.center.y;
    arrayOfFloat = this.vertexData;
    i = this.offset;
    this.offset = i + 1;
    arrayOfFloat[i] = paramCircle.center.z;
    for (i = 0; i <= paramInt; i++) {
      if (paramBoolean) {
        m = i;
      } else {
        m = paramInt - i;
      } 
      float f1 = m / paramInt;
      arrayOfFloat = this.vertexData;
      int m = this.offset;
      this.offset = m + 1;
      float f2 = paramCircle.center.x;
      float f3 = paramCircle.radius;
      double d = (f1 * 6.2831855F);
      arrayOfFloat[m] = f2 + f3 * (float)Math.cos(d);
      arrayOfFloat = this.vertexData;
      m = this.offset;
      this.offset = m + 1;
      arrayOfFloat[m] = paramCircle.center.y;
      arrayOfFloat = this.vertexData;
      m = this.offset;
      this.offset = m + 1;
      arrayOfFloat[m] = paramCircle.center.z + paramCircle.radius * (float)Math.sin(d);
    } 
    this.drawList.add(new DrawCommand() {
          public void draw() {
            GLES20.glDrawArrays(6, startVertex, numVertices);
          }
        });
  }
  
  private void appendOpenCylinder(Geometry.Cylinder paramCylinder, int paramInt) {
    final int startVertex = this.offset / 3;
    final int numVertices = sizeOfOpenCylinderInVertices(paramInt);
    float f1 = paramCylinder.center.y;
    float f2 = paramCylinder.height / 2.0F;
    float f3 = paramCylinder.center.y;
    float f4 = paramCylinder.height / 2.0F;
    int i;
    for (i = 0; i <= paramInt; i++) {
      float f5 = i / paramInt;
      float f6 = paramCylinder.center.x;
      float f7 = paramCylinder.radius;
      double d = (f5 * 6.2831855F);
      f5 = f6 + f7 * (float)Math.cos(d);
      f6 = paramCylinder.center.z + paramCylinder.radius * (float)Math.sin(d);
      float[] arrayOfFloat = this.vertexData;
      int n = this.offset;
      int m = n + 1;
      this.offset = m;
      arrayOfFloat[n] = f5;
      n = m + 1;
      this.offset = n;
      arrayOfFloat[m] = f1 - f2;
      m = n + 1;
      this.offset = m;
      arrayOfFloat[n] = f6;
      n = m + 1;
      this.offset = n;
      arrayOfFloat[m] = f5;
      m = n + 1;
      this.offset = m;
      arrayOfFloat[n] = f3 + f4;
      this.offset = m + 1;
      arrayOfFloat[m] = f6;
    } 
    this.drawList.add(new DrawCommand() {
          public void draw() {
            GLES20.glDrawArrays(5, startVertex, numVertices);
          }
        });
  }
  
  private GeneratedData build() {
    return new GeneratedData(this.vertexData, this.drawList);
  }
  
  public static GeneratedData createMallet(Geometry.Point3 paramPoint3, float paramFloat1, float paramFloat2, int paramInt) {
    VertexBuilder vertexBuilder = new VertexBuilder(sizeOfCircleInVertices(paramInt) * 2 + sizeOfOpenCylinderInVertices(paramInt) * 2);
    float f1 = 0.25F * paramFloat2;
    float f2 = -f1;
    Geometry.Circle circle2 = new Geometry.Circle(paramPoint3.translateY(f2), paramFloat1);
    Geometry.Cylinder cylinder2 = new Geometry.Cylinder(circle2.center.translateY(f2 / 2.0F), paramFloat1, f1);
    vertexBuilder.appendCircle(circle2, paramInt, true);
    vertexBuilder.appendOpenCylinder(cylinder2, paramInt);
    f1 = 0.75F * paramFloat2;
    paramFloat1 /= 3.0F;
    Geometry.Circle circle1 = new Geometry.Circle(paramPoint3.translateY(paramFloat2 * 0.5F), paramFloat1);
    Geometry.Cylinder cylinder1 = new Geometry.Cylinder(circle1.center.translateY(-f1 / 2.0F), paramFloat1, f1);
    vertexBuilder.appendCircle(circle1, paramInt, true);
    vertexBuilder.appendOpenCylinder(cylinder1, paramInt);
    return vertexBuilder.build();
  }
  
  public static GeneratedData createSolidCylinder(Geometry.Cylinder paramCylinder, int paramInt) {
    VertexBuilder vertexBuilder = new VertexBuilder(sizeOfCircleInVertices(paramInt) * 2 + sizeOfOpenCylinderInVertices(paramInt));
    Geometry.Circle circle1 = new Geometry.Circle(paramCylinder.center.translateY(paramCylinder.height / 2.0F), paramCylinder.radius);
    Geometry.Circle circle2 = new Geometry.Circle(paramCylinder.center.translateY(-paramCylinder.height / 2.0F), paramCylinder.radius);
    vertexBuilder.appendCircle(circle1, paramInt, false);
    vertexBuilder.appendCircle(circle2, paramInt, true);
    vertexBuilder.appendOpenCylinder(paramCylinder, paramInt);
    return vertexBuilder.build();
  }
  
  private static int sizeOfCircleInVertices(int paramInt) {
    return paramInt + 1 + 1;
  }
  
  private static int sizeOfOpenCylinderInVertices(int paramInt) {
    return (paramInt + 1) * 2;
  }
  
  public static interface DrawCommand {
    void draw();
  }
  
  public static class GeneratedData {
    public final List<VertexBuilder.DrawCommand> drawList;
    
    public final float[] vertexData;
    
    GeneratedData(float[] param1ArrayOffloat, List<VertexBuilder.DrawCommand> param1List) {
      this.vertexData = param1ArrayOffloat;
      this.drawList = param1List;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\VertexBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */