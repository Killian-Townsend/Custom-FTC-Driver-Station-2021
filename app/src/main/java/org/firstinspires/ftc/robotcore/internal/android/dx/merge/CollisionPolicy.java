package org.firstinspires.ftc.robotcore.internal.android.dx.merge;

public enum CollisionPolicy {
  FAIL, KEEP_FIRST;
  
  static {
    CollisionPolicy collisionPolicy = new CollisionPolicy("FAIL", 1);
    FAIL = collisionPolicy;
    $VALUES = new CollisionPolicy[] { KEEP_FIRST, collisionPolicy };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\merge\CollisionPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */