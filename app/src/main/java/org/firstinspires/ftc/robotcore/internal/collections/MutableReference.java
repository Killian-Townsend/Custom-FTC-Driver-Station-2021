package org.firstinspires.ftc.robotcore.internal.collections;

public class MutableReference<T> {
  protected T value;
  
  public MutableReference() {
    this(null);
  }
  
  public MutableReference(T paramT) {
    this.value = paramT;
  }
  
  public T getValue() {
    return this.value;
  }
  
  public void setValue(T paramT) {
    this.value = paramT;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[{");
    stringBuilder.append(getValue());
    stringBuilder.append("}]");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\collections\MutableReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */