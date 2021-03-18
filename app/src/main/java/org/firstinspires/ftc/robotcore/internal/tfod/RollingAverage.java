package org.firstinspires.ftc.robotcore.internal.tfod;

public class RollingAverage {
  private volatile double average;
  
  private final double[] elements;
  
  private int elementsIndex;
  
  private int numElements;
  
  private double sum;
  
  public RollingAverage(int paramInt) {
    this(paramInt, 0.0D);
  }
  
  public RollingAverage(int paramInt, double paramDouble) {
    if (paramInt > 0) {
      this.average = paramDouble;
      this.elements = new double[paramInt];
      return;
    } 
    throw new IllegalArgumentException("Need positive buffer size!");
  }
  
  public void add(double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield numElements : I
    //   6: iconst_1
    //   7: iadd
    //   8: istore #5
    //   10: aload_0
    //   11: iload #5
    //   13: putfield numElements : I
    //   16: iload #5
    //   18: aload_0
    //   19: getfield elements : [D
    //   22: arraylength
    //   23: if_icmple -> 55
    //   26: aload_0
    //   27: getfield elements : [D
    //   30: aload_0
    //   31: getfield elementsIndex : I
    //   34: daload
    //   35: dstore_3
    //   36: aload_0
    //   37: aload_0
    //   38: getfield elements : [D
    //   41: arraylength
    //   42: putfield numElements : I
    //   45: aload_0
    //   46: aload_0
    //   47: getfield sum : D
    //   50: dload_3
    //   51: dsub
    //   52: putfield sum : D
    //   55: aload_0
    //   56: getfield elements : [D
    //   59: aload_0
    //   60: getfield elementsIndex : I
    //   63: dload_1
    //   64: dastore
    //   65: aload_0
    //   66: aload_0
    //   67: getfield elementsIndex : I
    //   70: iconst_1
    //   71: iadd
    //   72: aload_0
    //   73: getfield elements : [D
    //   76: arraylength
    //   77: irem
    //   78: putfield elementsIndex : I
    //   81: aload_0
    //   82: getfield sum : D
    //   85: dload_1
    //   86: dadd
    //   87: dstore_1
    //   88: aload_0
    //   89: dload_1
    //   90: putfield sum : D
    //   93: aload_0
    //   94: dload_1
    //   95: aload_0
    //   96: getfield numElements : I
    //   99: i2d
    //   100: ddiv
    //   101: putfield average : D
    //   104: aload_0
    //   105: monitorexit
    //   106: return
    //   107: astore #6
    //   109: aload_0
    //   110: monitorexit
    //   111: aload #6
    //   113: athrow
    // Exception table:
    //   from	to	target	type
    //   2	55	107	finally
    //   55	104	107	finally
  }
  
  public double get() {
    return this.average;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\RollingAverage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */