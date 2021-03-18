package org.firstinspires.ftc.robotcore.external.navigation;

public enum AxesOrder {
  XYX,
  XYZ,
  XZX(new int[] { 0, 2, 0 }),
  XZY(new int[] { 0, 2, 0 }),
  YXY(new int[] { 0, 2, 0 }),
  YXZ(new int[] { 0, 2, 0 }),
  YZX(new int[] { 0, 2, 0 }),
  YZY(new int[] { 0, 2, 0 }),
  ZXY(new int[] { 0, 2, 0 }),
  ZXZ(new int[] { 0, 2, 0 }),
  ZYX(new int[] { 0, 2, 0 }),
  ZYZ(new int[] { 0, 2, 0 });
  
  private final int[] indices;
  
  static {
    XYX = new AxesOrder("XYX", 1, new int[] { 0, 1, 0 });
    YXY = new AxesOrder("YXY", 2, new int[] { 1, 0, 1 });
    YZY = new AxesOrder("YZY", 3, new int[] { 1, 2, 1 });
    ZYZ = new AxesOrder("ZYZ", 4, new int[] { 2, 1, 2 });
    ZXZ = new AxesOrder("ZXZ", 5, new int[] { 2, 0, 2 });
    XZY = new AxesOrder("XZY", 6, new int[] { 0, 2, 1 });
    XYZ = new AxesOrder("XYZ", 7, new int[] { 0, 1, 2 });
    YXZ = new AxesOrder("YXZ", 8, new int[] { 1, 0, 2 });
    YZX = new AxesOrder("YZX", 9, new int[] { 1, 2, 0 });
    ZYX = new AxesOrder("ZYX", 10, new int[] { 2, 1, 0 });
    AxesOrder axesOrder = new AxesOrder("ZXY", 11, new int[] { 2, 0, 1 });
    ZXY = axesOrder;
    $VALUES = new AxesOrder[] { 
        XZX, XYX, YXY, YZY, ZYZ, ZXZ, XZY, XYZ, YXZ, YZX, 
        ZYX, axesOrder };
  }
  
  AxesOrder(int[] paramArrayOfint) {
    this.indices = paramArrayOfint;
  }
  
  public Axis[] axes() {
    return new Axis[] { Axis.fromIndex(this.indices[0]), Axis.fromIndex(this.indices[1]), Axis.fromIndex(this.indices[2]) };
  }
  
  public int[] indices() {
    return this.indices;
  }
  
  public AxesOrder reverse() {
    switch (this) {
      default:
        return XZX;
      case null:
        return YXZ;
      case null:
        return XYZ;
      case null:
        return XZY;
      case null:
        return ZXY;
      case null:
        return ZYX;
      case null:
        return YZX;
      case null:
        return ZXZ;
      case null:
        return ZYZ;
      case null:
        return YZY;
      case null:
        return YXY;
      case null:
        break;
    } 
    return XYX;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\AxesOrder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */