package org.firstinspires.ftc.robotcore.internal.opengl.models;

import java.util.Iterator;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.opengl.shaders.PositionAttributeShader;

public class SolidCylinder {
  private static final int coordinatesPerVertex = 3;
  
  private final List<VertexBuilder.DrawCommand> drawList;
  
  public final float height;
  
  public final float radius;
  
  private final VertexArray vertexArray;
  
  public SolidCylinder(float paramFloat1, float paramFloat2, int paramInt) {
    VertexBuilder.GeneratedData generatedData = VertexBuilder.createSolidCylinder(new Geometry.Cylinder(new Geometry.Point3(0.0F, 0.0F, 0.0F), paramFloat1, paramFloat2), paramInt);
    this.radius = paramFloat1;
    this.height = paramFloat2;
    this.vertexArray = new VertexArray(generatedData.vertexData);
    this.drawList = generatedData.drawList;
  }
  
  public void bindData(PositionAttributeShader paramPositionAttributeShader) {
    this.vertexArray.setVertexAttribPointer(0, paramPositionAttributeShader.getPositionAttributeLocation(), 3, 0);
  }
  
  public void draw() {
    Iterator<VertexBuilder.DrawCommand> iterator = this.drawList.iterator();
    while (iterator.hasNext())
      ((VertexBuilder.DrawCommand)iterator.next()).draw(); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\SolidCylinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */