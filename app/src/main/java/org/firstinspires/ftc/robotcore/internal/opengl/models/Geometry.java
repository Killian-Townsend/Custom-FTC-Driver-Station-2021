package org.firstinspires.ftc.robotcore.internal.opengl.models;

public class Geometry {
  public static class Circle {
    public final Geometry.Point3 center;
    
    public final float radius;
    
    public Circle(Geometry.Point3 param1Point3, float param1Float) {
      this.center = param1Point3;
      this.radius = param1Float;
    }
    
    public Circle scale(float param1Float) {
      return new Circle(this.center, this.radius * param1Float);
    }
  }
  
  public static class Cylinder {
    public final Geometry.Point3 center;
    
    public final float height;
    
    public final float radius;
    
    public Cylinder(Geometry.Point3 param1Point3, float param1Float1, float param1Float2) {
      this.center = param1Point3;
      this.radius = param1Float1;
      this.height = param1Float2;
    }
  }
  
  public static class Point3 {
    public final float x;
    
    public final float y;
    
    public final float z;
    
    public Point3(float param1Float1, float param1Float2, float param1Float3) {
      this.x = param1Float1;
      this.y = param1Float2;
      this.z = param1Float3;
    }
    
    public Point3 translateY(float param1Float) {
      return new Point3(this.x, this.y + param1Float, this.z);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opengl\models\Geometry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */